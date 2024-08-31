# 包package

在 Protobuf 中，可以使用package关键字来定义一个消息所属的包（package）。

包是用于组织和命名消息类型的一种机制，类似于C++**命名空间**的概念。

---

## 1. quick start

### address.proto

在一个.proto文件中，可以通过在顶层使用`package`关键字来定义包：

```protobuf
syntax = "proto3";
// 添加命名空间 location
package location;

// 地址信息, 这个Address类属于命名空间: location
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
// 指定命名空间 info
package info;

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
    // 枚举类型
    Color color = 5;
    // 命名空间的名字.类名 变量名=编号;
    location.Address addr = 6;
}
```

## C++使用

```C++
#include "Person.pb.h"

//using namespace location;
//using namespace info;

void test()
{
    // 序列化
    info::Person p;
    p.set_id(10);
    p.set_age(32);
    p.set_sex("man");

    p.add_name();
    p.set_name(0,"路飞");
    p.add_name("艾斯");
    p.add_name("萨博");
    p.mutable_addr()->set_addr("北京市长安区天安门");
    p.mutable_addr()->set_num(1001);
    p.set_color(info::Color::Blue);

    // 序列化对象 p, 最终得到一个字符串
    std::string output;
    p.SerializeToString(&output);

    // 反序列化数据
    info::Person pp;
    pp.ParseFromString(output);
    std::cout << pp.id()  << ", " << pp.sex() << ", " << pp.age() << std::endl;
    std::cout << pp.addr().addr() << ", " << pp.addr().num() << std::endl;
    int size = pp.name_size();
    for(int i=0; i<size; ++i)
    {
        std::cout << pp.name(i) << std::endl;
    }
    std::cout << pp.color() << std::endl;
}
```

