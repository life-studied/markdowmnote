# 补充：opencv && Eigen3 cmake

## 使用find_package

```cmake
cmake_minimum_required(VERSION 3.25)
project(my_project)

set(CMAKE_C_STANDARD 11)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_C_STANDARD_REQUIRED ON)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# eigen 
set(EIGEN_DIR "E:\\3rdparty\\eigen\\eigen-3.4.0")
include_directories(${EIGEN_DIR})

# opencv
set(OpenCV_DIR "E:\\opencv\\opencv\\build")  # 目录包含OpenCVConfig.cmake
find_package(OpenCV REQUIRED)	# 找到opencv库

# my proj
include_directories(${CMAKE_CURRENT_SOURCE_DIR})

add_executable(my_project main.cpp rasterizer.cpp Triangle.cpp)
target_link_libraries(${PROJECT_NAME} ${OpenCV_LIBRARIES})
```

## 使用手动导入

```cmake
cmake_minimum_required(VERSION 3.25)
project(my_project)

set(CMAKE_C_STANDARD 11)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_C_STANDARD_REQUIRED ON)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# eigen 
set(EIGEN_DIR "E:\\3rdparty\\eigen\\eigen-3.4.0")
include_directories(${EIGEN_DIR})

# opencv
set(OpenCV_DIR "E:\\opencv\\opencv\\build")  # 目录包含OpenCVConfig.cmake
include_directories(${OpenCV_DIR}/include)	# 找到opencv库
link_directories(${OpenCV_DIR}/x64/vc16/lib)
link_libraries(opencv_world470 opencv_world470d)

# my proj
include_directories(${CMAKE_CURRENT_SOURCE_DIR})

add_executable(my_project main.cpp rasterizer.cpp Triangle.cpp)
target_link_libraries(${PROJECT_NAME} ${OpenCV_LIBRARIES})
```

