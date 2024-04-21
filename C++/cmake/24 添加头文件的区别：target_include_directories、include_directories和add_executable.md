# 添加头文件的区别：target_include_directories、include_directories和add_executable

​	这三种方式都可以对当前项目添加引用路径。

```cmake
include_directories()
target_include_directories()
add_executable(xx.cpp xx.h)
```

## include_directories

- `include_directories`的影响范围最大，可以为`CMakelists.txt`中的所有项目添加头文件目录
- 一般写在最外层CMakelists.txt中影响全局

## target_include_directories

- `target_include_directories`的影响范围可以自定义。通过关键字`PRIVATE`或`PUBLIC`。

- **一般用于库目标引用其它库路径**，作为外部依赖项引入进来，target是自己项目生成的lib。

  后续的target即使用到该lib，也不会找到被引用的路径。

```cpp
project(myLib)
target_include_directories（myLib PRIVATE ${OpenCV_Include_dir}）
```

​	我们将`${OpenCV_Include_dir}`头文件库路径只添加到了myLib项目

## add_executable

- `add_executable`中添加的引用路径一般是**当前目录下的源文件对应的头文件**。是生成项目时引入的头文件。
- 这种方式一般用于自己写的或某项目需要的头文件，这种方式需要加添加文件名字，而非头文件目录

```cpp
project(addFunc)
add_executable（addFunc addFunc.h  addFunc.cpp）
```