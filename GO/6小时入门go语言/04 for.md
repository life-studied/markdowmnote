---
create: 2023-07-27
---
# 04 for

​	下面的代码演示了for循环的基本用法

```go
// Package main -----------------------------
// @file      : for.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/27 19:21
// -------------------------------------------
package main

import "fmt"

func main() {
	for i := 0; i < 100; i++ {
		fmt.Println(i)
		if i == 10 {
			break
		}
		if i == 5 {
			continue
		}
	}
}

```

