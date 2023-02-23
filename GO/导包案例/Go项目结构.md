## Go项目结构

#### 总览

```go
/*
--Project path
  --bin     可执行文件
  --build   编译构建结果
  --cmd     代码，甚至可以只有一个main
    --具体包名
  --config  配置文件
  --docs    文档文件
  --example 样例应用程序
  --internal  不便在cmd做展示的、需要隐藏的、需要加密的代码
  --pkg     外部应用程序使用的代码库
  --script  脚本
  --test    测试代码
  --tools   编写的工具，可以从pkg、internal引入代码
  --vender  应用程序依赖项
  go.mod
*/
```

#### 项目搭建

首先，**mod文件应该放在你的项目的根目录**下，不要将他放到项目中的哪个文件夹下，对于整个项目而言，go.mod所在的位置就是项目根路径，对于同项目不同包的引用都是参照go.mod的位置和包声明的。

比如，go.mod文件内容如下：

```go
module example.com
go 1.18
```

那么当你在`/RootPath/cmd/myentry/main.go`中对`/RootPath/internal/decrypt/decrypt.go`中的函数`Decrypt()`进行调用时，你就需要在`main.go`中这么引用："`example.com/internal/decrypt`"，而使用时则是`decrypt.Decrypt()`这种包名+具体函数名的方式来调用。

另外，go.mod和`GOPATH`是冲突的，当你在根路径下创建了go.mod后，如果`GOPATH`也指向根目录，就会出现一些错误。
这也是有些人会将go.mod放在`.../src/`下的原因。

事实上，`GOPATH`的作用现在已经基本只剩下构建项目后保存的路径这么个作用了（当然，如果你删除go.mod并且将go的环境变量`GO111MODULE`设置为off的话，你依然可以使用`GOPATH`作为主要的项目路径。
对于使用`go.mod`又希望编译结果放在根目录下`/bin`路径下的使用者来说，可以通过设置go的环境变量`GOBIN`来实现这一目的，不过这样设置对于不同项目来说都会指向同一个路径。
我的建议是将`GOPATH`指向项目根路径下的`/build`路径。