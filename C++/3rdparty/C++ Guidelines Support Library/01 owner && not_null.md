---
create: 2024-04-24
---
# owner && not_null

## gsl::owner

​	`gsl::owner`是一个**封装了指针**的模板类，用于标记指针的所有权。

​	在智能指针不适用的情况下，或者是只需要裸指针的情况下，建议使用它。

​	它只是在**语义**上说明了，所有权的归属问题，但是并不会如智能指针一样自动析构。

```C++
gsl::owner<T*>;
```

### 所有权传递

​	下面演示了：

* 将指针所有权传递给函数对象
* 从函数中返回指针的所有权

```C++
gsl::owner<int*> get_ptr()
{
    return new int{1};
}

void set_ptr(gsl::owner<int*> p)
{}

int main()
{
    gsl::owner<int *> p = get_ptr();
}
```

### 代替unique_ptr不适用的场景

​	下面演示了一个虚函数的场景，两个类的虚函数如果使用`unique_ptr`会导致返回类型的不一致，从而报错。

```C++
#include <gsl/pointers>

class B
{
public:
    B() = default;
    virtual ~B() = default;
    virtual gsl::owner<B*> clone() const = 0;
protected:
    B(const B&) = default;
    B& operator=(const B&) = default;
    B(B&&) noexcept = default;
    B& operator=(B&&) noexcept = default;
    // ...
};

class D : public B
{
public:
    gsl::owner<D*> clone() const override
    {
        return new D{*this};
    }
}
```

## gsl::not_null

​	保证指针或智能指针仅保存非空值。

​	实现方式：在构造时检查指针是否为空，如果是，则调用`terminate`函数。

### 使用以检查非空值

​	下面演示了，函数不能返回或传入一个空指针作为参数。

```C++
#include <gsl/pointers>

gsl::not_null<int*> get_ptr()
{
    return new int{1};
}

void set_ptr(gsl::not_null<int*> p)
{}

int main()
{
    auto p = get_ptr();
    set_ptr(p.get());
}
```

### 使用智能指针的场景

​	使用智能指针的场景与裸指针一致，唯一不同在于，get函数但对于非平凡类型返回的是**引用**而非值。

```C++
template <typename T>
using value_or_reference_return_t = std::conditional_t<
	sizeof(T) < 2*sizeof(void*) && std::is_trivially_copy_constructible<T>::value,
	const T,
	const T&>;

constexpr details::value_or_reference_return_t<T> get() const;

//-----------------------------------------------------------------------------------------------

int main()
{
    gsl::not_null<std::unique_ptr<int>> p(std::make_unique<int>(1));
}
```

## `gsl::not_null<gsl::owner<T*>>`

​	两个指针模板可以结合使用，来代表非空的裸指针所有权传递。

```C++
void set_ptr(gsl::not_null<gsl::owner<int*>>) {}
```

## 参考资料

* [GSL/docs/headers.md at main · microsoft/GSL (github.com)](https://github.com/microsoft/GSL/blob/main/docs/headers.md#user-content-H-pointers-not_null)
* [每天5分钟 - 了解 C++ Guidelines Support Library - part1](https://www.bilibili.com/video/BV1gE421K7ez?spm_id_from=333.1245.0.0)