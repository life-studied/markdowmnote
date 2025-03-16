---
create: 2024-08-13
modified: '2024-11-17'
---

# 第五章 C++内存模型和原子类型操作

## 5.1 内存模型基础

### 5.1.1 对象和内存位置

​	无论对象是怎么样的一个类型，一个对象都会存储在**一个或多个内存位置**上。每一个内存位置不是一个对象，就是一个对象中的子对象，比如，unsigned short、my_class*或序列中的相邻位域。

​	当你使用位域，就需要注意：虽然相邻位域中是不同的对象，但仍视其为相同的内存位置。

```C++
struct MyStruct {
    int a;          // 一个int类型对象，占用4字节
    unsigned short bf1 : 16; // 位域对象，占用2字节，与bf2共享内存位置
    unsigned short bf2 : 16; // 位域对象，与bf1共享内存位置
    unsigned int bf3 : 0;    // 位域对象，宽度为0，通常用于对齐，有自己的内存位置
    std::string s;           // 一个std::string对象，由多个内存位置组成
    int b;                   // 另一个int类型对象，占用4字节
};
```

对应的内存模型：

```
+----------------+ <--- 内存地址
|    int a       | (4字节)
+----------------+
| bf1 (16位)     | (与bf2共享一个内存位置)
| bf2 (16位)     |
+----------------+
| bf3 (0位)      | (通常是对齐用的，有自己的位置)
+----------------+
| std::string s  | (由多个内存位置组成，具体取决于实现)
+----------------+
|    int b       | (4字节)
+----------------+
```

