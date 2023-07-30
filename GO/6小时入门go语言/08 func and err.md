# 08 func and err

​	下面演示了func和err的基本用法。

```go
// Package main -----------------------------
// @file      : funcAndErr.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/27 22:35
// -------------------------------------------
package main

import (
	"errors"
	"fmt"
)

func foo(a, b int) (int, error) {
	if b == 0 {
		return -1, errors.New("除数为0")
	}
	d := a / b
	return d, nil
}
func main() {
	m, n := 4, 0
	if p, err := foo(m, n); err != nil {
		fmt.Println("出错了", err)
	} else {
		fmt.Println("商为", p)
	}

}

```

