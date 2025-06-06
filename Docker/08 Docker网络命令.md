---
create: 2023-07-08
modified: '2025-06-07'
---

# Docker网络

## docker虚拟网桥（默认模式）

​	在Linux宿主机中，运行`ifconfig`，可以看到其中的`docker0`，即docker的虚拟网桥：

```shell
docker0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.17.0.1  netmask 255.255.0.0  broadcast 172.17.255.255
        inet6 fe80::42:a7ff:fe1b:1aac  prefixlen 64  scopeid 0x20<link>
        ether 02:42:a7:1b:1a:ac  txqueuelen 0  (Ethernet)
        RX packets 117  bytes 32222 (31.4 KiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 201  bytes 19776 (19.3 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0s
```

#### 2. 查看Docker网络模式

```shell
docker network ls
```

| 网络模式       | 简介                                                         |
| -------------- | ------------------------------------------------------------ |
| `bridge`       | 为每一个容器分配、设置ip，并将容器连接到一个名为`docker0`的虚拟网桥，默认为该模式。 |
| `host`         | 容器将不会虚拟出自己的虚拟网卡，设置自己的ip等。而是使用宿主机的ip的端口。 使用`--network host` 参数可以指定 |
| `none`（少用） | 容器有独立的`network namespace`，但并没有对其进行任何网络设置，入分配`veth pair` 和网络桥接 ip等。 使用`--network none`参数指定。一般很少使用。 |
| `container`    | 新创建的容器不会创建自己的网卡和设置自己的ip。而是和一个指定的容器共享ip 端口范围等。 |

#### 3. 常用Docker网络命令

* `connect`：Connect a container to a network
	* docker connect 
* `create`：Create a network
	* **example**：`docker network create mynet`
* `disconnect`：Disconnect a container from a network
* `inspect`：Display detailed information on one or more networks
	* **example**：`docker network inspect bridge`
* `ls`：List networks
	* **example**：`docker network ls`
* `prune`：Remove all unused networks
* `rm`：Remove one or more networks
	* **example**：`docker rm mynet`