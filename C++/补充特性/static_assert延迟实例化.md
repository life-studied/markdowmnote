---
create: '2025-06-14'
modified: '2025-06-14'
---

# static_assert延迟实例化

在很多情况下，我们需要用到`static_assert`来防止用户误用某个东西。例如：`declval`是不允许调用的，它只是提供了一个某种类型的引用。

如果我们希望用户调用它时，能够通过`static_assert`报错，那么就能做到防止误用了。

[cppreference](https://cppreference.cn/w/cpp/utility/declval)给出了一个可能的实现，但是这个实现在MSVC下是不可行的，其它的未测试：

```C++
template<typename T>
typename std::add_rvalue_reference<T>::type declval() noexcept
{
    static_assert(false, "declval not allowed in an evaluated context");
}
```

## 模仿

我在MSVC下对上述代码进行了一个简单的模仿测试，这样会直接编译出错：

```C++
template <typename T>
int my_declval_test_static_assert() {
	static_assert(false, "nnn");
}

int main()
{
	decltype(my_declval_test_static_assert<int>());
	return 0;
}
// .cpp(3,16): error C2338: static_assert failed: 'nnn'
```

原因在于，这个里面直接使用了`static_assert(false)`去进行判断，而编译器只要检测到`static_assert(false)`就会报错。

但是我们又不能放弃`static_assert`的特性，因此这里应该采用`static_assert`的延迟实例化来制作：

```C++
template <class>
constexpr bool _Always_false = false;

template <typename T>
int my_declval_test_static_assert() {
	static_assert(_Always_false<T>, "nnn");
}

int main()
{
	decltype(my_declval_test_static_assert<int>());
	return 0;
}
```

通过对false的延迟实例化，由于`decltype`不会去实例化模板体，因此对模板的检查到外层模板自身就会终止，而不会进一步到内层的`_Always_false`上，因此对`static_assert`就不会做出检查，这样就可以避免编译失败。

而一旦调用了这个`declcal()`，内层的`_Always_false`模板就会被实例化，`static_assert(false)`就会触发。

## 补充

通过实现`static_assert`报错，可以取代在旧版本里SFINAE中难以调试模板的情况。

下面的代码等同于SFINAE，即不给出process的默认实现。但是通过`static_assert`报错，可以给出更加明确的信息，避免SFINAE编译调试的困难。

这种实现非常简洁，解耦了各种类型，也没有引入各种多余的内容。

```C++
#include <utility>
#include <string>

template <class>
constexpr bool _Always_false = false;

template <typename T>
std::string process(const T& val) {
	static_assert(_Always_false<T>, "This type never been defined.");
}

template <>
std::string process<std::string>(const std::string& s) {
	return s;
}

template <>
std::string process<int>(const int& s) {
	return std::to_string(s);
}

// 补充其它type的实现

int main()
{
	process(3);
	process(3.f);	// error: This type never been defined.
	return 0;
}
```