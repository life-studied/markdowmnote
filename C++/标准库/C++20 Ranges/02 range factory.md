---
create: '2025-02-19'
modified: '2025-02-19'
---

# range factory

`range factory`用于创建view。

## empty_view

用于创建一个空的view

```C++
empty_view<int> e;
static_assert(0 == e.size());
```

## single_view

用于创建只有一个元素的view（拷贝）

```C++
single_view sv(3.14159);
single_view sv_str(std::string("hello world"));
```

## iota_view

生成序列的view。

```C++
iota_view<int>();		// 0,1,2,...
iota_view(1);			// 1,2,3,...
iota_view(2, 5);		// [2, 5)
iota_view('a', 'a'+26);	// [a,z]
```