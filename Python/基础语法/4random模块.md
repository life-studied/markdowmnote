---
create: 2023-07-08
modified: '2025-01-10'
---

## random模块

```python
import random	#导入random模块
random.randint(1,10)	#randomint(下限，上限)，返回int
#注：交互模式会直接输出，编辑模式不会
```

> 警告：random使用的是伪随机数，种子来源是当前系统时间

```python
x=random.getstate()#获取已被固定的随机数种子
...
...
random.setstate(x)#重新设置种子进入
#此时的随机数会被复现
```