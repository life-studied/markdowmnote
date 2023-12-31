# std::enable_if

## 1. 定义

```C++
template <bool, typename T=void>
struct enable_if {
};

template <typename T>
struct enable_if<true, T> { ///< 第一个模板参数为 true
  using type = T;           ///< type 才有定义
};
```

## 2. 使用案例

### 2.1 类型偏特化

```C++
template <typename T, typename Tag = void>
struct TCheck;

template <typename T>
struct TCheck<T, typename std::enable_if<T::value>::type> {
    static constexpr bool value = T::value;
};

int main()
{
    auto v = TCheck<std::is_integral<int>>::value;
    std::cout << v;
}
```

**案例**

​	下面的案例演示了允许string，整形和double类型的变量检测。

```C++
template <typename T, typename Tag = void>
struct TCheck;

template <typename T>
struct TCheck<T, typename std::enable_if<std::is_same_v<string, T>>::type> {
    static constexpr bool value = T::value;
};

template <typename T>
struct TCheck<T, typename std::enable_if<std::is_same_v<double, T>>::type> {
    static constexpr bool value = T::value;
};

template <typename T>
struct TCheck<T, typename std::enable_if<std::is_integral_v<T>>::type> {
    static constexpr bool value = T::value;
};
```

