# Union：标注可能的类型

​	一个参数可能是多种类型，最常见的莫过于可能是None。

## 使用Union

​	`Union[type1, type2, type3, ...]`

```python
from typing import Union

def f(x: Union[int, None]) -> int:
    if x is None:
        return 0
    return x

f(None)
f(0)
```

## 使用|符号（python3.10）

```python
def f(x: int | None) -> int:
    if x is None:
        return 0
    return x

f(None)
f(0)
```

