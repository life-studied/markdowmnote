---
create: '2025-02-24'
modified: '2025-02-24'
---

# 其它辅助函数

| 函数名称       | 作用                                           | 示例代码                                          |
| :------------- | :--------------------------------------------- | :------------------------------------------------ |
| `fmt.Append`   | 将内容格式化后追加到字节切片                   | `b := fmt.Append([]byte{}, "Hello")`              |
| `fmt.Appendf`  | 根据格式化字符串将内容追加到字节切片           | `b := fmt.Appendf([]byte{}, "Name: %s", "Alice")` |
| `fmt.Appendln` | 将内容格式化后追加到字节切片，并在末尾添加换行 | `b := fmt.Appendln([]byte{}, "Hello", "World")`   |
|                |                                                |                                                   |
| `fmt.Errorf`   | 根据格式化字符串生成错误对象                   | `err := fmt.Errorf("Error: %s", "Not found")`     |