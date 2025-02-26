---
create: '2025-02-25'
modified: '2025-02-25'
---

# bufio.Writer结构体

bufio.Writer 结构包装了一个 io.Writer 对象，提供缓存功能，同时实现了 io.Writer 接口。

```go
type Writer struct {
    err error        // 写过程中遇到的错误
    buf []byte        // 缓存
    n   int            // 当前缓存中的字节数
    wr  io.Writer    // 底层的 io.Writer 对象
}
```

## 构造函数

### NewWriter

```go
func NewWriter(wr io.Writer) *Writer {
    // 默认缓存大小：defaultBufSize=4096
    return NewWriterSize(wr, defaultBufSize)
}
```

### NewWriterSize

```go
func NewWriterSize(wr io.Writer, size int) *Writer {
    // 接口查询是否已经是一个bufio.Writer
    b, ok := wr.(*Writer)
    
    // 检查buf size是否符合
    if ok && len(b.buf) >= size {	
        return b
    }
    if size <= 0 {
        size = defaultBufSize
    }
    // new 一个 bufio.Writer 实例
    b = new(Writer)
    b.buf = make([]byte, size)
    b.wr = wr
    return b
}
```

## 成员函数

### Available

Available方法用于获取缓存中的`cap - len`。

### Buffered

Buffered方法用于获取缓存中的`len`。

### Flush

该方法将缓存中的所有数据写入底层的 `io.Writer` 对象中。使用 `bufio.Writer` 时，在所有的 `Write` 操作完成之后，应该调用 `Flush` 方法使得缓存都写入 `io.Writer` 对象中。

### 其他方法

Writer 类型其他方法是一些实际的写方法：

```go
// 实现了 io.ReaderFrom 接口
func (b *Writer) ReadFrom(r io.Reader) (n int64, err error)

// 实现了 io.Writer 接口
func (b *Writer) Write(p []byte) (nn int, err error)

// 实现了 io.ByteWriter 接口
func (b *Writer) WriteByte(c byte) error

// io 中没有该方法的接口，它用于写入单个 Unicode 码点，返回写入的字节数（码点占用的字节），内部实现会根据当前 rune 的范围调用 WriteByte 或 WriteString
func (b *Writer) WriteRune(r rune) (size int, err error)

// 写入字符串，如果返回写入的字节数比 len(s) 小，返回的error会解释原因
func (b *Writer) WriteString(s string) (int, error)
```

这些写方法在缓存满了时会调用 Flush 方法。另外，这些写方法源码开始处，有这样的代码：

```go
if b.err != nil {
    return b.err
}
```

也就是说，只要写的过程中遇到了错误，再次调用写操作会直接返回该错误，而不会继续写入。