---
create: '2025-02-25'
modified: '2025-02-25'
---

# Pointer

## Pointer 类型

这个类型比较重要，它是实现定位欲读写的内存的基础。官方文档对该类型有四个重要描述：

- （1）任何类型的指针都可以被转化为Pointer
- （2）Pointer可以被转化为任何类型的指针
- （3）uintptr可以被转化为Pointer
- （4）Pointer可以被转化为uintptr

举例来说，该类型可以这样使用：

```go
func main() {
    i := 100
    fmt.Println(i)  // 100
    
    p := (*int)unsafe.Pointer(&i)
    
    fmt.Println(*p) // 100
}
```