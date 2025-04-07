---
create: '2025-04-07'
modified: '2025-04-07'
---

# VS开发者命令提示符（Developer Command Prompt）——工具说明

## 1. 提供的工具

| **工具名**      | **说明**                                                     | **示例命令**                                                 |
| :-------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| **dumpbin**     | 用于分析 Windows 可执行文件、库文件和对象文件的工具。可以显示文件中的符号表、导入和导出函数等信息，这在调试链接问题时非常有用。 | 查看导出的符号（函数）：<br>`dumpbin /EXPORTS mylib.dll`<br>查看静态库中的符号：<br>`dumpbin /SYMBOLS mylib.lib`<br>查看文件头信息：<br>`dumpbin /HEADERS myprogram.exe` |
| **cl.exe**      | Visual Studio 的 C/C++ 编译器，用于编译源代码文件并生成目标文件或可执行文件。 | 编译代码：<br>`cl.exe main.cpp`                              |
| **link.exe**    | Visual Studio 的链接器，用于将多个对象文件和库链接成一个可执行文件或动态链接库。 | 链接对象文件：<br>`link.exe main.obj /OUT:main.exe`          |
| **msbuild.exe** | Visual Studio 的构建工具，用于根据项目文件（如 `.csproj` 或 `.vcxproj`）构建项目。 | 构建项目：<br>`msbuild myproject.vcxproj`                    |
| **devenv.exe**  | Visual Studio 的开发环境启动程序，可以通过命令行参数执行一些操作，例如构建、清理或运行项目。 | 构建解决方案：<br>`devenv myproject.sln /Build`              |
| **ildasm.exe**  | 用于反汇编 .NET 程序集的工具，可以查看程序集的中间语言代码。 | 反汇编程序集：<br>`ildasm myassembly.dll`                    |
| **NMAKE**       | 用于构建 C/C++ 项目的工具，它根据 `makefile` 文件中的规则来编译和链接项目。 | 使用 NMAKE 构建项目：<br>`nmake /f mymakefile.mak`           |
| **LIB**         | 用于创建和管理静态库的工具。                                 | 创建静态库：<br>`lib /OUT:mylib.lib myobj1.obj myobj2.obj`   |
| **clrver**      | 用于检查 .NET Framework 版本的工具。                         | 检查 .NET Framework 版本：<br>`clrver`                       |
| **CMake**       | （VS2022自带）跨平台的构建系统生成器，可以生成适用于不同平台的构建文件。 | 生成构建文件：<br>`cmake -B build`                           |

## 2. 预设的环境变量

VS 开发者命令提示符通过这些功能和配置，为开发者提供了一个预配置的开发环境，使得开发者可以更方便地使用 Visual Studio 的工具链（如编译器、链接器、调试器等），而无需手动设置环境变量和工具路径。

| **环境变量**         | **作用**                                                     | **如何使用**                                                 | **示例命令**                                                 |
| :------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| **PATH**             | 定义系统在哪些目录中查找可执行文件（如 `.exe`、`.bat`、`.cmd` 等）。在 Visual Studio 开发者命令提示符中，`PATH` 被扩展以包含 Visual Studio 的工具路径，例如编译器（如 `cl.exe`）、链接器（如 `link.exe`）和其他工具的路径。 | 查看当前 `PATH`：<br>在命令提示符中输入以下命令：<br>`echo %PATH%`<br>直接运行工具：<br>由于 `PATH` 包含了工具路径，你可以直接运行 Visual Studio 的工具，而无需指定完整路径。例如：<br>`cl.exe`<br>添加自定义工具路径：<br>如果你需要添加自己的工具路径，可以通过以下方式：<br>`set PATH=%PATH%;C:\MyTools` | 查看当前 `PATH`：<br>`echo %PATH%`<br>直接运行工具：<br>`cl.exe`<br>添加自定义工具路径：<br>`set PATH=%PATH%;C:\MyTools` |
| **INCLUDE**          | 定义编译器在哪些目录中查找头文件（`.h` 或 `.hpp` 文件）。在编译过程中，编译器会按照 `INCLUDE` 中指定的路径顺序查找头文件。 | 查看当前 `INCLUDE`：<br>在命令提示符中输入以下命令：<br>`echo %INCLUDE%`<br>编译代码：<br>当你编译代码时，编译器会自动使用 `INCLUDE` 中的路径来查找头文件。例如：<br>`#include <iostream>`<br>添加自定义头文件路径：<br>如果你需要添加自己的头文件路径，可以通过以下方式：<br>`set INCLUDE=%INCLUDE%;C:\MyHeaders` | 查看当前 `INCLUDE`：<br>`echo %INCLUDE%`<br>添加自定义头文件路径：<br>`set INCLUDE=%INCLUDE%;C:\MyHeaders` |
| **LIB**              | 定义链接器在哪些目录中查找库文件（如 `.lib` 文件）。在链接过程中，链接器会按照 `LIB` 中指定的路径顺序查找所需的库文件。 | 查看当前 `LIB`：<br>在命令提示符中输入以下命令：<br>`echo %LIB%`<br>链接库文件：<br>当你链接代码时，链接器会自动使用 `LIB` 中的路径来查找库文件。例如：<br>`cl.exe myprogram.cpp /link kernel32.lib user32.lib`<br>添加自定义库路径：<br>如果你需要添加自己的库路径，可以通过以下方式：<br>`set LIB=%LIB%;C:\MyLibs` | 查看当前 `LIB`：<br>`echo %LIB%`<br>添加自定义库路径：<br>`set LIB=%LIB%;C:\MyLibs` |
| **LIBPATH**          | 类似于 `LIB`，但主要用于指定其他类型的库路径，例如在使用 .NET Framework 或其他运行时库时。它通常用于查找中间语言（IL）库或其他特殊库。 | 查看当前 `LIBPATH`：<br>在命令提示符中输入以下命令：<br>`echo %LIBPATH%`<br>添加自定义库路径：<br>如果你需要添加自己的库路径，可以通过以下方式：<br>`set LIBPATH=%LIBPATH%;C:\MyOtherLibs` | 查看当前 `LIBPATH`：<br>`echo %LIBPATH%`<br>添加自定义库路径：<br>`set LIBPATH=%LIBPATH%;C:\MyOtherLibs` |
| **EXTERNAL_INCLUDE** | 用于指定额外的头文件路径。它可能用于第三方库或其他非标准的头文件路径。 | 查看当前 `EXTERNAL_INCLUDE`：<br>在命令提示符中输入以下命令：<br>`echo %EXTERNAL_INCLUDE%`<br>添加自定义头文件路径：<br>如果你需要添加自己的头文件路径，可以通过以下方式：<br>`set EXTERNAL_INCLUDE=%EXTERNAL_INCLUDE%;C:\MyExternalHeaders` | 查看当前 `EXTERNAL_INCLUDE`：<br>`echo %EXTERNAL_INCLUDE%`<br>添加自定义头文件路径：<br>`set EXTERNAL_INCLUDE=%EXTERNAL_INCLUDE%;C:\MyExternalHeaders` |