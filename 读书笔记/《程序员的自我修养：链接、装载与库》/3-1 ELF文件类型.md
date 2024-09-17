# ELF文件类型

## 类型-分类

Windows下的PE和Linux下的ELF都是可执行文件，都属于COFF格式的变种。

目标文件与可执行文件的内容和格式很相似，所以采取一种格式存储，从广义上可以看成一种文件。

ELF文件标准将系统中采用的ELF格式的文件归类如下：

| ELF文件类型                      | 说明                                                         | 实例                             |
| -------------------------------- | ------------------------------------------------------------ | -------------------------------- |
| 可重定位文件                     | 包含了代码和数据<br />可以被用来链接成可执行文件或共享目标文件<br />静态链接库也在这一类 | Linux的.o<br />Windows的.obj     |
| 可执行文件                       | 可以直接执行的程序，代表就是ELF可执行文件，一般没有扩展名    | /bin/bash文件<br />Windows的.exe |
| 共享目标文件                     | 包含了代码和数据，可以在两种情况下使用：<br />一种是链接器可以使用这种文件和其它可重定位文件和共享目标文件链接，形成新的目标文件<br />一种是动态链接器可以将几个这种文件与可执行文件结合，作为进程映像的一部分来运行 | Linux的.so<br />Windows的DLL     |
| 核心转储文件<br />Core Dump File | 当进程意外终止时，系统可以将该进程的地址空间的内容以及终止时的一些其它信息转储到Core Dump | Linux下的Core Dump               |

## 查看

可以通过`file`命令查看文件的类型：

```shell
file xxx.o
file /bin/bash
file /lib/ldxxx.so
```

for example:

```shell
yunyin@Yunyin:~$ file /bin/bash
/bin/bash: ELF 64-bit LSB pie executable, x86-64, version 1 (SYSV), dynamically linked, interpreter /lib64/ld-linux-x86-64.so.2, BuildID[sha1]=7a6408ba82a2d86dd98f1f75ac8edcb695f6fd60, for GNU/Linux 3.2.0, stripped
```

