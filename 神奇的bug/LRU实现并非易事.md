---
create: '2025-03-17'
modified: '2025-03-17'
---

# LRU实现并非易事

最常见的LRU实现，往往只是简单的Put和Get接口。它是一个很基础的用于演示LRU机制的简洁实现：

```C++
struct DLinkedNode {
    int key, value;
    DLinkedNode* prev;
    DLinkedNode* next;
    DLinkedNode(): key(0), value(0), prev(nullptr), next(nullptr) {}
    DLinkedNode(int _key, int _value): key(_key), value(_value), prev(nullptr), next(nullptr) {}
};

class LRUCache {
private:
    unordered_map<int, DLinkedNode*> cache;
    DLinkedNode* head;
    DLinkedNode* tail;
    int size;
    int capacity;

public:
    LRUCache(int _capacity): capacity(_capacity), size(0) {
        // 使用伪头部和伪尾部节点
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head->next = tail;
        tail->prev = head;
    }
    
    int get(int key) {
        if (!cache.count(key)) {
            return -1;
        }
        // 如果 key 存在，先通过哈希表定位，再移到头部
        DLinkedNode* node = cache[key];
        moveToHead(node);
        return node->value;
    }
    
    void put(int key, int value) {
        if (!cache.count(key)) {
            // 如果 key 不存在，创建一个新的节点
            DLinkedNode* node = new DLinkedNode(key, value);
            // 添加进哈希表
            cache[key] = node;
            // 添加至双向链表的头部
            addToHead(node);
            ++size;
            if (size > capacity) {
                // 如果超出容量，删除双向链表的尾部节点
                DLinkedNode* removed = removeTail();
                // 删除哈希表中对应的项
                cache.erase(removed->key);
                // 防止内存泄漏
                delete removed;
                --size;
            }
        }
        else {
            // 如果 key 存在，先通过哈希表定位，再修改 value，并移到头部
            DLinkedNode* node = cache[key];
            node->value = value;
            moveToHead(node);
        }
    }

    void addToHead(DLinkedNode* node) {
        node->prev = head;
        node->next = head->next;
        head->next->prev = node;
        head->next = node;
    }
    
    void removeNode(DLinkedNode* node) {
        node->prev->next = node->next;
        node->next->prev = node->prev;
    }

    void moveToHead(DLinkedNode* node) {
        removeNode(node);
        addToHead(node);
    }

    DLinkedNode* removeTail() {
        DLinkedNode* node = tail->prev;
        removeNode(node);
        return node;
    }
};
```

然而，在LRU真正投入到工程中，却没有这么简单。

## 起因

一切的开始源于一个朴素的想法，如果我希望将LRU作为一个纯粹的策略，而非耦合了数据的存储，我该如何实现？

这也意味着，对于缓存的存储和策略本身进行解耦。我们不再需要将`value`也放在LRU里，而是仅通过LRU得到我们应该做的事（直接load或者replace），然后在真正的缓存系统中执行对应的策略。

---

我首先设计了测试，一个缓存池（size=4），和一个磁盘池（size=20）。我将通过20个线程对磁盘进行读写，每个线程从随机一个磁盘序号开始，顺序遍历一遍（例如从9号开始，顺序遍历一遍到8号结束），追加一个字符在磁盘上。

这个字符在每个磁盘序号上是固定的，是`当前磁盘序号%4+‘1’`。最终实现效果应该如下，每一行是一个磁盘块：

```C++
11111111111111111111
22222222222222222222
33333333333333333333
44444444444444444444
11111111111111111111
22222222222222222222
33333333333333333333
44444444444444444444
11111111111111111111
22222222222222222222
33333333333333333333
44444444444444444444
11111111111111111111
22222222222222222222
33333333333333333333
44444444444444444444
11111111111111111111
22222222222222222222
33333333333333333333
44444444444444444444
```

## 思考

### pin与count

很显然地，我们会发现一个问题，如果一个`disk`被加载到一个`buffer`里，并且有一个线程正在读写它，那么我们不应该替换掉它，也就是它被pin住了。这在简单的lru的`history list`基础上，增加了一个pin数组，被pin住的`key`不允许被替换。

同时，如果有多个线程同时读写一个磁盘，那么引用计数的概念也会被引入进来，每一个被pin住的`key`应该有自己的`count`。

于是，LRU的接口设计不再如简单的Get和Put就能解决，而是转向了Pin和UnPin。

### pin有几种情况

对于pin操作，情况也变得复杂起来。我们希望pin是一个原子的操作，因此这个接口不能再被拆分。而pin内部的各种情况，由于我们希望将LRU作为一个策略，而非耦合缓存存储，因此我们必须要将pin的各种情况暴露给缓存系统，由它去做具体的处理（从上到下）：

* 已经在pin_table里，增加pin数
* 已经在cache_list里，转移到pin_table里
* 都不在，但有剩余空间进行pin
* 都不在，没有空间进行pin，但有空间可以替换后pin
* 完全没有空间，pin失败

