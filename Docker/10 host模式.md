---
create: '2025-06-07'
modified: '2025-06-07'
---

# host模式（共享主机ip）

容器直接使用宿主机ip与外部进行通信。

```shell
docker run -it --net=host tomcat /bin/bash
```