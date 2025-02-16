---
create: '2025-02-03'
modified: '2025-02-03'
---

# modules（cmake3.28）

cmake自从3.28开始支持modules特性。原因在于GCC最晚与cmake modules进行支持合作，最终与GCC14版本以上可以得到使用。

在cmake3.28之前需要通过`CMAKE_EXPERIMENTAL_CXX_MODULE_CMAKE_API`变量进行开启。

* 支持文档参考：[cmake-cxxmodules（7） — CMake 3.31.5 文档](https://cmake.org/cmake/help/latest/manual/cmake-cxxmodules.7.html)

## 测试代码

* 注：测试在VS2022中进行。

## vs安装模块组件

在单个组件中，安装 **适用于v143生成工具的C++模块（x64/x86 - 实验性）**

![img](https://pica.zhimg.com/v2-569282de63b88c7da45278b107f0744a_1440w.jpg)

### cmake

```cmake
cmake_minimum_required(VERSION 3.25)  # 确保使用支持 C++ 模块的版本  
project(MyCppModuleProject LANGUAGES CXX)  

# 设置 C++ 标准为 C++23  (C++20失败)
set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)  

# 启用实验性 C++ 模块支持（3.28以下）
set(CMAKE_EXPERIMENTAL_CXX_MODULES ON)  

# 导出dll的lib符号（Windows）
set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS TRUE)   

# VS添加实验性模块编译指令
add_compile_options($<$<COMPILE_LANGUAGE:CXX>:/experimental:module>)

# 添加模块库  
add_library(mymodule SHARED)  # 创建模块库  
target_sources(mymodule  
    PRIVATE  
        mymodule.cpp  
    PUBLIC  
        mymodule.ixx  
) 

# 添加可执行文件  
add_executable(main main.cpp)  
target_link_libraries(main PRIVATE mymodule)  # 链接模块库
```