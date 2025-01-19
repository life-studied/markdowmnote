---
create: 2024-04-27
modified: '2025-01-10'
---

# Any&None：函数返回值的区别

## 1. 默认情况

​	当函数没有对返回值进行标注时，返回的类型标注默认是`Any`类型。

### 没有返回值

​	但是如果函数没有返回值，那么实际上返回的是一个`None`：

```python
def f(a: list):
    a.append(1)
    
i: int = f([2])		# 不显示error，但是错误

def f(a: list) -> None:
    a.append(1)

i: int = f([2])		# 显示error
```

### 有多个返回值

​	有多个返回值，那么应该标注为Any，默认也可以，但是最好写上。



## 2. NoReturn

​	如果一个函数不会正常返回，例如raise一个exception，或者直接退出，那么可以将返回类型设置为NoReturn：

```python
from typing import NoReturn

def error() -> NoReturn:
    pass

error()
a = 3		# 不执行 dead code
```