之所以将这些复杂的情况都暴露出来，是因为简单的pin成功还是失败并不能给缓存系统足够的信息：

作为缓存系统，我应该去加载一个disk到空闲的buffer里呢？还是做缓存的替换呢？又或者是已经在缓存里？这些需要缓存系统本身有额外的空间存储信息和代码逻辑去实现，然而明明LRU策略已经知道了该如何做，却还要再在这里去实现一遍，非常容易出错和不一致。而如果LRU将pin的情况暴露出来，就能直接让缓存系统去处理对应的逻辑。

因此对于pin的设计，需要一个复杂的返回值：

```C++
/**
* @brief 将指定的Key装载入缓存，并进行固定
*
* @param key 要插入并固定的Key
* @return std::tuple<bool, std::optional<Key>>
*         - 第一个元素（bool）：表示操作是否成功。
*		   - 第二个元素（bool）：表示是否需要直接load（缓存池有空闲且数据不在其中）
*         - 第二个元素（std::optional<Key>）：表示是否需要替换Key，则返回替换的Key；否则为空。
*/
std::tuple<bool, bool, std::optional<Key>> PutAndPin(const Key& key);
```

在外部，也需要对这个复杂的返回值进行处理：

```C++
while (true) {
    auto [success, direct_load, replaced_disk_idx] = l.PutAndPin(k);

    // 没有能用的buffer，重试
    if (!success) {
        std::this_thread::yield();
        continue;
    }

    if (direct_load) {
        // 不在pool里，但有空闲buffer，直接加载
        pool.Load(k);
    }
    else if (replaced_disk_idx) {
        // 需要替换buffer的方式，加载
        pool.Replace(replaced_disk_idx.value(), k);
    }
    else {
        // 策略已经放入buffer pool里，但不一定立即生效
        pool.CheckLoad(k);
    }
}
```

直到这里，我们会发现，将LRU策略从缓存系统中单独拆分出来是一个非常糟糕的设计，此时的复杂接口就暴露了它使用困难的处境，当然这可以克服，也是不得不这么做，让我们接着往下看吧。

### 忙等检查Load

上面代码中很奇怪的地方在于，在能够直接使用的情况下，我们居然需要去检查缓存池是否正常加载了对应的`disk`。

这也是策略和缓存系统分割的困难点，我也认为这是设计上的困境：如果策略与缓存系统分离，那么在策略部分的缓存加载上了，并不代表缓存系统真的加载上了。**这两步单独都是一个原子操作，但是合在一起就不是一个原子操作了。**

### 两个原子操作合在一起并非一个原子操作

上面的忙等，在一定程度上解决了缓存尚未被加载的困境。然而，更可怕的事情隐藏在细节之中：

在调试代码的时候，我惊讶地发现，有时候多个线程会同时陷入无限等待，都希望某一个线程去对一个`disk`做`replace`，但是没有一个线程被指示去做`replace`。那么LRU策略里为什么会凭空产生了这个disk被pin住的幻觉呢？

经过大量的debug和测试，在少数情况下出现了如下的操作：

```txt
...	# 前面有910行日志
==================================================================================================================

threadid:149936	disk will replace in:5 and disk will be out:4           # LRU:  5 replace 4
buffer replace:	thread:149936,	5 replace 4, size:4                     # POOL: 5 replace 4     POOL里出现了第一个5号磁盘
write success:	thread:149936,	disk:5, idx:8, data:2 
threadid:149936	disk will move to cache:5                               # LRU:  5 pin => cache
threadid:149936	disk will replace in:6 and disk will be out:5           # LRU:  6 replace 5     LRU认为5是最旧的，所以6替换5，此时5还在POOL里
threadid:165480	disk will move to cache:14
threadid:111152	disk will replace in:5 and disk will be out:14          # LRU:  5 replace 14    LRU得知5再次被需要，所以将5替换14
buffer replace:	thread:111152,	5 replace 14, size:4                    # POOL: 5 replace 14    POOL执行将14替换成5，此时POOL里的buffer出现了两个5，同时map里的5号磁盘指向的buffer被修改了，但是旧的5号磁盘所在的buffer依然被占用，发生了隐形的buffer泄露
buffer replace:	thread:149936,	6 replace 5, size:4                     # POOL: 6 replace 5     POOL在这个时候才真的把5替换成6，在POOL里被替换的5是最新的5，同时map被更新，所以5号磁盘消失了
write success:	thread:111152,	disk:5, idx:9, data:2 
threadid:163964	disk will replace in:10 and disk will be out:3
write success:	thread:149936,	disk:6, idx:6, data:3 
threadid:149936	disk will move to cache:6
buffer replace:	thread:163964,	10 replace 3, size:3
threadid:149936	disk will replace in:7 and disk will be out:6
write success:	thread:163964,	disk:10, idx:9, data:3 
threadid:165636	disk pin add:19 (wait replace)
write success:	thread:165636,	disk:19, idx:13, data:4 
threadid:165636	disk will move to cache:19
threadid:126800	disk will replace in:16 and disk will be out:19
buffer replace:	thread:126800,	16 replace 19, size:3
write success:	thread:126800,	disk:16, idx:11, data:1 
threadid:126800	disk will move to cache:16
buffer replace:	thread:149936,	7 replace 6, size:3
threadid:154264	disk will replace in:0 and disk will be out:16
write success:	thread:149936,	disk:7, idx:6, data:4 
buffer replace:	thread:154264,	0 replace 16, size:3
threadid:149936	disk will move to cache:7
write success:	thread:154264,	disk:0, idx:11, data:1 
threadid:149936	disk will replace in:8 and disk will be out:7
threadid:144520	disk pin add:5 (wait replace)                           # LRU:  5 pin count++   LRU认为5此时还在POOL里，所以增加pin count，但是实际上POOL里已经找不到5号磁盘了（找不到并非是不在buffer池里，而是发生了buffer池的泄露，map里找不到了）
threadid:159528	disk pin add:5 (wait replace)
threadid:165636	disk pin add:0 (wait replace)
wait replace:	thread:159528,	disk_id:5                               # POOL: 等待替换5号磁盘  此时发生无限忙等，因为POOL里通过map找不到5号磁盘了，但是5号其实还在buffer池里（该线程认为是某个线程还在替换中，所以让出CPU等待它去做替换）
wait replace:	thread:144520,	disk_id:5

==================================================================================================================
...	# 后面有？？？行日志，系统陷入无限忙等
```

