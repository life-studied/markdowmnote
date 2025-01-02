---
create: '2024-12-27'
modified: '2024-12-27'
---

# INTERFACE库

> 在 cmake 中, INTERFACE 关键字用于定义接口库（Interface Libraries），这是一种不直接编译成二进制文件，而是用来**传递使用要求**（如编译选项、定义、包含目录等）给其他目标（如可执行文件或库）的特殊目标。

## 使用场景

* 无cpp文件：INTERFACE库本身不会产生任何二进制文件，因此里面也不会有任何cpp文件。（如果有，则应该选择shared或者static库）
* head only封装：INTERFACE库的一种很显然的情况就是head only的库，封装一层作为INTERFACE库使用，代替于项目直接导入头文件
* 共享编译配置：INTERFACE库的另一个作用在于不包含任何代码，而是只用来存储编译配置和选项，供多个项目共享编译配置
* 第三方不同版本管理：INTERFACE库还能用于对第三方库的不同版本进行管理，通过对每个版本创建特有的编译选项和链接选项等，保证兼容性

## 示例-导入asio作为INTERFACE库

### 目录

```shell
$ tree .
.
├── CmakeLists.txt
├── externals
│   ├── CmakeLists.txt
│   └── asio
│       ├── include
│           └── asio.hpp
...
└── main.cpp
```

### externals/CmakeLists.txt

```cmake
# 添加asio INTERFACE库
add_library(asio INTERFACE)
target_compile_definitions(asio INTERFACE ASIO_STANDALONE)

target_compile_definitions(asio INTERFACE $<$<CXX_COMPILER_ID:MSVC>:_WIN32_WINNT=0x0601>)
target_include_directories(asio INTERFACE ${CMAKE_CURRENT_SOURCE_DIR}/asio/include)
```

### /CmakeLists.txt

```cmake
cmake_minimum_required(VERSION 3.20)

project(test_asio_head_only)

set(CMAKE_CXX_STANDARD 20)

add_executable(test_asio_head_only main.cpp)

# 添加子目录
add_subdirectory(externals)

# 链接INTERFACE库（asio）
target_link_libraries(test_asio_head_only PRIVATE asio)
```