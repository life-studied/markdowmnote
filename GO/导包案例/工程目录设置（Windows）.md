## 工程目录设置（Windows）

#### 1.设置临时GOPATH

##### 1.1(cmd)

```shell
echo %GOPATH%	#查看GOPATH
set GOPATH=%GOPATH%;%CD%		#将当前目录加入到工作目录中
```

##### 1.2(powershell)

```shell
$env:GOPATH		#查看GOPATH
$env:GOPATH+=";$pwd"		#将当前目录加入到工作目录中
```



#### 2.目录结构

![](picture/工作目录.png)

```shell
go build [-o 输出路径+文件名] file1 file2 file3 ...

go build					#当前目录生成默认exe
go build -o bin main		#将main包输出在bin目录下（默认exe名为第一个文件main.exe）
go build -o bin/HelloWorld.exe main	#将main包输出在bin目录下（exe名HelloWorld.exe）
```

