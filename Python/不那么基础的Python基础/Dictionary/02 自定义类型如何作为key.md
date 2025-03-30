---
create: '2025-03-30'
modified: '2025-03-30'
---

# 自定义类型如何作为key

## dict判断key obj是否相等的依据

1. 对传入的key obj进行`__hash__`，然后比较是否存在这个key
2. 如果存在这个key，不代表就一定相等，可能会发生hash collision，因此会继续对传入的key进行`__eq__`，比较dict里相同hash的key obj是否相等
    （实际上在这里还会先比较`id(key)`，如果id一致就直接返回了，不去比较`__eq__`）
3. 结论：只有`__hash__`和`__eq__`都相等，才是相等的。

## 一个简单的实现案例

根据上面的依据，我们只要保证两个应该相等的对象，它们的`__hash__`和`__eq__`都相等。

```python
class Position:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __hash__(self):
        return hash((self.x, self.y))	# 将x,y组成一个tuple，然后用build in计算hash
    
    def __eq__(self, other):
        return self.x == other.x and self.y == other.y
    
d = {}
pos1, pos2 = Position(0,1), Position(0,1)	# 相等的pos
d[pos1] = d.get(pos1, 0) + 1	# 对应位置的player++
d[pos2] = d.get(pos2, 0) + 1	# 对应位置的player++
```

## 参考资料

* [【python】自定义类型的对象，可以成为字典的键值么？这里有什么你从没想过的坑呢？_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV19Y411G7ox/?spm_id_from=333.788.recommend_more_video.4&vd_source=7ea28e304f19f399517ee153057d1f10)