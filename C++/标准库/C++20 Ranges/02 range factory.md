---
create: '2025-02-19'
modified: '2025-03-22'
---

# range factory

`range factory`用于创建view。

## std::view::empty

用于创建一个空的view

```C++
empty_view<int> e;
static_assert(0 == e.size() && true == e.empty());
```

## std::view::single

用于创建只有一个元素的view（通过拷贝）

```C++
single_view sv(3.14159);
single_view sv_str(std::string("hello world"));
```

## std::view::iota

生成序列的view。

```C++
std::view::iota<int> v1();			// 0,1,2,...
std::view::iota v2(1);				// 1,2,3,...
std::view::iota v3(2, 5);			// [2, 5)
std::view::iota v4('a', 'a'+26);	// [a,z]
```

## std::view::istream

从任意类型的输入流中逐个读取数据。

```cpp
#include <ranges>
#include <iostream>
#include <sstream>

int main() {
    std::istringstream iss("1 2 3 4 5");
    auto issView = std::views::istream<int>(iss); // 创建一个从输入流读取 int 的 view

    for (auto elem : issView) {
        std::cout << elem << " "; // 输出 1 2 3 4 5
    }
    std::cout << "\n";
}
```

## std::view::repeat

重复产出同一值生成的序列组成的 `view`，无限重复

```C++
#include <ranges>
#include <iostream>

int main() {
    auto repeat = std::views::repeat(42); // 创建一个无限重复 42 的 view
    for (auto it = repeat.begin(); it != repeat.begin() + 5; ++it) {
        std::cout << *it << " "; // 输出 42 42 42 42 42
    }
    std::cout << "\n";
}
```

## std::view::cartesian_product

n 元笛卡尔积组成的 `view`

```C++
#include <ranges>
#include <iostream>
#include <vector>

int main() {
    std::vector<int> vec1 = {1, 2};
    std::vector<int> vec2 = {3, 4};

    auto cartesian = std::views::cartesian_product(vec1, vec2); // 创建笛卡尔积 view

    for (const auto& [a, b] : cartesian) {
        std::cout << "(" << a << ", " << b << ") "; // 输出 (1, 3) (1, 4) (2, 3) (2, 4)
    }
    std::cout << "\n";
}
```

## 附录

在标头 `<ranges>` 定义：

| 类/变量                                                      | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `ranges::empty_view` / `views::empty`                        | 无元素的空 `view`                                            |
| `ranges::single_view` / `views::single`                      | 含有具有指定值的单个元素的 `view`                            |
| `ranges::iota_view` / `views::iota`                          | 由通过重复对某个初值自增所生成的序列组成的 `view`            |
| `ranges::basic_istream_view` / `views::istream`              | 由在关联的输入流上相继应用 `operator>>` 获得的元素组成的 `view` |
| `ranges::repeat_view` / `views::repeat`                      | 由重复产出同一值生成的序列组成的 `view`                      |
| `ranges::cartesian_product_view` / `views::cartesian_product` | n 元笛卡尔积计算后的的元组组成的 `view`                      |