---
create: 2024-07-30
---
# 更严格的编译指令（Linux）

```cmake
# helpful compiler flags for gcc/clang
# the descriptions for these flags can be found on the GNU Compiler Collection's webpage.
add_compile_options(
  -Wall
  -Wextra
  -Werror
  -pedantic-errors
  -Wconversion
  -Wsign-conversion
  -Wdouble-promotion
  -Wcast-align
  -Wformat=2
  -Wuninitialized
  -Wnull-dereference
  -Wnon-virtual-dtor
  -Woverloaded-virtual
  -Wdeprecated-copy-dtor
  -Wold-style-cast
  -Wzero-as-null-pointer-constant
  -Wsuggest-override
  -fstack-protector-strong
)
```

解释：

1. `-Wall`：打开几乎所有的警告信息，除了一些非常具体或不常见的问题。
2. `-Wextra`：打开额外的警告信息，比 `-Wall` 更加严格。
3. `-Werror`：将所有警告视为错误。
4. `-pedantic-errors`：将所有语言不兼容的警告视为错误。
5. `-Wconversion`：警告隐式转换可能导致精度损失的情况。
6. `-Wsign-conversion`：警告隐式转换可能导致符号改变的情况。
7. `-Wdouble-promotion`：警告隐式将 float 转换为 double 的情况。
8. `-Wcast-align`：警告指针转换可能导致对齐问题的情况。
9. `-Wformat=2`：启用更严格的格式字符串检查。
10. `-Wuninitialized`：警告使用未初始化的变量。
11. `-Wnull-dereference`：警告解引用空指针的情况。
12. `-Wnon-virtual-dtor`：警告非虚析构函数的使用，这可能在多态类中导致问题。
13. `-Woverloaded-virtual`：警告重载的虚函数可能导致意外行为。
14. `-Wdeprecated-copy-dtor`：警告使用已弃用的复制构造函数或析构函数。
15. `-Wold-style-cast`：警告使用旧式 C 风格的类型转换。
16. `-Wzero-as-null-pointer-constant`：警告使用字面量 0 作为空指针常量。
17. `-Wsuggest-override`：在适当的地方建议使用 `override` 来明确虚函数的重写。
18. `-fstack-protector-strong`：启用强栈保护，以防止栈溢出攻击。