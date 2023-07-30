# 07 map

## 基本使用

​	下面的代码演示了：

1. 如何创建map
2. 如何添加key和val
3. 如何删除key
4. 如何防止误判key是否存在**（即使`key`不存在，打印`m[key]`依旧会返回`value`类型的默认值）**
5. 遍历map

```go
// Package main -----------------------------
// @file      : map.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/27 20:51
// -------------------------------------------
package main

import "fmt"

func main() {
	var m map[string]int
	//m=make(map[string]int,100)
	m = map[string]int{"a": 3, "b": 4}
	m["c"] = 9
	m["a"] = 4
	fmt.Println("m[\"a\"] is ", m["a"])

	delete(m, "a")
	if v, exists := m["a"]; exists {
		fmt.Println(v)
	} else {
		fmt.Println("m[\"a\"] is not exists")
	}

	for k, v := range m {
		fmt.Println(k, v)
	}
}

```

## map作为set使用

​	在go中不存在set的数据结构，但是可以将map用作set，方法是将值设置为strut{}类型，即空结构体类型。

```go
m := make(map[string]struct{}, 100)
//m:=map[string]struct{}{"a": {}, "b": {}}

if _, exists := m["a"]; exists {
    fmt.Println("key:a存在")
}
```

