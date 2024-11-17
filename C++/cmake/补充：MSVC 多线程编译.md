---
create: 2024-03-12
---
# MSVC 多线程编译

​	将如下命令添加入`CMakeLists.txt`：

```cmake
# windows 并行编译选项
add_compile_options($<$<CXX_COMPILER_ID:MSVC>:/Gm->) #高版本已被废弃，但是低版本的Gm会影响并行
cmake_host_system_information(RESULT CPU_NUMBER_OF_LOGICAL_CORES QUERY NUMBER_OF_LOGICAL_CORES)
add_compile_options($<$<CXX_COMPILER_ID:MSVC>:/MP${CPU_NUMBER_OF_LOGICAL_CORES}>)
```

## 更推荐的做法

### vs2022

​	通过在命令行添加参数，从而避免修改`CMakeLists.txt`。

```shell
cd build
cmake --build . --config Release -- /p:CL_MP=true /p:CL_MPCount=12
```

