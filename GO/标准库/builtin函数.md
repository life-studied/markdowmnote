---
create: '2025-07-06'
modified: '2025-07-06'
---

# builtin函数

参考：[builtin package - builtin - Go Packages](https://pkg.go.dev/builtin#pkg-functions)

| 函数名称 | 函数签名                                          | 说明                                                         |
| -------- | ------------------------------------------------- | ------------------------------------------------------------ |
| append   | `func append(slice []Type, elems ...Type) []Type` | 将元素追加到切片末尾，返回更新后的切片。                     |
| len      | `func len(v Type) int`                            | 返回变量的长度，具体取决于变量的类型（数组、指针到数组、切片、映射、字符串、通道）。 |
| cap      | `func cap(v Type) int`                            | 返回变量的容量，具体取决于变量的类型（数组、指针到数组、切片、通道）。 |
| clear    | `func clear[T ~[]Type | ~map[Type]Type1](t T)`    | 清空切片或映射的内容。                                       |
| copy     | `func copy(dst, src []Type) int`                  | 将源切片的元素复制到目标切片，返回复制的元素数量。           |
|          |                                                   |                                                              |
| make     | `func make(t Type, size ...IntegerType) Type`     | 分配并初始化切片、映射或通道。                               |
| close    | `func close(c chan<- Type)`                       | 关闭通道，通道必须是双向或只写类型。                         |
| delete   | `func delete(m map[Type]Type1, key Type)`         | 从映射中删除指定键的元素。                                   |
|          |                                                   |                                                              |
| new      | `func new(Type) *Type`                            | 分配内存，返回指定类型的零值的指针。                         |
|          |                                                   |                                                              |
| max      | `func max[T cmp.Ordered](x T, y ...T) T`          | 返回有序类型中最大值。                                       |
| min      | `func min[T cmp.Ordered](x T, y ...T) T`          | 返回有序类型中最小值。                                       |
|          |                                                   |                                                              |
| panic    | `func panic(v any)`                               | 触发运行时错误，终止当前协程的正常执行。                     |
| recover  | `func recover() any`                              | 在延迟函数中恢复发生 panic 的协程的正常执行。                |
|          |                                                   |                                                              |
| print    | `func print(args ...Type)`                        | 以实现特定的方式格式化参数并写入标准错误。                   |
| println  | `func println(args ...Type)`                      | 以实现特定的方式格式化参数并写入标准错误，参数之间添加空格，末尾添加换行符。 |
|          |                                                   |                                                              |
| complex  | `func complex(r, i FloatType) ComplexType`        | 从两个浮点值构造一个复数。                                   |
| real     | `func real(c ComplexType) FloatType`              | 返回复数的实部。                                             |
| imag     | `func imag(c ComplexType) FloatType`              | 返回复数的虚部。                                             |

**说明**：  

- `Type`、`Type1`、`FloatType`、`ComplexType` 等是文档中用于表示通用类型的占位符，具体使用时需要替换为实际的 Go 类型。  
- `...` 表示可变参数，即可以传入多个同类型的参数。  
- 部分函数（如 `clear` 和 `max`/`min`）是 Go 1.21 版本中新增的。