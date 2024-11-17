---
create: 2024-08-31
---
# defaults-file的使用

如果我们不想让 MySQL 到默认的路径下搜索配置文件（就是上表中列出的那些），可以在命令行指定 defaults-file 选项：

```shell
mysqld --defaults-file=/tmp/myconfig.txt 
```

这样，在程序启动的时候将只在 /tmp/myconfig.txt 路径下搜索配置文件。如果文件不存在或无法访问，则会发生错误。 

> 注意`defaults-extra-file`和`defaults-file`的区别，使用`defaults-extra-file`可以指定额外的配置文件搜索路径（也就是说那些固定的配置文件路径也会被搜索）。