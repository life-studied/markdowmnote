## 函数快速获取类型（decltype）

#### 1.获取函数指针类型

##### 1.1 确定函数

对于一个已经确定的函数，可以直接使用decltype来获取其函数指针类型

```C++
int foo(double) { return 1; };

using funcPtr = decltype(&foo);		

int main()
{
    funcPtr a = foo;
    a(1.1);
    std::cout<<typeid(a).name()<<std::endl;
}
```

##### 1.2 模板函数

对于一个模板函数，也可以使用decltype来获取其函数类型。

```C++
template <typename T>
T foo(T t) { return t; };

template <typename T>
using newFoo = decltype(&foo<T>);

int main()
{
	newFoo<int> a = foo<int>;
	a(1);
    std::cout<<typeid(a).name()<<std::endl;
}
```

#### 2.获取函数返回值类型

##### 2.1 获取一般函数类型

```C++
#include <utility>
#include <iostream>

int foo(double);

using returnType = decltype(foo(std::declval<double>()));

int main()
{
    std::cout<<typeid(returnType).name()<<std::endl;
}
```

##### 2.2 获取类中函数类型

```C++
#include <utility>
#include <iostream>

class DefaultCons {
public:
	DefaultCons() { std::cout << "DefaultCons"<<std::endl; }
	int func(double b) { return 1; };
};

class NoDefaultCons {
public:
	NoDefaultCons() = delete;
	int func(double b) { return 1; };
};

int main()
{
	using returnType1 = decltype(DefaultCons().func(std::declval<double>()));
	using returnType2 = decltype(std::declval<NoDefaultCons>().func(std::declval<double>()));
	std::cout << typeid(returnType1).name() << std::endl;
	std::cout << typeid(returnType2).name() << std::endl;
}
```



