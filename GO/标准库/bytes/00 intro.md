---
create: '2025-07-08'
modified: '2025-07-08'
---

# bytes包

bytes包的功能和strings基本一致，只是作用对象从`string`变成了`[]byte`。由于[]byte没有string的内置运算符重载，因此多了一些需要记忆的函数。

1. 字符串基本功能：

   * `Contains`：检查是否有子串
   * `Count`：检查子串数量
   * `Equal`：比较两个字符串相等
   * `Join`：拼接字符串
   * `Index`：获取子串下标
   * `Clone`：拷贝字符串

2. Reader结构体：用于从一个[]byte中读取数据，实现了io.Reader接口等，主要用于 Read 数据。

   > 实现了io 包下的 Reader, ReaderAt, RuneReader, RuneScanner, ByteReader, ByteScanner, ReadSeeker, Seeker, WriterTo 等多个接口

3. Buffer结构体：用于更加方便的上层读写操作。

   >实现了 io 包下的 ByteScanner, ByteWriter, ReadWriter, Reader, ReaderFrom, RuneReader, RuneScanner, StringWriter, Writer, WriterTo 等接口