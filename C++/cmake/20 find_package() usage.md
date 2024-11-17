---
create: 2024-03-12
---
# find_package() usage

[TOC]

​	find_package用于查找本地的package。

## 1. 引入内置module

​	cmake官方为我们预定义了许多寻找依赖包的Module，他们存储在`path_to_your_cmake/share/cmake-<version>/Modules`目录下。每个以`Find<LibaryName>.cmake`命名的文件都对应一个Module。

​	引入后，模块会定义如下变量便于检测结果：

* `<LibaryName>_FOUND`：判断是否成功引入
* `<LibaryName>_INCLUDE_DIR`/`<LibaryName>_INCLUDES`：获取include_DIR
* `<LibaryName>_LIBRARY`/`<LibaryName>_LIBRARIES`：获取LIB_DIR

---

​	以curl库为例，其文件名为`FindCURL.cmake`：

```cmake
find_package(CURL)

add_executable(curltest curltest.cc)

if(CURL_FOUND)	# if success
    target_include_directories(clib PRIVATE ${CURL_INCLUDE_DIR})	# add include
    target_link_libraries(curltest ${CURL_LIBRARY})					# add lib
else(CURL_FOUND)
    message(FATAL_ERROR ”CURL library not found”)
endif(CURL_FOUND)
```

## 2. 引入第三方module（只对支持cmake编译安装的库有效）

​	方法是安装该module，然后再通过find_package引入。

```shell
# clone该项目
git clone https://github.com/google/glog.git 
# 切换到需要的版本 
cd glog
git checkout v0.40  
```

---

```cmake
find_package(GLOG)
add_executable(glogtest glogtest.cc)
if(GLOG_FOUND)
    # 由于glog在连接时将头文件直接链接到了库里面，所以这里不用显示调用target_include_directories
    target_link_libraries(glogtest glog::glog)
else(GLOG_FOUND)
    message(FATAL_ERROR ”GLOG library not found”)
endif(GLOG_FOUND)
```

## 3. Module模式和Config模式

​	find_package有两种模式：

* Module:cmake
  * 需要找到一个`Find<LibraryName>.cmake`文件，该文件负责找到include和lib所在路径并引入项目。
  * cmake搜索该文件的路径为：
    * cmake的安装目录：`share/cmake-<version>/Modules`
    * 自定义的指定目录：`CMAKE_MODULE_PATH`所在目录
* Config:如果Module模式搜索失败，没有找到对应的`Find<LibraryName>.cmake`文件，则转入Config模式进行搜索。它主要通过`<LibraryName>Config.cmake` or `<lower-case-package-name>-config.cmake`这两个文件来引入我们需要的库。
  * 安装module成功后，以上文的glog为例，在`/usr/local/lib/cmake/glog/`目录下生成了`glog-config.cmake`文件，`/usr/local/lib/cmake/<LibraryName>/`正是find_package函数的搜索路径之一
  * 或者通过手动添加搜索路径的方法：`set(ModuleName_DIR "path_to_config_cmake")`

## 4. 为自己的项目添加`Find<LibraryName>.cmake`模块

​	以Add模块为例。

### 新建FindAdd.cmake

​	在项目根目录的cmake文件夹下，新建`FindAdd.cmake`文件：

```cmake
# 在指定目录下寻找头文件和动态库文件的位置，可以指定多个目标路径
find_path(ADD_INCLUDE_DIR libadd.h /usr/include/ /usr/local/include ${CMAKE_SOURCE_DIR}/ModuleMode)
find_library(ADD_LIBRARY NAMES add PATHS /usr/lib/add /usr/local/lib/add ${CMAKE_SOURCE_DIR}/ModuleMode)

if (ADD_INCLUDE_DIR AND ADD_LIBRARY)
    set(ADD_FOUND TRUE)
endif (ADD_INCLUDE_DIR AND ADD_LIBRARY)
```

### 引入该module

​	在addtest中通过添加`CMAKE_MODULE_PATH`引入该module(CMakeLists.txt):

```cmake
# 将项目目录下的cmake文件夹加入到CMAKE_MODULE_PATH中，让find_pakcage能够找到我们自定义的函数库
set(CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmake;${CMAKE_MODULE_PATH}")
add_executable(addtest addtest.cc)
find_package(ADD)
if(ADD_FOUND)
    target_include_directories(addtest PRIVATE ${ADD_INCLUDE_DIR})
    target_link_libraries(addtest ${ADD_LIBRARY})
else(ADD_FOUND)
    message(FATAL_ERROR "ADD library not found")
endif(ADD_FOUND)
```

## 参考资料

* [Cmake之深入理解find_package()的用法 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/97369704)
* [find_package — CMake 3.29.0-rc3 Documentation](https://cmake.org/cmake/help/latest/command/find_package.html)