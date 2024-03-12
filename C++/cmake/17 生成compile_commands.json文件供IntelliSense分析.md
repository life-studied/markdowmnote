# 生成compile_commands.json文件供IntelliSense分析

​	使用如下宏：

```cmake
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
```

## 注意！

​	只有Ninja和makefile支持该宏，从而生成`compile_commands.json`，MSVC不支持它。

​	可以通过指定编译器来规避Windows上的MSVC干扰。

```cmake
set(CMAKE_C_COMPILER "D:/ProgrammingSoftware/Mingw/mingw64/bin/gcc.exe")
set(CMAKE_CXX_COMPILER "D:/ProgrammingSoftware/Mingw/mingw64/bin/g++.exe")
```

​	cmake指令：

```shell
cmake -S . -B build -G "MinGW Makefiles"
```

​	必须指定`-G "MinGW Makefiles"`参数，否则在Windows上依旧会使用MSVC进行生成。