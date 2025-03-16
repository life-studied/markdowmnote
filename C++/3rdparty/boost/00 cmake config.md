---
create: 2024-04-09
modified: '2024-11-17'
---

# cmake config

## cmake

* 设置`BOOST_ROOT`
* 添加include目录：`include_directories(${Boost_INCLUDE_DIRS})`
* 添加依赖库：`target_link_libraries(test_boost ${Boost_LIBRARIES})`

```cmake
cmake_minimum_required(VERSION 3.25)
project(test_boost)

set(BOOST_ROOT "E:\\3rdparty\\boost_1_84_0")
find_package(Boost REQUIRED)
if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
    message("Boost has found")
endif()

add_executable(test_boost test.cpp)
target_link_libraries(test_boost ${Boost_LIBRARIES})
```

## 参考资料

* [FindBoost — CMake 3.29.1 Documentation](https://cmake.org/cmake/help/latest/module/FindBoost.html#boost-cmake)