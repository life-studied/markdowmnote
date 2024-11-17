---
create: 2024-08-29
---
# 数组repeated

在使用Protobuf中，可以使用`repeated`关键字作为限定修饰符来表示一个字段可以有多个值，即重复出现的字段。`repeated`关键字可以用于以下数据类型：`基本数据类型`、`枚举类型`和`自定义消息类型`。

## 1. quick start

```protobuf
syntax = "proto3";

message Person
{
    int32 id = 1;
    repeated bytes name = 2;	// 对应C++中的std::vector<std::string> name;
    bytes sex = 3;	
    int32 age = 4;
}
```

## 2. C++使用

```C++
#include "Person.pb.h"
void test()
{
    Person p;
    p.set_id(10);
    p.set_age(18);
    p.set_sex("man");

    // 添加一个name的空间，并根据idx初始化
    p.add_name();
    p.set_name(0,"路飞");
    
    // 直接添加一个name
    p.add_name("艾斯");
    p.add_name("萨博");
}
```

## 参考资料

* [Protobuf | 爱编程的大丙 (subingwen.cn)](https://subingwen.cn/cpp/protobuf/#2-2-repeated-限定修饰符)