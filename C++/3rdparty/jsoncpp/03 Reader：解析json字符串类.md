---
create: 2023-11-29
---
# Reader：解析json字符串类

## 1. 从字符串中解析

### json字符串

```C++
bool Json::Reader::parse(const std::string& document,
    Value& root, bool collectComments = true);
/*	参数:
        - document: json格式字符串
        - root: 传出参数, 存储了json字符串中解析出的数据
        - collectComments: 是否保存json字符串中的注释信息
        */
```

### 部分json字符串

```C++
// 通过begindoc和enddoc指针定位一个json字符串
// 这个字符串可以是完成的json字符串, 也可以是部分json字符串
bool Json::Reader::parse(const char* beginDoc, const char* endDoc,
    Value& root, bool collectComments = true);
```

## 2. 从文件流解析

```C++
// write的文件流  -> ofstream
// read的文件流   -> ifstream
// 假设要解析的json数据在磁盘文件中
// is流对象指向一个磁盘文件, 读操作
bool Json::Reader::parse(std::istream& is, Value& root, bool collectComments = true);
```

