## goroutine使用案例

```go
package main

import "fmt"

func Add(x, y int, ch chan int) {
	fmt.Println(x, "+", y, "=", x+y)
	ch <- 1
}

func main() {
	chs := make([]chan int, 100)
	for i := 0; i < 100; i++ {
		chs[i] = make(chan int)
		go Add(i, i, chs[i])
	}

	for i := 0; i < 100; i++ {
		<-chs[i]
	}
}

```

