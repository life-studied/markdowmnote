---
create: '2025-02-19'
modified: '2025-03-22'
---

# range adaptor

将一个range输出为一个view，同时进行一个操作，常用操作：

* `std::views::reverse(r)`：反转range
* `std::views::take(r, n)`：获取range的前n个元素
* `std::views::drop(r, n)`：跳过range的前n个元素
* `std::views::filter(r, p)`：筛选range的元素（keep `if p(*it)==true`）
* `std::views::transform(r, f)`：将f应用于range的每个元素上

## 管道运算符

管道运算符可以代替传统的函数调用，使用|连接各个操作，使得它们之间更加易用和易写：

```C++
std::vector<int> v{1,2,3,4,5,6,7,8};
auto v_view = v | reverse | filter(isOdd) | take(2) | transform([](int x){ return x*x; });
// {49, 25}
for(auto& i : v_view) {
    std::cout << i << std::endl;
}
```

## 常用 可能用错的adaptor讲解

简单又常用的例如`tramsform`、`take`、`filter`、`reverse`这样的就跳过了，上面有用法的案例。

### std::views::join

用于将嵌套的范围“展平”的`view`。

```cpp
#include <ranges>
#include <iostream>
#include <vector>

int main() {
    std::vector<std::vector<int>> nestedVec = {{1, 2}, {3, 4}, {5}};
    auto joinView = nestedVec | std::views::join; // 展平嵌套的 vector

    for (int i : joinView) {
        std::cout << i << " "; // 输出 1 2 3 4 5
    }
    std::cout << "\n";
}
```

### std::views::split

用某个分隔符切割 `view` 所获得的`subrange`的 `view`集合。

```C++
#include <ranges>
#include <iostream>
#include <vector>

int main() {
    std::vector<int> vec = {1, 2, 3, 0, 4, 5, 0, 6};
    auto splitView = vec | std::views::split(0); // 以 0 为分隔符分割

    for (const auto& subRange : splitView) {
        for (int i : subRange) {
            std::cout << i << " ";
        }
        std::cout << "| ";
    }
    //	1 2 3 | 4 5 | 6 |
    std::cout << "\n";
    
    // ======================================
    std::string str("123 321 1234567");

	auto split_res = str | std::views::split(' ');
	for (const auto& subrange_v : split_res) {
		for (auto& data : subrange_v)
			std::cout << data;
		std::cout << "\n-----------" << std::endl;
	}
    
	//123
    //----------
    //321
    //----------
    //1234567
    //----------
}
```

### std::views::elements

用于从`tuple-like`的`ranges`中，选取每个 tuple 的第 N 个元素的 `view`。

```cpp
#include <ranges>
#include <iostream>
#include <vector>
#include <tuple>

int main() {
    std::vector<std::tuple<int, char>> vec = {{1, 'a'}, {2, 'b'}, {3, 'c'}};
    auto elementsView = vec | std::views::elements<0>; // 提取每个 tuple 的第一个元素

    for (int i : elementsView) {
        std::cout << i << " "; // 输出 1 2 3
    }
    std::cout << "\n";
}
```

### std::views::keys | std::views::values

用于获取pair的key的view。

用于获取pair的value的view。

```C++
#include <ranges>
#include <iostream>
#include <vector>
#include <utility>

int main() {
    std::vector<std::pair<int, char>> vec = {{1, 'a'}, {2, 'b'}, {3, 'c'}};
    auto keysView = vec | std::views::keys; // 提取每个 pair 的第一个元素
    auto valuesView = vec | std::views::values; // 提取每个 pair 的第二个元素

    for (int i : keysView) {
        std::cout << i << " "; // 输出 1 2 3
    }
    
    for (char c : valuesView) {
        std::cout << c << " "; // 输出 a b c
    }
    
    std::cout << "\n";
}
```

### std::ranges::owning_view

`owning_view`是将各种`range`的特性抹去，变成一个纯粹的拥有数据，并且用于遍历的view。

