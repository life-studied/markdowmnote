---
create: '2025-06-08'
modified: '2025-06-08'
---

# docker build说明

```shell
docker build -t myapp:v1 .
```

以下是一些常用的 `docker build` 参数及其用途：

| 参数           | 描述                                                         |
| :------------- | :----------------------------------------------------------- |
| `-t`           | 指定镜像的名称和标签（repository:tag）。如果不指定标签，默认为 `latest`。 |
| `-f`           | 指定 Dockerfile 的路径。默认为当前目录下的 `Dockerfile`。    |
| `--build-arg`  | 设置构建时的变量（build arguments），用于指定替换Dockerfile中的ARG的值。 |
| `--cache-from` | 指定镜像作为缓存来源，加速构建过程。                         |
| `--no-cache`   | 禁用构建缓存，强制重新构建所有步骤。                         |
| `--pull`       | 在构建之前尝试拉取最新的基础镜像。                           |
| `--rm`         | 构建完成后删除中间容器，默认为 `true`。                      |
| `--squash`     | 将构建结果压缩为一个层（需要 Docker 17.05 及以上版本）。     |
| `--target`     | 指定多阶段构建中的目标阶段。                                 |
| `--label`      | 为镜像添加元数据标签。                                       |