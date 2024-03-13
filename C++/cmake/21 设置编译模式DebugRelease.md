# 设置编译模式Debug/Release

## cmake

​	`CMAKE_CONFIGURATION_TYPES`宏定义了编译模式。

​	`CMAKE_BUILD_TYPE`表示当前选中的编译模式。

```cmake
set(CMAKE_CONFIGURATION_TYPES "Debug;Release")
if (CMAKE_BUILD_TYPE MATCHES "(Debug)|(Release)")
    message("Current build type: ${CMAKE_BUILD_TYPE}")
else()
    message("Set default buile type to Debug.")
    set(CMAKE_BUILD_TYPE "Debug")
endif()
```

## command

​	如果使用命令的方式，则可以使用如下参数设置：

```cmake
cmake --config Debug ...
cmake --config Release ...
```

