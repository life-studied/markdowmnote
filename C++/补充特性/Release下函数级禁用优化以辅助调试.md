---
create: '2025-04-13'
modified: '2025-04-13'
---

# Release下函数级禁用优化以辅助调试

## 禁用优化宏

```C++
#ifdef _MSC_VER
#define DISABLE_OPTIMIZATION __pragma(optimize("", off))
#define ENABLE_OPTIMIZATION __pragma(optimize("", on))
#elif defined(__clang__)
#define DISABLE_OPTIMIZATION _Pragma("clang optimize off")
#define ENABLE_OPTIMIZATION _Pragma("clang optimize on")
#elif defined(__GNUC__)
#define DISABLE_OPTIMIZATION _Pragma("GCC push_options") _Pragma("GCC optimize(\"O0\")")
#define ENABLE_OPTIMIZATION _Pragma("GCC pop_options")
#else
#define DISABLE_OPTIMIZATION
#define ENABLE_OPTIMIZATION
#endif
```

## 使用

```C++
DISABLE_OPTIMIZATION
void f() {
    ...
}
ENABLE_OPTIMIZATION
```