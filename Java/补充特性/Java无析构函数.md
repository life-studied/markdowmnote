---
create: '2025-05-23'
modified: '2025-05-23'
---

# Java没有析构函数

## 旧版本的finalize()

在Java中，没有对应C++里的析构函数，虽然在旧版本中有一个`finalize`（JDK8以上不再支持），但是这个函数是仅用于“对象以某种方式分配存储空间，而不是通过创建对象来分配。”

>看起来之所以要有`finalize()`方法，是因为你可能没有使用Java中的通用方式来分配内存，而是采用了类似C语言的机制。
>
>这主要通过**本地方法**来实现，它可以在Java代码里调用非Java代码。Java里的本地方法目前只支持C和C++，但这些语言可以调用其他语言的代码，所以实际上Java可以调用任何代码。
>
>在非Java代码里，可能会调用C的malloc()系列函数来分配存储空间，此时除非明确调用了free()方法，否则该存储空间不会被释放，从而导致内存泄漏。free()是一个C和C++函数，所以需要在`finalize()`里通过本地方法来调用。

## finalize的陷阱

如果一定要使用`finalize`来实现析构的效果，就会成为一个陷阱。因为Java中的内存回收是通过GC来完成，而GC只有到快满的时候，才会启动一次，而非离开作用域的时候启动一次。这也就导致了，不确定什么时候`finalize`才会被调用。

## try-with-resource

Java提供了一个离开作用域清理资源的方式，即try-with-resources，在Java7引入。

它提供了一种自动关闭资源的机制。只要资源实现了 `java.lang.AutoCloseable` 接口或其子接口 `java.io.Closeable`，就可以在 `try` 块中声明这些资源，而不需要在 `finally` 块中手动关闭它们。

```Java
try (
    FileInputStream fis1 = new FileInputStream("file1.txt");
    FileInputStream fis2 = new FileInputStream("file2.txt");
    ) {
    // ...
} 
catch (IOException e) {}
```