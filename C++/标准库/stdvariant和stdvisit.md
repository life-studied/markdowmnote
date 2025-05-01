---
create: '2024-12-06'
modified: '2024-12-07'
---

# std::variant和std::visit

## std::variant

C++提供了类型安全的联合体std::variant代替原先C语言的Union。

1. 类型安全：variant不能访问错误的类型（`std::get`和`std::get_if`），而Union可以。
2. 状态维护：variant内部维护了当前存储了哪个类型的状态
3. 构造析构：variant允许类型的构造和析构函数自动调用，Union需要手动调用

```C++
#include <cassert>
#include <iostream>
#include <string>
#include <variant>
 
int main()
{
    std::variant<int, float> v, w;
    v = 42; // v 含 int
    int i = std::get<int>(v);
    assert(42 == i); // 成功
    w = std::get<int>(v);
    w = std::get<0>(v); // 与前一行效果相同
    w = v; // 与前一行效果相同
 
//  std::get<double>(v); // 错误：[int, float] 中无 double
//  std::get<3>(v);      // 错误：有效索引值为 0 与 1
 
    try
    {
        std::get<float>(w); // w 含 int 而非 float：会抛出异常
    }
    catch (const std::bad_variant_access& ex)
    {
        std::cout << ex.what() << '\n';
    }
    
    auto result = std::get_if<float>(w);	// nullptr if error
    if(result)
    {
        std::cout << *result;
    }
}
```

## std::visit

### 通用逻辑

该函数是C++为了能优雅方便地处理std::variant而设计的。它支持传入一个泛型lambda来实现不同类型的通用逻辑。

```C++
#include <iostream>
#include <variant>
#include <string>

int main() {
    std::variant<int, double, std::string> myVariant = "Hello, world!";
    std::visit([](auto&& arg) {
        std::cout << "The value is: " << arg << std::endl;
    }, myVariant);

    return 0;
}
```

### 特定类型的特殊处理

同时对于特定类型的特殊处理，也可以使用类型萃取来特殊处理：

#### std::is_same_v和if constexpr

使用标准库提供的类型萃取工具`std::is_same_v`：

```C++
#include <iostream>
#include <variant>
#include <string>
#include <type_traits>

int main() {
    std::variant<int, double, std::string> myVariant = "Hello, world!";
    std::visit([](auto&& arg) {
        if constexpr(std::is_same_v<decltype(arg), std::string>) {
         	std::cout << "str: " << arg << std::endl;   
        } else {
        	std::cout << "The value is: " << arg << std::endl;
        }
     }, myVariant);

    return 0;
}
```

#### std::is_same_v与std::true_type

如果标准低于C++17，可以使用std::true_type来实现类似的效果：

```C++
#include <iostream>
#include <variant>
#include <string>
#include <type_traits>

template <typename T>
void process(T&& arg, std::true_type)
{
    std::cout << "str: " << arg << std::endl; 
}

template <typename T>
void process(T && arg, std::false_type)
{
    std::cout << "The value is: " << arg << std::endl;
}

int main() {
    std::variant<int, double, std::string> myVariant = "Hello, world!";
    std::visit([](auto&& arg) {
        process(std::forward<decltype(arg)>(arg), std::is_same<decltype(arg), std::string>());
     }, myVariant);

    return 0;
}
```

#### 模板匹配

或者自己构造模板匹配来实现相同的类型萃取效果：

```C++
#include <iostream>
#include <variant>
#include <string>
#include <type_traits>

template <typename T>
void process(T&& arg)
{
	std::cout << "The value is: " << arg << std::endl;
}

template <>
void process<std::string>(std::string& arg)
{
	std::cout << "str: " << arg << std::endl;
}

int main() {
	std::variant<int, double, std::string> myVariant = "Hello, world!";
	std::visit([](auto&& arg) {
		process(arg);
		}, myVariant);

	return 0;
}
```