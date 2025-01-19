---
create: '2025-01-17'
modified: '2025-01-17'
---

# cmake打印所有变量和环境变量

* 参考资料：https://stackoverflow.com/questions/9298278

## 打印所有变量

```cmake
get_cmake_property(_variableNames VARIABLES)
list (SORT _variableNames)
foreach (_variableName ${_variableNames})
    message(STATUS "${_variableName}=${${_variableName}}")
endforeach()
```

## 打印环境变量

```cmake
execute_process(COMMAND "${CMAKE_COMMAND}" "-E" "environment")
```