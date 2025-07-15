---
create: '2025-07-08'
modified: '2025-07-08'
---

# Buffer结构体

```go
type Buffer struct {
	// contains filtered or unexported fields
}
```

## functions

```go
// 创建
func NewBuffer(buf []byte) *Buffer
func NewBufferString(s string) *Buffer

// 控制
func (b *Buffer) Available() int
func (b *Buffer) AvailableBuffer() []byte
func (b *Buffer) Bytes() []byte
func (b *Buffer) Cap() int
func (b *Buffer) Grow(n int)
func (b *Buffer) Len() int
func (b *Buffer) Reset()

// 读
func (b *Buffer) Next(n int) []byte
func (b *Buffer) Read(p []byte) (n int, err error)
func (b *Buffer) ReadByte() (byte, error)
func (b *Buffer) ReadBytes(delim byte) (line []byte, err error)
func (b *Buffer) ReadFrom(r io.Reader) (n int64, err error)
func (b *Buffer) ReadRune() (r rune, size int, err error)
func (b *Buffer) ReadString(delim byte) (line string, err error)

func (b *Buffer) String() string
func (b *Buffer) Truncate(n int)
func (b *Buffer) UnreadByte() error
func (b *Buffer) UnreadRune() error

// 写
func (b *Buffer) Write(p []byte) (n int, err error)
func (b *Buffer) WriteByte(c byte) error
func (b *Buffer) WriteRune(r rune) (n int, err error)
func (b *Buffer) WriteString(s string) (n int, err error)
func (b *Buffer) WriteTo(w io.Writer) (n int64, err error)
```