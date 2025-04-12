---
create: '2025-04-12'
modified: '2025-04-12'
---

# vscode-golang环境配置

## 1. 安装g工具（推荐）

使用[g工具](../g管理golang版本/g管理go版本.md)去管理golang版本，可以在需要的时候随时切换对应的版本。

当然也可以直接安装golang SDK。

## 2. vscode安装Go插件

![image-20250412233137673](./assets/image-20250412233137673.png)

安装好后，使用ctrl+shift+P，打开命令面板，输入下面这个命令：

![image-20250412233230498](./assets/image-20250412233230498.png)

全选之后确定，就可以自动安装对应的golang工具了：

![image-20250412233258899](./assets/image-20250412233258899.png)

等待输出即可（挂梯子，否则就要配置goproxy环境变量去找国内代理）：

![image-20250412233949594](./assets/image-20250412233949594.png)

## 测试开发

### 创建初始工程

```powershell
go mod init test
```

### 创建main.go

```go
// main.go
package main

import "fmt"

func main() {
	fmt.Println("Hello World!")
}

```

#### run

```powershell
PS D:\codeSpace\bad_code\test_go_init> go run main.go  
Hello World!
```

## 补充：切换golang版本

在vscode右下角点击当前的golang版本：

![image-20250412234352074](./assets/image-20250412234352074.png)

点击Choose Go Environment（或者用ctrl+shift+P，输入`Go:Choose Go Environment`）：

![image-20250412234415974](./assets/image-20250412234415974.png)

由于vscode提供的自动下载的版本不全，因此推荐使用g工具管理golang版本，再从这里去选择对应的go.exe的二进制文件，即可换上对应的golang版本：

![image-20250412234548822](./assets/image-20250412234548822.png)