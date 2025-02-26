---
create: '2025-02-25'
modified: '2025-02-25'
---

# bufio.ReadWriter结构体

ReadWriter 结构只是存储了 bufio.Reader 和 bufio.Writer 类型的指针：

```go
type ReadWriter struct {
    *Reader
    *Writer
}
```

## 构造函数

`ReadWriter` 的实例化可以跟普通结构类型一样，也可以通过调用 `bufio.NewReadWriter` 函数来实现：

```go
func NewReadWriter(r *Reader, w *Writer) *ReadWriter {
    return &ReadWriter{r, w}
}
```