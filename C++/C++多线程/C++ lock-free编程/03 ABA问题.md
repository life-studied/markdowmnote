---
create: '2025-03-19'
modified: '2025-03-19'
---

# ABA问题

ABA问题指的是对于一个变量，线程1在读取时是A，此时其它线程将其修改为B后，又被其它线程修改回了A。

在多数情况下，这种修改不会影响线程1的操作。然而某些情况下，这种A——B——A的修改做法，会让线程1误解变量的属性都未发生改变，从而产生预期之外的结果。

构成 ABA 问题有三个重要的条件：

1. 某个线程需要重复读某个变量，并以变量的值变化作为该值是否变化的唯一判定依据；
2. 重复读取的变量会被多线程共享，且存在『值回退』的可能，即值变化后有可能因为某个操作复归原值；
3. 在多次读取间隔中，开发者没有采取有效的同步手段，比如上锁。

以上三个关键点构成了 ABA 问题的充要条件，我们只需要打破其中一个条件就可以解决 ABA 问题。

但是实际上ABA问题在实践中可能会有不同维度的情况。

## 经典转账案例

### 场景

假设小琳银行卡有 100 块钱余额，且假定银行转账操作就是一个单纯的 CAS 命令，对比余额旧值是否与当前值相同，如果相同则发生扣减/增加，我们将这个指令用 CAS(origin,expect) 表示。于是，我们看看接下来发生了什么：

1. 小琳在 ATM 1 转账 100 块钱给小李；
2. 由于ATM 1 出现了网络拥塞的原因卡住了，这时候小琳跑到旁边的 ATM 2 再次操作转账；
3. ATM 2 没让小琳失望，执行了 CAS(100,0)，很痛快地完成了转账，此时小琳的账户余额为 0；
4. 小王这时候又给小琳账上转了 100，此时小琳账上余额为 100；
5. 这时候 ATM 1 网络恢复，继续执行 CAS(100,0)，居然执行成功了，小琳账户上余额又变为了 0；
6. 这时候小王微信跟小琳说转了 100 过去，是否收到呢？小琳去查了下账，摇了摇头，那么问题来了，钱去了哪呢？

关于钱的去向，有一种可能就是小王给小琳的 100 大洋，因为 ATM 1 网络恢复再次被转给了小李，毕竟小琳尝试了两次转账，出现这种情况虽不合理，但情有可原。假设我们作为银行系统设计者和开发者，不接受这种情况存在，那我们就需要着手处理这种 ABA 问题了。

### 解决思路

我们从 ABA 达成条件分析上述转账场景：

1. 银行系统以 CAS 指令操作扣款 / 加款，并以旧值是否与账户余额当前值一致作为判断转账是否成功的唯一标准，通俗地说，如果账户余额旧值与当前值相同，系统会认为可以继续转账，这满足了条件 a；
2. 账户信息被多个 ATM 终端共享，且不同 ATM 终端均可操作转账，且余额存在『值回退』的可能，如案例描述，小琳转 100 给小李，而后小王转 100 给小琳，小琳的账户余额被回退到了 100，这满足了条件 b；
3. ATM 上操作转账，先是读取账户余额，然后执行 CAS(origin,expect) ，但是这两步操作不具备原子性和事务性，这满足了条件 c。

综上分析，我们为了解决 ABA 问题，关键就是打破三个达成条件中任意一个均可，那么解决思路就很简单了：

1. 引入epoch（这个 epoch 是一个正向递增的值）：
    * 我们只需要将 `CAS(origin,expect) & CAS(origin_epoch,current_epoch)` 两个命令封装成一个事务，那么就意味着，epoch相同的 加款/扣款 的所有操作，只会有一个成功，这个很好地杜绝了条件 a 判定单一性；
    * 同理，引入 epoch 这个变量是不存在『值回退』的风险的，因为他是个定向递增的值，因此这又可以打破条件 b；
2. 加锁：
    * 如果我们不引入额外的 epoch 变量，我们只需要在每次账户余额变更的过程里加上锁，将读取余额和 CAS 执行的过程封装到一个事务里，就能打破条件 c。然而，这种做法会大大增加系统可用性的风险，就上述案例而言，小琳在发现 ATM 1 网络拥塞的时候，即便转到 ATM 2 去操作转账，也需要等待 ATM 1 执行完，完全释放锁才可以。

