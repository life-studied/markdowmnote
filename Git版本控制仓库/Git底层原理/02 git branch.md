# git branch

​	branch可以理解为一个另一个对象，其存储在`.git/refs/heads/`下，一个文件就是一个分支对象。

<img src="./assets/image-20240324214459710.png" alt="image-20240324214459710" style="zoom:33%;" />

​	其内部存储了指向当前分支的最新commit的hash值。

<img src="./assets/image-20240324214604147.png" alt="image-20240324214604147" style="zoom:33%;" />

## 参考资料

* [【*原理*解析】让你完全搞明白*Git*是如何管理你的代码的](https://www.bilibili.com/video/BV11z4y1X79p/)