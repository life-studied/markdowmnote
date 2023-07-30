# 19 chan

## 1. 无缓冲的chan

​	所谓无缓冲chan就是容量为0的chan。

```go
// Package main -----------------------------
// @file      : chan.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/30 16:21
// -------------------------------------------
package main

import (
	"fmt"
	"sync"
	"time"
)

func main() {
	ch := make(chan int, 0)
	wg := sync.WaitGroup{}
	wg.Add(2)
	go func() {
		defer wg.Done()
		ch <- 7
		fmt.Println("写入成功", time.Now().Unix())
	}()

	time.Sleep(2 * time.Second)

	go func() {
		defer wg.Done()
		<-ch
		fmt.Println("读取成功", time.Now().Unix())
	}()

	wg.Wait()
}

```

## 2. 有缓冲的chan

​	下面演示一个生产者消费者模型。使用了waitgroup来阻塞主协程。

```go
// Package main -----------------------------
// @file      : producer-customer.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/30 16:27
// -------------------------------------------
package main

import (
	"fmt"
	"sync"
)

func main() {
	ch := make(chan int, 100)

	//customer
	wgCus := sync.WaitGroup{}
	wgCus.Add(1)
	go func() {
		defer wgCus.Done()
		for {
			val, ok := <-ch
			if !ok { //chan为空，且已经被关闭，ok为false
				break
			}
			fmt.Println("get val:", val)
		}
	}()

	//producer
	wgPro := sync.WaitGroup{}
	wgPro.Add(2)
	for i := 0; i < 2; i++ {
		go func() {
			defer wgPro.Done()
			for i := 0; i < 100; i++ {
				ch <- i
			}
		}()
	}
	wgPro.Wait()
	close(ch)
	wgCus.Wait()
}

```

​	下面是改成使用无缓冲的chan阻塞主协程。

```go
// Package main -----------------------------
// @file      : producer-customer.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/30 16:27
// -------------------------------------------
package main

import (
	"fmt"
	"sync"
)

func main() {
	ch := make(chan int, 100)

	//customer
	mc := make(chan struct{}, 0)
	go func() {
		defer func() { mc <- struct{}{} }()
		for {
			val, ok := <-ch
			if !ok { //chan为空，且已经被关闭，ok为false
				break
			}
			fmt.Println("get val:", val)
		}
	}()

	//producer
	wgPro := sync.WaitGroup{}
	wgPro.Add(2)
	for i := 0; i < 2; i++ {
		go func() {
			defer wgPro.Done()
			for i := 0; i < 100; i++ {
				ch <- i
			}
		}()
	}
	wgPro.Wait()
	close(ch)
	<-mc
}

```

