---
create: '2025-07-06'
modified: '2025-07-06'
---

# 标准库说明

## io

golang对io的处理，建立于io库的接口之上，但io库不提供实现，只是提供Reader和Writer接口，和一些其他接口。

在此之上，需要存在实际的io对象才能使用接口。这些io对象既是读写的真实对象，又实现了`io.Reader`和`io.Writer`接口，例如：字符串、文件、标准输入输出、网络等。

一般不会直接使用它们的io接口，而是将它们传入到更好用的封装好的工具里，例如：

* 结构体：`bufio.Scanner`等
* 函数：`ioutil.ReadAll`等

### 相关包

* io
* bufio
* ioutil