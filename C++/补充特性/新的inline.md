---
create: '2025-04-07'
modified: '2025-04-07'
---

# 新的inline

## 其它inline

* 旧标准inline：用于建议编译器内联函数，此处不再多说
* 命名空间inline：用于库版本迭代，见[此处](./inline命名空间（版本迭代）.md)
* 类static变量拓展的inline：用于类static变量的声明和定义一起，见[此处](../../读书笔记/现代C++语言核心特性解析读书笔记/26 扩展的inline说明符（C++17）.md)

## 新的inline

### 函数签名重定义（C++11）

从C++11开始，允许使用inline修饰函数，使得多个函数的定义可以出现在不同的cpp里。或者是在头文件里直接实现函数：

```c++
// test.h
#pragma once
#include <iostream>
inline void func() {
    std::cout << "Hello inline\n";
}
```

在以前，多个cpp如果include这个test.h就会报错，现在可以正常编译通过。

> 但是如果多个函数定义不同，则会引发ub，不确定编译器会选择哪个进行调用。

### 变量签名重定义（C++17）

和函数一样，在C++17之后，可以通过inline在头文件里定义变量，更加方便的共享一个全局变量。

```C++
// test.h
#pragma once
inline int a = 1;
```

### 实现原理

在编译阶段，inline会生成一个类似软链接的东西，在链接阶段，linker找到这些同名的软链接，合并生成一份实际的符号。