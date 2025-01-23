---
create: '2025-01-23'
modified: '2025-01-23'
---

# 补充：cuda使用cmake教程

```cmake
cmake_minimum_required(VERSION 3.25)

project(CUDA_TEST LANGUAGES CXX CUDA)  # 在此处启用CUDA语言支持
add_executable(main main.cu)
```

## 1. 自定义安装问题（找不到MSBuildExtensions）

### 报错信息

这种情况下出现的报错信息是：

```shell
PS D:\codeSpace\bad_code\test_cuda> cmake -S . -B build -DCMAKE_GENERATOR_TOOLSET="cuda=D:/ProgrammingSoftware/Cuda/Toolkit" -DCUDAToolkit_ROOT="D:/ProgrammingSoftware/Cuda/Toolkit" -DCMAKE_TOOLCHAIN_FILE=""
-- Building for: Visual Studio 17 2022
-- Selecting Windows SDK version 10.0.22621.0 to target Windows 10.0.22631.
CMake Error at CMakeLists.txt:3 (project):
  Generator

    Visual Studio 17 2022

  given toolset

    cuda=D:/ProgrammingSoftware/Cuda/Toolkit\

  cannot detect Visual Studio integration files in path

    D:/ProgrammingSoftware/Cuda/Toolkit/extras/visual_studio_integration/MSBuildExtensions


-- The CXX compiler identification is MSVC 19.36.32546.0
CMake Error at D:/UsefulTools/Cmake/share/cmake-3.25/Modules/CMakeDetermineCompilerId.cmake:491 (message):
  No CUDA toolset found.
Call Stack (most recent call first):
  D:/UsefulTools/Cmake/share/cmake-3.25/Modules/CMakeDetermineCompilerId.cmake:6 (CMAKE_DETERMINE_COMPILER_ID_BUILD)
  D:/UsefulTools/Cmake/share/cmake-3.25/Modules/CMakeDetermineCompilerId.cmake:48 (__determine_compiler_id_test)
  D:/UsefulTools/Cmake/share/cmake-3.25/Modules/CMakeDetermineCUDACompiler.cmake:307 (CMAKE_DETERMINE_COMPILER_ID)
  CMakeLists.txt:3 (project)


-- Configuring incomplete, errors occurred!
See also "D:/codeSpace/bad_code/test_cuda/build/CMakeFiles/CMakeOutput.log".
```

### 解决

如果指定了自定义安装目录，实测会出现一些问题：在工程编译前，MSVC会尝试编译一个测试工程，并且使用cuda自带的extras中的设置，一般放在这里（即使你自定义了安装目录）：

> `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v12.2\extras\visual_studio_integration\MSBuildExtensions`

同时MSVC会去环境变量指定的cuda安装目录下去寻找它，但是会发现在目录下不存在：

> `D:\ProgrammingSoftware\Cuda\Toolkit\extras\visual_studio_integration\MSBuildExtensions`。

因此只需要将第一个目录下的extras中的内容拷到第二个目录下，就行。

或者实测后发现，直接使用`cmake -S . -B build`，不指定cuda相关的宏也能解决问题（大概是环境变量里已经自动设置了）。

## 2. vcpkg干扰问题

### 报错

另一个情况是出现vcpkg干扰cmake正常使用的情况，比如出现了明明没有使用vcpkg，cmake的configure过程依然出现了vcpkg：

```shell
  已完成生成项目“D:\codeSpace\bad_code\test_cuda\build\CMakeFiles\3.25.2\CompilerIdCUDA\CompilerIdCUDA.vcxproj”(CudaBuildCore
  个目标)的操作。

  VcpkgTripletSelection:

    Using triplet "x64-windows" from "D:\UsefulTools\vcpkg\vcpkg\installed\x64-windows\"
    Using normalized configuration "Release"
```

### 解决

只需要移除vcpkg的全局集成就行，或者删掉vcpkg的环境路径也是一样的道理：

```shell
vcpkg integrate remove
```