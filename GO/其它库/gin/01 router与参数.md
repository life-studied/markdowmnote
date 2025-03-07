---
create: '2025-03-02'
modified: '2025-03-02'
---

# router与参数

## router

### 单router

```go
package main

import (
    "net/http"
    "github.com/gin-gonic/gin"
)

func main() {
    r := gin.Default()
    r.GET("/", func(c *gin.Context) {
        c.String(http.StatusOK, "hello word")
    })
    r.POST("/xxxpost",getting)
    r.PUT("/xxxput")
    
    //监听端口默认为8080
    r.Run(":8000")
}
```

### router group

routes group是为了管理一些相同的URL。

```go
package main

import (
   "github.com/gin-gonic/gin"
   "fmt"
)

func main() {
    r := gin.Default()

    v1 := r.Group("/v1")
    {
        v1.GET("/login", login)
        v1.GET("submit", submit)
    }

    v2 := r.Group("/v2")
    {
        v2.POST("/login", login)
        v2.POST("/submit", submit)
    }
    
    r.Run(":8000")
}

func login(c *gin.Context) {
    name := c.DefaultQuery("name", "jack")
    c.String(200, fmt.Sprintf("hello %s\n", name))
}

func submit(c *gin.Context) {
    name := c.DefaultQuery("name", "lily")
    c.String(200, fmt.Sprintf("hello %s\n", name))
}
```

## 参数

### API参数（路径参数）

```go
package main

import (
    "net/http"
    "strings"
    "github.com/gin-gonic/gin"
)

func main() {
    r := gin.Default()
    r.GET("/user/:name/*action", func(c *gin.Context) {
        name := c.Param("name")
        action := c.Param("action")
        action = strings.Trim(action, "/")
        
        c.String(http.StatusOK, name+" is "+action)
    })
    //默认为监听8080端口
    r.Run(":8000")
}
```

* `c.Param`：获取路径参数
    * `:name`：匹配路径上的接下来的第一个路径段
    * `*action`：匹配路径上之后的所有内容（包括 `/`）。
* `action = strings.Trim(action, "/")`：action开头会附带一个`/`，有些反直觉，需要额外处理掉这个开头的`/`

#### 补充：放在中间的通配符

虽然 `*xxx` 通常用于末尾，但 Gin 允许它出现在路由的中间位置。

不过，这种用法相对少见，且可能会导致一些不直观的行为。例如：

```go
r.GET("/prefix/*middle/suffix", func(c *gin.Context) {
    middle := c.Param("middle")
    c.String(http.StatusOK, "Middle part: "+middle)
})
```

如果请求路径是 `/prefix/abc/def/suffix`，那么：

- `*middle` 会捕获 `/abc/def`，因为它是从 `*middle` 开始到下一个固定路径段 `/suffix` 之间的所有内容。
- `middle` 的值将是 `/abc/def`，包含斜杠。

### URL参数（附带参数）

- URL参数可以通过`DefaultQuery()`或`Query()`方法获取
    - `DefaultQuery()`若参数不存在，返回默认值
    - `Query()`若参数不存在，返回空串

```go
package main

import (
    "fmt"
    "net/http"
    "github.com/gin-gonic/gin"
)

func main() {
    r := gin.Default()
    r.GET("/user", func(c *gin.Context) {
        //指定默认值
        name := c.DefaultQuery("name", "yunyin")
        c.String(http.StatusOK, fmt.Sprintf("hello %s", name))
    })
    r.Run()
}
```