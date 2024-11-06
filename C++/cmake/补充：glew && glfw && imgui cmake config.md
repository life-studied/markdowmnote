# glew && glfw && imgui cmake config

>使用FetchContent模块
>
>* 二进制库：下载对应的zip链接，解压到对应的库
>* 源码：下载对应的仓库地址到指定地址

## 目录

```
.
├── CMakeLists.txt
├── Init.hpp
├── cmake
│   ├── Glew.cmake
│   ├── Glfw.cmake
│   └── Imgui.cmake
├── externals
├── test.cpp
└── ttf
    └── SmileySans-Oblique.ttf
```

## Glew.cmake

```cmake
include(FetchContent)

# 设置Glew的下载链接
if(WIN32)
  set(Glew_URL https://github.com/nigels-com/glew/releases/download/glew-2.2.0/glew-2.2.0-win32.zip)
else()
  message(FATAL_ERROR "Unsupported platform")
endif()

# 获取Glew的压缩包文件名
get_filename_component(Glew_ZIP_FILENAME "${Glew_URL}" NAME)

set(Glew_FILENAME "glew-2.2.0")

# 下载Glew
FetchContent_Declare(
  Glew
  URL ${Glew_URL}
  URL_HASH SHA256=ea6b14a1c6c968d0034e61ff6cb242cff2ce0ede79267a0f2b47b1b0b652c164
  DOWNLOAD_DIR ${EXTERNAL_PROJECTS_DIR}/Glew
  DOWNLOAD_EXTRACT_TIMESTAMP true
)

FetchContent_MakeAvailable(Glew)

# 解压Glew
execute_process(COMMAND ${CMAKE_COMMAND} -E tar xzf ${EXTERNAL_PROJECTS_DIR}/Glew/${Glew_ZIP_FILENAME}
                    WORKING_DIRECTORY ${EXTERNAL_PROJECTS_DIR}/Glew)

# 设置Glew的安装路径
set(Glew_INSTALL_DIR ${EXTERNAL_PROJECTS_DIR}/Glew/${Glew_FILENAME})

# 添加Glew的头文件路径
target_include_directories(${project_name} PRIVATE ${Glew_INSTALL_DIR}/include)
message(STATUS "Glew_INCLUDE_DIR: ${Glew_INSTALL_DIR}/include")

# 添加Glew的库文件路径
set(Glew_LIB_DIR ${Glew_INSTALL_DIR}/bin/Release/x64)

target_link_directories(${project_name} PRIVATE ${Glew_LIB_DIR})

# 添加Glew的库文件
set(Glew_DLLS glew32.dll)
set(Glew_LIBS glew32s.lib)
```

## Glfw.cmake

```cmake
include(FetchContent)

# 设置Glfw的下载链接
if(WIN32)
  set(Glfw_URL https://github.com/glfw/glfw/releases/download/3.4/glfw-3.4.bin.WIN64.zip)
else()
  message(FATAL_ERROR "Unsupported platform")
endif()

# 获取Glfw的压缩包文件名
get_filename_component(Glfw_ZIP_FILENAME "${Glfw_URL}" NAME)

string(REGEX REPLACE "\\.[^.]*$" "" Glfw_FILENAME ${Glfw_ZIP_FILENAME})

# 下载Glfw
FetchContent_Declare(
  Glfw
  URL ${Glfw_URL}
  URL_HASH SHA256=54efa829400f2a0537f742b2b3bdd74e437bb4f2f048e4b7d3c5557d11a611e6
  DOWNLOAD_DIR ${EXTERNAL_PROJECTS_DIR}/glfw
  DOWNLOAD_EXTRACT_TIMESTAMP true
)

FetchContent_MakeAvailable(Glfw)

# 解压Glfw
execute_process(COMMAND ${CMAKE_COMMAND} -E tar xzf ${EXTERNAL_PROJECTS_DIR}/glfw/${Glfw_ZIP_FILENAME}
                    WORKING_DIRECTORY ${EXTERNAL_PROJECTS_DIR}/glfw)

# 设置Glfw的安装路径
set(GLFW_INSTALL_DIR ${EXTERNAL_PROJECTS_DIR}/glfw/${Glfw_FILENAME})

# 添加Glfw的头文件路径
include_directories(${GLFW_INSTALL_DIR}/include)

# 添加Glfw的库文件路径
if(CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
  set(GLFW_LIB_DIR ${GLFW_INSTALL_DIR}/lib-mingw-w64)
  message(STATUS "Compiling with MinGW, setting GLFW_LIB_DIR to ${GLFW_LIB_DIR}")
elseif(CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
  set(GLFW_LIB_DIR ${GLFW_INSTALL_DIR}/lib-vc2022)
  message(STATUS "Compiling with MSVC, setting GLFW_LIB_DIR to ${GLFW_LIB_DIR}")
else()
  message(FATAL_ERROR "Unsupported compiler")
endif()

target_link_directories(${project_name} PRIVATE ${GLFW_LIB_DIR})

# 添加Glfw的库文件
set(GLFW_DLLS glfw3.dll)
set(GLFW_LIBS glfw3.lib)
```

## Imgui.cmake

```cmake
include(FetchContent)


# 下载ImGui
FetchContent_Declare(
  ImGui
  GIT_REPOSITORY git@github.com:ocornut/imgui.git
  GIT_TAG v1.91.4-docking
  SOURCE_DIR ${EXTERNAL_PROJECTS_DIR}/imgui
)

FetchContent_MakeAvailable(ImGui)

# 添加ImGui的头文件路径
include_directories(${EXTERNAL_PROJECTS_DIR}/imgui)
include_directories(${EXTERNAL_PROJECTS_DIR}/imgui/backends)

# 添加ImGui的源文件
set(IMGUI_SRCS
    ${EXTERNAL_PROJECTS_DIR}/imgui/imgui_demo.cpp
    ${EXTERNAL_PROJECTS_DIR}/imgui/imgui_draw.cpp
    ${EXTERNAL_PROJECTS_DIR}/imgui/imgui_tables.cpp
    ${EXTERNAL_PROJECTS_DIR}/imgui/imgui_widgets.cpp
    ${EXTERNAL_PROJECTS_DIR}/imgui/imgui.cpp
    ${EXTERNAL_PROJECTS_DIR}/imgui/backends/imgui_impl_glfw.cpp
    ${EXTERNAL_PROJECTS_DIR}/imgui/backends/imgui_impl_opengl3.cpp
)
```

## CmakeLists.txt

```cmake
cmake_minimum_required(VERSION 3.20)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(project_name "myglfwtest")

project(${project_name})

# 添加externals相关变量
set(CMAKE_MODULE_PATH "${CMAKE_CURRENT_SOURCE_DIR}/cmake;${CMAKE_MODULE_PATH}")
set(EXTERNAL_PROJECTS_DIR "${CMAKE_CURRENT_SOURCE_DIR}/externals")

# 导入ImGui
include(Imgui)

add_executable(${project_name} test.cpp Init.hpp ${IMGUI_SRCS})

# 导入Glfw
include(Glfw)

# 导入Glew
include(Glew)

target_link_libraries(${project_name} 
    PRIVATE ${GLEW_LIBS}
    PRIVATE ${GLFW_LIBS}
    PRIVATE Opengl32.lib
)

target_link_libraries(${project_name}
    PRIVATE ${GLEW_DLLS}
    PRIVATE ${GLFW_DLLS}
)

target_compile_definitions(${project_name} PRIVATE TTF_FILE_PATH="${PROJECT_SOURCE_DIR}/ttf/SmileySans-Oblique.ttf")
```

