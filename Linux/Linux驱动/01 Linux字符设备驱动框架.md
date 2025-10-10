---
create: '2025-10-10'
modified: '2025-10-10'
---

# Linux字符设备驱动框架

字符设备是Linux三大设备之一(另外两种是块设备，网络设备)，字符设备就是字节流形式通讯的I/O设备。

绝大部分设备都是字符设备，常见的字符设备包括鼠标、键盘、显示器、串口等等，当我们执行`ls -l /dev`的时候，就能看到大量的设备文件，c就是字符设备，b就是块设备，网络设备没有对应的设备文件。

## 参考资料

* [ARM-Linux-Study/【Linux 通用驱动开发】 at main · Staok/ARM-Linux-Study](https://github.com/Staok/ARM-Linux-Study/tree/main/【Linux 通用驱动开发】)