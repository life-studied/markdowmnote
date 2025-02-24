---
create: '2025-02-24'
modified: '2025-02-24'
---

# Writer接口

## 接口定义

```go
type Writer interface {
    Write(p []byte) (n int, err error)
}
```

* 作用：Write 将 len(p) 个字节从 p 中写入到基本数据流中。
* 返回值：它返回从 p 中被写入的字节数 n（0 <= n <= len(p)）以及任何遇到的引起写入提前停止的错误。若 Write 返回的 n < len(p)，它就必须返回一个 非nil 的错误。