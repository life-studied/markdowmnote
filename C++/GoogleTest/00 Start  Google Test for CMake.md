---
create: 2024-03-11
---
# Start : Google Test for CMake

> 编译器必须支持C++14

## 1. 创建项目并依赖Google Test（pull from remote）

​	创建项目根目录：

```shell
mkdir my_project && cd my_project
```

​	创建`CMakeLists.txt`，本项目使用`FetchContent`来获取GoogleTest：

```cmake
cmake_minimum_required(VERSION 3.14)
project(my_project)

# GoogleTest requires at least C++14
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/03597a01ee50ed33e9dfd640b249b4be3799d395.zip
)
# For Windows: Prevent overriding the parent project's compiler/linker settings
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)
```

## 2. 使用GoogleTest编写测试

​	创建`hello_test.cc`：

```C++
#include <gtest/gtest.h>

// Demonstrate some basic assertions.
TEST(HelloTest, BasicAssertions) {
  // Expect two strings not to be equal.
  EXPECT_STRNE("hello", "world");
  // Expect equality.
  EXPECT_EQ(7 * 6, 42);
}
```

​	补充`CMakeLists.txt`：

```cmake
enable_testing()

add_executable(
  hello_test
  hello_test.cc
)
target_link_libraries(
  hello_test
  GTest::gtest_main
)

include(GoogleTest)
gtest_discover_tests(hello_test)
```

## 3. 执行测试

```shell
cmake -S . -B build
cmake --build build
cd build && ctest
```

## 参考资料

* [快速入门：使用 CMake 进行构建 |谷歌测试 (google.github.io)](https://google.github.io/googletest/quickstart-cmake.html)