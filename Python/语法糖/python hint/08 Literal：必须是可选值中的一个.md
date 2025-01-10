---
create: 2024-04-27
modified: '2024-11-17'
---

# Literal：必须是可选值中的一个

## 1. 检查值是否符合

​	有时候不想使用enum来规定，但是需要对传入的参数进行检查：

```python
from typing import Literal

class Person:
    def __init__(
    	self,
    	name: str,
    	gender: Literal["male", "female"]		# check
    ):
        self.name = name
        self.gender = gender

a = Person("Bob", "male")
b = Person("Bob", "woman")	# error
```

## 2. 变量不符合Literal类型

​	直接传入字面量是可以的，但是如果使用变量就会出错：

```python
from typing import Literal

class Person:
    def __init__(
    	self,
    	name: str,
    	gender: Literal["male", "female"]		# check
    ):
        self.name = name
        self.gender = gender

g = "female"
a = Person("Bob", g)	# error, g is str, not Literal["male", "female"]
```

## 3. 对变量进行Literal标注

​	为了解决上面的问题，对变量也需要标注相同的Literal类型：

```python
from typing import Literal

class Person:
    def __init__(
    	self,
    	name: str,
    	gender: Literal["male", "female"]		# check
    ):
        self.name = name
        self.gender = gender

g: Literal["male", "female"] = "female"
a = Person("Bob", g)		# ok
```

## 4. 类型别名

​	更普遍意义上，为了保证代码的可用性，会对类型进行别名，在一处定义，多处使用：

```python
from typing import Literal

GenderType = Literal["male", "female"]

class Person:
    def __init__(
    	self,
    	name: str,
    	gender: GenderType		# check
    ):
        self.name = name
        self.gender = gender

g: GenderType = "female"
a = Person("Bob", g)		# ok
```