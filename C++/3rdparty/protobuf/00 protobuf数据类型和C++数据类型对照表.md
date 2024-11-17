---
create: 2024-08-29
---
# protobuf数据类型和C++数据类型对照表

| Protobuf 类型 | C++ 类型            | 备注                                                         |
| ------------- | ------------------- | ------------------------------------------------------------ |
| double        | double              | 64位浮点数                                                   |
| float         | float               | 32位浮点数                                                   |
| int32         | int                 | 32位整数                                                     |
| int64         | long                | 64位整数                                                     |
| uint32        | unsigned int        | 32位无符号整数                                               |
| uint64        | unsigned long       | 64位无符号整数                                               |
| sint32        | signed int          | 32位整数，处理负数效率比int32更高                            |
| sint64        | signed long         | 64位整数，处理负数效率比int64更高                            |
| fixed32       | unsigned int(32位)  | 总是4个字节。如果数值总是比\(2^{28}\)大的话，这个类型会比uint32高效。 |
| fixed64       | unsigned long(64位) | 总是8个字节。如果数值总是比\(2^{56}\)大的话，这个类型会比uint64高效。 |
| sfixed32      | int (32位)          | 总是4个字节                                                  |
| sfixed64      | long (64位)         | 总是8个字节                                                  |
| bool          | bool                | 布尔类型                                                     |
| string        | string              | 一个字符串必须是UTF-8编码或者7-bit ASCII编码的文本           |
| bytes         | string              | 处理多字节的语言字符、如中文，建议protobuf中字符型类型使用bytes |
| enum          | enum                | 枚举                                                         |
| message       | object of class     | 自定义的消息类型                                             |

## 参考资料

* [Protobuf | 爱编程的大丙 (subingwen.cn)](https://subingwen.cn/cpp/protobuf/#1-3-如何使用)