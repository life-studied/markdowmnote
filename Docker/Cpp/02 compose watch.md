---
create: '2024-12-23'
modified: '2024-12-23'
---

# compose watch

> 使用watch配置可以在工作时，一旦出现文件修改后保存就会自动进行操作（例如rebuild）。

## 添加watch选项

```yaml
services:
  ok-api:
    image: ok-api
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    develop:
      watch:
        - action: rebuild
          path: .
```

## Cmd

```shell
docker compose watch
```