---
create: 2024-08-31
---
# catch2使用cmake示例

## 项目结构

```shell
$ tree
.
├── CMakeLists.txt
├── cmake
│   └── catch2.cmake
├── externals
│   └── catch2
└── test.cpp
```

## catch2.cmake

```cmake
include(FetchContent)

FetchContent_Declare(
  Catch2
  GIT_REPOSITORY git@github.com:catchorg/Catch2.git	# use ssh instead of https
  GIT_TAG        v3.5.3
  SOURCE_DIR ${EXTERNAL_PROJECTS_DIR}/catch2		# declared in root cmake
)

FetchContent_MakeAvailable(Catch2)
```

## project cmake

```cmake
cmake_minimum_required(VERSION 3.20)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

project(catch2test)

# 添加externals相关变量
set(CMAKE_MODULE_PATH "${CMAKE_CURRENT_SOURCE_DIR}/cmake;${CMAKE_MODULE_PATH}")
set(EXTERNAL_PROJECTS_DIR "${CMAKE_CURRENT_SOURCE_DIR}/externals")

# 导入catch2.cmake
include(catch2)

# 开启测试
include(CTest)
include(Catch)

add_executable(catch2test test.cpp)

target_link_libraries(catch2test PRIVATE Catch2::Catch2WithMain)

# 自动进行测试
catch_discover_tests(catch2test)
```

## test.cpp

```C++
#include <catch2/catch_test_macros.hpp>

#include <cstdint>

uint32_t factorial( uint32_t number ) {
    return number <= 1 ? number : factorial(number-1) * number;
}

TEST_CASE( "Factorials are computed", "[factorial]" ) {
    REQUIRE( factorial( 1) == 1 );
    REQUIRE( factorial( 2) == 2 );
    REQUIRE( factorial( 3) == 6 );
    REQUIRE( factorial(10) == 3'628'800 );
}
```

