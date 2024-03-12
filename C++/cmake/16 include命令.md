# include命令

​	include命令用于导入`cmake`模块，即`.cmake`文件。该文件中存储了一些`cmake`命令和一些宏/函数，当`CMakeLists.txt`包含该`.cmake`文件时，当编译运行时，该`.cmake`里的一些命令就会在该包含处得到执行，并且在包含以后的地方能够调用该.`cmake`里的一些宏和函数。

```cmake
include(<file|module> [OPTIONAL] [RESULT_VARIABLE <var>]
                      [NO_POLICY_SCOPE])
```



