---
create: 2023-12-27
modified: '2025-01-23'
---

# 编译期判断STL类是否存在成员函数

## 1. 检测类是否存在push_back

* 只能检测stl，因为用到了`T::value_type`，只在stl中存在
* 可以通过修改push_back的参数类型来判断其它类

```C++
#include <iostream>
#include <list>
#include <map>
#include <set>
#include <string>
#include <vector>

template <typename>
using void_t = void;

template <typename T, typename V = void>
struct has_push_back:std::false_type {};

template <typename T>
struct has_push_back<T, void_t<decltype(std::declval<T>().push_back(std::declval<typename T::value_type>()))>>:std::true_type {};

int main() {
    std::cout << has_push_back<std::list<int>>::value << std::endl;
    std::cout << has_push_back<std::map<int, int>>::value << std::endl;
    std::cout << has_push_back<std::set<int>>::value << std::endl;
    std::cout << has_push_back<std::string>::value << std::endl;
    std::cout << has_push_back<std::vector<int>>::value << std::endl;
    return 0;
}
```

## 2. 检测stringstream是否存在对应参数的重载函数

* 注意：不能判断引用类型的参数，**或者修改T为T&**

```C++
template <typename>
using void_t = void;

template <typename T, typename V = void>
struct has_ss_type : std::false_type {};

template <typename T>
struct has_ss_type <T, void_t<decltype(std::declval<std::stringstream>().operator<<(std::declval<T>()))>> : std::true_type {};

/*
template <typename T>
struct has_ss_type <T, void_t<decltype(std::declval<std::stringstream>().operator<<(std::declval<T&>()))>> : std::true_type {};
*/

int main() {
    std::cout << has_ss_type<int>::value << std::endl;
    std::cout << has_ss_type<double>::value << std::endl;
    std::cout << has_ss_type<std::string>::value << std::endl;
    std::cout << has_ss_type<std::vector<int>>::value << std::endl;
    return 0;
}
```