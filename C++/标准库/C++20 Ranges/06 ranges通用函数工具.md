---
create: '2025-03-22'
modified: '2025-03-22'
---

# ranges通用函数工具

一个`range`，都可以使用这些通用的工具。

在标头 `<ranges>` 定义：

| 函数              | 描述                             |
| ----------------- | -------------------------------- |
| `ranges::begin`   | 返回指向范围起始的迭代器         |
| `ranges::end`     | 返回指示范围结尾的哨位           |
| `ranges::cbegin`  | 返回指向只读范围起始的迭代器     |
| `ranges::cend`    | 返回指示只读范围结尾的哨位       |
| `ranges::rbegin`  | 返回指向范围的逆向迭代器         |
| `ranges::rend`    | 返回指向范围的逆向尾迭代器       |
| `ranges::crbegin` | 返回指向只读范围的逆向迭代器     |
| `ranges::crend`   | 返回指向只读范围的逆向尾迭代器   |
| `ranges::size`    | 返回等于范围大小的整数           |
| `ranges::ssize`   | 返回等于范围大小的有符号整数     |
| `ranges::empty`   | 检查范围是否为空                 |
| `ranges::data`    | 获得指向连续范围的起始的指针     |
| `ranges::cdata`   | 获得指向只读连续范围的起始的指针 |