---
create: '2025-10-09'
modified: '2025-10-09'
---

# atomic的acq和rel语义

在C++标准中，原子变量的操作允许使用acq和rel作为常用的内存序。

* `acquire`代表获取，即下面的操作会依赖本次获取，来同步其他核心的缓存数据。

  这也是为什么此处会有内存屏障的副作用，下面的指令不允许向上，因为在`acquire`是不能保证数据已经被同步的，后续的操作可能出问题。

* `release`代表释放，即通知其它核心，此处的数据发生了改变。

一般`acquire`和`release`同时使用，才能构成一个同步的关系，即`happen-before`。

```C++
#include <atomic>
#include <iostream>
#include <thread>

std::atomic<bool> flag(false);
int data;

void t1() {
    data = 1; // 写入data
    flag.store(true, std::memory_order_release); // 设置flag为true，使用release语义
}

void t2() {
	flag.load(std::memory_order_acquire)
    std::cout << "data: " << data << std::endl; // 读取data
}
```

如上所示，acquire和release构成了一个`happen-before`关系。但是可以很明显地注意到，`happen-before`关系不代表A一定发生在B之前，而是代表“如果A发生在B之前了，B一定可以看到”。