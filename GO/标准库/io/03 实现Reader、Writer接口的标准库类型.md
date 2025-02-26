---
create: '2025-02-24'
modified: '2025-02-25'
---

# 实现Reader、Writer接口的标准库类型

## 常用类型

### string

#### **`strings.Reader`**

- 标准库`strings`包，是一个基于字符串的可读取流。

- 提供了实现`io.Reader`的方法（如`Read`方法）。

    ```go
    package main
    
    import (
        "fmt"
        "strings"
    )
    
    func main() {
        str := "Hello, World!"
        reader := strings.NewReader(str)
        buffer := make([]byte, len(str))
        n, err := reader.Read(buffer)
        if err != nil {
            fmt.Println("Error:", err)
        }
        fmt.Printf("Read %d bytes: %s\n", n, buffer)
    }
    ```

### 字符数组

- 字符数组（固定长度的字节数组）本身并不是动态可扩展的，因此不适合直接作为`io.Writer`。但是可以通过一些适配器来进行写入。

- 可以将其包装为`bytes.Buffer`或使用`bytes.Writer`等。

    ```go
    package main
    
    import (
        "bytes"
        "fmt"
    )
    
    func main() {
        var data [10]byte // 字符数组
        buffer := bytes.NewBuffer(data[:])
        n, err := buffer.Write([]byte("Hello"))
        if err != nil {
            fmt.Println("Error:", err)
        }
        fmt.Printf("Wrote %d bytes: %s\n", n, data[:])
    }
    ```

### 切片

#### **`bytes.Reader`**

- 来自标准库`bytes`包，基于字节切片实现的可读取流。

    ```go
    package main
    
    import (
        "bytes"
        "fmt"
    )
    
    func main() {
        data := []byte{1, 2, 3, 4, 5}
        reader := bytes.NewReader(data)
        buffer := make([]byte, len(data))
        n, err := reader.Read(buffer)
        if err != nil {
            fmt.Println("Error:", err)
        }
        fmt.Printf("Read %d bytes: %v\n", n, buffer)
    }
    ```

#### **`bytes.Buffer`**

- 是一个双向缓冲区，实现了`io.Reader`和`io.Writer`接口。

- 可以动态扩展容量，适用于读写操作。

    ```go
    package main
    
    import (
        "bytes"
        "fmt"
    )
    
    func main() {
        buffer := bytes.NewBuffer(nil)
        n, err := buffer.Write([]byte("Hello"))
        if err != nil {
            fmt.Println("Error:", err)
        }
        fmt.Printf("Wrote %d bytes: %s\n", n, buffer.Bytes())
    
        data := make([]byte, 5)
        nRead, err := buffer.Read(data)
        if err != nil {
            fmt.Println("Error:", err)
        }
        fmt.Printf("Read %d bytes: %s\n", nRead, data[:nRead])
    }
    ```

### 文件

#### **`os.File`**

- 表示一个操作系统文件句柄，直接实现了`io.Reader`和`io.Writer`接口。

    ```go
    package main
    
    import (
        "fmt"
        "os"
    )
    
    func main() {
        // 打开文件以读取
        f, err := os.Open("example.txt")
        if err != nil {
            fmt.Println("Error opening file:", err)
            return
        }
        defer f.Close()
    
        buffer := make([]byte, 1024)
        n, err := f.Read(buffer)
        if err != nil {
            fmt.Println("Error reading file:", err)
            return
        }
        fmt.Printf("Read %d bytes from file: %s\n", n, string(buffer[:n]))
    
        // 创建文件以写入
        f, err = os.Create("output.txt")
        if err != nil {
            fmt.Println("Error creating file:", err)
            return
        }
        defer f.Close()
    
        n, err = f.Write([]byte("Hello, World!"))
        if err != nil {
            fmt.Println("Error writing file:", err)
            return
        }
        fmt.Printf("Wrote %d bytes to file.\n", n)
    }
    ```


## 附录：类型表格

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