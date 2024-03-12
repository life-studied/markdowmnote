# FetchContent模块

​	`FetchContent`模块与`ExternalProject`模块非常相似，都是获取外部第三方库并构建。

​	区别在于，前者在`cmake time`执行，而后者在`build time`执行。因此使用前者会更早地拉取代码，从而能使用`add_subdirectory`来引入项目。

## 1. 工作步骤

* **引入模块**：在`CMakeLists.txt`文件顶部，使用`include(FetchContent)`命令来使FetchContent模块可用。
* **声明依赖**：使用`FetchContent_Declare`函数声明外部项目，包括指定项目的源代码位置（如Git仓库）和其他相关设置（如特定的标签、分支或提交）。
* **下载依赖**：通过调用`FetchContent_MakeAvailable`或`FetchContent_Populate`命令，CMake会下载声明的依赖，并将其源代码放置在构建目录的子目录中。
* **使用依赖**：一旦下载完成，外部项目的源代码就可以像项目内的代码一样被使用和构建了。

## 2. 命令参数详细

> ​	一般`FetchContent_MakeAvaliable`和`FetchContent_Populate`选择一个使用即可，前者保证只构建一次更推荐使用，后者有更多的灵活性和使用难度。

### 使用`FetchContent_MakeAvaliable`

1. `FetchContent_Declare`: 描述如何下载依赖库

   ```cmake
   FetchContent_Declare(
     <name>
     <contentOptions>...
     [EXCLUDE_FROM_ALL]
     [SYSTEM]
     [OVERRIDE_FIND_PACKAGE |
      FIND_PACKAGE_ARGS args...]
   )
   
   # examples
   FetchContent_Declare(
     googletest
     GIT_REPOSITORY https://github.com/google/googletest.git
     GIT_TAG        703bd9caab50b139428cea1aaff9974ebee5742e # release-1.10.0
   )
   
   FetchContent_Declare(
     myCompanyIcons
     URL      https://intranet.mycompany.com/assets/iconset_1.12.tar.gz
     URL_HASH MD5=5588a7b18261c20068beabfb4f530b87
   )
   
   FetchContent_Declare(
     myCompanyCertificates
     SVN_REPOSITORY svn+ssh://svn.mycompany.com/srv/svn/trunk/certs
     SVN_REVISION   -r12345
   )
   ```

   

2. `FetchContent_MakeAvaliable(<name1> [<name2>...])`: 构建命令1所声明的 **name1, name2, ...**；

---

### 使用`FetchContent_Populate`

1. `FetchContent_Populate(<name>)`: 构建命令1所声明的 **name** 依赖， 该命令会定义三个变量：

   1. *`<lowercaseName>_POPULATED`*: 依赖是否已被构建（bool）

   2. *`<lowercaseName>_SOURCE_DIR`*: 依赖存储路径

   3. `<lowercaseName>_BINARY_DIR`: 依赖 Build 路径

      注意：若 *`FetchContent_Populate`* 不止有 `name` 参数，此时，不再使用 *`FetchContent_Declare`* 所定义的配置，而是由 *`FetchContent_Populate`* 给出，不再定义 *`<lowercaseName>_POPULATED`*，不会在全局定义`<lowercaseName>_SOURCE_DIR` 和 *`<lowercaseName>_BINARY_DIR`*，但仍会在当前作用域内定义，不再检测是否已经构建该依赖，具体语法为:

```cmake
FetchContent_Populate(
  <name>
  [QUIET]
  [SUBBUILD_DIR <subBuildDir>]
  [SOURCE_DIR <srcDir>]
  [BINARY_DIR <binDir>]
  ...
)
```

​	其中，**QUIET** 表示隐藏与激活依赖相关的输出；**SUBBUILD_DIR** 用于指定 sub-build 路径；**SOURCE_DIR** 用于指定 source 路径，**BINARY_DIR** 用于指定 binary 路径，其余参数均会传递给 [ExternalProject_Add()](https://link.juejin.cn?target=https%3A%2F%2Fcmake.org%2Fcmake%2Fhelp%2Flatest%2Fmodule%2FExternalProject.html%23command%3Aexternalproject_add)命令。

4. *`FetchContent_GetProperties(<name> [SOURCE_DIR <srcDirVar>] [BINARY_DIR <binDirVar>] [POPULATED <doneVar>])`*: 获取与外部依赖 **name** 相关的属性。

## 3. 案例

### 1. git master

​	假设我们想在项目中集成一个名为`SomeLibrary`的外部库，该库托管在GitHub上。

```cmake
cmake_minimum_required(VERSION 3.14)
project(MyProject)

include(FetchContent)		# 1

FetchContent_Declare(		# 2
  SomeLibrary
  GIT_REPOSITORY https://github.com/someuser/SomeLibrary.git
  GIT_TAG master 			# 指定使用的branch、tag或commit
)

FetchContent_MakeAvailable(SomeLibrary)	# 3	start to pull and build

# 假设SomeLibrary提供了一个名为some_library的目标
add_executable(my_executable main.cpp)
target_link_libraries(my_executable PRIVATE some_library)
```

### 2. git tag/zip

​	获取两个不同方式的项目并build：

```cmake
include(FetchContent)

FetchContent_Declare(
  googletest
  GIT_REPOSITORY https://github.com/google/googletest.git
  GIT_TAG        703bd9caab50b139428cea1aaff9974ebee5742e # release-1.10.0
)

FetchContent_Declare(
  myCompanyIcons
  URL      https://intranet.mycompany.com/assets/iconset_1.12.tar.gz
  URL_HASH MD5=5588a7b18261c20068beabfb4f530b87
)

FetchContent_MakeAvailable(googletest myCompanyIcons)
```



## 参考资料

* [详解 CMake FetchContent 模块 - 掘金 (juejin.cn)](https://juejin.cn/post/7102762548423819272)
* [CMake进阶教程：深入FetchContent与ExternalProject模块 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/681696425)
* [FetchContent — CMake 3.29.0-rc3 Documentation](https://cmake.org/cmake/help/latest/module/FetchContent.html)