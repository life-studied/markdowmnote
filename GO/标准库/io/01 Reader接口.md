---
create: '2025-02-24'
modified: '2025-02-24'
---

# Reader接口

## 接口定义

```go
type Reader interface {
    Read(p []byte) (n int, err error)
}
```

* 作用：Read 将 len(p) 个字节读取到 p 中。它返回读取的字节数 n（0 <= n <= len(p)） 以及任何遇到的错误。

* 特性：
    * 暂存：即使 Read 返回的 n < len(p)，它也会在调用过程中使用 p 的全部作为暂存空间。
    * 立即返回：若一些数据可用但不到 len(p) 个字节，Read 会照例返回可用的数据，而不是等待更多数据。
* 返回值：
    * 当 Read 在成功读取 n > 0 个字节后遇到一个错误或 EOF (end-of-file)，它就会返回读取的字节数。它会从相同的调用中返回错误或从随后的调用中返回错误（同时 n == 0）
    * 也就是说，当 Read 方法返回错误时，不代表没有读取到任何数据。调用者应该处理返回的任何数据，之后才处理可能的错误。

## 使用

```go
func ReadFrom(reader io.Reader, num int) ([]byte, error) {
    p := make([]byte, num)
    n, err := reader.Read(p)
    if n > 0 {
        return p[:n], nil
    }
    return p, err
}

// 从标准输入读取
data, err = ReadFrom(os.Stdin, 11)

// 从普通文件读取，其中 file 是 os.File 的实例
data, err = ReadFrom(file, 9)

// 从字符串读取
data, err = ReadFrom(strings.NewReader("from string"), 12)
```