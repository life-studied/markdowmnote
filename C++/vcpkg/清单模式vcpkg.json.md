# 清单模式vcpkg.json

```cmake
cmake -B build -S . -DCMAKE_TOOLCHAIN_FILE=D:\UsefulTools\vcpkg\vcpkg\scripts\buildsystems\vcpkg.cmake
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

核心在于在项目根目录下创建一个vcpkg.json，并在其中配置dependencies列表：

```json
{
  "dependencies": [
    "cxxopts",
    "fmt",
    "range-v3"
  ]
}
```

### cmake

#### CMakeLists.txt

在使用vcpkg的情况下，直接使用`find_package`就能导入包。

```cmake
cmake_minimum_required(VERSION 3.15)

set(CMAKE_TOOLCHAIN_FILE %VCPKG_ROOT%\scripts\buildsystems\vcpkg.cmake)

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

#### config

为了让cmake能正常使用vcpkg的配置，需要添加vcpkg.cmake。如果当前目录下存在`vcpkg.json`则会使用本地配置。

* 在命令中添加`-DCMAKE_TOOLCHAIN_FILE="%VCPKG_ROOT%\scripts\buildsystems\vcpkg.cmake"`
  * 实测在vscode中使用`%VCPKG_ROOT%`可能会找不到，所以直接使用绝对路径：`-DCMAKE_TOOLCHAIN_FILE=D:\UsefulTools\vcpkg\vcpkg\scripts\buildsystems\vcpkg.cmake`
* 或者设置cmake变量`set(CMAKE_TOOLCHAIN_FILE=%VCPKG_ROOT%\scripts\buildsystems\vcpkg.cmake)`

```shell
cmake -B build -S . -DCMAKE_TOOLCHAIN_FILE=D:\UsefulTools\vcpkg\vcpkg\scripts\buildsystems\vcpkg.cmake
```

> 另外vcpkg不支持windows下的mingw，因为预编译好的lib是用msvc编译的，里面的符号对不上。

---

>补充：vscode cmake tools settings.json
>
>添加以下配置即可：
>
>```json
>"cmake.configureArgs": [
>        "-DCMAKE_TOOLCHAIN_FILE=D:/UsefulTools/vcpkg/vcpkg/scripts/buildsystems/vcpkg.cmake"
>],
>```

---

### Visual Studio

![Screenshot of a Visual Studio's project Properties window.](https://learn.microsoft.com/en-us/vcpkg/resources/vs-enable-vcpkg-manifest.png)

## 参考资料

* [教程：从清单文件安装依赖项 |Microsoft 学习](https://learn.microsoft.com/en-us/vcpkg/consume/manifest-mode?tabs=cmake%2Cbuild-MSBuild)