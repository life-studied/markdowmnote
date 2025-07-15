---
create: '2025-07-08'
modified: '2025-07-08'
---

# heap

隶属于contain/heap包

## 定义

基于sort包实现，只提供了接口：

```go
type Interface interface {
    sort.Interface
    Push(x interface{}) // add x as element Len()
    Pop() interface{}   // remove and return element Len() - 1.
}
```

也就是说，如果要实现heap接口，就要提供sort.Interface的三个接口：

* Len() int
* Less(i, j int) bool
* Swap(i, j int)

和heap.Interface的两个接口：

- Push(x interface{})
- Pop() interface{}

## functions

通过定义5个接口，就能使用heap了：

* **`func Init(h Interface)`**：初始化一个堆，将传入的容器类型调整为一个有效的堆结构。
* **`func Push(h Interface, x any)`**：将一个新元素 `x` 添加到堆中。
* **`func Pop(h Interface) any`**：从堆中移除并返回堆顶元素（最小堆或最大堆的根节点）。
* **`func Fix(h Interface, i int)`**：（当修改索引`i`的元素后）修复堆中索引为 `i` 的元素，使其重新满足堆的性质。
* **`func Remove(h Interface, i int) any`**：从堆中移除索引为 `i` 的元素，并返回该元素，同时修复堆结构以保持堆的性质。

## 大顶堆和小顶堆

如果 `Less(i, j int) bool` 方法的实现是：`h[i] > h[j]`，那么会按照大顶堆的规则组织。

反之是小顶堆。

## 案例

### 定义接口

```go
type IntHeap []int

func (h IntHeap) Len() int           { return len(h) }
func (h IntHeap) Less(i, j int) bool { return h[i] < h[j] }
func (h IntHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }

func (h *IntHeap) Push(x interface{}) {
    *h = append(*h, x.(int))
}

func (h *IntHeap) Pop() interface{} {
    old := *h
    n := len(old)
    x := old[n-1]
    *h = old[0 : n-1]
    return x
}
```

### 使用heap

```go
h := &IntHeap{2, 1, 5}
heap.Init(h)
heap.Push(h, 3)
heap.Pop(h)

// Fix
(*h)[2] = 1
heap.Fix(h, 2)
```