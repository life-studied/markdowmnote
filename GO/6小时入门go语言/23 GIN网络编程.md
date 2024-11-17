---
create: 2023-08-03
---
# 23 GIN网络编程

## import

```go
import(
    "github.com/gin-gonic/gin"
    "net/http"
)
```

## 1. 设置HTTP路由（GET/POST）

```go
// Package main -----------------------------
// @file      : ginTest.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/3 16:20
// -------------------------------------------
package main

import (
	"context"
	"github.com/gin-gonic/gin"
	"github.com/redis/go-redis/v9"
	"net/http"
	"strconv"
)

type Student struct {
	Name   string
	Age    int
	Height float32
}

func GetStudentInfo(studentId string) Student {
	client := redis.NewClient(&redis.Options{
		Addr:     "172.16.136.8:6379", //ip:port
		Password: "123456",
		DB:       0, //默认创建0-15个DB，此处使用0号DB
	})
	ctx := context.TODO()
	stu := Student{}
	for field, value := range client.HGetAll(ctx, studentId).Val() {
		if field == "Name" {
			stu.Name = value
		} else if field == "Age" {
			age, err := strconv.Atoi(value)
			if err != nil {
				stu.Age = age
			}
		} else if field == "Height" {
			height, err := strconv.ParseFloat(value, 10)
			if err != nil {
				stu.Height = float32(height)
			}
		}
	}
	return stu
}

func GetAge(ctx *gin.Context) {
	parm := ctx.PostForm("student_id") //从request中取出student_id字段对应的值
	if len(parm) == 0 {                //用len而不是==""
		ctx.String(http.StatusBadRequest, "please indidate the student id") //读取失败，返回状态码400和提示信息
		return
	}
	stu := GetStudentInfo(parm)
	ctx.String(http.StatusOK, strconv.Itoa(stu.Age)) //正常返回
	return
}

func GetName(ctx *gin.Context) {
	parm := ctx.Query("student_id") //从request中取出student_id字段对应的值
	if len(parm) == 0 {             //用len而不是==""
		ctx.String(http.StatusBadRequest, "please indidate the student id") //读取失败，返回状态码400和提示信息
		return
	}
	stu := GetStudentInfo(parm)
	ctx.String(http.StatusOK, stu.Name) //正常返回
	return
}

func main() {
	engine := gin.Default()          //创建engine
	engine.GET("/get_name", GetName) //设置路由
	engine.POST("./get_age", GetAge)
	err := engine.Run("0.0.0.0:2345")
	if err != nil {
		panic(err)
	}
}

```

## 2. 发起HTTP请求来测试服务器

```go
go test -v ./ -run=GetName -count=1
go test -v ./ -run=GetAge -count=1
```

* -v：允许输出fmt.Println
* -run：设置单元测试函数
* -count：不允许使用缓存，而是直接发送请求来测试

```go
// Package main -----------------------------
// @file      : ginTest_test.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/7 15:20
// -------------------------------------------
package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"testing"
)

func TestGetName(t *testing.T) {
	resp, err := http.Get("http://localhost:2345/get_name?student_id=学生1")
	if err != nil {
		fmt.Println(err)
		t.Fail()
	} else {
		defer resp.Body.Close()
		bytes, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			fmt.Println(err)
			t.Fail()
		} else {
			fmt.Println(string(bytes))
		}
	}

}

func TestGetAge(t *testing.T) {
	//post请求将参数放入url.Values中去传递
	//url.Values是一个map[string][]string
	resp, err := http.PostForm("http://localhost:2345/get_name", url.Values{"student_id": []string{"学生1"}})
	if err != nil {
		fmt.Println(err)
		t.Fail()
	} else {
		defer resp.Body.Close()
		bytes, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			fmt.Println(err)
			t.Fail()
		} else {
			fmt.Println(string(bytes))
		}
	}

}

```

## 3. 请求json，响应string

### 请求客户端

```go
// Package main -----------------------------
// @file      : gJson_test.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/8 10:43
// -------------------------------------------
package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"
	"testing"
)

func TestGetHeight(t *testing.T) {
	//构造请求内容
	client := http.Client{} //构造客户端
	reader := strings.NewReader(`{"stuent_id":"学生1"}`)
	request, err := http.NewRequest("POST", "http://localhost:2345/get_height", reader)
	if err != nil {
		fmt.Println(err)
		return
	}
	//添加请求头，http请求类型为json
	request.Header.Add("Content-Type", "application/json")

	//发送请求
	resp, err := client.Do(request)
	if err != nil {
		fmt.Println(err)
		t.Fail()
	} else {
		defer resp.Body.Close()
		bytes, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			fmt.Println(err)
			t.Fail()
		} else {
			fmt.Println(string(bytes))
		}
	}
}

```

### 响应服务端

```go
// Package main -----------------------------
// @file      : gJson.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/8 10:27
// -------------------------------------------
package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
)

type Request struct {
	StudentId string `json:"student_id"`
}

func GetHeight(ctx *gin.Context) {
	var param Request
	err := ctx.BindJSON(&param) //将请求解析为一个json，并将结果赋给结构体param
	if err != nil {
		ctx.String(http.StatusBadRequest, "please indidate student_id in json")
		return
	}
	stu := GetStudentInfo(param.StudentId)
	//将float64转换为str，保留1位小数，64位
	ctx.String(http.StatusOK, strconv.FormatFloat(float64(stu.Height), 'f', 1, 64))
	return
}

```

## 4. 请求json，响应json

### 响应json

```go
// Package main -----------------------------
// @file      : gJson.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/8 10:27
// -------------------------------------------
package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

type Request struct {
	StudentId string `json:"student_id"`
}

func GetHeight(ctx *gin.Context) {
	var param Request
	err := ctx.BindJSON(&param) //将请求解析为一个json，并将结果赋给结构体param
	if err != nil {
		ctx.String(http.StatusBadRequest, "please indidate student_id in json")
		return
	}
	stu := GetStudentInfo(param.StudentId)
	//返回json string
	ctx.JSON(http.StatusOK, stu)
	return
}

```

### 请求json

```go
// Package main -----------------------------
// @file      : gJson_test.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/8 10:43
// -------------------------------------------
package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"
	"testing"
)

func TestGetHeight(t *testing.T) {
	//构造请求内容
	client := http.Client{} //构造客户端
	reader := strings.NewReader(`{"stuent_id":"学生1"}`)
	request, err := http.NewRequest("POST", "http://localhost:2345/get_height", reader)
	if err != nil {
		fmt.Println(err)
		return
	}
	//添加请求头，http请求类型为json
	request.Header.Add("Content-Type", "application/json")

	//发送请求
	resp, err := client.Do(request)
	if err != nil {
		fmt.Println(err)
		t.Fail()
    } else {	//处理响应
		defer resp.Body.Close()
		bytes, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			fmt.Println(err)
			t.Fail()
		} else {
			var stu Student
			err := json.Unmarshal(bytes, &stu)	//将传过来的string反序列化为json
			if err != nil {
				fmt.Println(err)
				t.Fail()
			} else {
				fmt.Printf("%+v\n", stu)
			}
		}
	}
}

```

