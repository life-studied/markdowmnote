---
create: 2023-07-27
---
# 11 时间相关（time）

​	下面的代码介绍了：

* 时间格式的指定字符串（`const`）
* 如何获取当前时间
* 如何获取时间的差值
* 如何在当前时间的基础上加上一个时间
* 如何将一个Time类型转换为指定格式的字符串
* 如何将一个时间字符串解析为一个Time类型

```go
// Package main -----------------------------
// @file      : time.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/27 23:34
// -------------------------------------------
package main

import (
	"fmt"
	"time"
)

const (
	DATE = "2006-01-02"          //YYYY-mm-dd
	TIME = "2006-01-02 15:04:05" //YYYY-mm-dd HH:MM:ss
)

func main() {
	t0 := time.Now()       //time.Time
	fmt.Println(t0.Unix()) //时间戳
	time.Sleep(50 * time.Millisecond)

	t1 := time.Now()

	// Time - Time = Duration
	diff := t1.Sub(t0)
	fmt.Println(diff.Milliseconds()) //<==>fmt.Println(time.Since(t0).Milliseconds())

	// Time + Duration = Time
	d := time.Duration(2 * time.Second)
	t2 := t0.Add(d)
	fmt.Println(t2.Unix())

	// Time格式化->字符串
	fmt.Println(t0.Format(DATE))
	fmt.Println(t0.Format(TIME))

	// 时间字符串->Time
	s := t0.Format(TIME)         //loc,_:=time.LoadLocation("Asia/Shanghai")	定义时区后，使用ParseInLocation更保险
	t3, _ := time.Parse(TIME, s) //t3,_:=time.ParseInLocation(TIME,s,loc)
	fmt.Println(t3.Unix())

}

```

