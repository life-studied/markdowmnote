---
create: '2025-01-02'
modified: '2025-01-02'
---

# MSVC输出路径问题

MSVC输出的路径会自动根据Debug和Release模式，在设置的生成目录下多一层目录。

例如：

```cmake
set(CMAKE_RUNTIME_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/bin)
```

MSVC会将exe生成在/bin/Debug下。这会导致某些时候exe目录与希望的不一致（这可能发生在有一个外部的dll，需要与exe同目录）

## 解决方法

### 破坏MSVC的规则

通过下面的方法就能解决MSVC的规则问题：

```cmake
set(targetname HelloWorld)
set(target_output_path ${CMAKE_CURRENT_SOURCE_DIR}/bin)

if(MSVC)
    set_target_properties( ${targetname} PROPERTIES LIBRARY_OUTPUT_DIRECTORY ${target_output_path} )
    set_target_properties( ${targetname} PROPERTIES LIBRARY_OUTPUT_DIRECTORY_DEBUG ${target_output_path} )
    set_target_properties( ${targetname} PROPERTIES LIBRARY_OUTPUT_DIRECTORY_RELEASE ${target_output_path} )
    # etc for the other available configuration types (MinSizeRel, RelWithDebInfo)
endif(MSVC)
```

### 遵循MSVC的规则

经过考虑，遵循MSVC的规则是一种可以接受的解决方案，让需要拷贝的文件去符合它的目录规则。

下面是一个示例，子项目中存在一些需要拷贝的文件，会自动拷贝到上级目录设置的`CMAKE_RUNTIME_OUTPUT_DIRECTORY`中。

```cmake
# 检查父级CMakeLists.txt是否设置了输出路径，如果没有直接报错
if(NOT CMAKE_RUNTIME_OUTPUT_DIRECTORY)
    message(FATAL_ERROR "Please set CMAKE_RUNTIME_OUTPUT_DIRECTORY in parent CMakeLists.txt")
endif()

# 设置输出dll的目录
set(MysqlConn_LIB_DIRS ${CMAKE_RUNTIME_OUTPUT_DIRECTORY})

# 导出变量给上层CMakeLists.txt使用
set(MysqlConn_INCLUDE_DIRS ${CMAKE_CURRENT_SOURCE_DIR}/MysqlConn/include PARENT_SCOPE)
set(MysqlConn_LIB_DIRS ${CMAKE_RUNTIME_OUTPUT_DIRECTORY} PARENT_SCOPE)

# 特别设置：MSVC库输出目录
# 1. 保证后文拷贝的文件在正确的dll输出目录
# 2. 保证上层CMakeLists.txt能够正确找到dll
if(${CMAKE_GENERATOR} MATCHES "Visual Studio*")
    if(NOT CMAKE_BUILD_TYPE)
        set(CMAKE_BUILD_TYPE "Debug")
    endif()
    # 修改MysqlConn_LIB_DIRS至bin/Debug或bin/Release
    set(MysqlConn_LIB_DIRS ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE})
    set(MysqlConn_LIB_DIRS ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE} PARENT_SCOPE)
endif()

# 拷贝必要文件
file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/Mysql/lib/libmysql.dll DESTINATION ${MysqlConn_LIB_DIRS})
file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/MysqlConn/dbConfig.json DESTINATION ${MysqlConn_LIB_DIRS})
```