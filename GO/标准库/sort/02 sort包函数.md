---
create: '2025-02-26'
modified: '2025-02-26'
---

# sort函数

## sort.Sort()

数据集合实现了`sort.Interface{}`后，即可调用该包的Sort()方法进行排序。 Sort()方法定义如下：

```go
func Sort(data Interface)
```

## sort.IsSorted()

```go
func IsSorted(data Interface) bool {
    n := data.Len()
    for i := n - 1; i > 0; i-- {
        if data.Less(i, i-1) {
            return false
        }
    }
    return true
}
```

## sort.Reverse()

此外，`sort`包提供了`Reverse()`方法，可以允许将数据按`Less()`定义的排序方式逆序排序，而不必修改`Less()`代码：

```go
func Reverse(data Interface) Interface
```

### 实现原理

可以看到`Reverse()`返回的一个`sort.Interface`接口类型，整个`Reverse()`的内部实现比较有趣：

```golang
//定义了一个reverse结构类型，嵌入Interface接口
type reverse struct {
    Interface
}

//reverse结构类型的Less()方法拥有嵌入的Less()方法相反的行为
//Len()和Swap()方法则会保持嵌入类型的方法行为
func (r reverse) Less(i, j int) bool {
    return r.Interface.Less(j, i)
}

//返回新的实现Interface接口的数据类型
func Reverse(data Interface) Interface {
    return &reverse{data}
}
```

## sort.Search()

Search()方法使用**“二分查找”**算法来搜索某指定切片[0:n]，并返回能够使`f(i)=true`的最小的`i`值。若找不到返回`n`。

```go
func Search(n int, f func(int) bool) int
```