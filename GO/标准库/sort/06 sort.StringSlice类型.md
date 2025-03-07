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
	data := []string{"a", "b", "c", "d", "e"}
	if !sort.StringsAreSorted(data) {
		sort.Strings(data)
	}

	idx := sort.SearchStrings(data, "c")
	fmt.Println(data[idx]) // c
}
```

## StringSlice类型和[]string类型

`sort`包定义了一个`StringSlice`类型，并且实现了`sort.Interface`接口：

```golang
type StringSlice []string

func (p StringSlice) Len() int           { return len(p) }
func (p StringSlice) Less(i, j int) bool { return p[i] < p[j] }
func (p StringSlice) Swap(i, j int)      { p[i], p[j] = p[j], p[i] }
func (p StringSlice) Sort() { Sort(p) }
func (p StringSlice) Search(x string) int { return SearchStrings(p, x) }
```

## Strings() | StringsAreSorted() | SearchStrings()

与Sort()、IsSorted()、Search()相对应的三个方法，应该使用下面特化的包装方法，而不是直接用上述的`StringSlice`类型方法：

```golang
func Strings(a []string) { return Sort(StringSlice(a)) }
func StringsAreSorted(a []string) bool
func SearchStrings(a []string, x string) int
```