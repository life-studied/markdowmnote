---
create: '2025-06-15'
modified: '2025-06-15'
---

# auto缩写函数模板 | 约束性auto（c++20）

## 缩写函数模板

在C++20之前，编写函数模板都需要template来支持：

```C++
template <typename Integer>
void f(const Integer& i) {
    // ...
}
```

在C++20之后，可以用auto放在函数形参里，代替template：

```C++
void f(const auto& i) {
    // ...
}
```

## 约束性auto

上面的做法很方便，template允许使用concept进行约束。

为了同样能使用concept来进行约束，auto支持在前面使用concept：

```C++
#include <concepts>

// 基于template的concept约束
template <std::integral Integer>
void f(const Integer& i) {
    // ...
}

// 基于auto的concept约束
void f(std::integral auto& i) {
    // ...
}
```