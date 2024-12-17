---
create: '2024-12-08'
modified: '2024-12-08'
---

# std::true_type

```C++
template <typename T>
struct is_integral : std::false_type {};

template<>
struct is_integral<int> : std::true_type {};
```