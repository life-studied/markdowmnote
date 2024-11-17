---
create: 2023-07-30
---
# 20 gorm操作数据库

[TOC]

​	关键字：gorm;

---

* import:

```go
import(
    "gorm.io/gorm"
    "gorm.io/driver/mysql"
) 
```

* shell:

```shell
go mod tidy
```

## 1. 结构体与数据库表对应

* 结构体名与表名对应，其中，表名是结构体名的蛇形复数形式。例如：结构体名为`UserInfo`，对应的表名为`user_infos`。

  * 可以使用下面的方法显式指明

  ```go
  func (User) TableName() string{
      return "user"
  }
  ```

* 结构体中的成员与字段对应，其中字段是成员的蛇形形式，例如：成员名为`Keyword`，字段名为`keyword`。

  * 可以使用下面的方法显式指明（对应的数据库字段和主键字段）

  ```go
  type User struct {
      Id int		`gorm:"column:aid,primaryKey"`
      Keyword string	`gorm:"column:keywords"`
      City string 	//city
  }
  ```

## 2. 数据库配置信息

​	格式为：用户名:密码@连接方式(ip:端口)/数据库名?可选参数

可选参数：

* charset=utf8：使用utf8字符集
* parseTime=true：允许将数据库时间格式和go时间格式统一

```go
dataSourceName:="tester:123456@tcp(127.0.0.1:3306)/test?charset=utf8&parseTime=true"
```

## 3. 案例

### 综合案例（Select操作）

​	下面展示了一个查询的案例，主要代码在`main`和`read`函数中。

```go
// Package main -----------------------------
// @file      : gorm.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/7/30 22:09
// -------------------------------------------
package main

import (
	"fmt"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"os"
)

type User struct {
	Id      int
	Keyword string `gorm:column:keywords`
	City    string
}

func (User) TableName() string {
	return "user"
}

func read(client *gorm.DB, city string) *User {
	var users []User
    // Where:设置筛选条件
    // Find:执行查询，将结果集存储到users中
	client.Where("city=?", city).Find(&users)
	if len(users) > 0 {
		return &users[0]
	} else {
		return nil
	}
}

func main() {
	dataSourceName := "tester:123456@tcp(127.0.0.1:3306)/test?charset=utf8&parseTime=true"
	client, err := gorm.Open(mysql.Open(dataSourceName), nil)
	checkErr(err)
	user := read(client, "北京")
	if user != nil {
		fmt.Printf("%+v\n", *user)
	} else {
		fmt.Println("无结果")
	}
}

func checkErr(err error) {
	if err != nil {
		fmt.Println("出错", err)
		os.Exit(2)
	}
}

```

### Select（一）

​	`read`函数也可以改成这样：

```go
func read(client *gorm.DB, city string) *User {
	var user User
    // Select:设置查询结果字段
    // Where:设置筛选条件
    // Limit:限制数量
    // Take:执行查询，从查询结果中随机取一个
    err:=client.Select("id,city").Where("city=?", city).Limit(1).Take(&user).Error
    checkErr(err)
    return user
}
```

### Select（二）

​	`read`函数还可以在结构体中赋值，来隐含`Where`条件：

```go
func read(client *gorm.DB, city string) *User {
	var user User
    user.Id = 853885
    // Select:设置查询结果字段
    // First:执行查询，取结果集的第一个
    err:=client.Select("id,city").First(&user).Error
    checkErr(err)
    return user
}
```

### 案例（Create操作）

​	将信息提前写入结构体中，直接写入数据库。

```go
func write(client *gorm.DB, user *User) err {
    //client.BenchCreate(users)	//插入切片
    return client.Create(user)
}
```

### 案例（Update操作）

```go
func update(client *gorm.DB) err {
    // Model:指定Update的表名
    // Where:指定Update的筛选条件
    // Update:指定Update的字段
    client.Model(User{}).Where("id=?", 5858).Update("city","郑州")
    
    // Update含有多个字段，使用Updates，传入的是map
    client.Model(User{}).Where("id=?", 5858).Updates(
        map[string]interface{}{"city":"郑州","gender":"男"}
    )
}
```

### 案例（Delete操作）

```go
client.Where("id=?", 5858).Delete(User{})
```