## 链表案例

对于一个无锁list，pop接口的CAS操作仅检查指针的地址是否相同，而忽略了指针所指向对象的内部状态变化，从而导致ABA问题：即使指针地址未变，但其指向的对象可能已经被修改过。

```C++
#include <iostream>
#include <atomic>

// 定义链表节点结构
struct Node {
    int data;
    Node* next;
    Node(int val) : data(val), next(nullptr) {}
};

// 定义栈结构
class Stack {
    private:
    std::atomic<Node*> head;

    public:
    Stack() : head(nullptr) {}

    // 入栈操作，插入一个元素到头部
    void push(int value) {
        Node* newNode = new Node(value);
        Node* old = head.load(std::memory_order_relaxed());
        newNode->next = old;

        while (!head.compare_exchange_weak(old, newNode, std::memory_order_release, std::memory_order_relaxed())) {
            newNode->next = old;
        }
    }

    // 出栈操作，将头部的一个元素弹出
    int pop() {
        Node* old = head.load(std::memory_order_relaxed());
        Node* next = old->next;

        // 此处可能出现ABA问题，即old指向的head地址虽然一致，但是内部的next可能已经不一样了，此时应该给出额外的检测
        while (!head.compare_exchange_weak(old, next, std::memory_order_release, std::memory_order_relaxed())) {
            next = old->next;
        }

        int value = old->data;
        delete old;
        return value;
    }
};

int main() {
    Stack stack;
    stack.push(1);
    stack.push(2);
    stack.push(3);

    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 3
    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 2
    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 1

    return 0;
}
```

## 两类ABA问题的本质

### **1. 数据抽象层次不同**

- **经典转账案例**：属于**“值的ABA问题”**，这里的变量是账户余额，是一个**单一的、具体的数值**。ABA问题的根源在于对数值的直接比较，忽略了数值变化的中间过程。
- **链表案例**：属于**“对象的ABA问题”**，这里的变量是链表的头指针，是一个**对象的引用**。ABA问题的根源在于对指针的比较，忽略了指针所指向对象的内部状态变化。

------

### **2. 问题产生的核心机制不同**

- **经典转账案例**：问题的核心在于**数值的“值回退”**，即余额从A变为B再变回A。这种问题是因为系统仅依赖数值的当前状态，而忽略了数值变化的“历史”。
- **链表案例**：问题的核心在于**指针的“引用不变性”**，即指针地址未变，但指针所指向对象的内部状态发生了变化。这种问题是因为系统仅依赖指针的地址，而忽略了指针所指向对象的“内容变化”。

------

### **3. 解决问题的关键点不同**

- **经典转账案例**：解决问题的关键在于**引入“版本号”或“时间戳”**，通过记录数值变化的“历史”来区分不同的操作。例如，引入`epoch`变量，使得每次操作都有一个唯一的版本标识。
- **链表案例**：解决问题的关键在于**引入“标记指针”或“额外的状态信息”**，通过记录指针所指向对象的内部状态变化来区分不同的操作。例如，使用`std::atomic`的标记指针特性，或者在节点中增加版本号。

## 对于链表案例中ABA问题的解决

常用的解决方式依然是引入版本号，只是引入的方式不同。

### 方法 1：使用标记指针（Tagged Pointer）

标记指针是一种在指针中嵌入额外信息的技术。通过将指针的低位（通常是未使用的位）用作版本号，可以检测指针指向的对象是否被修改过。

