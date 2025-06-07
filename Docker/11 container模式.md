---
create: '2025-06-07'
modified: '2025-06-07'
---

# container模式（容器共享ip）

新建的容器和一个已经存在的指定容器，共享一个网络ip配置，就像它们运行在同一个主机上一样。

```shell
docker run -it --name alpine1 alpine /bin/sh	#第一个容器
docker run -it --network container:alpine1 --name alpine2 alpine /bin/sh	#第二个容器
```

* 它们具有相同的网络IP地址
* 其他资源（如文件系统、进程空间等）仍然隔离