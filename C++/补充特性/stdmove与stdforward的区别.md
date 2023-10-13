# std::move与std::forward的区别

## 1. 语义上的区别

* std::move：将一个类型强制转换成右值引用，无论其原本的类型
* std::forward：将一个类型根据其原本类型强转回去
  * 因为任意类型R匹配上T&&都会变成左值R&&，但是能拿到其原有类型R
  * R可能是一个&，也可能是一个&&

但是，它们都用到了`remove_reference`来实现。

## 2. 实现上的区别

​	注意：static_cast<type>()不会导致引用折叠，导致引用折叠的是type&&;（type = T\T&\T&&）。

### 2.1 std::move

```C++
template <typename T>
typename remove_reference<T>::type&& move(T&& t) {
    return static_cast<typename remove_reference<T>::type &&>(t);
}
```

**struct remove_reference**

​	用于提取基本类型。

```C++
//原始的，最通用的版本
template <typename T> struct remove_reference{
    typedef T type;  //定义T的类型别名为type
};

//部分版本特例化，将用于左值引用和右值引用
template <class T> struct remove_reference<T&> //左值引用
{ typedef T type; }

template <class T> struct remove_reference<T&&> //右值引用
{ typedef T type; }

//别名
template <class T>
using remove_reference_t = typename remove_reference<T>::type;
```

---

### 2.2 std::forward

​	基本通过第一个去匹配，不论是左值还是右值

```C++
template<class T>
constexpr T&& forward(std::remove_reference_t<T>& arg) noexcept{
    // forward an lvalue as either an lvalue or an rvalue
    return (static_cast<T&&>(arg));
}

template<class T>
constexpr T&& forward(std::remove_reference_t<T>&& arg) noexcept{
    // forward an rvalue as an rvalue
    return (static_cast<T&&>(arg));
}
```

## 3. 案例分析

​	下面的案例演示了使用std::forward的必要性。

```C++
#include <cstdio>
#include <type_traits>

void foo(int&)
{
	puts("foo(int&)");
}

void foo(int&&)
{
	puts("foo(int&&)");
}

template <typename T>
void bar(T&& s)
{
	//foo(std::forward<T>(s));
}

int main() {
	int temp{};
	int&& c = 1;
	bar(temp);		//左值，匹配左值引用
	bar(int(1));	//右值，匹配右值引用
	bar(c);			//注意：右值引用是一个左值，匹配左值引用
}

```



## 补充：源码实现

### 1.std::move

```C++
_EXPORT_STD template <class _Ty>
_NODISCARD _MSVC_INTRINSIC constexpr remove_reference_t<_Ty>&& move(_Ty&& _Arg) noexcept {
    return static_cast<remove_reference_t<_Ty>&&>(_Arg);
}
```

### 2.std::forward

```C++
_EXPORT_STD template <class _Ty>
_NODISCARD _MSVC_INTRINSIC constexpr _Ty&& forward(remove_reference_t<_Ty>& _Arg) noexcept {
    return static_cast<_Ty&&>(_Arg);
}

_EXPORT_STD template <class _Ty>
_NODISCARD _MSVC_INTRINSIC constexpr _Ty&& forward(remove_reference_t<_Ty>&& _Arg) noexcept {
    static_assert(!is_lvalue_reference_v<_Ty>, "bad forward call");
    return static_cast<_Ty&&>(_Arg);
}
```

**typename remove_reference_t**

```C++
_EXPORT_STD template <class _Ty>
struct remove_reference {
    using type                 = _Ty;
    using _Const_thru_ref_type = const _Ty;
};

template <class _Ty>
struct remove_reference<_Ty&> {
    using type                 = _Ty;
    using _Const_thru_ref_type = const _Ty&;
};

template <class _Ty>
struct remove_reference<_Ty&&> {
    using type                 = _Ty;
    using _Const_thru_ref_type = const _Ty&&;
};

_EXPORT_STD template <class _Ty>
using remove_reference_t = typename remove_reference<_Ty>::type;
```

