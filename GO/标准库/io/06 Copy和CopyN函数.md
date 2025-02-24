---
create: '2025-02-24'
modified: '2025-02-24'
---

# Copy和CopyN函数

## **Copy 函数**

```go
func Copy(dst Writer, src Reader) (written int64, err error)
```

* 作用：Copy 将 src 复制到 dst，直到在 src 上到达 EOF 或发生错误。它返回复制的字节数，如果有的话，还会返回在复制时遇到的第一个错误。
* 返回值：成功的 Copy 返回 err == nil，而非 err == EOF。由于 Copy 被定义为从 src 读取直到 EOF 为止，因此它不会将来自 Read 的 EOF 当做错误来报告。
* 其它：若 dst 实现了 ReaderFrom 接口，其复制操作可通过调用 dst.ReadFrom(src) 实现。此外，若 src 实现了 WriterTo 接口，其复制操作可通过调用 src.WriteTo(dst) 实现。

## **CopyN 函数**

```go
func CopyN(dst Writer, src Reader, n int64) (written int64, err error)
```

* 作用：CopyN 将 n 个字节从 src 复制到 dst。 
* 返回值：它返回复制的字节数以及在复制时遇到的最早的错误。由于 Read 可以返回要求的全部数量及一个错误（包括 EOF），因此 CopyN 也能如此。
* 其它：若 dst 实现了 ReaderFrom 接口，复制操作也就会使用它来实现。