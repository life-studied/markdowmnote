---
create: 2023-09-22
---
# 扩展的inline说明符

## 1. 定义非常量静态成员变量的问题

​	C++17之前，非常量的静态成员变量的定义和声明必须分开。

```C++
class X
{
public:
    static std::string text;		//declaration
};

std::string X::text{"hello"};		//definition

int main()
{
	X::text += "world";
    std::cout << X::text << std::endl;
}
```

​	在上面的代码中，如果`std::string X::text{"hello"};`被放在头文件中，则必须保证其只会被包含一次。或者将其放在`.cpp`文件中，以保证全局只存在一份定义。

---

​	对于一些字面量类型，比如整形、浮点类型等，使用**常量声明**可以直接声明和定义放在一起。

​	但是它失去了修改变量的能力。

```C++
class X
{
public:
    static const int num{5};
};

int main()
{
	std::cout << X::num << std::endl;
}
```

## 2. inline内联静态常量

​	**C++17**标准开始，允许使用`inline`关键字拓展功能：内联定义静态变量。

​	编译器会在**类X的定义首次出现**时对内联静态成员变量进行定义和声明。

```C++
#include <iostream>
#include <string>

class X
{
public:
    inline static std::string text{"hello"};
}

int main()
{
    X::text += "world";
    std::cout << X::text << std::endl;
}
```

