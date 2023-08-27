# 22 go context上下文

​	在 Goroutine 构成的树形结构中对信号进行同步以减少计算资源的浪费是`context.Context` 的最大作用。Go 服务的每一个请求都是通过单独的 Goroutine 处理的[2](https://draveness.me/golang/docs/part3-runtime/ch06-concurrency/golang-context/#fn:2)，HTTP/RPC 请求的处理器会启动新的 Goroutine 访问数据库和其他服务。

​	如下图所示，我们可能会创建多个 Goroutine 来处理一次请求，而`context.Context` 的作用是在不同 `Goroutine` 之间同步请求特定数据、取消信号以及处理请求的截止日期。

![golang-context-usage](https://img.draveness.me/golang-context-usage.png)

## 场景一 延时终止

​	设定一个时间，到时则所有操作终止。

```go
func worker1() {
    deep := 10
    ctx, cancel := context.WithTimeout(context.Background(), 1*time.Second)	//设定终止时间
    
    defer cancel()
    
    go handle(ctx, 500*time.Millisecond, deep)
    select {
        case <-ctx.Done():
        fmt.Println("main",ctx.Err())
    }
}

func handle(ctx context.Context, duration time.Duration, deep int) {
    if deep > 0 {
        time.Sleep(200*time.Millisecond)
        go handle(ctx, duration, deep-1)
    }
    
    if ctx.value("token") != nil {
        fmt.Printf("token is %s\n", ctx.value("token"))
    }
    
    select {
        case <-ctx.Done():
        	fmt.Println("handle", ctx.Err())
        case <-time.After(duration):
        	fmt.Printf("process request with %v, %d\n",
                  duration, deep)
    }
}
```

## 场景二 操作终止

​	设置一个信号，信号出现则所有操作终止。

```go
func worker2() {
    deep := 10
    ctx, cancel := context.WithCancel(context.Background())	//设定终止信号
    
    go handle(ctx, 500*time.Millisecond, deep)
    time.Sleep(2*time.Second)	//模拟一些操作
    cancel()	//发射终止信号
}

func handle(ctx context.Context, duration time.Duration, deep int) {
    if deep > 0 {
        time.Sleep(200*time.Millisecond)
        go handle(ctx, duration, deep-1)
    }
    
    if ctx.value("token") != nil {
        fmt.Printf("token is %s\n", ctx.value("token"))
    }
    
    select {
        case <-ctx.Done():
        	fmt.Println("handle", ctx.Err())
        case <-time.After(duration):
        	fmt.Printf("process request with %v, %d\n",
                  duration, deep)
    }
}
```

## 场景三 跨协程异步共享数据

​	通过context传递一个数据给所有的子协程。

```go
func worker3() {
    deep := 10
    ctx:= context.WithValue(context.Background(), "token", "val")	//传递键值对
    
    ctx,cancel:=context.WithTimeout(ctx, 2*time.Second)
    defer cancel()
    go handle(ctx, 500*time.Millisecond, deep)
    select {
        case <-ctx.Done():
        	fmt.Println("main", ctx.Err())
    }
}

func handle(ctx context.Context, duration time.Duration, deep int) {
    if deep > 0 {
        time.Sleep(200*time.Millisecond)
        go handle(ctx, duration, deep-1)
    }
    
    if ctx.value("token") != nil {
        fmt.Printf("token is %s\n", ctx.value("token"))
    }
    
    select {
        case <-ctx.Done():
        	fmt.Println("handle", ctx.Err())
        case <-time.After(duration):
        	fmt.Printf("process request with %v, %d\n",
                  duration, deep)
    }
}
```

