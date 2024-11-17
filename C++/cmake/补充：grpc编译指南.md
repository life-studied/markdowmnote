---
create: 2024-05-10
---
# grpc编译指南

## 1. 下载版本

* [Release Release v1.63.0 · grpc/grpc (github.com)](https://github.com/grpc/grpc/releases/tag/v1.63.0)

## 2. 编译（VS2022）

​	创建visualpro文件夹，在该文件夹中输入以下两个命令：

```shell
cmake .. -DCMAKE_BUILD_TYPE=Debug -DCMAKE_CXX_STANDARD=17 -Dprotobuf_BUILD_TESTS=OFF -DCMAKE_FIND_PACKAGE_PREFER_CONFIG=ON

cmake --build . --config Debug -- /p:CL_MP=true /p:CL_MPCount=20
```

## 3. 附件（属性配置表）：PropertySheet.props