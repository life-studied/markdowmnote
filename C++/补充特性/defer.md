---
create: '2024-12-05'
modified: '2025-01-25'
---

# defer

如果要实现自动创建，可以通过编译器扩展，通过函数对defer变量实例进行命名，然后通过宏拓展来创建变量。类似于这样：`DEFER{ func(); };`。其中`DEFER`是一个宏。

```C++
template <class F>
class DeferClass {
public:
    DeferClass(F&& f) : m_func(std::forward<F>(f)) {}
    DeferClass(const F& f) : m_func(f) {}
    ~DeferClass() {
        m_func();
    }

    DeferClass(const DeferClass& e) = delete;
    DeferClass& operator=(const DeferClass& e) = delete;

private:
    F m_func;
};

#define _CONCAT(a, b) a##b
#define _MAKE_DEFER_(line) DeferClass _CONCAT(defer_placeholder, line) = [&]()

#undef DEFER
#define DEFER _MAKE_DEFER_(__LINE__)
```