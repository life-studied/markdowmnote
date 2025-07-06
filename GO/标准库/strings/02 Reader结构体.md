---
create: '2025-07-06'
modified: '2025-07-06'
---

# Reader结构体

`strings.Reader`结构体实现了io包的接口：

* io.Reader
* io.ReaderAt
* io.Seeker
* io.WriterTo
* io.ByteReader
* io.ByteScanner
* io.RuneReader
* io.RuneScanner

```go
type Reader struct {
  s        string    // Reader 读取的数据来源
  i        int // current reading index（当前读的索引位置）
  prevRune int // index of previous rune; or < 0（前一个读取的 rune 索引位置）
}
```

该结构体通过`strings.NewReader(s string)`创建：

```go
func NewReader(s string) *Reader
```

该方法接收一个字符串，返回的 Reader 实例就是从该参数字符串读数据。

> `bytes` 包的 `bytes.NewBufferString` 有类似的功能，不过，如果只是为了读取，`NewReader` 会更高效。