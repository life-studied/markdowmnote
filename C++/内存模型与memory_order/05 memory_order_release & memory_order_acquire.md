---
create: 2024-09-16
---
# memory_order_release & memory_order_acquire

## 1. memory_order_release保证写不后

```C++
std::atomic<bool> has_release;

// thread_2 
void release_software(int *data) {
    int a = 100;                // line 1
    int c = 200;                // line 2
    if (!data) {
        data = new int[100];    // line 3
    }

    has_release.store(true, std::memory_order_release); // line 4
}
```

std::memory_order_release功能如果用一句比较长的话来说，就是：在本行代码之前，有任何写内存的操作，都是不能放到本行语句之后的。

简单地说，就是**写不后**。即，写语句不能调到本条语句之后。

以这种形式通知编译器/CPU保证真正执行的时候，写语句不会放到has_release.store(true, std::memory_order_relese)之后。

## 2. memory_order_acquire保证读不前

```C++
std::atomic<bool> has_release;
int *data = nullptr;

// thread_1
void releae_software() {
    if (!data) {
        data = new int[100];                            // line 1
    }
    has_release.store(true, std::memory_order_release); // line 2

    //.... do something other.
}

// thread_use
void use_software() {
    // 检查是否已经发布了，并且保证读操作在这之后进行
    while (!has_release.load(std::memory_order_acquire));
    // 即然已经发布，那么就从里面取值
    int x = *data;
}
```

std::memory_order_acquire表示的是，后续的读操作都不能放到这条指令之前。简单地可以写成读不前。

## 3. std::memory_order_acq_rel同时保证两者