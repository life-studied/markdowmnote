---
create: '2025-02-25'
modified: '2025-02-25'
---

# bufio.Scanner结构体（Go1.1）

对于简单的读取一行，在Reader类型中，感觉没有让人特别满意的方法。于是，Go1.1增加了一个类型：Scanner。以便更容易的处理如按行读取输入序列或空格分隔的词等，这类简单的任务。

Scanner 类型和 Reader 类型一样，没有任何导出的字段，同时它也包装了一个 io.Reader 对象，但它没有实现 io.Reader 接口。

```go
type Scanner struct {
    r            io.Reader // The reader provided by the client.
    split        SplitFunc // The function to split the tokens.
    maxTokenSize int       // Maximum size of a token; modified by tests.
    token        []byte    // Last token returned by split.
    buf          []byte    // Buffer used as argument to split.
    start        int       // First non-processed byte in buf.
    end          int       // End of data in buf.
    err          error     // Sticky error.
}
```

## example

它终结了如输入一个很长的有问题的行这样的输入错误，并且提供了简单的默认行为：基于行的输入，每行都剔除分隔标识。

```go
scanner := bufio.NewScanner(os.Stdin)

// 读取一行 剔除\n
for scanner.Scan() {
    fmt.Println(scanner.Text())
}

if err := scanner.Err(); err != nil {
    fmt.Fprintln(os.Stderr, "reading standard input:", err)
}
```

## SplitFunc

### 签名

**SplitFunc 函数签名**如下：

```
type SplitFunc func(data []byte, atEOF bool) (advance int, token []byte, err error)
```

参数：

* data 是还未处理的数据
* atEOF 标识 Reader 是否还有更多数据（是否到了EOF）

返回值：

* advance 表示从输入中读取的字节数
* token 表示下一个结果数据
* err 则代表可能的错误

### 预定义函数

在 bufio 包中预定义了一些 split 函数：

* **ScanBytes** 返回单个字节作为一个 token。
* **ScanRunes** 返回单个 UTF-8 编码的 rune 作为一个 token。返回的 rune 序列（token）和 range string类型 返回的序列是等价的，也就是说，对于无效的 UTF-8 编码会解释为 U+FFFD = "\xef\xbf\xbd"。
* **ScanWords** 返回通过“空格”分词的单词。如：study golang，调用会返回study。注意，这里的“空格”是 `unicode.IsSpace()`，即包括：'\t', '\n', '\v', '\f', '\r', ' ', U+0085 (NEL), U+00A0 (NBSP)。
* **ScanLines** 返回一行文本，不包括行尾的换行符。这里的换行包括了Windows下的"\r\n"和Unix下的"\n"。

## 构造函数

```go
func NewScanner(r io.Reader) *Scanner {
    return &Scanner{
        r:            r,
        split:        ScanLines,
        maxTokenSize: MaxScanTokenSize,
        buf:          make([]byte, 4096), // Plausible starting size; needn't be large.
    }
}
```

可见，返回的 Scanner 实例默认的 split 函数是 ScanLines。

## 成员函数

### Split

用于设置上述Scanner的split成员。下面是一个统计单词的例子：

```go
const input = "This is The Golang Standard Library.\nWelcome you!"
scanner := bufio.NewScanner(strings.NewReader(input))
scanner.Split(bufio.ScanWords)
count := 0
for scanner.Scan() {
    count++
}
if err := scanner.Err(); err != nil {
    fmt.Fprintln(os.Stderr, "reading input:", err)
}
fmt.Println(count)
```

### Scan

该方法好比 iterator 中的 Next 方法，它用于将 Scanner 获取下一个 token，以便 Bytes 和 Text 方法可用。

当扫描停止时，它返回false，这时候，要么是到了输入的末尾要么是遇到了一个错误。（因此返回false总是先判断err）

```go
for scanner.Scan() {
    count++
}
if err := scanner.Err(); err != nil {
    fmt.Fprintln(os.Stderr, "reading input:", err)
}
fmt.Println(count)
```

### Bytes 和 Text 方法

这两个方法的行为一致，都是返回最近的 token，无非 Bytes 返回的是 []byte，Text 返回的是 string。该方法应该在 Scan 调用后调用，而且，下次调用 Scan 会覆盖这次的 token。

```go
scanner := bufio.NewScanner(strings.NewReader("http://studygolang.com. \nIt is the home of gophers"))
if scanner.Scan() {
    scanner.Scan()
    fmt.Printf("%s", scanner.Text())
}
```

### Err 

前面已经提到，通过 `Err` 方法可以获取第一个遇到的错误（但如果错误是 io.EOF，Err 方法会返回 nil）。