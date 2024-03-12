# 使用CMake编译

## 创建build目录

​	一般会创建一个build目录以将复杂的中间文件装载在一起，防止与源代码文件混杂，也便于删除。

```shell
cmake -S . -B build		# 源文件目录：.	编译目录：build

# 等价于
# mkdir build
# cmake ..
```

## 执行编译

```cmake
cmake --build build		# 执行编译，编译目录为build
```

## 执行exe

```cmake
cd build && xxx.exe
```



