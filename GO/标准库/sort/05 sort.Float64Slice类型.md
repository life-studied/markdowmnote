---
create: '2025-02-26'
modified: '2025-02-26'
---

# sort支持的数据类型

`sort`包原生支持`[]int`、`[]float64`和`[]string`三种内建数据类型切片的排序操作，即不必我们自己实现相关的`Len()`、`Less()`和`Swap()`方法。

## quick to start

```go
package main

import (
	"fmt"
	"sort"
)

func main() {
	data := []float64{3, 5, 4, 6, 2, 1}
	if !sort.Float64sAreSorted(data) {
		sort.Float64s(data)
	}

	idx := sort.SearchFloat64s(data, 5)
	fmt.Println(data[idx]) // 5
}
```

## Float64Slice类型和[]float64类型

`sort`包定义了一个`Float64Slice`类型，并且实现了`sort.Interface`接口：

```golang
type Float64Slice []float64

func (p Float64Slice) Len() int           { return len(p) }
func (p Float64Slice) Less(i, j int) bool { return p[i] < p[j] || isNaN(p[i]) && !isNaN(p[j]) }
func (p Float64Slice) Swap(i, j int)      { p[i], p[j] = p[j], p[i] }
func (p Float64Slice) Sort() { Sort(p) }
func (p Float64Slice) Search(x float64) int { return SearchFloat64s(p, x) }
```

## Float64s() | Float64sAreSorted() | SearchFloat64s()

与Sort()、IsSorted()、Search()相对应的三个方法，应该使用下面特化的包装方法，而不是直接用上述的`Float64Slice`类型方法：

```go
func Float64s(a []float64) { Sort(Float64Slice(a)) }
func Float64sAreSorted(a []float64) bool
func SearchFloat64s(a []float64, x float64) int
```