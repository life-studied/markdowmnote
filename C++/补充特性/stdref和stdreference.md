---
create: 2023-09-09
---
# std::ref和std::reference

​	`std::ref`用于传递引用，用于不能显式地使用`&`进行传递的情况。例如，在创建`std::thread`时，总是会先复制一份参数再传递引用，而不是根据函数的参数列表直接传递需要的引用类型。为了解决这种不能直接传递引用的情况，可以使用`std::ref`来实现。

```C++
int func(int &a)
{
    return a;
}

int b;
std::thread t(func, std::ref(b));
```

## 1. 如何实现`std::ref`

​	std::ref是一个模板函数，具有三种实现，分别对应了其最常用的构建方法，拷贝运算和（删除）移动运算。

​	可以看到，其实现主要是生成了一个`std::reference_wrapper<_Ty>`的模板辅助类。

```C++
_EXPORT_STD template <class _Ty>
_NODISCARD _CONSTEXPR20 reference_wrapper<_Ty> ref(_Ty& _Val) noexcept {
    return reference_wrapper<_Ty>(_Val);
}

_EXPORT_STD template <class _Ty>
void ref(const _Ty&&) = delete;

_EXPORT_STD template <class _Ty>
_NODISCARD _CONSTEXPR20 reference_wrapper<_Ty> ref(reference_wrapper<_Ty> _Val) noexcept {
    return _Val;
}
```

## 2. `std::reference_wrapper<T>`

​	`std::reference_wrapper<T>`的实现很简单，其本质就是接收并保存了指向实际数据的指针，以及定义了隐式转换函数（`operator type()`）用于从这种类转换为原本传递进来的类型的引用。

```C++
template <class T>
class reference_wrapper {
public:
  // 类型
  using type = T;
 
  // 构造/复制/销毁
  template <class U, class = decltype(
    detail::FUN<T>(std::declval<U>()),
    std::enable_if_t<!std::is_same_v<reference_wrapper, std::remove_cvref_t<U>>>()
  )>
  constexpr reference_wrapper(U&& u) noexcept(noexcept(detail::FUN<T>(std::forward<U>(u))))
    : _ptr(std::addressof(detail::FUN<T>(std::forward<U>(u)))) {}
  reference_wrapper(const reference_wrapper&) noexcept = default;
 
  // 赋值
  reference_wrapper& operator=(const reference_wrapper& x) noexcept = default;
 
  // 访问
  constexpr operator T& () const noexcept { return *_ptr; }
  constexpr T& get() const noexcept { return *_ptr; }
 
  template< class... ArgTypes >
  constexpr std::invoke_result_t<T&, ArgTypes...>
    operator() ( ArgTypes&&... args ) const {
    return std::invoke(get(), std::forward<ArgTypes>(args)...);
  }
 
private:
  T* _ptr;
};
```

## 3. 总结

​	对于`std::ref`，其本质是返回一个引用包裹，里面有一个指向实际数据的成员指针。当需要被隐式转换为该指针所指向的引用类型时，会调用`operator type&()`的转换函数用于隐式转换。因此，整个结构看起来像是显式传递了引用一样。但是实际上是通过上述机制来实现的。

​	例如，对于`std::thread`类型，通过下面的代码：

```C++
int func(int &a)
{
    return a;
}

int b;
std::thread t(func, std::ref(b));
```

​	在传递b参数前就构造了一份`std::reference_wrapper`类型的结构体指向b，即使`std::thread`强制复制一份该结构体，也不影响真正的指向是正确的。