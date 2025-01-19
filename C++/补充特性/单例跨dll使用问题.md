---
create: '2025-01-18'
modified: '2025-01-18'
---

# 单例跨dll使用问题

首先是结论：对于单例对象，C++标准**不保证跨dll依然是单例**。

```C++
inline my_class& get_instance()
{
    static my_class instance;
    return instance;
}
```

## dll

* 错误场景：如果在dll中存在该头文件，同时在项目的exe中也使用了该头文件，则大概率存在两份单例。
* 正确用法：如果要保证dll与exe共享一个单例，那么必须保证单例的实现仅存在于dll中通过接口暴露给外部。

## lib或obj

lib和obj会保证单例仅存在一份。

原因在于编译器会在编译期就查找到相同的符号，进行合并。即使这个函数存在于头文件并被多个cpp所include。