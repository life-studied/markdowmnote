---
create: '2025-02-26'
modified: '2025-02-26'
---

# sort支持的数据类型

`sort`包原生支持`[]int`、`[]float64`和`[]string`三种内建数据类型切片的排序操作，即不必我们自己实现相关的`Len()`、`Less()`和`Swap()`方法。

## IntSlice类型与[]int类型

`sort`包定义了一个`IntSlice`类型，并且实现了`sort.Interface`接口：

```golang
type IntSlice []int
func (p IntSlice) Len() int           { return len(p) }
func (p IntSlice) Less(i, j int) bool { return p[i] < p[j] }
func (p IntSlice) Swap(i, j int)      { p[i], p[j] = p[j], p[i] }
//IntSlice类型定义了Sort()方法，包装了sort.Sort()函数
func (p IntSlice) Sort() { Sort(p) }
//IntSlice类型定义了SearchInts()方法，包装了SearchInts()函数
func (p IntSlice) Search(x int) int { return SearchInts(p, x) }
```

### sort.Ints()

提供的sort.Ints()方法使用了该IntSlice类型：

```goalng
func Ints(a []int) { Sort(IntSlice(a)) }
```

所以，对[]int切片排序是更常使用sort.Ints()，而不是直接使用IntSlice类型：

```golang
s := []int{5, 2, 6, 3, 1, 4} // 未排序的切片数据
sort.Ints(s) //排序后的s为[1 2 3 4 5 6]
```

### sort.SearchInts()

如果要查找整数x在切片a中的位置，相对于前面提到的`Search()`方法，`sort`包提供了`SearchInts()`:

```golang
func SearchInts(a []int, x int) int
```

注意，SearchInts()的使用条件为：**切片a已经升序排序**

```golang
s := []int{5, 2, 6, 3, 1, 4} // 未排序的切片数据
sort.Ints(s) //排序后的s为[1 2 3 4 5 6]
```