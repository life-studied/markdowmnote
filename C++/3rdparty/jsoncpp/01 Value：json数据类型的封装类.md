# Value：json数据类型的封装类

​	Value对象创建时可以不指定类型，在使用时会根据操作自动指定类型。

## 1. valueType枚举类

​	`valueType`是用于初始化`Value`类的枚举类，用于指定`Value`中的数据类型。

| 枚举类型     | 说明                                            | 备注                       |
| ------------ | ----------------------------------------------- | -------------------------- |
| nullValue    | `null` value                                    | 不表示任何数据，空值       |
| intValue     | `signed integer` value                          | 表示有符号整数             |
| uintValue    | `unsigned integer` value                        | 表示无符号整数             |
| realValue    | `double` value                                  | 表示浮点数                 |
| stringValue  | `UTF-8 string` value                            | 表示utf8格式的字符串       |
| booleanValue | `bool` value                                    | 表示布尔数                 |
| arrayValue   | `array` value (ordered list)                    | 表示数组，即JSON串中的[]   |
| objectValue  | `object` value (collection of name/value pairs) | 表示键值对，即JSON串中的{} |

## 2. 构造函数

```C++
// 因为Json::Value已经实现了各种数据类型的构造函数
Value(ValueType type = nullValue);

Value(Int value);
Value(UInt value);
Value(Int64 value);
Value(UInt64 value);
Value(double value);
Value(const char* value);
Value(const char* begin, const char* end);
Value(bool value);
Value(const Value& other);
Value(Value&& other);
```

## 3. 检测与转换函数

### 3.1 检测类型

```C++
// 检测保存的数据类型
bool isNull() const;
bool isBool() const;
bool isInt() const;
bool isInt64() const;
bool isUInt() const;
bool isUInt64() const;
bool isIntegral() const;
bool isDouble() const;
bool isNumeric() const;
bool isString() const;
bool isArray() const;
bool isObject() const;
```

### 3.2 转换为类型

```C++
Int asInt() const;
UInt asUInt() const;
Int64 asInt64() const;
UInt64 asUInt64() const;
LargestInt asLargestInt() const;
LargestUInt asLargestUInt() const;
JSONCPP_STRING asString() const;
float asFloat() const;
double asDouble() const;
bool asBool() const;
const char* asCString() const;
```

## 4. Json数组的操作

```C++
ArrayIndex size() const;

Value& operator[](ArrayIndex index);
Value& operator[](int index);

const Value& operator[](ArrayIndex index) const;
const Value& operator[](int index) const;

// 根据下标的index返回这个位置的value值
// 如果没找到这个index对应的value, 返回第二个参数defaultValue
Value get(ArrayIndex index, const Value& defaultValue) const;

Value& append(const Value& value);

const_iterator begin() const;
const_iterator end() const;

iterator begin();
iterator end();
```

## 5. json对象的操作

```C++
Value& operator[](const char* key);
const Value& operator[](const char* key) const;
Value& operator[](const JSONCPP_STRING& key);
const Value& operator[](const JSONCPP_STRING& key) const;
Value& operator[](const StaticString& key);

// 通过key, 得到value值
Value get(const char* key, const Value& defaultValue) const;
Value get(const JSONCPP_STRING& key, const Value& defaultValue) const;
Value get(const CppTL::ConstString& key, const Value& defaultValue) const;

// 得到对象中所有的键值
typedef std::vector<std::string> Members;
Members getMemberNames() const;
```

## 6. Value序列化->string

```C++
// 序列化得到的字符串有样式 -> 带换行 -> 方便阅读
// 写配置文件的时候
std::string toStyledString() const;
```

