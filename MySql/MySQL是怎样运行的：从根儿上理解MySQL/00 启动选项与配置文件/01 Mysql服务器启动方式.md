---
create: 2024-08-31
---
# Mysql服务器启动方式

## 类Unix

| 启动方式     | 命令                                          | 说明                                                         |
| ------------ | --------------------------------------------- | ------------------------------------------------------------ |
| mysqld       | `mysqld`                                      | 直接启动一个mysql进程，基础但不常用                          |
| mysqld_safe  | `mysqld_safe`                                 | 启动脚本，间接调用mysqld<br />顺便开启一个监控进程，在mysqld挂掉的时候自动重启 |
| mysql.server | `mysql.server start`<br />`mysql.server stop` | 启动脚本，间接调用mysqld_safe<br />这个 `mysql.server` 文件其实是一个链接文件，它的实际文件是 `../support-files/mysql.server` |
| mysqld_multi | `mysqld_multi`                                | 启动多个服务器进程，比较复杂                                 |

## Windows

### 直接双击

在安装目录的bin下有mysqld可执行文件，直接双击即可。

### 以服务的方式

#### 注册服务

可以将mysql注册为**windows服务**（需要长时间运行，并且在计算机启动的时候自动启动的程序）。

```shell
"C:\Program Files\MySQL\MySQL Server 5.7\bin\mysqld" --install
```

#### 启停服务

通过**服务管理器**界面或者下面的命令：

```shell
net start MySQL
net stop MySQL
```

