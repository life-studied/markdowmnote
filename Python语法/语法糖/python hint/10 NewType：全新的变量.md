# NewType：全新的变量

​	typing中可以使用NewType来创建一个全新的type，它不是别名，而是一个类似于struct的封装，阻止了其与真实类型的交互。

## 好处：更强的类型限制

```python
from typing import NewType

UserId = NewType("UserId", int)
AttackPoint = NewType("AttackPoint", int)

class Player:
    def __init(
    	self,
    	uid: UserId
    	attack: AttackPoint
    ):
        self.uid = uid
        self.attack = attack
    
    def update_attack(self, atk: AttackPoint):
        self.uid = atk		# error, 类型不匹配
```

## 坏处：不能进行隐式的转换

​	必须进行显式的类型转换才行。

```python
from typing import NewType

UserId = NewType("UserId", int)
AttackPoint = NewType("AttackPoint", int)

class Player:
    def __init(
    	self,
    	uid: UserId
    	attack: AttackPoint
    ):
        self.uid = uid
        self.attack = attack
    
    def update_attack(self, atk: AttackPoint):
        self.attack = atk
        
p = Player(UserId(1), AttackPoint(100))		# 必须进行显式的类型转换
```

