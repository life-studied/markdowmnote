---
create: 2024-01-24
---
# 保存与复制虚拟环境（conda）

## 导出环境

​	默认导出的环境存在`C:\Users\**\`下

```cmd
activate myenv

conda env export > environment.yaml
```

## 复制虚拟环境

​	将environment.yaml复制到另一台电脑，使用：

```cmd
conda env create -f environment.yaml
```

