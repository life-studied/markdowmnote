---
create: '2025-07-08'
modified: '2025-07-08'
---

# ring

隶属于contain/ring包，和list不同，它只需要保持一个结构就行，因为任一元素都能代表一个ring。

```go
type Ring struct {
    next, prev *Ring
    Value      interface{}
}
```

## functions

```go
type Ring
    func New(n int) *Ring  // 初始化环
    func (r *Ring) Do(f func(interface{}))  // 循环环进行操作
    func (r *Ring) Len() int // 环长度
    func (r *Ring) Link(s *Ring) *Ring // 连接两个环
    func (r *Ring) Move(n int) *Ring // 指针从当前元素开始向后移动或者向前（n 可以为负数）
    func (r *Ring) Next() *Ring // 当前元素的下个元素
    func (r *Ring) Prev() *Ring // 当前元素的上个元素
    func (r *Ring) Unlink(n int) *Ring // 从当前元素开始，删除 n 个元素
```

## 参考资料

* [ring package - container/ring - Go Packages](https://pkg.go.dev/container/ring@go1.24.4)