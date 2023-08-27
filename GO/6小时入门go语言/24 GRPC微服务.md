# 24 GRPC微服务

​	IDL（Interface description Language）文件：通过一种中立的方式来描述接口，使得在不同平台上的不同的对象和不同语言编写的程序可以互相通信。

​	下面使用proto作为IDL文件。

## 1.proto文件规则

```protobuf
syntax="proto3";		//采用protobuffer V3版本的语法编写

package idl;	//等同于go语言的package功能

option go_package = "./idl/my_proto;student_service";	//分号前是生成的go文件所在路径，其中.是--go_out指定的路径
//分号后是生成的go文件的package名称，可省略
import "google/protobuf/timestamp.proto";
message Student {	//等同与go语言的struct
  string name = 1;	//变量名的驼峰形式就是go里的成员变量名，后面的数字用来作pb序列化，每个成员对应的数字需要唯一
  repeated string Location = 4;	//repeated表示list
  map<string,float> Scores = 3;	//map
  bool Gender = 5;
  int32 Age = 6;		//int32或int64
  float Height = 7;	//转成go是float32
  google.protobuf.Timestamp Birthday = 8;		//需要go get google.golang.org/protobuf
}

message Request {
  string studentId = 1;
}

service StudentService {	//等同于go语言的接口，可以定义多个函数
  rpc GetStudentInfo (Request) returns (Student);	//指定函数名，入参类型，出参类型
}
```

​	通过以下命令（之一）把proto文件转为go文件：

```shell
protoc --go_out=plugins=grpc:. --proto_path=./idl -I=./idl/third_proto student_service.proto	#go_out
protoc --gogofaster_out=plugins=grpc:. --proto_path=./idl -I=./idl/third_proto student_service.proto #gogofaster_out
```

* `go_out`可以换成中的go可以换成gogofaster等其它指令
* `grpc:.`中的`.`表示基准地址，与文件中的`go_package`路径共同生成go文件的最终地址。
* `proto_path`用于指定原始的输入文件在哪个目录下
* `-I`用于指定引用的`proto`文件所在路径，再从文件中的`import`路径来共同引用到那个`proto`文件。

## 2.实现proto生成的接口并注册服务

​	在proto文件中定义了GetStudentInfo接口，你需要在自己的go文件中实现它。

* 定义一个结构体（可以是空的，只要能实现接口即可）
* 利用这个结构体去实现接口
  * 入参增加一个context
  * 出参增加一个error
* Register这个接口（通过结构体类型来找到其成员函数（地址））

```go
// Package mainn -----------------------------
// @file      : grpc.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/8 17:05
// -------------------------------------------
package main

import (
	"context"
	"errors"
	"fmt"
	"github.com/redis/go-redis/v9"
	"google.golang.org/grpc"
	student_service "grpc/idl/my_proto"
	"net"
	"strconv"
)

func GetStudentInfo(studentId string) student_service.Student {
	client := redis.NewClient(&redis.Options{
		Addr:     "172.16.136.8:6379", //ip:port
		Password: "123456",
		DB:       0, //默认创建0-15个DB，此处使用0号DB
	})
	ctx := context.TODO()
	stu := student_service.Student{}
	for field, value := range client.HGetAll(ctx, studentId).Val() {
		if field == "Name" {
			stu.Name = value
		} else if field == "Age" {
			age, err := strconv.Atoi(value)
			if err != nil {
				stu.Age = int32(age)
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

type StudentServer struct {
}

func (s *StudentServer) GetStudentInfo(ctx context.Context, request *student_service.Request) (*student_service.Student, error) { // 实现接口，入参增加一个context，出参增加一个error
	defer func() {
		if err := recover(); err != nil {
			fmt.Printf("接口出错:%v\n", err)
		}
	}()
	studentId := request.StudentId
	if len(studentId) == 0 {
		return nil, errors.New("student id is empty")
	}
	student := GetStudentInfo(studentId)
	return &student, nil
}

func main() {
	list, err := net.Listen("tcp", ":2346") //创建监听属性
	if err != nil {
		panic(err)
	}
	server := grpc.NewServer()                                               //创建服务端
	student_service.RegisterStudentServiceServer(server, new(StudentServer)) //调用proto生成的register注册服务
	err = server.Serve(list)                                                 //服务启动
	if err != nil {
		panic(err)
	}

}

```

## 3.客户端测试rpc

​	使用grpc的Dial去连接服务器。

```go
// Package main -----------------------------
// @file      : grpc_test.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/8 20:27
// -------------------------------------------
package main

import (
	"context"
	"fmt"
	"google.golang.org/grpc"
	student_service "grpc/idl/my_proto"
	"testing"
)

func TestService(t *testing.T) {
	conn, err := grpc.Dial("127.0.0.1", grpc.WithInsecure())
	if err != nil {
		fmt.Printf("连接grpc服务器失败:%+v", err)
		t.Fail()
	}
	defer conn.Close()
	client := student_service.NewStudentServiceClient(conn)	//调用proto生成的NewClient函数创建client
	resp, err := client.GetStudentInfo(context.TODO(), &student_service.Request{StudentId: "学生1"})	//rpc
	if err != nil {
		fmt.Printf("调用grpc服务器失败:%+v", err)
		t.Fail()
	}
	fmt.Printf("Name %s Age %d Height %.1f\n", resp.Name, resp.Age, resp.Height)
}

```

