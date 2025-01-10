---
create: 2024-11-08
modified: '2024-11-17'
---

# package与module

## 从import开始

在python中，module和package都是`Python.Object`。

区别在于，module一般代表一个文件，由import从文件中生成。而package代表一个文件夹，同样可以通过import生成。

```python
# start.py
import pkg
import pkg.moduleA

# dictory: /pkg/moduleA.py
```

## import发生了什么

```python
import test
```

1. 查看缓存：根据`test字符串`查找是否存在这个module，如果有就结束
2. 缓存未命中：从sys.path中寻找这个test.py文件
   1. 在test的独立命名空间中执行test.py里面的代码（对package则是`__init__.py`）
   2. 将该module缓存起来，存入到test这个变量中

### 缓存机制

由于缓存机制的存在，所以即使import多次，module里的代码只会被执行一次：

```python
import test
import test
import test

# 只执行一次："this is test"
```

### as和from

as是将最后一步的存入变量，变量名改成了我们想要的。

from则是为了更细致的导入一个module中的部分

## 操作系统层面

package就是一种特殊的module，它们的功能几乎一样，只是多了一个`__path__`属性。

从操作系统层级来看，package下可以有subpackage和module，而module应该是最末端的东西。

> 因此如果import一个package，做的事情**与上面一致**。唯一不同的是执行的是`__init__.py`下的代码。

## 参考资料

* https://www.bilibili.com/video/BV1K24y1k7XA