# vscode网页版服务器安装

## 新建目录|下载文件

我们先新建一个文件夹vscode并进入到vscode文件夹里来：

```
mkdir vscode
ls
cd vscode
```

​	本地下载文件，使用xftp传到服务器上

​	百度网盘：链接：https://pan.baidu.com/s/1QDcTRnGYvsw0MBPiNcJrOg  提取码：31ch 

## 安装code server

```
rpm -ivh code-server-4.1.0-amd64.rpm
```

## 前台启动

```
export PASSWORD="yaaJqH5034HLf9Wi" && code-server --host 0.0.0.0 --port 9999
```

## 后台启动

```
# 安装screen
yum install epel-release -y
yum install screen -y

# 启动后台服务
screen -S codeserver
export PASSWORD="yaaJqH5034HLf9Wi" && code-server --host 0.0.0.0 --port 9999
```

