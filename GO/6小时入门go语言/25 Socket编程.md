---
create: 2023-08-22
---
# Socket编程

​	go语言的Socket编程更简单，服务器用`Listen`函数监听端口，客户端用`Dial`函数连接端口，双方都用`Read`和`Write`来通信。

## 1.服务器监听端口（Listen函数）

```go
package main

import (
	"fmt"
	"net"
)

func main() {
	// 监听端口
	listener, err := net.Listen("tcp", "localhost:8080")
	if err != nil {
		fmt.Println("监听失败：", err.Error())
		return
	}

	defer listener.Close()

	fmt.Println("正在监听端口 8080...")

	for {
		// 接受连接
		conn, err := listener.Accept()
		if err != nil {
			fmt.Println("接受连接失败：", err.Error())
			continue
		}

		// 处理连接（这里只打印一条信息）
		go handleConnection(conn)
	}
}

func handleConnection(conn net.Conn) {
	remoteAddr := conn.RemoteAddr().String()
	fmt.Printf("收到来自 %s 的连接\n", remoteAddr)

	// 关闭连接
	conn.Close()
}

```

## 2.客户端连接服务器（Dial）

```go
package main

import (
	"fmt"
	"net"
)

func main() {
	// 连接服务器
	conn, err := net.Dial("tcp", "localhost:8080")
	if err != nil {
		fmt.Println("连接失败：", err.Error())
		return
	}

	defer conn.Close()

	fmt.Println("已成功连接服务器")

	// 可以在此处进行读写操作...
}

```

