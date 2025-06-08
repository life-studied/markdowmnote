---
create: 2023-07-08
modified: '2025-06-08'
---

# Dockerfile基础

## quick view

```dockerfile
FROM alpine:latest
WORKDIR /app		# 进入后的工作目录
COPY src/ /app		# build时copy的内容
ARG text="hello dockerfile"	# build时的变量
RUN echo $text >> hello.txt	# build时执行的shell命令（通常用于install环境）
ENV hello_text=$text		# build时和run时都存在的环境变量
CMD cat hello.txt			# run时启动执行的命令
```

## RUN

在build时执行的指令。有两种使用方式：

* shell格式

```dockerfile
RUN shell命令
```

* exec格式

```dockerfile
RUN ["可执行文件"，"参数1"，"参数2"]
# RUN ["./test.php","dev","offline"]
```

## CMD

容器启动后执行的指令。只会执行最后一条`CMD`指令。

```dockerfile
CMD cataline.sh run
```

注意：会被`docker run`后的参数覆盖。如果`docker run... /bin/bash`则会不执行`CMD`后的指令。

## ENRTRYPOINT

同样是容器启动后执行的指令，但不会被`docker run`后的命令覆盖。

另外，如果后面跟CMD，将会把CMD中的参数作为默认参数传递给当前命令，如果有传参，则会覆盖该参数。

**案例**

```dockerfile
FROM ubuntu
COPY myscript.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/myscript.sh
ENTRYPOINT ["myscript.sh"]
CMD ["arg1", "arg2"]
```

| docker run 传参              | 容器启动时执行        |
| ---------------------------- | --------------------- |
| docker run myimage           | myscript.sh arg1 arg2 |
| docker run myimage arg3 arg4 | myscript.sh arg3 arg4 |

## 其它

### EXPOSE

用于提示用户，容器运行时会对外暴露的端口，可选协议默认tcp。

```dockerfile
EXPOSE <port> [<port>...]
# EXPOSE 8080/tcp 8080/udp
# EXPOSE 8080 9090
```

`EXPOSE`命令不会有效果，只是指示用户在`docker run`时需要用`-p`做好端口映射。

```shell
docker run -p 80:8080 myapp
```

### ENV

作用于build和run时的环境变量。

```dockerfile
ENV CATALINE_HOME /usr/local/tomcat
WORKDIR $CATALINE_HOME
CMD echo $CATALINE_HOME
```

### VOLUME

指示容器数据卷目录，用于指示用户去主动挂载该目录到宿主机。

```dockerfile
VOLUME /data

docker run -v /home/user1/data:/data myapp
```

> 如果在运行容器时没有指定 `-v` 或 `--volume` 参数，Docker 会根据 `VOLUME` 指令自动创建一个匿名卷，并将其挂载到容器内部指定的路径。匿名卷的具体路径由 Docker 管理，通常位于 `/var/lib/docker/volumes/` 目录下。

### COPY

将宿主机中的文件或目录拷贝到镜像中。

* src：源文件或目录的路径。可以是`相对路径`（相对于 Dockerfile 所在目录），也可以是绝对路径。
* dest：路径必须是`绝对路径`，或者以 `/` 开头。

```dockerfile
# 将当前目录下的src目录拷贝到容器内的/dest下
COPY src/ /dest

# 将test.txt拷贝到容器/app下
COPY test.txt /app
```