核心便在于，策略里替换和实际的缓存系统内部替换，这两步是两个原子操作，而非一个，那么在其中就会出现线程切换：

```txt
# LRU:  6 replace 5     LRU认为5是最旧的，所以6替换5，此时5还在POOL里

此时发生线程切换 A => B

# LRU:  5 replace 14    LRU得知5再次被需要，所以将5替换14
# POOL: 5 replace 14    POOL执行将14替换成5，此时POOL里的buffer出现了两个5，同时map里的5号磁盘指向的buffer被修改了，但是旧的5号磁盘所在的buffer依然被占用，发生了隐形的buffer泄露

此时再次发生线程切换 B => A

# POOL: 6 replace 5     POOL在这个时候才真的把5替换成6，在POOL里被替换的5是最新的5，同时map被更新，所以5号磁盘消失了
```

所以，并非LRU产生了disk被pin住的幻觉，而是缓存系统在多线程情况下不能和LRU的策略保持一致了。缓存没有对这个情况进行防范，这个时候就会出现错误的替换。

## 或许我们不应该完全解耦策略和缓存系统

再回过头来看，将替换策略和缓存进行拆分看似是一个很顺理成章的事情，实际上隐含了多线程编程中，两个原子操作合在一起不是原子操作的困境，同时在设计上也是两边互相解耦的误解。它们就应该是耦合的，准确来说是一方对另一方应该是知晓的。

在简洁版本的LRU中，本质上是策略对缓存进行了认知，缓存本身是无知觉的。而我希望的情况应该是缓存对于策略进行认知，策略本身对此是无知觉的。但是我的实现确实双方都对彼此互不了解，由上层去组织，这变得非常糟糕，因为**互相解耦最终会导致互相耦合**：

```C++
BufferPool pool;

LRU<int> l(buffer_size);

while (true) {
    auto [success, direct_load, replaced_disk_idx] = l.PutAndPin(k);

    // 没有能用的buffer，重试
    if (!success) {
        std::this_thread::yield();
        continue;
    }

    if (direct_load) {
        // 不在pool里，但有空闲buffer，直接加载
        pool.Load(k);
    }
    else if (replaced_disk_idx) {
        // 需要替换buffer的方式，加载
        pool.Replace(replaced_disk_idx.value(), k);
    }
    else {
        // 策略已经放入buffer pool里，但不一定立即生效
        pool.CheckLoad(k);
    }

    int buffer_idx = pool.GetBufferIdx(k);

    // 写入数据
    char data = k % 4 + '1';
    pool.Write(k, data);

    std::atomic_thread_fence(std::memory_order::acq_rel);

    l.UnPin(k);
}
```

一个合理的设计应该是：

```C++
class BufferPool {
public:
    void Pin(int disk_id);
    
    void UnPin(int disk_id);
    
    void Write(...);
private:
    policy* p = new LRU<int>;
}
```

这样在内部进行pin的时候，起码我们能做到策略和缓存系统对于哪个`disk`被`pin`的认知是一致的，同时上层使用也不再复杂：

```C++
BufferPool pool;
pool.Pin(1);	// pin住第1个，可能陷入忙等（没有buffer）

pool.Write();

pool.UnPin(1);	// 释放第1个，pin count--
```