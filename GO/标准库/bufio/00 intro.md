---
create: '2025-02-25'
modified: '2025-02-25'
---

# bufio

bufio 包是对io包中接口的一种缓存型实现。它包装了 io.Reader 和 io.Writer 对象，创建了另外的Reader和Writer对象；这些对象也实现了io.Reader和io.Writer接口，不过它们是有缓存的。

该包同时为文本I/O提供了一些便利操作。