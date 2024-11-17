---
create: 2023-11-27
---
# find_if（+advance）

​	可以配合`lambda`表达式和`std::advance`使用。

```C++
#include <iostream>
#include <vector>
int main()
{
    std::vector<int> v = { 1,2,3,4,5,6 };
    auto end = v.begin();
    std::advance(end, 5);
    auto index = std::find_if(v.begin(), end, [](auto &x) {return x == 1; });
    std::cout << *index << std::endl;
}
```

