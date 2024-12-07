---
create: '2024-11-24'
modified: '2024-11-24'
---

# chmod

change mode，修改权限

## quick start | chmod用法

```shell
chmod u+x u+w u-r a.sh	# 去除r 增加wx

chmod 755 a.sh			# rwx r-x r-x

chmod u+x g+x o-x		# 给user和group添加x other去除x
```

## 查看权限

```shell
ll
#total 44
#drwxr-xr-x  2 yunyin yunyin  4096 Nov 24 21:34 ./
#drwxr-x--- 12 yunyin yunyin  4096 Nov 24 21:34 ../
#-rw-r--r--  1 yunyin yunyin 30674 Nov 24 21:24 all_image_file.txt
#-rw-r--r--  1 yunyin yunyin   476 Nov 24 21:24 docker_png.txt
```

## 角色 | 从左至右

* user
* group
* other

## 权限 | 从左至右

* read
* write
* execute