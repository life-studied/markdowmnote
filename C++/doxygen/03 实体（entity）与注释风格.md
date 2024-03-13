# 实体（entity）与注释风格

## 实体

> 在Doxygen中，实体是类，结构体，变量，函数，函数参数。可以被描述的代码抽象体。

## 注释

### 简介与详细

​	实体开头的第一行是简介，下面的一段是详细（中间可以空行便于观察）。

​	使用`///`注释，可以被doxygen识别。或者用`/*`注释。

```C++
/// this is a brief introduction for class MyClass
///
/// this class has two members, money and age.
class MyClass
{
public:
    int money;
    int age;
};


```

### 成员实体注释

​	使用`///<`注释。

```c++
/// this is a brief introduction for class MyClass
///
/// this class has two members, money and age.
class MyClass
{
public:
    int money;		///< 拥有的金钱数
    int age;		///< 年龄
};


```

