---
create: 2023-08-22
---
# Cgo语言交互

## 1.调用方法

* 用注释写C代码
* 完成后使用`import "C"`向编译器传递信号
* 调用时使用`C.xxx`调用

---

## 2.调用案例

​	下面是一个简单使用go调用C的例子。

```go
package main

/*
#include <stdio.h>
void hello() {
    printf("Hello from C!\n");
}
*/
import "C"

func main() {
    C.hello()
}

```

## 3.类型映射

​	go语言与C语言之间存在着类型互相映射的转换，便于使用。

### 3.1 基本类型映射

| go语言类型     | C语言类型          |
| -------------- | ------------------ |
| C.char         | char               |
| C.schar        | signed char        |
| C.uchar        | unsigned char      |
| C.short        | short              |
| C.ushort       | unsigned short     |
| C.int          | int                |
| C.uint         | unsigned int       |
| C.long         | long               |
| C.ulong        | unsigned long      |
| C.longlong     | long long          |
| C.ulonglong    | unsigned long long |
| C.float        | float              |
| C.double       | double             |
| unsafe.Pointer | void*              |

### 3.2 struct/enum/union映射

| 类型   | go语言形式   | C语言 |
| ------ | ------------ | ----- |
| 结构体 | C.struct_xxx | xxx   |
| 枚举   | C.enum_xxx   | xxx   |
| 联合体 | C.union_xxx  | xxx   |

### 3.3 字符串映射

可以使用以下函数处理go语言string到C语言字符数组的转换。（有额外的内存开销）

* C.CString()
* C.GoString()
* C.GoStringN()

注意：使用`C.free(unsafe.Pointer(cstr))`的方式释放字符数组的内存。

```go
var gostr string
gostr="xxxx"

cstr:=C.CString(gostr)
defer C.free(unsafe.Pointer(cstr))

C.sprintf(cstr,"content is: %d",123)
```

## 4.函数调用注意点

### 4.1 错误码（第二参数返回）

​	go对常规C语言函数调用增加了一个err的返回值。

``` go
n, err:= C.atoi("a234")
```

### 4.2 数组传参

​	go语言使用C语言传递数组类型参数，必须将第一个元素的地址作为整个数组的起始地址传入。

```go
n, err:= C.f(&array[0])		//代替C语言中的f(array)
```

