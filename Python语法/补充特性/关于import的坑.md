## 关于import的坑

#### 1.在库路径下的文件

​	注：库路径：通过运行代码`import sys; print(sys.path)`查看

直接import对应的模块名即可

```python3
import os
import sys
```

#### 2.自定义的文件

目录结构如下：

```text
Tree
|____ m1.py
|____ m2.py
|____ Branch
     |____m3.py
     |____m4.py
```

##### 1.当m1要导入m3，m3要导入m4：

```python
from Branch import m3		#m1
import m4					#m3
```

报错：找不到m4

原因是：m3的import语句被先导入到m1中，然后在m1的目录下去找m4，所以找不到。

##### 2.解决方法：使用相对导入写法：

- `from . import module_name`。导入和自己同目录下的模块。
- `from .package_name import module_name`。导入和自己同目录的包的模块。
- `from .. import module_name`。导入上级目录的模块。
- `from ..package_name import module_name`。导入位于上级目录下的包的模块。
- 当然还可以有更多的`.`，每多一个点就多往上一层目录。

（所谓相对，是相对于源文件，而绝对导入，是绝对于在解释时的文件）

上述代码改为：

```python
from Branch import m3	#绝对导入，没有变
from . import m4		#相对导入
```

##### 3.如果m1也使用相对导入写法：

```python
from .Branch import m3	#相对导入
from . import m4		#相对导入
```

使用`python m1.py`会报错

通过在Tree目录的上一目录下使用`python -m Tree.m1`成功。

原因：

执行`python m1.py`命令后，当前目录所代表的包'.'变成了`__main__`。

而`python -m Tree.m1`不把m1.py当作运行入口文件，而是也把它当作被导入的模块，这就和非运行入口文件有一样的表现了。