```cpp
#include <iostream>
#include <atomic>
#include <cstdint>

// 定义链表节点结构
struct Node {
    int data;
    Node* next;
    Node(int val) : data(val), next(nullptr) {}
};

// 定义栈结构
class Stack {
private:
    std::atomic<uintptr_t> head;  // 使用 uintptr_t 来存储标记指针

    // 辅助函数：将指针和版本号打包成一个 uintptr_t
    static uintptr_t pack(Node* ptr, uint32_t tag) {
        return reinterpret_cast<uintptr_t>(ptr) | tag;
    }

    // 辅助函数：从 uintptr_t 中解包指针和版本号
    static Node* unpack_ptr(uintptr_t tagged_ptr) {
        return reinterpret_cast<Node*>(tagged_ptr & ~0x3);  // 假设低 2 位用于版本号
    }

    static uint32_t unpack_tag(uintptr_t tagged_ptr) {
        return tagged_ptr & 0x3;  // 假设低 2 位用于版本号
    }

public:
    Stack() : head(0) {}  // 初始化为 0，表示空栈

    // 入栈操作，插入一个元素到头部
    void push(int value) {
        Node* newNode = new Node(value);
        uintptr_t old_head = head.load(std::memory_order_relaxed);
        Node* old_node = unpack_ptr(old_head);
        newNode->next = old_node;

        while (!head.compare_exchange_weak(old_head, pack(newNode, unpack_tag(old_head)),
                                           std::memory_order_release, std::memory_order_relaxed)) {
            old_node = unpack_ptr(old_head);
            newNode->next = old_node;
        }
    }

    // 出栈操作，将头部的一个元素弹出
    int pop() {
        uintptr_t old_head = head.load(std::memory_order_relaxed);
        Node* old_node = unpack_ptr(old_head);
        Node* next_node = old_node->next;

        while (!head.compare_exchange_weak(old_head, pack(next_node, unpack_tag(old_head) + 1),
                                           std::memory_order_release, std::memory_order_relaxed)) {
            old_node = unpack_ptr(old_head);
            next_node = old_node->next;
        }

        int value = old_node->data;
        delete old_node;
        return value;
    }
};

int main() {
    Stack stack;
    stack.push(1);
    stack.push(2);
    stack.push(3);

    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 3
    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 2
    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 1

    return 0;
}
```

解释：

1. 使用 `uintptr_t` 来存储标记指针，其中低 2 位用于版本号。
2. 在 `pop` 操作中，每次成功修改头指针时，版本号加 1。
3. `compare_exchange_weak` 会检查整个 `uintptr_t`，包括版本号，从而避免 ABA 问题。

### 方法 2：使用额外的版本号

另一种方法是使用一个额外的原子变量来记录版本号，每次修改指针时，版本号加 1。

```cpp
#include <iostream>
#include <atomic>

// 定义链表节点结构
struct Node {
    int data;
    Node* next;
    Node(int val) : data(val), next(nullptr) {}
};

// 定义栈结构
class Stack {
private:
    std::atomic<Node*> head;
    std::atomic<uint32_t> version;  // 用于记录版本号

public:
    Stack() : head(nullptr), version(0) {}

    // 入栈操作，插入一个元素到头部
    void push(int value) {
        Node* newNode = new Node(value);
        Node* old = head.load(std::memory_order_relaxed);
        newNode->next = old;

        while (!head.compare_exchange_weak(old, newNode, std::memory_order_release, std::memory_order_relaxed)) {
            newNode->next = old;
        }
    }

    // 出栈操作，将头部的一个元素弹出
    int pop() {
        uint32_t old_version = version.load(std::memory_order_relaxed);
        Node* old = head.load(std::memory_order_relaxed);
        Node* next = old->next;

        while (!head.compare_exchange_weak(old, next, std::memory_order_release, std::memory_order_relaxed) ||
               !version.compare_exchange_strong(old_version, old_version + 1, std::memory_order_release, std::memory_order_relaxed)) {
            old = head.load(std::memory_order_relaxed);
            next = old->next;
            old_version = version.load(std::memory_order_relaxed);
        }

        int value = old->data;
        delete old;
        return value;
    }
};

int main() {
    Stack stack;
    stack.push(1);
    stack.push(2);
    stack.push(3);

    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 3
    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 2
    std::cout << "Popped: " << stack.pop() << std::endl;  // 应输出 1

    return 0;
}
```

解释：

1. 使用 `std::atomic<uint32_t>` 来记录版本号。
2. 在 `pop` 操作中，`compare_exchange_weak` 检查头指针，同时 `compare_exchange_strong` 检查版本号是否匹配。
3. 每次成功修改头指针时，版本号加 1，从而避免 ABA 问题。