---
create: 2024-04-21
---
# TARGET类别与传播方式

## 1. 什么是TARGET

​	现给定一个文件架构：

```txt
.
├── CMakeLists.txt (待创建)
└── main.cpp
```

​	这是最简单的一类情况，即仅有一个 `*.cpp` 构成可执行程序的源文件。对于这种通常只需要短短几行 CMake 语句即可完成，首先在当前目录直接创建 CMakeLists.txt，输入：

```cmake
cmake_minimum_required(VERSION 3.16) # 指定 CMake 最小版本号，小于此版本的无法通过 CMake
project(MyProject)                   # 定义项目名
add_executable(my_main main.cpp)     # 使用 main.cpp 生成可执行文件 my_main
```

​	其中`my_main`就是目标名，在 CMake 中是具有`TARGET`属性的变量，即目标，同时也是可执行文件的文件名。

---

根据[CMake官网](https://cmake.org/cmake/help/latest/manual/cmake-buildsystem.7.html)对目标库的划分，可简单分为两类：二进制目标、伪目标，其大致内容如下

```txt
├── 二进制目标
│   ├── 可执行文件
│   └── 二进制库
│       ├── 普通库（动态库、静态库）
│       └── 对象库
└── 伪目标
    ├── 导入目标
    ├── 别名目标
    └── 接口库
```

## 2. 目标的传播方式

​	目标的传播方式是指，当绑定某个目标时，该目标是否允许被其它目标访问到。

​	目标有三种传播方式：

- `PUBLIC`：在绑定当前目标时给指定的内容设置公有属性，其他目标在链接当前目标时，能访问这些指定的内容
- `PRIVATE`：在绑定当前目标时给指定的内容设置私有属性，其他目标在链接当前目标时，无法访问这些内容
- `INTERFACE`：在绑定当前目标时给指定的内容设置接口属性，通常在接口库中使用。其他目标在链接当前目标时，只允许访问其声明（接口）

---

### 示例

​	在`target_include_directories`和`target_link_libraries`中都会用到该设定：

```cmake
target_include_directories(
  MyLib1
  PUBLIC include # 表示当前 CMakeLists.txt 所在目录下的 include 文件夹
)

target_link_libraries(
  MyLib1
  PUBLIC xxxx
)
```

- `target_include_directories`：为目标添加需要包含的文件（一般是头文件）的搜索路径，该路径将绑定至目标上
- `target_link_libraries`：为目标添加依赖的库，该库也将绑定至目标上

​	此外，文档中给出了这样一句话：

> *New in version 3.13:* The `<target>` doesn't have to be defined in the same directory as the `target_link_libraries` call.

​	翻译过来就是

> 3.13 版本的新内容：`<target>`不需要在与调用`target_link_libraries`命令相同的目录中被定义。

​	这句话很关键，表示在较新的 CMake 版本中，允许在整个项目中跨`CMakeLists.txt`文件进行链接操作，例如，即使上文的`my_main`在父目录的作用域，`xxxx`在子目录的作用域，`my_main`所在的`CMakeLists.txt`也能访问到`xxxx`这一目标变量。这一点有别于普通的变量，可以当做是具有目标属性的变量的专属功能。

## 3. 伪目标

### 接口库

​	在程序开发中，有时候会遇到只有头文件（`*.h`、`*.hpp`），而没有源文件（`*.c`、`*.cpp`）的情况，而我们知道，一般在使用 `add_library` 的时候只能为源文件（`*.h`以及`*.hpp`之类的头文件不属于源文件）生成目标库。

​	在这种情况下，如果我们需要对只有头文件的库生成目标，并且进行链接，我们需要创建接口库。这种目标由于没有源文件，不会实质性的参与构建（编译），但提供了与普通目标相同的操作方式，因此接口库属于伪目标。

​	还是刚才的文件结构，先创建只有头文件的文件夹及 `CMakeLists.txt`：

```txt
.
├── CMakeLists.txt
├── main.cpp
├── MyLib1
│   ├── CMakeLists.txt
│   ├── include
│   │   └── MyLib1.h
│   └── src
│       ├── a.cpp
│       ├── b.cpp
│       └── c.cpp
└── MyLib2
    ├── CMakeLists.txt (待创建)
    └── include
        └── MyLib2.hpp
```

​	首先项目根目录的 `CMakeLists.txt` 需要添加

```cmake
add_subdirectory(MyLib2)
```

​	创建接口库的语法与普通库类似，只是少了源文件的添加的步骤：

```cmake
add_library(MyLib2 INTERFACE)
target_include_directories(
  MyLib2
  INTERFACE include # 接口库的目标只能使用 INTERFACE 属性
)
target_link_libraries(
  MyLib2
  INTERFACE xxx
)
```

### 导入目标

​	我们会遇到这种情况，仅提供了若干头文件和若干库文件（例如 `*.so` 和 `*.a`）在这种情况下我们无法通过自己 `add_library` 从源文件创建目标，我们需要引入**导入目标**。

​	假设某家相机厂商的 SDK 提供了以下内容，假设将其放在了项目文件夹的 `camera` 文件夹下，请根据以下文件结构创建一个 CMake 目标

```txt
.
├── include
│   ├── CameraApi.h
│   ├── CameraDefine.h
│   └── CameraStatus.h
└── lib
    └── libMVSDK.so
```

​	在 `camera` 文件夹中创建 `CMakeLists.txt` 文件，写入

```cmake
add_library(camera SHARED IMPORTED)
set_target_properties(camera PROPERTIES
  INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_CURRENT_LIST_DIR}/include"
  IMPORTED_LOCATION "${CMAKE_CURRENT_LIST_DIR}/lib/libMVSDK.so"
)
```

​	对于 `INTERFACE_INCLUDE_DIRECTORIES` 的属性，我们也可以用常规的写法，写成

```cmake
target_include_directories(
  camera
  INTERFACE include
)
```

> #### [#](https://www.cccolt.top/tutorial/cmake/05.html#注意-2)注意
>
> - 在设置 `IMPORTED_LOCATION` 属性的时候需要指定绝对路径
> - 对于 `lib` 目录中存在多个 `*.so` 等动态库的情况下，请找到真正的动态库（其他动态库一般是陪衬的）

### 别名目标

​	允许为现有的目标创建一个可供引用的别名。别名目标可以用于简化构建过程、重命名目标或创建便于引用的名称。

​	要创建别名目标，可以使用 `add_library`、`add_executable` 或 `add_custom_target` 命令，并将 `ALIAS` 关键字与目标名称一起使用。下面是一个 `CMakeLists.txt` 示例：

```cmake
# 添加一个库
add_library(my_library STATIC my_source.cpp)
# 创建别名目标
add_library(lib1 ALIAS my_library)
# 添加可执行文件，并链接别名目标
add_executable(my_app main.cpp)
target_link_libraries(
  my_app
  PRIVATE lib1
)
```

​	但要注意，别名目标（例如上文中的 `lib1`）不得作为下面两个的目标名

- `target_link_libraries`
- `target_include_directories`

​	即不得写成

```cmake
target_include_directories(
  lib1
  PUBLIC xxx
)
# 或者
target_link_libraries(
  lib1
  PRIVATE xxx
)
```

## 4. 对象库

​	允许将多个源文件设置为一个单独的目标，而不生成可执行文件或普通库。对象库可以被其他目标（例如可执行文件或普通库）链接和使用。

​	要创建一个对象库，同样可以使用 `add_library` 命令，并将 `OBJECT` 关键字与源文件列表一起使用。下面是一个 `CMakeLists.txt` 示例：

```cmake
# 添加对象库
add_library(
  my_object_lib
  OBJECT file1.cpp file2.cpp file3.cpp
)
# 添加可执行文件，并链接对象库
add_executable(my_app main.cpp)
target_link_libraries(my_app PRIVATE my_object_lib)
```

​	在上面的示例中，`add_library` 命令创建了一个名为 `my_object_lib` 的对象库，并将 `file1.cpp`、`file2.cpp` 和 `file3.cpp` 作为源文件。

​	通过这种方式，对象库中的源文件将会被编译为目标文件，但不会生成一个独立的可执行文件或共享库。其他目标可以链接到对象库，并在链接时使用其中的目标文件

## 5. 设置目标属性

​	即使用命令 `set_target_properties`

​	一般情况下，目标属性通常很少直接使用，一般是间接使用，例如

- 在使用 `target_include_directories` 的时候，会为目标设置 `INTERFACE_INCLUDE_DIRECTORIES` 属性
- 在使用 `target_link_libraries` 的时候，会为目标设置 `INTERFACE_LINK_LIBRARIES` 属性

​	实际上，对于导入目标、接口库目标，我们可以不通过以上命令来实现对应功能，譬如说，为了指定目标包含的目录，原先写为

```cmake
target_include_directories(
  my_target
  PUBLIC include
)
```

​	现在可以通过使用

```cmake
set_target_properties(
  my_target PROPERTIES
  INTERFACE_INCLUDE_DIRECTORIES ${CMAKE_CURRENT_LIST_DIR}/include
)
```

​	或者

```cmake
set_property(
  TARGET my_target PROPERTIES
  INTERFACE_INCLUDE_DIRECTORIES ${CMAKE_CURRENT_LIST_DIR}/include
)
```

​	来实现同样功能，此操作在配置导入目标的时候比较常见。此外，CMake 官网提供了非常非常多的属性，可以点击[此处 (opens new window)](https://cmake.org/cmake/help/latest/manual/cmake-properties.7.html#properties-on-targets)查阅。

> 注意
>
> 在设置 `INTERFACE_INCLUDE_DIRECTORIES` 和 `INTERFACE_LINK_LIBRARIES` 属性的时候需要指定绝对路径

## 6. 非必要不使用全局包含、链接

非必要情况下不要使用

- `link_library`
- `include_directories`

语句，会影响所有子目录的构建，带来污染问题。但有些情况下这些语句可以实现其他功能。例如 `target_include_directories` 可以将内容绑定到目标并且在导出目标后依赖关系仍然生效，而使用 `include_directories` 可以避免安装后仍然存在依赖关系。

## 7. 自定义目标（add_custom_target）

​	见26；