​	首先，完整的struct是一个有多个子对象(每一个成员变量)组成的对象。位域bf1和bf2共享同一个内存位置(int是4字节、32位类型)，并且`std::string`类型的对象s由内部多个内存位置组成，但是其他的每个成员都拥有自己的内存位置。注意，位域宽度为0的bf3是如何与bf4分离，并拥有各自的内存位置的。(译者注：图中bf3是一个错误展示，在C++和C中规定，宽度为0的一个未命名位域强制下一位域对齐到其下一type边界，其中type是该成员的类型。这里使用命名变量为0的位域，可能只是想展示其与bf4是如何分离的。）

### 5.1.2 对象、内存位置和并发

​	所有东西都在内存中。当两个线程访问不同(*separate*)的内存位置时，不会存在任何问题，一切都工作顺利。而另一种情况下，当两个线程访问同一(*same*)个内存位置：如果没有线程更新内存位置上的数据不会出问题，只读数据不需要保护或同步。当有线程对内存位置上的数据进行修改，那就有可能会产生条件竞争。

### 5.1.3 修改顺序

​	每一个在C++程序中的对象，都有(由程序中的所有线程对象)确定好的修改顺序(*modification order*)。

​	如果对象不是一个原子类型(将在5.2节详述)，你必要确保有足够的同步操作，来确定每个线程都遵守了变量的修改顺序。当不同线程在不同序列中访问同一个值时，你可能就会遇到数据竞争或未定义行为(详见5.1.2节)。如果你使用原子操作，编译器就有责任去替你做必要的同步。

​	这一要求意味着：投机执行（指令乱序重排）是不允许的。因为当线程按修改顺序访问内存并写该对象，之后的读操作，必须由线程返回较新的值，并且之后的写操作必须发生在修改顺序（读操作）之后。同样的，在同一线程上允许读取对象的操作，要不返回一个已写入的值，要不在对象的修改顺序后(也就是在读取后)再写入另一个值。

```C++
#include <iostream>
#include <thread>
#include <atomic>

std::atomic<int> sharedVar(0); // 使用atomic来保证修改顺序

void threadA() {
    sharedVar.store(10); // 线程A写入10
    std::cout << "Thread A writes 10 to sharedVar" << std::endl;
}

void threadB() {
    int value = sharedVar.load(); // 线程B读取sharedVar
    std::cout << "Thread B reads " << value << " from sharedVar" << std::endl;
}

int main() {
    std::thread t1(threadA);
    std::thread t2(threadB);
    t1.join();
    t2.join();
    return 0;
}
```

## 5.2 C++中的原子操作和原子类型

### 5.2.1 标准原子类型

​	标准原子类型(*atomic types*)可以在`<atomic>`头文件中找到。所有在这种类型上的操作都是原子的，虽然可以使用互斥量去达到原子操作的效果，但只有在这些类型上的操作是原子的(语言明确定义)。

​	实际上，标准原子类型都很相似：它们(大多数)都有一个`is_lock_free()`成员函数，这个函数允许用户决定是否直接对一个给定类型使用原子指令(`x.is_lock_free()`返回`true`)，或对编译器和运行库使用内部锁(`x.is_lock_free()`返回`false`)。

#### std::atomic_flag

​	只有`std::atomic_flag`类型不提供`is_lock_free()`成员函数。这个类型是一个简单的布尔标志，并且在这种类型上的操作都需要是无锁的(*lock-free*)。当你有一个简单无锁的布尔标志时，你可以使用其实现一个简单的锁（自旋锁），并且实现其他基础的原子类型。

```C++
#include <atomic>
#include <thread>
#include <iostream>

class SpinLock {
private:
    std::atomic_flag flag = ATOMIC_FLAG_INIT;	// 必须明确初始化，或者使用{}

public:
    SpinLock() = default;
    ~SpinLock() = default;

    SpinLock(const SpinLock&) = delete;
    SpinLock& operator=(const SpinLock&) = delete;

    void lock() {
        while (flag.test_and_set(std::memory_order_acquire)) {
            // 忙等待，直到锁被释放
        }
    }

    void unlock() {
        flag.clear(std::memory_order_release);
    }
};

int shared_resource = 0;

void thread_function(SpinLock& lock) {
    for (int i = 0; i < 10000; ++i) {
        lock.lock();
        // 安全地修改共享资源
        ++shared_resource;
        lock.unlock();
    }
}

int main() {
    SpinLock lock;
    std::thread t1(thread_function, std::ref(lock));
    std::thread t2(thread_function, std::ref(lock));

    t1.join();
    t2.join();

    std::cout << "Final value of shared_resource: " << shared_resource << std::endl;
    return 0;
}
```

注意，自旋锁在锁争用不高的情况下表现良好，但如果锁被长时间持有，可能会导致性能问题，因为其他线程会忙等待而不是进入睡眠状态。在实际应用中，可能需要考虑使用更高级的锁机制，如互斥锁（`std::mutex`）或条件变量，以避免长时间饥饿问题。

#### `std::atomic<T>`

​	通常，标准原子类型是不能拷贝和赋值，他们没有拷贝构造函数和拷贝赋值操作。但是，因为可以隐式转化成对应的内置类型，所以这些类型依旧支持赋值，可以使用`load()`和`store()`成员函数，`exchange()`、`compare_exchange_weak()`和`compare_exchange_strong()`。它们都支持复合赋值符：+=, -=, *=, |= 等等。

​	使用整型和指针的特化类型还支持 ++ 和 --。当然，这些操作也有功能相同的成员函数所对应：fetch_add(), fetch_or() 等等。

​	`std::atomic<>`类模板不仅仅一套特化的类型，其作为一个原发模板也可以使用用户定义类型创建对应的原子变量。因为，它是一个通用类模板，很多成员函数的操作在这种情况下有所限制：load(),store()(赋值和转换为用户类型), exchange(), compare_exchange_weak()和compare_exchange_strong()。

​	每种函数类型的操作都有一个可选内存排序参数，这个参数可以用来指定所需存储的顺序。在5.3节中，会对存储顺序选项进行详述。现在，只需要知道操作分为三类：

1. Store操作，可选如下顺序：memory_order_relaxed, memory_order_release, memory_order_seq_cst。
2. Load操作，可选如下顺序：memory_order_relaxed, memory_order_consume, memory_order_acquire, memory_order_seq_cst。
3. Read-modify-write(读-改-写)操作，可选如下顺序：memory_order_relaxed, memory_order_consume, memory_order_acquire, memory_order_release, memory_order_acq_rel, memory_order_seq_cst。
   所有操作的默认顺序都是memory_order_seq_cst。

---

### 5.2.2 std::atomic_flag的相关操作

​	`std::atomic_flag`是最简单的标准原子类型，它表示了一个布尔标志。这个类型的对象可以在两个状态间切换：设置和清除。

**初始化**

​	`std::atomic_flag`类型的对象必须被`ATOMIC_FLAG_INIT`初始化。初始化标志位是“清除”状态。这里没得选择；这个标志总是初始化为“清除”：

```C++
std::atomic_flag f = ATOMIC_FLAG_INIT;
```

**清除与设置**

​	当你的标志对象已初始化，那么你只能做两个事情：清除或设置。这些事情对应的函数分别是：clear()成员函数，和test_and_set()成员函数。

​	clear()和test_and_set()成员函数可以指定好内存顺序。clear()是一个存储操作，所以不能有memory_order_acquire或memory_order_acq_rel语义，但是test_and_set()是一个“读-改-写”操作，所以可以应用于任何内存顺序标签。每一个原子操作，默认的内存顺序都是memory_order_seq_cst。

```C++
f.clear(std::memory_order_release);  // 1
bool x=f.test_and_set();  // 2
```

**禁止拷贝**

​	一个原子类型的所有操作都是原子的，因赋值和拷贝调用了两个对象，这就破坏了操作的原子性。在这样的情况下，拷贝构造和拷贝赋值都会将第一个对象的值进行读取，然后再写入另外一个。对于两个独立的对象，这里就有两个独立的操作了，合并这两个操作必定是不原子的。

### 5.2.3 std::atomic的相关操作

#### `std::atomic<bool>`

​	非原子bool类型的赋值操作不同于通常的操作：它返回一个bool值来代替指定对象的引用。

​	如果一个原子变量的引用被返回了，任何依赖与这个赋值结果的代码都需要显式加载这个值，潜在的问题是，结果可能会被另外的线程所修改。通过使用返回非原子值进行赋值的方式，你可以避免这些多余的加载过程，并且得到的值就是实际存储的值。



## 附录：标准库atomic表

| 原子类型        | 相关特化类                      |
| --------------- | ------------------------------- |
| atomic_bool     | std::atomic<bool>               |
| atomic_char     | std::atomic<char>               |
| atomic_schar    | std::atomic<signed char>        |
| atomic_uchar    | std::atomic<unsigned char>      |
| atomic_int      | std::atomic<int>                |
| atomic_uint     | std::atomic<unsigned>           |
| atomic_short    | std::atomic<short>              |
| atomic_ushort   | std::atomic<unsigned short>     |
| atomic_long     | std::atomic<long>               |
| atomic_ulong    | std::atomic<unsigned long>      |
| atomic_llong    | std::atomic<long long>          |
| atomic_ullong   | std::atomic<unsigned long long> |
| atomic_char16_t | std::atomic<char16_t>           |
| atomic_char32_t | std::atomic<char32_t>           |
| atomic_wchar_t  | std::atomic<wchar_t>            |

C++标准库不仅提供基本原子类型，还定义了与原子类型对应的非原子类型，就如同标准库中的`std::size_t`。如表5.2所示这些类型:

| 原子类型定义          | 标准库中相关类型定义 |
| --------------------- | -------------------- |
| atomic_int_least8_t   | int_least8_t         |
| atomic_uint_least8_t  | uint_least8_t        |
| atomic_int_least16_t  | int_least16_t        |
| atomic_uint_least16_t | uint_least16_t       |
| atomic_int_least32_t  | int_least32_t        |
| atomic_uint_least32_t | uint_least32_t       |
| atomic_int_least64_t  | int_least64_t        |
| atomic_uint_least64_t | uint_least64_t       |
| atomic_int_fast8_t    | int_fast8_t          |
| atomic_uint_fast8_t   | uint_fast8_t         |
| atomic_int_fast16_t   | int_fast16_t         |
| atomic_uint_fast16_t  | uint_fast16_t        |
| atomic_int_fast32_t   | int_fast32_t         |
| atomic_uint_fast32_t  | uint_fast32_t        |
| atomic_int_fast64_t   | int_fast64_t         |
| atomic_uint_fast64_t  | uint_fast64_t        |
| atomic_intptr_t       | intptr_t             |
| atomic_uintptr_t      | uintptr_t            |
| atomic_size_t         | size_t               |
| atomic_ptrdiff_t      | ptrdiff_t            |
| atomic_intmax_t       | intmax_t             |
| atomic_uintmax_t      | uintmax_t            |