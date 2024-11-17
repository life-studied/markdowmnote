---
create: 2023-07-29
---
# 13 json序列化

## 1. encoding/json包

​	下面演示了标准库中的序列化函数`json.Marshal`和反序列化函数`json.Unmarshal`。

```go
// Package main -----------------------------
// @file      : json.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/29 23:02
// -------------------------------------------
package main

import (
	"encoding/json"
	"fmt"
)

type Student struct {
	Name   string
	Age    int
	Gender bool
}

type Class struct {
	Id       string
	Students []Student
}

func main() {
	s := Student{"小明", 20, true}
	c := Class{"1班", []Student{s, s, s}}
	bytes, err := json.Marshal(c)
	if err != nil {
		fmt.Println("序列化错误", err)
		return
	}

	fmt.Println(string(bytes))

	var c2 Class
	err = json.Unmarshal(bytes, &c2)
	if err != nil {
		fmt.Println("反序列化失败", err)
	}
	fmt.Printf("%+v", c2)
}

```

## 2. github.com/bytedance/sonic

​	字节跳动的`json`序列化库。比标准库更快。

```go
// Package main -----------------------------
// @file      : json.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/29 23:02
// -------------------------------------------
package main

import (
	"fmt"
	"github.com/bytedance/sonic"
)

type Student struct {
	Name   string
	Age    int
	Gender bool
}

type Class struct {
	Id       string
	Students []Student
}

func main() {
	s := Student{"小明", 20, true}
	c := Class{"1班", []Student{s, s, s}}
	bytes, err := sonic.Marshal(c)
	if err != nil {
		fmt.Println("序列化错误", err)
		return
	}

	fmt.Println(string(bytes))

	var c2 Class
	err = sonic.Unmarshal(bytes, &c2)
	if err != nil {
		fmt.Println("反序列化失败", err)
	}
	fmt.Printf("%+v", c2)
}

```

