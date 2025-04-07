---
create: '2025-04-07'
modified: '2025-04-07'
---

# dumpbin工具

`dumpbin` 是一个功能强大的命令行工具，用于分析 Windows 可执行文件（如 DLL、EXE、OBJ 等）的结构和内容。它可以帮助开发者、系统管理员和逆向工程师深入了解程序的内部信息。以下是 `dumpbin` 的一些常用功能及其使用方法：

最常用的是查看符号：

```cmd
dumpbin /SYMBOLS mylib.lib | find "xxx" # 查看xxx符号是否存在
```

## 功能附录

| **功能**           | **说明**                                               | **命令示例**                                      |
| :----------------- | :----------------------------------------------------- | :------------------------------------------------ |
| **查看导出表**     | 显示 DLL 或 EXE 文件中导出的函数和变量信息。           | `dumpbin /EXPORTS mylib.dll`                      |
| **查看导入表**     | 显示文件中导入的函数及其来源 DLL。                     | `dumpbin /IMPORTS myprogram.exe`                  |
| **查看文件头信息** | 显示文件的头部信息，包括文件类型、目标机器、时间戳等。 | `dumpbin /HEADERS myprogram.exe`                  |
| **查看符号表**     | 显示文件中的符号表，包括函数名、变量名及其地址。       | `dumpbin /SYMBOLS mylib.lib`                      |
| **查看节区信息**   | 显示文件中各个节区的详细信息。                         | `dumpbin /SECTION:.text myprogram.exe`            |
| **查看依赖项**     | 显示文件依赖的其他 DLL。                               | `dumpbin /DEPENDENTS myprogram.exe`               |
| **反汇编代码**     | 显示指定节区的反汇编代码。                             | `dumpbin /DISASM /SECTION:.text myprogram.exe`    |
| **查看重定位表**   | 显示基址重定位表。                                     | `dumpbin /RELOCATIONS mydll.dll`                  |
| **查看所有信息**   | 显示文件的所有信息，包括头部、节区、符号表等。         | `dumpbin /ALL myprogram.exe`                      |
| **检查文件架构**   | 检查 DLL 或 EXE 是 32 位还是 64 位。                   | `dumpbin /HEADERS myprogram.exe | find "machine"` |