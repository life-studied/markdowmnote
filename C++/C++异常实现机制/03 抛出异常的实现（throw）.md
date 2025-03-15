---
create: '2025-03-15'
modified: '2025-03-15'
---

# 抛出异常（throw）

在编译一段 C++ 代码时，编译器会将所有 throw 语句替换为其 C++ 运行时库中的某一指定函数，这里我们叫它 `__CxxRTThrowExp(EXCEPTION* e)`（实际它可以是任意名称）。

该函数接收一个编译器认可的内部结构（我们叫它 `EXCEPTION` 结构）。这个结构中包含了：

* 待抛出异常对象的起始地址
* 用于销毁它的析构函数
* 以及它的 `type_info` 信息

备注：对于没有启用 RTTI 机制（编译器禁用了 RTTI 机制或没有在类层次结构中使用虚表）的异常类层次结构，可能还要包含其所有基类的 type_info 信息，以便与相应的 catch 块进行匹配。

## 抛出异常

假设发生了`throw myExp(1)`，实际上编译器会这样做：

```C++
struct EXCEPTION {
    void* pObj = &myExp(1);
    void (*pfnDestory)(void*);
    type_info* tblTypes[] = &{typeid(myExp)};
}iExp;

call __CxxRTThrowExp(&iExp);
```

实际上在多数情况下，`__CxxRTThrowExp` 函数即我们前面曾多次提到的“异常处理器”，异常捕获和栈回退等各项重要工作都由它来完成：

`__CxxRTThrowExp` 首先接收（并保存）EXCEPTION 对象；然后从 `TLS：Current ExpHdl` 处找到与当前函数对应的 `piHandler`、`nStep` 等异常处理相关数据；并按照前文所述的机制完成异常捕获和栈回退。由此完成了包括“抛出”->“捕获”->“回退”等步骤的整套异常处理机制。

最后再附上zheng'ge

![image-20250315144244578](./assets/image-20250315144244578.png)