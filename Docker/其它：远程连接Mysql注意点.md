## 远程连接Mysql注意点

背景：

* 两台Windows物理机在同一个局域网下
* 其中一台Windows上搭载了Linux虚拟机，并进行了端口映射：`16001:16001`
* Linux虚拟机上使用docker，运行了mysql容器，端口映射：`16001:3306`

#### 1.Mysql修改配置，使允许远程连接

```mysql
use mysql;		#使用mysql数据库
grant all privileges  on *.* to root@'%' identified by "password";#允许所有远程连接，并且密码为password
flush privileges;#更新配置
```

#### 2.修改Linux防火墙对应端口



#### 3.修改Windows防火墙对应端口

在Windows下，您可以使用“Windows防火墙”来关闭MySQL端口的防火墙。以下是具体步骤：

1. 打开“Windows防火墙”：按下“Windows键+R”，输入“wf.msc”并回车。

2. 在窗口左侧选择“高级设置”。

3. 在左侧列表中选择“Inbound Rules（入站规则）”。

4. 在右侧列表中找到“MySQL”或者“3306”相关条目，右键点击并选择“Disable Rule（禁用规则）”。

5. 确认更改。

#### 4.连接服务器

```shell
mysql -h 192.168.122.23 -P 16001 -u root -p
```

