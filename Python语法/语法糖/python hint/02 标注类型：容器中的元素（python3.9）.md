---
create: 2024-04-27
---
# 标注类型：容器中的元素（python3.9）

​	有时候需要对容器进行类型标注，来保证容器中的每个元素都是一样的类型。

​	如果直接使用list或者tuple，是不能检测容器里的元素类型的。

```python
def my_sum(lst: list) -> int:
    total = 0
    for i in lst:
        total += i
    return total

my_sum([0, 1, 2])
my_sum([0, 1, '2'])		# bad
```

## 标注list中的元素（python3.9）

​	使用`list[int]`的方法进行标注：

```python
def my_sum(lst: list[int]) -> int:
    total = 0
    for i in lst:
        total += i
    return total

my_sum([0, 1, 2])
my_sum([0, 1, '2'])		# error
```

## 补充：3.9之前的方法

​	使用typing中的List。

```python
from typing import List

def my_sum(lst: List[int]) -> int:
    total = 0
    for i in lst:
        total += i
    return total

my_sum([0, 1, 2])
my_sum([0, 1, '2'])		# error
```

## 标注容器中的元素（sequence抽象版本）

​	有时候希望传入的可以是一个list，也可以是一个tuple，或者一个range，一个byte等等。可以使用更加抽象的容器：`Sequence`。

```python
from typing import Sequence

def my_sum(lst: Sequence[int]) -> int:
    total = 0
    for i in lst:
        total += i
    return total

my_sum([0, 1, 2])
my_sum((0, 1, 2))
my_sum(b'012')
my_sum(range(3))
```

## 标注dict的key和value

```python
def my_sum(d: dict[str, int]) -> int:
    total = 0
    for i in d.values():
        total += i
    return total

my_sum({"a": 1, "b": 2, "c": 3})
my_sum({"a": 1, "b": 2, "c": 'a'})  # error
```

