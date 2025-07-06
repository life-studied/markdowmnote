---
create: '2025-02-25'
modified: '2025-07-06'
---

# bufio.Reader结构体

bufio.Reader 结构包装了一个 io.Reader 对象，提供缓存功能，同时实现了 io.Reader 接口。

Reader 结构没有任何导出的字段，结构定义如下：

```go
type Reader struct {
    buf          []byte        // 缓存
    rd           io.Reader    // 底层的io.Reader
    // r:从buf中读走的字节（偏移）；w:buf中填充内容的偏移
    // w - r 是buf中可被读的长度（缓存数据的大小），也是Buffered()方法的返回值
    r, w         int
    err          error        // 读过程中遇到的错误
    lastByte     int        // 最后一次读到的字节（ReadByte/UnreadByte)
    lastRuneSize int        // 最后一次读到的Rune的大小(ReadRune/UnreadRune)
}
```

## 构造函数

### NewReader

bufio 包提供了两个实例化 bufio.Reader 对象的函数：NewReader 和 NewReaderSize。其中，NewReader 函数是调用 NewReaderSize 函数实现的：

```go
func NewReader(rd io.Reader) *Reader {
    // 默认缓存大小：defaultBufSize=4096
    return NewReaderSize(rd, defaultBufSize)
}
```

### NewReaderSize

```go
func NewReaderSize(rd io.Reader, size int) *Reader {
    // 已经是bufio.Reader类型，且缓存大小不小于 size，则直接返回
    b, ok := rd.(*Reader)
    if ok && len(b.buf) >= size {
        return b
    }
    // 缓存大小不会小于 minReadBufferSize （16字节）
    if size < minReadBufferSize {
        size = minReadBufferSize
    }
    // 构造一个bufio.Reader实例
    return &Reader{
        buf:          make([]byte, size),
        rd:           rd,
        lastByte:     -1,
        lastRuneSize: -1,
    }
}
```

## 成员方法

ReadSlice、ReadBytes、ReadString 和 ReadLine都具有相似的行为。后三个都是通过调用`ReadSlice`实现的。

### ReadSlice

将数据（包括delim）读取到`Reader`的`buffer`中，通过切片返回（底层数组指向`Reader`的`buffer`）。

注意：由于切片都指向`Reader`的`buffer`，后续读取会改变前面读取的切片的结果！

```go
func (b *Reader) ReadSlice(delim byte) (line []byte, err error)
```

**example**

读取第二行时，第一行返回的切片也改变了，因为返回的不是copy，而是指向buffer的Slice。

```go
// 创建bufio.Reader
reader := bufio.NewReader(strings.NewReader("http://studygolang.com. \nIt is the home of gophers"))

// 读取第一行
line, _ := reader.ReadSlice('\n')
fmt.Printf("the line:%s\n", line)	// the line:http://studygolang.com. \n\n

// 读取第二行，第一行读取的内容发生了改变
n, _ := reader.ReadSlice('\n')
fmt.Printf("the line:%s\n", line)	// the line:It is the home of gophers\n
fmt.Println(string(n))				// It is the home of gophers
```

### ReadBytes

同样是读取到delim结束（包括delim），但是返回的是一个copy的slice。

```go
func (b *Reader) ReadBytes(delim byte) (line []byte, err error)
```

**example**

如下，可以看到通过`ReadBytes`返回的结果更符合我们自然的认识，但是代价是一份copy。

```go
// 创建bufio.Reader
reader := bufio.NewReader(strings.NewReader("http://studygolang.com. \nIt is the home of gophers"))

// 读取第一行
line, _ := reader.ReadBytes('\n')
fmt.Printf("the line:%s\n", line) // the line:http://studygolang.com. \n\n

// 读取第二行
n, _ := reader.ReadBytes('\n')
fmt.Printf("the line:%s\n", line) // the line:http://studygolang.com. \n\n
fmt.Println(string(n))            // It is the home of gophers
```

### ReadString

同样是copy，返回一个string。

```go
func (b *Reader) ReadString(delim byte) (line string, err error) {
    bytes, err := b.ReadBytes(delim)
    return string(bytes), err
}
```

### ReadLine

ReadLine是一个底层的原始行读取命，尝试返回单独的行，不包括行尾的换行符。与ReadSlice一样，数据存储在buffer里，会被下一次读取所干扰。

```go
func (b *Reader) ReadLine() (line []byte, isPrefix bool, err error)
```

* 如果一行小于缓存，isPrefix会被设置为false，正常读取。
* 如果一行大于缓存，isPrefix会被设置为true，同时返回读取到的部分（缓存大小）。该行剩余的部分就会在下次调用的时候返回。当下次调用返回该行剩余部分时，isPrefix将会是false。
* 跟ReadSlice一样，返回的line只是buffer的引用，在下次执行IO操作时，line会无效。

---

#### 更简单的读取一行

`ReadLine`它现在的实现，用不好会出现意想不到的问题，比如丢数据。个人建议可以这么实现读取一行：

```go
line, err := reader.ReadBytes('\n')
line = bytes.TrimRight(line, "\r\n")
```

advance：更好的方法是通过bufio.Scanner去操作。

### Peek

从方法的名称可以猜到，该方法只是“窥探”一下Reader中没有读取的n个字节。好比栈数据结构中的取栈顶元素，但不出栈。

同上面介绍的ReadSlice一样，返回的[]byte只是buffer中的引用，在下次IO操作后会无效。

```go
func (b *Reader) Peek(n int) ([]byte, error)
```

### 其它方法

Reader的其他方法都是实现了io包中的接口，它们的使用方法在io包中都有介绍，在此不赘述。

这些方法包括：

```go
func (b *Reader) Read(p []byte) (n int, err error)
func (b *Reader) ReadByte() (c byte, err error)
func (b *Reader) ReadRune() (r rune, size int, err error)
func (b *Reader) UnreadByte() error
func (b *Reader) UnreadRune() error
func (b *Reader) WriteTo(w io.Writer) (n int64, err error)
```