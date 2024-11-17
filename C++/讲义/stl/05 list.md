---
create: 2024-07-12
---
# list

​	list是一种离散的容器，它本质是一个双向链表，这意味着你可以在任意的地方插入和删除，没有额外的开销。

## usage

```C++
#include <list>

std::list<int> lst;

lst.push_back(1);  // 1
lst.push_front(1); // 1 1

auto it = std::find(lst.begin(), lst.end(), 1);

lst.insert(it, 2);		// 2 1 1
```

## 排序

​	list不支持std::sort，而是内置了sort函数。原因在于，其底层是链表，其空间不是连续的，因此迭代器就不是随机迭代器，不支持迭代器之间的直接比较大小（it2 - it1）。但是迭代器可以前后移动，因此还是可以进行排序的，只是需要用自带的排序。

```C++
lst.sort(); 		// 默认需要 operator<
```