---
create: 2023-07-30
---
# go mod依赖管理

[TOC]

​	关键字：go mod;导包;导第三库;init函数;包别名;tidy;

## 1. 管理并使用自己的包

### 1.1 情景假设

​	假设项目目录如下：

```
project
	|__go.mod
	|__src
        |__main.go
        |__utils
            |__query.go
```

​	**go.mod**文件如下：

```go
mudule test

go 1.17
```

​	**main.go**文件如下：

```go
package main

import(
	"fmt"
)


func main(){
    ...
}
```

​	**query.go**文件如下：

```go
package utils	

func Query(){
    ...
}
```

### 1.2 导入utils包中的Query函数

#### 1.2.1 确保go.mod文件存在

​	导入包的时候，首先保证你的项目拥有`go.mod`文件，同时保证你的`go.mod`文件已被初始化，若没有，请用：`go mod init 模块名`的命令来创建`go.mod`文件。

```go
go mod init test
```

#### 1.2.2 确保被导入的函数名大写

​	导入包并想使用函数或者变量时，确保你的函数或变量的首字母大写，使其成为导出变量或函数。

#### 1.2.3 如何import包

​	在`import`的时候，从项目模块名开始，一级一级**目录**往下写。（也就是**包的路径**）

​	例如`utils`目录下的文件，你应该导入的是`test/src/utils`，即

```go
import(
	"fmt"
    "test/src/utils"
)
```

​	注意：导入的最后一级是文件夹名，不是包名。若文件夹名改成`hello`，则`import`的也应该是`test/src/hello`。

#### 1.2.4 如何使用函数

​	使用函数应该用`包名.函数名`的方式使用，具体如下：
```go
package main

import(
	"fmt"
    "test/src/utils"
)

func main() {
    utils.Query()
}
```

##### Tips:import取别名

​	你可以给`import`的包路径取别名，此时你后续使用就能用`别名.函数名`的方式调用。例如：

```go
import(
	"fmt"
    hello "test/src/utils"
)

func main() {
    hello.Query()	//utils.Query()
}
```

#### 1.2.5 运行main函数

​	运行main函数时，使用`go run main函数地址`的方式。无需指定到go文件，只需指定到所在目录即可。

```go
go run ./src
```

---

## 2. 如何导入第三方包

​	假设需要导入字节跳动的json序列化库：https://github.com/bytedance/sonic

```go
package main

import(
	"fmt"
)

func main() {
    ...
    //此处需要sonic.Marshal()
}
```

### 2.1 如何写import

​	从网址里的github.com开始，将后续的内容复制，写道import里：

```go
import(
	"fmt"
    "github.com/bytedance/sonic"
)
```

### 2.2 如何下载第三方包

​	`import`仅仅是将代码写好了，接下来还需要将该第三方包下载到本地使用。

#### 方法1：go get 地址

​	方法是在命令行使用`go get 地址`的方式下载：

```go
go get github.com/bytedance/sonic
```

​	此时，你的`go mod`里会生成`require`部分的内容，同时还会多出一个`go.sum`文件。（这些都不用关心，是自动完成的）

#### 方法2：go mod tidy

​	也可以直接使用`go mod tidy`命令，无需写入地址，结果与上面一致。

```go
go mod tidy
```

## 3. 其它补充内容

### 3.1 init函数

​	 每一个包下，都可以放置一个特殊的函数：**init函数**。init函数是导入一个包时自动执行的代码，不需要显式调用。（在**main函数执行前**完成的）

注意：

1. init函数没有参数和返回值。
2. init函数在一个包里可以存在多个。
3. init函数调用顺序是：按照import的顺序执行，同一包里按照上下的顺序执行。

---

#### 3.1.1 情景假设

​	例如还是一开始的目录结构。

```
project
	|__go.mod
	|__src
        |__main.go
        |__utils
            |__query.go
```

​	**main.go**

```go
package main

import(
	"fmt"
    "test/src/utils"
)

func main() {
    utils.Query()
}
```

​	**query.go**

```go
package utils	

import "fmt"

func Query(){
    ...
}

func init() {
    fmt.Println("init utils 1")
}

func init() {
    fmt.Println("init utils 2")
}
```

#### 3.1.2 结果

​	下面是命令以及执行结果：

```go
go run ./src

init utils 1
init utils 2
...
```

### 3.2 不引用函数，但保留import

​	这种情况用于：为了调用该包中的init函数，但是不需要引用包里的其它函数的情况。

#### 方法：下划线别名

​	只需要在import这个包的时候，在前面用下划线`_`作为别名即可。

```go
package main

import(
	"fmt"
    _ "test/src/utils"
)

func main() {
    ...
}
```

​	在这种情况下，init函数依旧会执行，同时也不会报错：没有引用包中的函数。