---
create: 2023-07-27
---
# 09 defer

​	defer的注意点：

* 如果后面跟的是语句，则语句中的变量在defer注册时就完成传入。
* 如果后面跟的是闭包，则闭包中的变量在defer执行时才传入。

---

​	下面演示了defer的使用和上面的特性：

```go
// Package main -----------------------------
// @file      : defer.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/27 22:45
// -------------------------------------------
package main

import "fmt"

func foo() int {
	a, b := 3, 5
	c := a + b
	defer fmt.Println(c)
	fmt.Println(c)
	defer fmt.Println(c)
	defer func() {
		fmt.Println(c)
	}()
	c = 100
	return c
}
func main() {
	foo()
}

/*
output:
8
100
8
8
*/
```

