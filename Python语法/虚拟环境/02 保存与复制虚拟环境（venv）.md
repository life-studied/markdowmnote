---
create: 2024-01-24
---
# 02 保存与复制虚拟环境

## 导出虚拟环境

​	直接查看虚拟环境的所有第三方包：`pip list`

​	如果想要在另一台电脑上复制同一份虚拟环境，可以使用pip（在虚拟环境中）：

```cmd
pip freeze > requirements.txt
```

## 安装虚拟环境

​	将`requirements.txt`发送到另一台电脑，在新的虚拟环境（提前创建好）中使用：

```cmd
pip install -r requirements.txt
```

