---
create: 2023-07-27
---
# 06 字符串（byte和rune）

## 字符串本质

​	字符串本质是不可修改的`byte`数组。`utf8`编码的大部分字符在`byte`数组里占位大于1，因此可以使用`rune`数组来统计其实际字符数。（强转为rune数组）

​	下面的代码演示了：

1. byte数组的len
2. rune数组的len
3. 遍历数组

```go
// Package main -----------------------------
// @file      : string.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/27 20:28
// -------------------------------------------
package main

import "fmt"

func main() {
	s := "golang" //字符串的本质是【不可修改的byte数组】
	fmt.Println(s)

	anotherString := "golang你好"
	fmt.Println("s ([]byte)长度\t", len(s))             //6
	fmt.Println("as([]byte)长度\t", len(anotherString)) //12，汉字占用3byte

	arr := []rune(anotherString)
	fmt.Println("arr([]rune)长度\t", len(arr))

	for _, ele := range arr {
		fmt.Printf("%d\t%c\n", ele, ele)
	}
}

/*
output:
golang
s ([]byte)长度   6
as([]byte)长度   12
arr([]rune)长度  8
103     g
111     o
108     l
97      a
110     n
103     g
/*
```

## 多行字符串

​	使用反引号可以制作多行字符串。

```go
s:=`hello
你好
golang
`
```

## 字符串的拼接

​	字符串可以直接使用+进行拼接。

```go
s := "hello"
a := "golang"
b := s + a
fmt.Println(b)
```

