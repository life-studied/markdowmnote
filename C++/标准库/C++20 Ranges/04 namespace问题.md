---
create: '2025-02-19'
modified: '2025-02-19'
---

# namespace问题

## 1. namespace过长

```C++
std::ranges::sort(std::ranges::views::drop(std::ranges::views::reverse(v), 3));

// 解决
{
    using namespace std::ranges;
    sort(v | reverse | drop(3));
}
```

## 2. 别名

* `std::views` <=> `std::ranges::views`
* `std::ranges::single_view` <=> `std::ranges::views::single`

## 3. 引用混乱

```C++
using namespace std;
using namespace std::views;
std::reverse ? std::views::reverse;	// 无法区分
```