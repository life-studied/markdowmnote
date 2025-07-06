---
create: '2025-07-01'
modified: '2025-07-01'
---

# CMake parallel

## 1. CMake 3.12 及以上版本的通用方法
从 CMake 3.12 开始，CMake 提供了直接支持并行构建的功能。

使用命令行选项 `--parallel` 或 `-j` 来指定并行构建的线程数。例如：
```bash
cmake --build . --parallel <jobs>

cmake --build . -j <jobs>
```

- 如果不指定 `<jobs>`，CMake 会使用底层构建工具的默认并行级别，因此最好指定。

> [!note]
>
> 还可以通过设置环境变量 `CMAKE_BUILD_PARALLEL_LEVEL` 来指定默认的并行级别。

## 2. 老版本 CMake 的解决方案
对于 CMake 3.12 之前的版本，可以通过以下方式实现并行构建：

- Unix MakeFile：通过修改 `CMAKE_MAKE_PROGRAM` 来添加 `-j` 参数。例如：
  ```cmake
  set(MULTITHREADED_BUILD 12 CACHE STRING "Number of threads to use for parallel builds")
  set(CMAKE_MAKE_PROGRAM "${CMAKE_MAKE_PROGRAM} -j${MULTITHREADED_BUILD}")
  ```

- MSVC：可以通过在 `CMAKE_CXX_FLAGS` 中添加 `/MP` 选项来启用并行编译：
  ```cmake
  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /MP")
  ```

综合一下：

```cmake
# 定义并行构建的线程数
set(MULTITHREADED_BUILD 12 CACHE STRING "Number of threads to use for parallel builds")

# 根据不同的构建系统添加并行构建选项
if(MULTITHREADED_BUILD)
    if(${CMAKE_GENERATOR} MATCHES "Unix Makefiles")
        # 对于 Unix Makefiles，修改 CMAKE_MAKE_PROGRAM 添加 -j 参数
        set(CMAKE_MAKE_PROGRAM "${CMAKE_MAKE_PROGRAM} -j${MULTITHREADED_BUILD}")
        message(STATUS "Added arguments to CMAKE_BUILD_TOOL: ${CMAKE_MAKE_PROGRAM}")
    elseif(MSVC)
        # 对于 MSVC，添加 /MP 参数
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /MP")
        message(STATUS "Added parallel build arguments to CMAKE_CXX_FLAGS: ${CMAKE_CXX_FLAGS}")
    endif()
endif()
```