---
create: '2025-02-24'
modified: '2025-02-24'
---

# scanf函数族

## **扫描（输入）函数**

| 函数名称      | 作用                                             | 示例代码                                  |
| :------------ | :----------------------------------------------- | :---------------------------------------- |
| `fmt.Scan`    | 从标准输入读取内容，存储到参数中                 | `var name string; fmt.Scan(&name)`        |
| `fmt.Scanln`  | 从标准输入读取内容，存储到参数中，遇到换行符停止 | `var name string; fmt.Scanln(&name)`      |
| `fmt.Scanf`   | 根据格式化字符串从标准输入读取内容               | `var name string; fmt.Scanf("%s", &name)` |
|               |                                                  |                                           |
| `fmt.Fscan`   | 从指定的 `io.Reader` 读取内容                    | `fmt.Fscan(r, &name)`                     |
| `fmt.Fscanf`  | 根据格式化字符串从指定的 `io.Reader` 读取内容    | `fmt.Fscanf(r, "%s", &name)`              |
| `fmt.Fscanln` | 从指定的 `io.Reader` 读取内容，遇到换行符停止    | `fmt.Fscanln(r, &name)`                   |
|               |                                                  |                                           |
| `fmt.Sscan`   | 从字符串读取内容，存储到参数中                   | `fmt.Sscan("Alice", &name)`               |
| `fmt.Sscanf`  | 根据格式化字符串从字符串读取内容                 | `fmt.Sscanf("Alice", "%s", &name)`        |
| `fmt.Sscanln` | 从字符串读取内容，遇到换行符停止                 | `fmt.Sscanln("Alice", &name)`             |