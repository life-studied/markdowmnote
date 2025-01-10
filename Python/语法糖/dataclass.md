---
create: '2024-11-18'
modified: '2024-11-24'
---

# dataclass

dataclass是一种修饰符，用于修饰纯数据的类。其作用是自动进行init，以及生成`__repr__`和`__eq__`方法。

```python
@dataclass
class Person:
    name: str
    age: int
    
p1 = Person(name="A",age=11)
p2 = Person(name="A",age=11)
print(p1)
print(p2)
print(p1==p2)



@dataclass
class Config:
    db_host: str = "localhost"
    db_port: int = 3306
    debug_mode: bool = false
    max_conn: int = 20

myconf = Config(db_host="192.168.0.1")
```

## order=True

设置order属性会自动生成比大小的方法：`__lt__`、`__le__`、`__gt__`和`__ge__`。按照字段顺序依次比较。

```python
@dataclass
class Person:
    name: str
    age: int
    
p1 = Person(name="A",age=11)
p2 = Person(name="B",age=11)
p3 = Person(name="A",age=12)

print(p1<p2)
print(p1<p3)
```