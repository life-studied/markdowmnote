---
create: 2024-08-29
---
# 枚举enum

## 1. quick start

```protobuf
syntax = "proto3";

// 定义枚举类型
enum Color
{
    Red = 0;		// 必须以0开始
    Green = 3;		// 第一个元素以外的元素值可以随意指定
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
}
```

## 2. C++中的使用

```C++
#include "Person.pb.h"
void test()
{
    Person p;
    p.set_id(10);
    p.set_age(32);
    p.set_sex("man");

    p.add_name();
    p.set_name(0,"路飞");
    p.add_name("艾斯");
    p.add_name("萨博");
    
    // 设置枚举
    p.set_color(::Color::Blue);
}
```

## 参考资料

* [Protobuf | 爱编程的大丙 (subingwen.cn)](https://subingwen.cn/cpp/protobuf/#2-3-枚举)