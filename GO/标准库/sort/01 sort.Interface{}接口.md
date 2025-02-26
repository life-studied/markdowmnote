---
create: '2025-02-26'
modified: '2025-02-26'
---

# sort.Interface{}接口

对数据集合（包括自定义数据类型的集合）排序需要实现sort.Interface接口的三个方法：

```go
type Interface interface {
        // 获取数据集合元素个数
        Len() int
        // 如果i索引的数据小于j所以的数据，返回true，不会调用
        // 下面的Swap()，即数据升序排序。
        Less(i, j int) bool
        // 交换i和j索引的两个元素的位置
        Swap(i, j int)
}
```