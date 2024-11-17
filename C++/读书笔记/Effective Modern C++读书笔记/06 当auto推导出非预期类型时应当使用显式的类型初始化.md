---
create: 2023-11-28
---
# 06 当auto推导出非预期类型时应当使用显式的类型初始化

## 1. auto推导出非预期类型

​	有时候 `auto` 的类型推导会和你想的南辕北辙。举一个例子，假设我有一个函数接受一个 `Widget` 返回一个 `std::vector<bool>` ，其中每个 `bool` 表征 `Widget` 是否接受一个特定的特性：

```C++
std::vector<bool> features(const Widget& w);
```

​	假设要取第5个`bool`：

```C++
Widget w;
// ...
bool highPriority = features(w)[5]; 
// ...
processWidget(w, highPriority);
```

​	这份代码没有任何问题。它工作正常。但是如果我们做一个看起来无伤大雅的修改，把 `highPriority` 的显式的类型换成 `auto` ：

```C++
auto highPriority = features(w)[5];

processWidget(w, highPriority); // 未定义行为
```

​	正如注释中所提到的，调用 `processWidget` 现在会导致未定义的行为。但是为什么呢？答案是：在使用 auto 的代码中，`highPriority` 的类型已经不是 bool 了。

​	尽管 `std::vector<bool>` 从概念上说是 `bool` 的容器，对 `std::vector<bool>` 的 `operator[]` 运算符并不一定是返回容器中的元素的引用（ `std::vector::operator[]` 对所有的类型都返回引用，就是**除了 `bool`** ）。

​	事实上，他返回的是一个 `std::vector<bool>::reference` 对象（一个在 `std::vector<bool>` 中内嵌的`class`）。

## 2. 显式的类型初始化原则

​	不管你是如何发现它们，一旦 `auto` 被决定作为推导代理类的类型而不是它被代理的类型，它就不需要涉及到关于 `auto` ， `auto` 自己本身没有问题。问题在于 auto 推导的类型不是所想让它推导出来的类型。

​	解决方案就是强制一个不同的类型推导。 我把这种方法叫做**显式的类型初始化原则**。 显式的类型初始化原则涉及到使用 `auto` 声明一个变量，但是转换初始化表达式到 `auto` 想要 的类型。下面就是一个强制 `highPriority` 类型是 `bool` 的例子：

```C++
auto highPriority = static_cast<bool>(features(w)[5]);
```

