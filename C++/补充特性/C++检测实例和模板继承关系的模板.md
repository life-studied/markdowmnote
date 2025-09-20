---
create: '2025-09-20'
modified: '2025-09-20'
---

# 检测实例和模板继承关系的模板

## 1. 检测实例

```C++
// 定义一个模板结构来检查是否是特定模板类的特化类
template <typename T, template <typename...> class Template>
struct is_a_instance_template_of : std::false_type {};

template <typename... Args, template <typename...> class Template>
struct is_a_instance_template_of<Template<Args...>, Template> : std::true_type {};

// 定义变量模板
template <typename T, template <typename...> class Template>
constexpr bool is_a_instance_template_of_v = is_a_instance_template_of<T, Template>::value;

// 使用
constexpr bool result = 
    is_a_instance_template_of_v<std::vector<int>, std::vector>;
```

## 2. 检测模板继承关系

C++提供`is_base_of`检测继承关系。

但是必须提供完整的类型，不支持对模板类进行检测（例如，对`std::vector`这种未实例化的模板进行检测）。

下面给出一个实现，用于检测某个类型是不是基于某个模板类的：

```C++
#include <iostream>
#include <type_traits>
#include <vector>
#include <tuple>

namespace detail {
	template <template<typename...> class U, typename... Args>
	auto extract_template_args_from(const U<Args...>&) -> std::tuple<Args...>;
}

template <typename T, template<typename...> class U, typename = void>
struct is_derived_from_template : std::false_type {};

template <typename T, template<typename...> class U>
struct is_derived_from_template<T, U,
	std::void_t<decltype(detail::extract_template_args_from<U>(std::declval<T>()))>
> : std::true_type {
};

template <typename T, template<typename...> class U>
inline constexpr bool is_derived_from_template_v = is_derived_from_template<T, U>::value;




class MyVector : public std::vector<int> {};
class MyVector2 {}; // 不从 std::vector 派生

int main() {
	static_assert(is_derived_from_template_v<MyVector, std::vector>, "MyVector should derive from std::vector");
	static_assert(!is_derived_from_template_v<MyVector2, std::vector>, "MyVector2 should not derive from std::vector");

	std::cout << "MyVector derives from std::vector: "
		<< std::boolalpha << is_derived_from_template_v<MyVector, std::vector> << std::endl;
	std::cout << "MyVector2 derives from std::vector: "
		<< is_derived_from_template_v<MyVector2, std::vector> << std::endl;

	return 0;
}
```