---
create: '2025-03-01'
modified: '2025-03-01'
---

# 00 install & import

## install

```shell
go mod init <module-name>
go get github.com/gin-gonic/gin
```

## import

```go
import "github.com/gin-gonic/gin"
```

（可选）导入net/http。例如，如果使用常量，则需要这样做：`http.StatusOK`。

```go
import "net/http"
```