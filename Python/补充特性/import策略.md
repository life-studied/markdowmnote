---
create: 2023-07-08
modified: '2024-11-17'
---

# import策略

> package代码使用相对导入，入口代码使用绝对导入。

## 核心 sys.path

在python中，import一个包都是从`sys.path`中去寻找。查找顺序从前往后找，找到就结束。

这个变量包含了三个路径：**入口文件的路径**、标准库与第三方库。

> 一种简单粗暴的方式就是通过添加sys.path使得能找到需要路径下的module。

### 入口路径

问题：下面的代码如果执行，则找不到moduleB，即使moduleA和moduleB在同一个路径下。

> 原因正是sys.path并不包含moduleA的路径，而是只有入口路径start.py

```python
# /start.py
from pkg.subPkg1 import moduleA

# /pkg/subPkg1/moduleA
import moduleB

# sys.path:['d:\\codeSpace\\bad_code\\t_py', 'D:\\ProgrammingSoftware\\Python3\\python311.zip', 'D:\\ProgrammingSoftware\\Python3\\DLLs', 'D:\\ProgrammingSoftware\\Python3\\Lib', 'D:\\ProgrammingSoftware\\Python3', 'C:\\Users\\10654\\AppData\\Roaming\\Python\\Python311\\site-packages', 'D:\\ProgrammingSoftware\\Python3\\Lib\\site-packages', 'D:\\ProgrammingSoftware\\Python3\\Lib\\site-packages\\win32', 'D:\\ProgrammingSoftware\\Python3\\Lib\\site-packages\\win32\\lib', 'D:\\ProgrammingSoftware\\Python3\\Lib\\site-packages\\Pythonwin']
```

## 相对导入

### 语法区分

绝对导入是`import pkg.module`或者`from pkg.module import object`，总之就是可以根据一个确定的string来找到module。

相对导入则是`from .module import object`，总之就是从一个已知的package路径+module名寻找object。

原理是通过`__package__`拿到当前的绝对路径，加上相对导入的from的module名，转换成一个确定的string，再去sys.path中去做绝对导入。

### 避开sys.path

使用相对导入可以避开`sys.path`中入口路径不固定的问题。

即不管从哪个文件进入，都会从本module的`__package__`中去寻找。

语法：`from 相对路径 import module`

```python
# /start.py
from pkg.subPkg1 import moduleA

# /pkg/subPkg1/moduleA
from . import moduleB
from ..subPkg2 import moduleX
print(f"moduleA.__package__={__package__}")
# moduleA.py -> None
# start.py	 -> pkg.subPkg1

# /pkg/subPkg2/moduleX
print("This is moduleX")
```

注意：入口文件的`__package__`会被设置为`None`，`__name__`会被设置为`__main__`。

也就是说，库文件如果直接运行，则可能因为相对导入的方式，没有自己的`__package__`，进而无法导入其它module。
正确的方法是在外面新建一个start来启动。

## 参考资料

* https://www.bilibili.com/video/BV1K24y1k7XA
* https://www.bilibili.com/video/BV1p3DVY6E5A