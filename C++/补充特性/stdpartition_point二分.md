---
create: '2025-03-30'
modified: '2025-03-30'
---

# std::partition_point二分

`std::partition_point`是一个比`std::binary_search`更加灵活的二分查找方式，它允许传入一个谓词lambda，返回第一个不符合的迭代器。

给定范围 `[first, last)` 的大小为 `N`，该函数的时间复杂度为 O(log N)，因为它使用二分查找的方式。

```C++
#include <algorithm>
#include <array>
#include <iostream>

int main() {
    std::array<int, 9> v = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    auto is_even = [](int i) { return i % 2 == 0; };

    // 对数组进行分区
    std::partition(v.begin(), v.end(), is_even);

    // 查找分区点
    auto pp = std::partition_point(v.begin(), v.end(), is_even);
    std::cout << "Partition point is at index " << std::distance(v.begin(), pp)
              << "; v[" << std::distance(v.begin(), pp) << "] = " << *pp << '\n';

    // 输出分区结果
    std::cout << "First partition (all even elements): ";
    for (auto it = v.begin(); it != pp; ++it) {
        std::cout << *it << " ";
    }
    std::cout << "\nSecond partition (all odd elements): ";
    for (auto it = pp; it != v.end(); ++it) {
        std::cout << *it << " ";
    }
    std::cout << '\n';
}
```