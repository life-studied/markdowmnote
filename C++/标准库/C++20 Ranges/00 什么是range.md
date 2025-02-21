---
create: '2025-02-19'
modified: '2025-02-19'
---

# range

range是一个concept：

```C++
template <typename T>
concept range = requires(T& t) {
    ranges::begin(t);
    ranges::end(t);
};
```

* 任意符合`begin(t)`和`end(t)`的类型T都是`range`
  * `begin(t)`：调用的是类型T的成员函数`begin()`
  * `end(t)`：调用的是类型T的成员函数`end()`
* STL容器基本都符合这个要求
  * array/vector/list/set
* C array也符合这个要求