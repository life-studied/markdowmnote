---
create: '2025-03-30'
modified: '2025-03-30'
---

# key在不在dict里

在`Dictionary`的使用中，我们经常会遇到一个情况：不确定一个key是否存在。

对于这个情况，我们一般会有好几种选择：

* 使用`if key in d`的判断
* 使用`d.get(key)`的内置函数
* 使用`try except KeyError`的异常处理

## get——我们希望有默认值

如果我们希望，在没有这个key的时候，可以有一个默认值，那么get就是一个合适的选择。

```python
d = {'a': 0}
x = d.get('a', 0) + 1	# 如果key不存在，value为default，此处是0
```

## if key in d——特殊的处理方式

如果我们希望，在没有key的时候，逻辑会发生一个特殊的情况，那么用if判断就是合适的：

```python
d = {'a': 0}
if 'a' in d:
    x = 1
else:
    x = len(d)	# 特殊的逻辑
```

## try except KeyError——key本应该在字典里

如果我们希望，在检查key的时候，出现了不太可能的情况：key不在字典里，那么用except的语义就是合适的：

```python
d = {'a': 0}
try:
    x = d['a'] + 1
except KeyError:
    x = 1
```