---
create: 2023-11-29
modified: '2024-11-17'
---

# 14 如果函数不抛出异常请使用`noexcept`

## 1. 展开调用栈方面的优化

​	考虑一个函数`f`，它保证调用者永远不会收到一个异常。两种表达方式如下：

```cpp
int f(int x) throw();   //C++98风格，没有来自f的异常
int f(int x) noexcept;  //C++11风格，没有来自f的异常
```

​	如果在运行时，`f`出现一个异常，那么就和`f`的异常说明冲突了。

​	在C++98的异常说明中，调用栈（the *call stack*）会展开至`f`的调用者，在一些与这地方不相关的动作后，程序被终止。C++11异常说明的运行时行为有些不同：调用栈只是**可能**在程序终止前展开。

​	在一个`noexcept`函数中，当异常可能传播到函数外时，优化器不需要保证运行时栈（the runtime stack）处于可展开状态；也不需要保证当异常离开`noexcept`函数时，`noexcept`函数中的对象按照构造的反序析构。而标注“`throw()`”异常声明的函数缺少这样的优化灵活性，没加异常声明的函数也一样。

​	综上：

```C++
RetType function(params) noexcept;  //极尽所能优化
RetType function(params) throw();   //较少优化
RetType function(params);           //较少优化
```

## 2. 容器带来的优化（std::move）

​