```C++
#include <ranges>
#include <string>
#include <vector>
#include <iostream>
#include <array>
#include <list>
#include <map>

int main()
{
	// std::array
	std::array<int, 5> arr = { 1, 2, 3, 4, 5 };
	auto owningView1 = std::ranges::owning_view(std::move(arr));

	// std::vector
	std::vector<int> vec = { 1, 2, 3, 4, 5 };
	auto owningView2 = std::ranges::owning_view(std::move(vec));

	// std::list
	std::list<int> lst = { 1, 2, 3, 4, 5 };
	auto owningView3 = std::ranges::owning_view(std::move(lst));

	// std::map
	std::map<int, std::string> myMap = { {1, "one"}, {2, "two"}, {3, "three"} };
	auto owningView4 = std::ranges::owning_view(std::move(myMap));

}
```

### views::all和ranges::ref_view的区别

虽然它们似乎都是对一个range进行view转化。

但是区别在于：

* 前者是操作，后者是实质

* 前者是可以用管道运算符进行链接的一个操作，将一个range转为一个view

* 而后者则是实质上做的事情，所有的range转为view的本质都是通过这个包了一层：

    `std::ranges::ref_view<std::vector<int>>`

```C++
std::vector<int> v{1,2,3,4,5,6};

auto all_view = v | std::views::all;	// std::ranges::ref_view<std::vector<int>>
```

## 附录

在标头 `<ranges>` 定义：

| 类/变量                                         | 描述                                                         |
| ----------------------------------------------- | ------------------------------------------------------------ |
| `ranges::range_adaptor_closure`                 | 用于定义范围适配器闭包对象的辅助基类模板                     |
| `views::all`                                    | 包含 `range` 的所有元素的 `view`                             |
| `ranges::ref_view`                              | 某个其他 `range` 的元素的 `view`                             |
| `ranges::owning_view`                           | 拥有某 `range` 的独占所有权的 `view`                         |
| `ranges::filter_view` / `views::filter`         | 由 `range` 中满足某个谓词的元素构成的 `view`                 |
| `ranges::transform_view` / `views::transform`   | 对序列的每个元素应用某个变换函数的 `view`                    |
| `ranges::take_view` / `views::take`             | 由另一 `view` 的前 N 个元素组成的 `view`                     |
| `ranges::take_while_view` / `views::take_while` | 由另一 `view` 的到首个谓词返回 `false` 为止的起始元素组成的 `view` |
| `ranges::drop_view` / `views::drop`             | 由另一 `view` 跳过首 N 个元素组成的 `view`                   |
| `ranges::drop_while_view` / `views::drop_while` | 由另一 `view` 跳过元素的起始序列，直至首个谓词返回 `false` 的元素组成的 `view` |
| `ranges::join_view` / `views::join`             | 由拉平 `range` 组成的 `view` 所获得的序列构成的 `view`       |
| `ranges::split_view` / `views::split`           | 用某个分隔符切割另一 `view` 所获得的子范围的 `view`          |
| `ranges::lazy_split_view` / `views::lazy_split` | 用某个分隔符切割另一 `view` 所获得的子范围的 `view`          |
| `views::counted`                                | 从迭代器和计数创建子范围                                     |
| `ranges::common_view` / `views::common`         | 转换 `view` 为 `common_range`                                |
| `ranges::reverse_view` / `views::reverse`       | 以逆序迭代另一双向视图上的元素的 `view`                      |
| `ranges::elements_view` / `views::elements`     | 选取 `tuple-like` 值组成的 `view` 和数值 N，产生每个 tuple 的第 N 个元素的 `view` |
| `ranges::keys_view` / `views::keys`             | 选取 pair 式值组成的 `view` 并产生每个 pair 的第一元素的 `view` |
| `ranges::values_view` / `views::values`         | 选取 pair 式值组成的 `view` 并产生每个 pair 的第二元素的 `view` |