---
create: 2023-07-08
---
## assert

#### 语法

```c++
assert(bool表达式);
```

* 如果bool表达式为false，则会调用`std::abort()`函数，弹出对话框
* 头文件为：`cassert`

#### 使用说明

> 一般在某些不确定是否会发生，但的确有可能出问题的地方，可以使用assert代替if做一个检测。

**案例**

```c++
#include<cassert>
#include<iostream>
int main()
{
    std::cout<<"请输入一个整数\n";
    int c{};
    std::cin>>c;
    assert(c);		//此处使用assert在release的情况下，程序不会直接崩溃，而是会用一个窗口来提示情况
    std::cout<<1000/c;
    return 0;
}
```



#### 关闭assert

> 在程序里某些时候，不想看到assert，但是也不应该删除，可以使用宏定义NDEBUG，在头文件之前来去除。

**代码案例**

```c++
#define NDEBUG
#include<cassert>
#include<iostream>
int main()
{
    std::cout<<"请输入一个整数\n";
    int c;
    std::cin>>c;
    assert(c);		//不起作用
    std::cout<<1000/c;
    return 0;
}
```

**如何实现**

```c++
#ifdef NDEBUG

    #define assert(expression) ((void)0)

#else

    _ACRTIMP void __cdecl _wassert(
        _In_z_ wchar_t const* _Message,
        _In_z_ wchar_t const* _File,
        _In_   unsigned       _Line
        );

    #define assert(expression) (void)(                                                       \
            (!!(expression)) ||                                                              \
            (_wassert(_CRT_WIDE(#expression), _CRT_WIDE(__FILE__), (unsigned)(__LINE__)), 0) \
        )

#endif
```

## static_assert

#### 语法

```c++
static_assert(bool表达式,"错误信息");	//以前版本
static_assert(bool表达式)			//c++17
```

> 与assert不同，static_assert主要是用来在编译时检查重要的条件，里面的bool表达式只能是常量

* 不需要头文件

#### 使用说明

**案例**

```c++
int main()
{
    static_assert(sizeof(int*)==4,"it's not x86");	//用于检测是否在x86环境下编译
    return 0;
}
```

