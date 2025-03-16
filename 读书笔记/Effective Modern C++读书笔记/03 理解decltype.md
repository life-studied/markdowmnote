---
create: 2023-11-28
modified: '2024-11-17'
---

# 03 理解decltype

​	在使用auto的情况下，如果想要创建下面这样的函数（**返回容器c的c[i]引用**）：

```C++
template<typename Container, typename Index> 	// C++14;
auto authAndAccess(Container &c, Index i) 		// not quite
{ 												// correct
    authenticateUser();
    return c[i];
}
```

​	对于auto而言，其遵守模板类型推导的规则，即：**不会推导出引用语义**（见[条款1情况3](./01 理解模板类型推导.md #第三种情况： ParamType 既不是指针也不是引用)）。因此，此处仅仅返回`c[i]`的拷贝。也就无法实现下面的代码：

```C++
std::deque<int> d;
// ...
authAndAccess(d, 5) = 10; 	// authenticate user, return d[5],
                            // then assign 10 to it;
                            // this won't compile!
```

## 1. decltype(auto)推导引用

​	decltype(auto)表明：`auto` 指定需要推导的类型， `decltype` 表明在推导的过程中使用 decltype 推导规则。下面是修正之后的代码：

```C++
template<typename Container, typename Index> 	// C++14; works,
decltype(auto) 									// but still
authAndAccess(Container &c, Index i) 			// requires
{ 												// refinement
    authenticateUser();
    return c[i];
}
```

​	现在 `authAndAccess` 的返回类型就是 `c[i]` 的返回类型。在一般情况下， `c[i]` 返 回 `T&` ， `authAndAccess` 就返回 `T&` ，在不常见的情况下， `c[i]` 返回一个对象， `authAndAccess` 也返回一个对象。

---

​	`decltype(auto)` 并不仅限使用在函数返回值类型上。当时想对一个表达式使用 `decltype` 的推导规则时，它也可以很方便的来声明一个变量： 

```C++
Widget w; 
const Widget& cw = w; 

auto myWidget1 = cw; 			// auto = Widget
decltype(auto) myWidget2 = cw; 	// decltype(auto) = const Widget&
```

## 2. decltype推导表达式类型

​	对一个变量名使用 `decltype` 得到这个变量名的声明类型。变量名属于左值表达式，但这并不影响 `decltype` 的行为。

​	然而，对于一个**比变量名更复杂的左值表达式**， `decltype` **保证返回的类型是左值引用**。

---

​	下面是一个案例，用于说明其导致的潜在问题：

​	`x` 是一个变量名，因此 `decltyper(x)` 是 `int` 。但是如果给 `x` 加上括号，`(x)`就得到一个比变量名复杂的表达式。作为变量名， x 是一个左值，同时 C++ 定义表达式 `(x)` 也是左值。因此 `decltype((x))` 是 `int&` 。

​	**给一个变量名加上括号会改变 `decltype` 返回的类型。**

```C++
decltype(auto) f1()
{
    int x = 0;
    // ...
    return x; // decltype(x) is int, so f1 returns int
}

decltype(auto) f2()
{
    int x = 0;
    return (x); // decltype((x)) is int&, so f2 return int&
}
```