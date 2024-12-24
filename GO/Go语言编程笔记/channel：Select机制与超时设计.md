---
create: '2024-12-23'
modified: '2024-12-23'
---

# channel：Select机制与超时设计

## 1. select

类似于unix上的select，用于处理异步io问题。

go语言中的select类似于switch结构，但是不会判断，而是选择符合条件的语句进行执行。

```go
select {
    case <- chan1:
	case <- chan2:
    case chan3 <- 1:
    default:
}
```

## 2. 随机产生0或1的小程序

```go
ch := make(chan int, 1) 
for { 
    select { 
        case ch <- 0: 
        case ch <- 1: 
    } 
    i := <-ch 
    fmt.Println("Value received:", i) 
} 
```

## 3. 超时设计的实现

channel并没设计超时机制，但是超时对于防止死锁的发生至关重要。

通过select，就能简单巧妙地实现一个带超时机制的channel：

```go
// 首先，我们实现并执行一个匿名的超时等待函数 
timeout := make(chan bool, 1) 
go func() { 
    time.Sleep(1e9) // 等待1秒钟 
    timeout <- true 
}() 
// 然后我们把timeout这个channel利用起来 
select { 
	case <-ch: 
    // 从ch中读取到数据 
	case <-timeout: 
    // 一直没有从ch中读取到数据，但从timeout中读取到了数据 
}
```