---
create: '2024-12-27'
modified: '2024-12-27'
---

# INTERFACE asio库

## 导入asio作为INTERFACE库

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