---
create: '2024-12-05'
modified: '2024-12-05'
---

# std::erase(C++20)

从C++20开始，允许使用std::erase直接删除想要删除的**所有元素**。

```C++
#include <iostream>
#include <list>
#include <algorithm>

int main()
{
	std::list<int> v = { 1,2,3,4,5,6,7 };
	std::erase(v, 1);
	for_each(v.begin(), v.end(), [](auto data) {std::cout << data << std::endl; });
	return 0;
}
```

## 原理

等价于下面这种写法（返回值表示被擦除的元素数）：

```C++
auto it = std::remove(c.begin(), c.end(), value);
auto r = c.end() - it;
c.erase(it, c.end());
return r;
```