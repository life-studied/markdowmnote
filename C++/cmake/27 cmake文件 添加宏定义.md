---
create: 2024-10-30
---
# cmake文件添加宏定义

## example

### cmake

```cmake
set(project_name "test_macro")

# ...

target_compile_definitions(${project_name} PRIVATE TTF_FILE_PATH="${PROJECT_SOURCE_DIR}/ttf/SmileySans-Oblique.ttf")
```

### init.cpp

```C++
#ifndef TTF_FILE_PATH
#error "TTF_FILE_PATH not defined"
#endif

// ...

io.Fonts->AddFontFromFileTTF(TTF_FILE_PATH, 28, nullptr, io.Fonts->GetGlyphRangesChineseFull());
```

## 有参宏

### add_definitions（早期版本不建议使用）

```cmake
add_definitions(-DNONE_VALUE_MACRO=0x10000000)
```

### target_compile_definitions（为指定target添加宏）！！！

```cmake
target_compile_definitions(myapp PRIVATE PI=3.1415926)
```

## 无参宏

```cmake
add_definitions(-DNONE_VALUE_MACRO)

add_compile_definitions(NONE_VALUE_MACRO)

target_compile_definitions(myapp PRIVATE NONE_VALUE_MACRO)
```

