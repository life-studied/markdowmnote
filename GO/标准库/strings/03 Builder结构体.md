---
create: '2025-07-06'
modified: '2025-07-06'
---

# Builder结构体

strings.Builder结构体实现了 io 包的接口，可以向该对象内写入数据：

* Writer
* ByteWriter
* StringWriter 等接口

```go
type Builder struct {
    addr *Builder // of receiver, to detect copies by value
    buf  []byte
}
```

Builder 没有实现 Reader 等接口，所以该类型不可读，但提供了 String 方法可以获取对象内的数据。

| 方法名      | 签名                                                   | 描述                                                         |
| ----------- | ------------------------------------------------------ | ------------------------------------------------------------ |
| WriteByte   | `func (b *Builder) WriteByte(c byte) error`            | 向 `b` 写入一个字节。                                        |
| WriteRune   | `func (b *Builder) WriteRune(r rune) (int, error)`     | 向 `b` 写入一个字符。                                        |
| Write       | `func (b *Builder) Write(p []byte) (int, error)`       | 向 `b` 写入字节数组 `p`。                                    |
| WriteString | `func (b *Builder) WriteString(s string) (int, error)` | 向 `b` 写入字符串 `s`。                                      |
|             |                                                        |                                                              |
| Len         | `func (b *Builder) Len() int`                          | 返回 `b` 的数据长度。                                        |
| Cap         | `func (b *Builder) Cap() int`                          | 返回 `b` 的容量（cap）。                                     |
|             |                                                        |                                                              |
| Grow        | `func (b *Builder) Grow(n int)`                        | 将 `b` 的容量至少增加 `n`（可能会更多）。如果 `n` 为负数，会导致 panic。 |
| Reset       | `func (b *Builder) Reset()`                            | 清空 `b` 的所有内容。                                        |
|             |                                                        |                                                              |
| String      | `func (b *Builder) String() string`                    | 将 `b` 的数据以 `string` 类型返回。                          |