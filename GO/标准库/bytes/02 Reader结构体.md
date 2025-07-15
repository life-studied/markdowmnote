---
create: '2025-07-08'
modified: '2025-07-08'
---

# Reader结构体

## bytes.Reader

```go
type Reader struct {
	// contains filtered or unexported fields
}
```

## functions

都是io包里的接口，要么就是内部容量相关的。

```go
// 创建
func NewReader(b []byte) *Reader

// funcions
func (r *Reader) Len() int
func (r *Reader) Read(b []byte) (n int, err error)
func (r *Reader) ReadAt(b []byte, off int64) (n int, err error)
func (r *Reader) ReadByte() (byte, error)
func (r *Reader) ReadRune() (ch rune, size int, err error)
func (r *Reader) Reset(b []byte)
func (r *Reader) Seek(offset int64, whence int) (int64, error)
func (r *Reader) Size() int64
func (r *Reader) UnreadByte() error
func (r *Reader) UnreadRune() error
func (r *Reader) WriteTo(w io.Writer) (n int64, err error)
```

## 参考资料

* [bytes package - bytes - Go Packages](https://pkg.go.dev/bytes#Reader)