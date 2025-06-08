---
create: '2025-06-07'
modified: '2025-06-08'
---

# 自定义网络模式

自定义bridge网络可以做到更好的隔离效果，将互相之间没有关系的容器，互相隔离在不同的网络下。

## 创建网络

```shell
docker network create yunyin_network
```

## 使用新网络启动容器

```shell
# 容器1
docker run -d --network yunyin_network --name u1 ubuntu
# 容器2
docker run -d --network yunyin_network --name u2 ubuntu
```