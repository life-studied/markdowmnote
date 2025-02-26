---
create: '2025-02-25'
modified: '2025-02-25'
---

# Offsetof 函数

该函数的定义如下：

```go
func Offsetof(v ArbitraryType) uintptr
```

## 读取offset

该函数返回由v所指示的某结构体中的字段在该结构体中的位置偏移字节数，注意，v的表达方式必须是“struct.filed”形式。 

举例说明，在64为系统中运行以下代码：

```go
type Datas struct{
    c0 byte
    c1 int
    c2 string
    c3 int
}
func main(){
    var d Datas
    fmt.Println(unsafe.Offset(d.c0))    // 0
    fmt.Println(unsafe.Offset(d.c1))    // 8
    fmt.Println(unsafe.Offset(d.c2))    // 16
    fmt.Println(unsafe.Offset(d.c3))    // 32
}
```

## 通过offset修改值

如果知道的结构体的起始地址和字段的偏移值，就可以直接读写内存：

```go
d.c3 = 13
offset := unsafe.Offsetof(d.c3)
q := (*int)(unsafe.Pointer(uintptr(p) + offset))
fmt.Println(*q) // 13

p := unsafe.Pointer(&d)
*p = 1013
fmt.Println(d.c3)   // 1013
```