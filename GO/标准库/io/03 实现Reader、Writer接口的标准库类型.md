---
create: '2025-02-24'
modified: '2025-02-24'
---

# 实现Reader、Writer接口的标准库类型

| 类型                         | 实现的接口                 | 所属包           | 常用程度 |
| :--------------------------- | :------------------------- | :--------------- | :------- |
| `os.File`                    | `io.Reader` 和 `io.Writer` | `os`             | 常用     |
| `strings.Reader`             | `io.Reader`                | `strings`        | 常用     |
| `bufio.Reader`               | `io.Reader`                | `bufio`          | 常用     |
| `bufio.Writer`               | `io.Writer`                | `bufio`          | 常用     |
| `bytes.Buffer`               | `io.Reader` 和 `io.Writer` | `bytes`          | 常用     |
| `bytes.Reader`               | `io.Reader`                | `bytes`          | 常用     |
|                              |                            |                  |          |
| `compress/gzip.Reader`       | `io.Reader`                | `compress/gzip`  | 中等     |
| `compress/gzip.Writer`       | `io.Writer`                | `compress/gzip`  | 中等     |
|                              |                            |                  |          |
| `crypto/cipher.StreamReader` | `io.Reader`                | `crypto/cipher`  | 较低     |
| `crypto/cipher.StreamWriter` | `io.Writer`                | `crypto/cipher`  | 较低     |
| `crypto/tls.Conn`            | `io.Reader` 和 `io.Writer` | `crypto/tls`     | 中等     |
|                              |                            |                  |          |
| `encoding/csv.Reader`        | `io.Reader`                | `encoding/csv`   | 中等     |
| `encoding/csv.Writer`        | `io.Writer`                | `encoding/csv`   | 中等     |
|                              |                            |                  |          |
| `mime/multipart.Part`        | `io.Reader`                | `mime/multipart` | 中等     |
|                              |                            |                  |          |
| `io.LimitedReader`           | `io.Reader`                | `io`             | 较低     |
| `io.PipeReader`              | `io.Reader`                | `io`             | 中等     |
| `io.PipeWriter`              | `io.Writer`                | `io`             | 中等     |
| `io.SectionReader`           | `io.Reader`                | `io`             | 较低     |

备注：`os.Stdin`、`os.Stdout`和`os.Stderr`其实是`os.File`类型，因此也实现了`Reader`和`Writer`接口。

```go
var (
    Stdin  = NewFile(uintptr(syscall.Stdin), "/dev/stdin")
    Stdout = NewFile(uintptr(syscall.Stdout), "/dev/stdout")
    Stderr = NewFile(uintptr(syscall.Stderr), "/dev/stderr")
)
```