---
create: '2025-01-29'
modified: '2025-01-29'
---

# 类模板参数推导（CTAD）（C++17）

CTAD全称是Class template argument deduction (CTAD)，类模版参数推导，你给定编译器一个推导指南，编译器就能根据构造时的参数的类型去自动完成类模板所需的模板参数推导。

```C++
Class(type_list)->Class<type_list>;
```

## 应用

### 辅助推导模板参数

最基础的使用就是在复杂类型时可以减少推导的代码：

```C++
std::vector<FooBar<int, const char*>> obj{a, b, c}; 

std::vector obj{a, b, c};
```

### 特定类型的推导修改

另一个常用的应用就是，对于某个或某些类型，我们希望将其作为另一个类型进行推导：

```C++
#include <iostream>
#include <type_traits>
#include <string>
template <typename T>
class A {
public:
	A(T t) : data(t) {}
	T data;
};

A(const char*)->A<std::string>;

int main()
{
	A a("hello world");		// 隐式生成std::string并进行赋值
	std::cout << a.data;
	return 0;
}
```

## 补充说明

一般来说，CTAD往往与构造函数紧密相连，因为它是根据构造时的参数去进行推导，但是这并非是绝对的说法，例如对于没有构造函数的聚合类型，它能直接依靠聚合类型的自动赋值特性进行推导：

```C++
template<class Ty, size_t size>
struct array {
	Ty* begin() { return arr; };
	Ty* end() { return arr + size; };
	Ty arr[size];
};

template<class Ty, class...Args>
	requires (std::same_as<Ty, Args>&&...)
array(Ty, Args...)->array<Ty, 1 + sizeof...(Args)>;

int main() {
	::array arr{ 1, 2, 3, 4, 5 };
	for (const auto& i : arr) {
		std::cout << i << ' ';
	}
}
```

但是一旦存在构造函数，这种特性就会消失。

如果加上构造函数依然需要符合上面的CTAD需要的话，则可以这样做：

```C++
template<class Ty, size_t size>
struct array {
	Ty* begin() { return arr; };
	Ty* end() { return arr + size; };
	Ty arr[size];

	template<class... Args>
	array(Ty first, Args... rest) : array(rest...){
		arr[size - sizeof...(Args) - 1] = first;
	}

	array() = default;
};

template<class Ty, class...Args>
	requires (std::same_as<Ty, Args>&&...)
array(Ty, Args...)->array<Ty, 1 + sizeof...(Args)>;

int main() {
	::array arr{ 1, 2, 3, 4, 5 };
	for (const auto& i : arr) {
		std::cout << i << ' ';
	}
}
```