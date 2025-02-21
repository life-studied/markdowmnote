---
create: '2025-02-19'
modified: '2025-02-19'
---

# range adaptor

将一个range输出为一个view，同时进行一个操作。

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