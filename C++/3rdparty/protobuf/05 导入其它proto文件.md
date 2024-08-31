# 导入其它proto文件

## 1. quick start

### address.proto

```protobuf
syntax = "proto3";
// 地址信息
message Address
{
    bytes addr = 1;
    bytes number = 2;
}
```

### Person.proto

```protobuf
syntax = "proto3";

import "address.proto";

enum Color
{
    Red = 0;
    Green = 3;
    Yellow = 6;
    Blue = 9;
}

message Person
{
    int32 id = 1;
    repeated bytes name = 2;
    bytes sex = 3;	
    int32 age = 4;
    Color color = 5;
    // 添加地址信息, 使用的是外部proto文件中定义的数据类型
    Address addr = 6;
}
```

## 2. import

* import语句中指定的文件路径可以是相对路径或绝对路径。如果文件在相同的目录中，只需指定文件名即可。
* 导入的文件将会在编译时与当前文件一起被编译。

## 参考资料

* [Protobuf | 爱编程的大丙 (subingwen.cn)](https://subingwen.cn/cpp/protobuf/#2-4-proto文件的导入)