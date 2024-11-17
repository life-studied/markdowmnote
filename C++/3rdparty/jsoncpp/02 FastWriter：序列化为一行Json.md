---
create: 2023-11-29
---
# FastWriter：序列化为一行Json

```C++
// 将数据序列化 -> 单行
// 适合进行数据的网络传输
std::string Json::FastWriter::write(const Value& root);
```

