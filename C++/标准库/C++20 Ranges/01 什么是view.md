---
create: '2025-02-19'
modified: '2025-02-19'
---

# view

`view`是一种特殊的`range concept`。

* view是一种range，但是range不一定是view
* view通过`range factories`或者`range adaptors`创建
* view不持有数据
* view是懒惰计算（lazily-evaluated）
* view不一定是`read-only`的，取决于来源：真正的range的属性

## 创建view

```C++
std::vector v{1,2,3,4,5};
auto v_view = std:::view::reverse(v);
```

## 使用view

```C++
std::cout << *(v_view.begin() + 1) << std::endl;	// 4
*v_view.begin() = 0;	// 5 => 0
```