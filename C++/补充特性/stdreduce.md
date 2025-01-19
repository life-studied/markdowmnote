---
create: '2024-12-05'
modified: '2025-01-13'
---

# std::reduce

std::reduce是C++17中新增加的一个算法，用于将一个范围内的元素累加起来、相乘、组合等。它比手动循环处理的方式更加简洁和高效。

```C++
#include <iostream>
#include <vector>
#include <numeric>

int main()
{
    std::vector<int> numbers{ 1, 2, 3, 4 };
    double avg = std::reduce(numbers.begin(), numbers.end(), 0.0, std::plus<double>()) / numbers.size();
    std::cout << "Average: " << avg << std::endl;
    return 0;
}
```

在上面的例子中，使用std::reduce将vector中的元素累加起来，并将结果除以元素个数得到平均值。

参数：

* begin
* end
* init_val
* pred

用std::plus()作为二元操作符，初始值为0，表示将所有元素相加起来。