---
create: '2024-12-23'
modified: '2024-12-24'
---

# channel：类管道的流式处理

channel本身也能作为被channel传递的类型。由此就可以做到流式传递数据。

```go
package main

import (
	"fmt"
)

type PipeData struct {
	value int
	handler func(int) int
	next chan int
}

func handle(queue <-chan *PipeData) {
	for data := range queue {
		data.next <- data.handler(data.value)
	}
}

func main() {
	// 初始化管道
	p := make(chan *PipeData)
	next_p := make(chan int)

	// 启动协程
	go handle(p)

	for i := 0; i < 100; i++ {
		// 创建 PipeData 实例并发送到管道
		p <- &PipeData{i, func(x int) int { return x + 1 }, next_p}
	}

	// 从 next_p 接收处理结果
	for i := 0; i < 100; i++ {
		fmt.Println(<-next_p) // 打印处理后的结果
	}
}
```