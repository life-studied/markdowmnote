---
create: '2025-02-19'
modified: '2025-03-22'
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

## 附录

在标头 `<ranges>` 定义：

| 概念                          | 描述                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| `ranges::range`               | 指定类型为范围，即它同时提供 `begin` 迭代器和 `end` 哨位     |
| `ranges::borrowed_range`      | 指定类型为 `range` 而且能安全返回从该类型表达式获得的迭代器而无悬垂之虞 |
| `ranges::sized_range`         | 指定类型为可在常数时间内知晓大小的范围                       |
| `ranges::view`                | 指定范围为视图，即它拥有常数时间的复制/移动/赋值             |
| `ranges::input_range`         | 指定范围的迭代器类型满足 `input_iterator`                    |
| `ranges::output_range`        | 指定范围的迭代器类型满足 `output_iterator`                   |
| `ranges::forward_range`       | 指定范围的迭代器类型满足 `forward_iterator`                  |
| `ranges::bidirectional_range` | 指定范围的迭代器类型满足 `bidirectional_iterator`            |
| `ranges::random_access_range` | 指定范围的迭代器类型满足 `random_access_iterator`            |
| `ranges::contiguous_range`    | 指定范围的迭代器类型满足 `contiguous_iterator`               |
| `ranges::common_range`        | 指定范围拥有相同的迭代器和哨位类型                           |
| `ranges::viewable_range`      | 指定针对 `range` 的要求，令其可安全转换为 `view`             |
| `ranges::constant_range`      | 指定范围的所有元素只读                                       |