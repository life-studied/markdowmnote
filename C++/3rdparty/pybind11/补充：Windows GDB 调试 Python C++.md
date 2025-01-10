---
create: '2025-01-10'
modified: '2025-01-10'
---

# Windows GDB 调试 Python C++

## PythonCpp扩展

### launch.json

安装vscode的`Python C++ Debuger`扩展

**必须要在工作区目录下的.vscode/launch.json中添加：**

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "name": "Python C++ Debug",
      "type": "pythoncpp",
      "request": "launch",
      "pythonLaunchName": "Python: Current File",
      "cppAttachName": "(Windows) Attach",
    },
    {
      "name": "(Windows) Attach",
      "type": "cppvsdbg",
      "request": "attach",
      "processId": ""
    },
    {
      "name": "Python: Current File",
      "type": "python",
      "request": "launch",
      "program": "${file}",
      "console": "integratedTerminal"
    }
  ]
}
```

### cmake

```cmake
if(MSVC)
    add_compile_options("/Zi")  # 生成调试信息
else()
    add_compile_options("-g")   # 生成调试信息
    add_compile_options("-O0")  # 无优化
endif()
```

附加信息： 

* 确保您加载函数的共享对象文件（.so/.dll）已使用调试信息（.debug info）进行编译。 
* 在连续的断点之间，其中一个位于Python代码中，另一个位于C++代码中，只有“继续”按钮会正确工作。 （如果不能可以通过左边的调用堆栈中，每个暂停的线程都点击一下继续，来尝试继续）
* 此外，由于Python调试器在重启后会更改其进程ID，因此不支持“重启”按钮。

## 纯GDB调试

mingw的高版本在调试Python时报错：

```shell
PS D:\codeSpace\bad_code\test_pybind11_2> gdb python
GNU gdb (GDB) 14.2
Copyright (C) 2023 Free Software Foundation, Inc.
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.
Type "show copying" and "show warranty" for details.
This GDB was configured as "x86_64-w64-mingw32".
Type "show configuration" for configuration details.
For bug reporting instructions, please see:
<https://www.gnu.org/software/gdb/bugs/>.
Find the GDB manual and other documentation resources online at:
    <http://www.gnu.org/software/gdb/documentation/>.

For help, type "help".
Type "apropos word" to search for commands related to "word"...
Reading symbols from python...
(No debugging symbols found in python)
(gdb) r 1.py
Starting program: D:\codeSpace\bad_code\test_pybind11_2\.venv\Scripts\python.exe 1.py
[New Thread 165364.0x21eb8]
[New Thread 165364.0x26ec0]
Python path configuration:
  PYTHONHOME = 'D:\ProgrammingSoftware\Mingw\mingw64\bin\..\opt'
  PYTHONPATH = (not set)
  program name = 'D:\ProgrammingSoftware\Python3\python.exe'
  isolated = 0
  environment = 1
  user site = 1
  safe_path = 0
  import site = 1
  is in build tree = 0
  stdlib dir = 'D:\ProgrammingSoftware\Mingw\mingw64\opt\Lib'
  sys._base_executable = 'D:\\ProgrammingSoftware\\Python3\\python.exe'
  sys.base_prefix = 'D:\\ProgrammingSoftware\\Mingw\\mingw64\\bin\\..\\opt'
  sys.base_exec_prefix = 'D:\\ProgrammingSoftware\\Mingw\\mingw64\\bin\\..\\opt'
  sys.platlibdir = 'DLLs'
  sys.executable = 'D:\\codeSpace\\bad_code\\test_pybind11_2\\.venv\\Scripts\\python.exe'
  sys.prefix = 'D:\\ProgrammingSoftware\\Mingw\\mingw64\\bin\\..\\opt'
  sys.path = [
    'D:\\ProgrammingSoftware\\Python3\\python311.zip',
    'D:\\ProgrammingSoftware\\Mingw\\mingw64\\opt\\DLLs',
    'D:\\ProgrammingSoftware\\Mingw\\mingw64\\opt\\Lib',
    'D:\\codeSpace\\bad_code\\test_pybind11_2\\.venv\\Scripts',
  ]
Fatal Python error: init_fs_encoding: failed to get the Python codec of the filesystem encoding
Python runtime state: core initialized
ModuleNotFoundError: No module named 'encodings'

Current thread 0x00021c1c (most recent call first):
  <no Python frame>
[Thread 165364.0x21eb8 exited with code 1]
[Thread 165364.0x26ec0 exited with code 1]
[Inferior 1 (process 165364) exited with code 01]
(gdb) q
```

核心地方在于，mingw内部自带一个opt，里面有python的exe，同时会覆盖Python的Lib地址，也就是`PYTHONPATH`，导致其不存在。

因此只要在power里提前设置好这个变量就能让其正常工作：

```powershell
$env:PYTHONPATH = "D:\ProgrammingSoftware\Python3\Lib"
```