## MySQL连接的注意事项

#### 1.启动服务

> 在win+r中输入services.msc查看MySQL是否启动

#### 2.连接mysql

> 使用管理员打开cmd
>
> 输入mysql -uroot -p -P3306
>
> 输入密码root

#### 3.创建数据库

查看现有数据库：show databases;

创建数据库：create database 数据库名;

---

**注：**

主机名：localhost

用户名：root

密码：root

端口号：3306

#### 4.设置vc++

> 1.C/C++——常规——附加包含目录
>
> 添加：$(SolutionDir)include;
>
> 2.链接器——常规——附加库目录
>
> 添加：$(SolutionDir)lib;
>
> 3.输入——附加依赖项
>
> 添加：libmysql.lib;
>
> 4.在.vs同目录下粘贴MySQL的include和lib文件夹
>
> 5.在main函数目录下粘贴libmysql.dll文件（在lib文件夹中）
>
> 6.main.cpp包含头文件”mysql.h"



