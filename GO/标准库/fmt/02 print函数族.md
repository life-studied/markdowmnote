---
create: '2025-02-24'
modified: '2025-02-24'
---

# print函数族

以下是 Go 语言中 `fmt` 包提供的主要打印函数及其作用、侧重点和示例代码：

| 函数名称       | 作用                                                         | 侧重点                                     | 示例代码                                                     |
| -------------- | ------------------------------------------------------------ | ------------------------------------------ | ------------------------------------------------------------ |
| `fmt.Print`    | 将内容输出到标准输出，不自动添加换行或空格                   | 适合连续输出，不添加额外分隔符             | `fmt.Print("Hello", "World")` 输出：`HelloWorld`             |
| `fmt.Println`  | 将内容输出到标准输出，并在末尾自动添加换行，参数间自动添加空格 | 适合调试和简单输出，格式化简单             | `fmt.Println("Hello", "World")` 输出：`Hello World`          |
| `fmt.Printf`   | 根据格式化字符串输出内容，支持多种格式化占位符               | 提供灵活的格式化控制，适用于复杂输出       | `fmt.Printf("Name: %s, Age: %d\n", "Alice", 25)` 输出：`Name: Alice, Age: 25` |
|                |                                                              |                                            |                                                              |
| `fmt.Fprint`   | 将内容输出到指定的 `io.Writer`（如文件或网络连接）           | 适用于将输出写入文件或流                   | `fmt.Fprint(os.Stdout, "Hello World")` 输出到标准输出        |
| `fmt.Fprintf`  | 根据格式化字符串将内容输出到指定的 `io.Writer`               | 格式化输出到文件或流                       | `fmt.Fprintf(os.Stdout, "Name: %s\n", "Alice")` 输出到标准输出 |
|                |                                                              |                                            |                                                              |
| `fmt.Sprint`   | 将内容格式化为字符串，不添加换行                             | 生成字符串而不直接打印，适合后续处理       | `str := fmt.Sprint("Hello", " ", "World")` 结果：`Hello World` |
| `fmt.Sprintln` | 将内容格式化为字符串，并在末尾添加换行                       | 生成带换行的字符串而不直接打印             | `str := fmt.Sprintln("Hello", "World")` 结果：`Hello World\n` |
| `fmt.Sprintf`  | 根据格式化字符串生成格式化后的字符串                         | 生成格式化字符串而不直接打印，适合后续处理 | `message := fmt.Sprintf("User: %s, Score: %d", "Bob", 85)` 结果：`User: Bob, Score: 85` |