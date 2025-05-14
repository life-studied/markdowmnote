---
create: 2024-11-07
modified: '2025-05-14'
---

# 清单模式vcpkg.json

```shell
cmake --preset vcpkg
```

## 使用清单模式的原因

清单模式允许将依赖集成到**项目级别**，而不是全局级别。

全局级别的集成将所有的库放在一个公共目录下，会使得**不能同时使用同一个库的不同版本**，从而导致不同项目不能同时使用。

## 基础使用方法

```shell
yunyin@Yunyin:/mnt/d/codeSpace/bad_code/test_cmake_vcpkg$ tree
.
├── CMakeLists.txt
├── main.cpp
└── vcpkg.json
```

### vcpkg.json

核心在于在项目根目录下**创建**一个vcpkg.json（`vcpkg new` 命令在项目的目录中添加一个 `vcpkg.json` 文件和一个 `vcpkg-configuration.json` 文件）：

```shell
vcpkg new --application
```

在其中配置dependencies列表：

```json
{
  "dependencies": [
    "cxxopts",
    "fmt",
    "range-v3"
  ]
}
```

可以手动配置，也可以通过：

```shell
vcpkg add port fmt cxxopts range-v3
```

另外：生成的 `vcpkg-configuration.json` 文件引入了一个[基线](https://learn.microsoft.com/zh-cn/vcpkg/reference/vcpkg-configuration-json#registry-baseline)，用于对项目的依赖项设置[最低版本约束](https://learn.microsoft.com/zh-cn/vcpkg/users/versioning)。 修改此文件超出了本教程的范围。 虽然在本教程中不适用，但建议将 `vcpkg-configuration.json` 文件保留在源代码管理之下，以确保不同开发环境中的版本一致性。

### cmake如何开启vcpkg支持

> 无论如何，实际上都要让cmake知道vcpkg.cmake的文件路径就好，下面是两种方式。

### 方法一：使用`-DCMAKE_TOOLCHAIN_FILE=`来导入

在使用vcpkg的情况下，直接使用`find_package`就能导入包。

为了让cmake能正常使用vcpkg的配置，需要添加vcpkg.cmake。如果当前目录下存在`vcpkg.json`则会使用本地配置。

* 在命令中添加`-DCMAKE_TOOLCHAIN_FILE="%VCPKG_ROOT%\scripts\buildsystems\vcpkg.cmake"`
  * 实测在vscode中使用`%VCPKG_ROOT%`可能会找不到，所以直接使用绝对路径：`-DCMAKE_TOOLCHAIN_FILE=D:\UsefulTools\vcpkg\vcpkg\scripts\buildsystems\vcpkg.cmake`
* 或者设置cmake变量`set(CMAKE_TOOLCHAIN_FILE=%VCPKG_ROOT%\scripts\buildsystems\vcpkg.cmake)`

```shell
cmake -B build -S . -DCMAKE_TOOLCHAIN_FILE=D:\UsefulTools\vcpkg\vcpkg\scripts\buildsystems\vcpkg.cmake
```

> 另外vcpkg不支持windows下的mingw，因为预编译好的lib是用msvc编译的，里面的符号对不上。

```cmake
cmake_minimum_required(VERSION 3.25)

project(fibonacci CXX)

find_package(fmt REQUIRED)
find_package(range-v3 REQUIRED)
find_package(cxxopts REQUIRED)

set(CMAKE_CXX_STANDARD 17)

add_executable(fibo main.cpp)

target_link_libraries(fibo
  PRIVATE
    fmt::fmt
    range-v3::range-v3
    cxxopts::cxxopts)
```

### 方法二：使用CMakePresets.json来指定vcpkg.cmake的路径

```json
{
  "version": 2,
  "configurePresets": [
    {
      "name": "vcpkg",
      "generator": "Visual Studio 17 2022",
      "binaryDir": "${sourceDir}/build",
      "cacheVariables": {
        "CMAKE_TOOLCHAIN_FILE": "$env{VCPKG_ROOT}\\scripts\\buildsystems\\vcpkg.cmake"
      }
    }
  ]
}
```

## 结合IDE使用的一些小trick

### vscode: cmake tools settings.json

>如果想配置宏`DCMAKE_TOOLCHAIN_FILE`，添加以下配置即可：
>
>```json
>"cmake.configureArgs": [
>   "-DCMAKE_TOOLCHAIN_FILE=D:/UsefulTools/vcpkg/vcpkg/scripts/buildsystems/vcpkg.cmake"
>],
>     ```

---

### Visual Studio

![Screenshot of a Visual Studio's project Properties window.](https://learn.microsoft.com/en-us/vcpkg/resources/vs-enable-vcpkg-manifest.png)

## 参考资料

* [教程：从清单文件安装依赖项 |Microsoft 学习](https://learn.microsoft.com/en-us/vcpkg/consume/manifest-mode?tabs=cmake%2Cbuild-MSBuild)
* [在 Visual Studio 中使用 CMake 安装和管理包 | Microsoft Learn](https://learn.microsoft.com/zh-cn/vcpkg/get_started/get-started-vs?pivots=shell-powershell)