# std::is_integral

​	用于判断是否是一个整数类型，包括bool类型。

## 使用

​	从C++17开始，用`is_integral_v`来代替`is_integral::value`

```C++
#include <iostream>
#include <type_traits>
 
class A {};
 
enum E : int {};
 
template <class T>
T f(T i)
{
    static_assert(std::is_integral<T>::value, "Integral required.");
    return i;
}
 
int main() 
{
    std::cout << std::boolalpha;
    std::cout << std::is_integral<A>::value << '\n';
    std::cout << std::is_integral<E>::value << '\n';
    std::cout << std::is_integral<float>::value << '\n';
    std::cout << std::is_integral<int>::value << '\n';
    std::cout << std::is_integral<bool>::value << '\n';
    std::cout << f(123) << '\n';
}
```

