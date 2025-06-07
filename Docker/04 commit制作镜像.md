---
create: 2023-07-08
modified: '2025-06-07'
---

# 使用commit命令制作镜像

## 基础：根据容器制作镜像

docker支持修改容器之后，在外部将修改后的`容器`打包成一个新的镜像。

```shell
# commit
docker commit -m="提交的描述信息" -a="作者" 容器ID 要创建的镜像名:[标签名]
# example
docker commit -m="add vim" -a="Yunyin" ba6acccedd29 Linux:[1.1]
```

## 原理

docker commit能制作镜像的根本原理，是镜像分层，详细见镜像底层原理。

本次打包所有的更改都会被放在最上层的镜像文件里，镜像文件会增加修改的部分，适合快速制作镜像并分发。

如果需要更加精确地分发，应该编写Dockerfile自底而上地去准确使用最佳镜像结构。