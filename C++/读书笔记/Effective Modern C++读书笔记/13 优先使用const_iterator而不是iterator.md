---
create: 2023-11-29
---
# 13 优先使用const_iterator而不是iterator

​	在`C++98`中，如果使用`const_iterator`可能导致语义可行但是编译不过的情况：

```C++
typedef std::vector<int>::iterator IterT; 				// typedef
typedef std::vector<int>::const_iterator ConstIterT; 	// defs

std::vector<int> values;

ConstIterT ci =
std::find(static_cast<ConstIterT>(values.begin()), 		// cast
static_cast<ConstIterT>(values.end()), 1983); 			// cast

values.insert(static_cast<IterT>(ci), 1998); 			// 可能无法编译
```

​	在`C++11`中，能正常编译这样的代码：

```C++
std::vector<int> values; // 和之前一样

auto it = // use cbegin
std::find(values.cbegin(),values.cend(), 1983); // and cend
values.insert(it, 1998);
```

