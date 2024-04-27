# Callable：标注可调用对象

## 任意的可调用对象

​	有时候需要传入一个可调用对象，但是实际传入的可能不符合要求，可以使用Callable来修饰：

```python
def my_dec(func: Callable):
    def wrapper(*args, **kwargs):
        print("start")
        ret = func(*args, **kwargs)
        print("end")
        return ret
    return wrapper

@my_dec
def add(a: int, b: int) -> int:
    return a + b
```

## 限制可调用对象的类型

​	Callable的类型取决于参数和返回值，因此语法为：`Callable[[type1, type2, ...] ret_type]`

```python
def my_dec(func: Callable[[int, int] int]):
    def wrapper(a: int, b: int) -> int:
        print("start")
        ret = func(a, b)
        print("end")
        return ret
    return wrapper

@my_dec
def add(a: int, b: int) -> int:
    return a + b
```

