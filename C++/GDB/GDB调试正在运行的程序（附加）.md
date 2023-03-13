## GDB调试正在运行的程序（附加）

#### 1.查看进程pid

```shell
ps -ef | grep test		#process status
```

#### 2.将gdb附加到进程上

```shell
gdb test -p 进程编号
```

#### 3.调试(使用调试命令)

```shell
bt
```

