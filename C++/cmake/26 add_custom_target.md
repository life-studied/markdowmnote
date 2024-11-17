---
create: 2024-04-21
---
# add_custom_target

​	在很多时候，需要在`cmake`中创建一些目标，如`clean`、`copy`等等，这就需要通过`add_custom_target`来指定。同时，`add_custom_command`可以用来完成对`add_custom_target`生成的`target`的补充。

​	`add_custom_target()` 是 CMake 中的一个命令，用于创建一个自定义目标。自定义目标可以用来执行任何您想要在构建过程中执行的命令，例如：

- 生成文件
- 运行脚本
- 调用外部工具

​	一般在cmake生成文件后，通过`make xxx`来执行自定义的目标内容。

```shell
mkdir build && cd build
cmake ..
make CopyTask
```

## 语法

```cmake
add_custom_target(NAME [ALL] [COMMAND command1 [args1...]] [COMMAND command2 [args2...] ...] [DEPENDS depend depend depend ... ] [WORKING_DIRECTORY dir] [COMMENT comment] [VERBATIM] [SOURCES src1 [src2...]])
```

**参数说明**

- `NAME`: 目标名称
- `ALL`: 可选参数，指示该目标应该添加到默认构建目标中
- `COMMAND`: 要执行的命令
- `DEPENDS`: 依赖项
- `WORKING_DIRECTORY`: 工作目录
- `COMMENT`: 注释
- `VERBATIM`: 可选参数，指示命令应该原样执行
- `SOURCES`: 源文件

## 示例代码

以下是一个简单的示例，演示如何使用 `add_custom_target()` 来生成一个文件：

```
add_custom_target(generate_file
  COMMAND touch output.txt
)
```

该示例将创建一个名为 `generate_file` 的目标，该目标将执行 `touch output.txt` 命令来生成一个名为 `output.txt` 的文件。

**注释**

- `add_custom_target()` 创建的目标没有输出文件，因此它总是被认为是过时的。
- 可以使用 `add_dependencies()` 命令来添加依赖项到自定义目标。
- 可以使用 `set_target_properties()` 命令来设置自定义目标的属性。

**错误**

- 如果 `COMMAND` 参数没有指定，则会发生错误。
- 如果 `WORKING_DIRECTORY` 参数指定的目录不存在，则会发生错误。