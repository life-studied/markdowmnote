---
create: 2024-01-31
---
# vsconfig文件

​	在UE创建时，会生成一个`.vsconfig`文件，用于保存这个UE工程需要用到的VS项目里的组件信息，即 `visual studio installer` 中出现的组件信息。

​	这个文件通常保存在项目的根目录下。在基于git的使用下（推荐包含该文件），如果新的设备不存在该文件中所需要的组件，打开VS后，会提示安装它们。

​	另外，在 `visual studio installer` 中可以手动在对应版本处import该文件，来检查对应的组件是否安装。