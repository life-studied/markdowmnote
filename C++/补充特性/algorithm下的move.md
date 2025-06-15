---
create: '2025-06-14'
modified: '2025-06-14'
---

# algorithm下的move

在C++中有三种move，utility下的`std::move`，algorithm下的`std::move`和ranges下的`std::ranges::move`。

## move的语义

algorithm下的move的语义是，将一个范围内的元素移动到以某个迭代器开头的地方：

```C++
template <class InputIt, class OutputIt>
OutputIt move(InputIt first, InputIt last, OutputIt d_first);
```

## 常用方法

常用的方法是，使用`std::make_move_iterator`构造移动迭代器，然后使用`std::back_inserter`构造插入迭代器，最后用std::move去移动元素：

* 移动迭代器`move_iterator`：解引用得到的是右值引用`T&&`
* 插入迭代器`back_inserter_iterator`：在尾部插入，会自动使用`push_back`去插入，否则可能会因容器空间不足而越界。

```C++
#include <iostream>
#include <vector>
#include <algorithm>
#include <iterator>

int main() {
	std::vector<std::string> source = { "apple", "banana", "cherry" };
	std::vector<std::string> destination;

	// 使用 std::make_move_iterator 将源容器中的元素移动到目标容器
	std::move(std::make_move_iterator(source.begin()),
		std::make_move_iterator(source.end()),
		std::back_inserter(destination));

	// 输出结果
	std::cout << "Source vector after move:\n";
	for (const auto& str : source) {
		std::cout << str << "\n";
	}

	std::cout << "Destination vector:\n";
	for (const auto& str : destination) {
		std::cout << str << "\n";
	}

	return 0;
}
```