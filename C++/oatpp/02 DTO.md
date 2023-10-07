# 02 DTO

> ​	DTO代表数据传输对象（Data Transfer Object），是一种设计模式，用于在不同层之间传输数据。它主要用于解决应用程序中数据传输的问题，尤其是在客户端和服务器之间进行数据交换时。

[TOC]

---

## 0 DTO示例

### 创建一个简单DTO

​	DTO类必须以`#include OATPP_CODEGEN_BEGIN(DTO)`开头，以`#include OATPP_CODEGEN_END(DTO)`结尾。

```C++
#include "oatpp/core/Types.hpp"
#include "oatpp/core/macro/codegen.hpp"

#include OATPP_CODEGEN_BEGIN(DTO) ///< Begin DTO codegen section

class User : public oatpp::DTO {

  DTO_INIT(User, DTO /* extends */)

  DTO_FIELD(String, name);
  DTO_FIELD(Int32, age);

};

#include OATPP_CODEGEN_END(DTO) ///< End DTO codegen section
```

## 1.类型

### 1.1 Core Type

​	Types defined in `oatpp/core/Types.hpp`。

### 1.1.1 基础类型

​	这些类型本质上都是**指针**，它们底层指向一个真正的C++值类型。

| Type                                                         | Underlying Type | Default Value |
| ------------------------------------------------------------ | --------------- | ------------- |
| [Int8](https://oatpp.io/api/latest/oatpp/core/Types/#int8)   | `v_int8`        | `nullptr`     |
| [UInt8](https://oatpp.io/api/latest/oatpp/core/Types/#uint8) | `v_uint8`       | `nullptr`     |
| [Int16](https://oatpp.io/api/latest/oatpp/core/Types/#int16) | `v_int16`       | `nullptr`     |
| [UInt16](https://oatpp.io/api/latest/oatpp/core/Types/#uint16) | `v_uint16`      | `nullptr`     |
| [Int32](https://oatpp.io/api/latest/oatpp/core/Types/#int32) | `v_int32`       | `nullptr`     |
| [UInt32](https://oatpp.io/api/latest/oatpp/core/Types/#uint32) | `v_uint32`      | `nullptr`     |
| [Int64](https://oatpp.io/api/latest/oatpp/core/Types/#int64) | `v_int64`       | `nullptr`     |
| [UInt64](https://oatpp.io/api/latest/oatpp/core/Types/#uint64) | `v_uint64`      | `nullptr`     |
| [Float32](https://oatpp.io/api/latest/oatpp/core/Types/#float32) | `v_float32`     | `nullptr`     |
| [Float64](https://oatpp.io/api/latest/oatpp/core/Types/#float64) | `v_float64`     | `nullptr`     |
| [Boolean](https://oatpp.io/api/latest/oatpp/core/Types/#boolean) | `bool`          | `nullptr`     |

##### Examples

```cpp
oatpp::Int32 a = 32;
v_int32 va = *a;
```

### 1.1.2 容器类型

​	这些类型本质上都是指针，它们底层指向一个真正的C++容器类型。

| Type                                                         | Underlying Collection              | Default Value |
| ------------------------------------------------------------ | ---------------------------------- | ------------- |
| [Vector](https://oatpp.io/api/latest/oatpp/core/Types/#vector) | `std::vector<T>`                   | `nullptr`     |
| [List](https://oatpp.io/api/latest/oatpp/core/Types/#list)   | `std::list<T>`                     | `nullptr`     |
| [UnorderedSet](https://oatpp.io/api/latest/oatpp/core/Types/#unorderedset) | `std::unordered_set<T>`            | `nullptr`     |
| [Fields](https://oatpp.io/api/latest/oatpp/core/Types/#fields)（Map） | `std::list<std::pair<Key, Value>>` | `nullptr`     |
| [UnorderedFields](https://oatpp.io/api/latest/oatpp/core/Types/#unorderedfields) | `std::unordered_map<Key, Value>`   | `nullptr`     |

##### Examples

```cpp
oatpp::Vector<oatpp::String> porridges = {"Owsianka", "Stirabout", "Zabkása"};
for(auto& porridge : *porridges) {
  ...
}
```

### 1.1.3 特殊类型

​	这些类型本质上都是指针。

| Type                                                         | Underlying Type                                              | Default Value |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------- |
| [String](https://oatpp.io/api/latest/oatpp/core/Types/#string) | [oatpp::base::StrBuffer](https://oatpp.io/api/latest/oatpp/core/base/StrBuffer/) | `nullptr`     |
| [Object](https://oatpp.io/api/latest/oatpp/core/Types/#object) | class which extends [oatpp::DTO](https://oatpp.io/api/latest/oatpp/core/Types/#dto) | `nullptr`     |
| [Enum](https://oatpp.io/api/latest/oatpp/core/Types/#enum)   | `enum` declared via [ENUM](https://oatpp.io/api/latest/oatpp/codegen/dto/enum_define/#enum) | `nullptr`     |
| [Any](https://oatpp.io/api/latest/oatpp/core/Types/#any)     | any other mapping-enabled type                               | `nullptr`     |

#### Examples - String

```C++
#include "oatpp/core/data/stream/BufferStream.hpp"
#include "oatpp/core/utils/ConversionUtils.hpp"
#include "oatpp/core/Types.hpp"

void constructExample() {
  // 构造函数和赋值操作符
  oatpp::String str1 = "Hello";
  oatpp::String str2("Oat++");
  oatpp::String str3 = str1 + ", " + str2;

  // 长度和大小
  std::cout << "Length: " << str3->getLength() << std::endl;
  std::cout << "Size: " << str3->getSize() << std::endl;

  // 访问字符和字节
  char ch = str3->getCharAt(0);
  const void* data = str3->getData();

  // 拼接和连接
  oatpp::String joined = str1->concat(", ").concat(str2);

  // 格式化和转换
  oatpp::String formatted = oatpp::String::format("Value: %d", 42);
  int value = oatpp::utils::conversion::strToInt32(formatted);

  // 子字符串和切割
  oatpp::String subString = str3->substring(7, 5);
  auto splitString = str3->split(',');

  // 编码和解码
  oatpp::String encoded = oatpp::web::Url::encode(str3);
  oatpp::String decoded = oatpp::web::Url::decode(encoded);

  // 比较和查找
  bool isEqual = str1->equals(str2);
  v_int32 index = str3->indexOf("Oat");

  std::cout << joined->c_str() << std::endl;
  std::cout << formatted->c_str() << std::endl;
}

```

> ​	URL 编码是将 URL 中的特殊字符转换为特定的编码形式，以便能够安全地传输和处理。例如，空格字符会被转换为 %20，而其他特殊字符也有相应的转义形式。这样可以确保 URL 中的特殊字符不会被误解析或引起错误。
>
> ​	URL 编码和解码方法常用于处理 URL 参数、路径或查询字符串中的特殊字符。这避免了由于特殊字符引起的解析错误或安全问题。例如，在构建 HTTP 请求时，通过对参数进行 URL 编码，可以确保参数值中不包含特殊字符，从而使请求能够正确地发送到服务器。

#### Examples - Object

​	Object就是一个类似智能指针的包裹。`Object<MyDto>`与`MyDto::Wrapper`是同一种类型。

```C++
class MyDto : public oatpp::DTO {
  DTO_INIT(MyDto, DTO)
  
  DTO_FIELD(String, name);
  DTO_FIELD(Int32, age);
};

oatpp::Object<MyDto> dto = MyDto::createShared();
//MyDto::Wrapper dto = MyDto::createShared();
```

#### Examples - Enum

​	这是一个枚举类型，通过使用ENUM宏声明。

```C++
ENUM(MyEnum, v_int32,
  VALUE(V1, 1, "Value 1"),
  VALUE(V2, 2, "Value 2")
);

MyEnum enumValue = MyEnum::V1;
```

#### Examples - Any

​	这是一个通用类型，可以存储任何支持映射的类型。

```C++
oatpp::Object<MyDto> dto = MyDto::createShared();
oatpp::Any any = dto;	//存入any

if (any.isA<oatpp::Object<MyDto>>()) {
  auto retrievedObject = any.retrieve<oatpp::Object<MyDto>>();	//从any转换解析出来
  // 处理 retrievedObject
}
```

​	有时也可以用来存储不同类型的对象集合：oatpp::AnyArray。

​	这种做法用于一种容易扩展的系统（当any数组中增加了一个数据，无需改动代码就能自动处理这个增加的数据）。

```C++
// 包含必要的头文件
#include "oatpp/core/Types.hpp"
#include "oatpp/core/base/Countable.hpp"
#include "oatpp/core/collection/Array.hpp"

// 使用命名空间
using namespace oatpp;

// 创建一个任意类型的数组
oatpp::AnyArray anyArray;

// 添加不同类型的对象到数组中
anyArray.push_back<String>("Hello, Oat++!"); // 添加一个字符串对象
anyArray.push_back<Int32>(42);                // 添加一个整数对象
anyArray.push_back<Float64>(3.14);            // 添加一个浮点数对象

// 在数组中循环遍历并进行动态处理
for (const auto& item : anyArray) {
  if (item.isA<String>()) {
    auto str = item.retrieve<String>();
    // 处理字符串对象
    std::cout << "String: " << *str << std::endl;
  } else if (item.isA<Int32>()) {
    auto intValue = item.retrieve<Int32>();
    // 处理整数对象
    std::cout << "Int: " << *intValue << std::endl;
  } else if (item.isA<Float64>()) {
    auto floatValue = item.retrieve<Float64>();
    // 处理浮点数对象
    std::cout << "Float: " << *floatValue << std::endl;
  }
}

```

## 2.字段

### 2.1 String字段

#### 2.1.1 限定符

​	意味着在生成的`Swagger-UI`中，字段名将以`user-name`的形式出现，而不是`name`，但是代码内部的变量叫做`name`。

```C++
DTO_FIELD(String, name, "user-name");
```

#### 2.1.2 默认值

​	设置字段的默认值为`"Ivan"`

```C++
DTO_FIELD(String, name) = "Ivan";
```

#### 2.1.3 补充描述

​	字段的补充描述会显示在`Swagger-UI`上。

```C++
DTO_FIELD_INFO(name) {
  info->description = "user first name"; //<-- Fields description is integrated with Swagger-UI.
}
DTO_FIELD(String, name) = "Ivan";
```

### 2.2 容器字段

#### 2.2.1 `Object<DTO>`（DTO::Wrapper）

​	`Object<DTO>`用于对DTO的复用，其本质上等于`DTO::Wrapper`。详细见[DTO高级说明](#highlevel)。

```cpp
DTO_FIELD(Object<User>, user);
```

#### 2.2.2 List

1. List of **primitives**:

```cpp
DTO_FIELD(List<Int32>, colors);
```

2. List of **Objects**:

```cpp
DTO_FIELD(List<Object<MyObject>>, colors);
```

#### 2.2.3 Map

​	Map都是以String为键，因此只需设置value类型。

1. Map `String --> Int32`:

```cpp
DTO_FIELD(Fields<Int32>, colors);
```

2. Map `String --> Object`:

```cpp
DTO_FIELD(Fields<Object<MyObject>>, colors);
```

## 3.<span id="highlevel">DTO的高级说明</span>

### 3.1 DTO代码复用

​	DTO所对应的是一个json，也可以是json的封闭部分（以`{}`封闭），因此，DTO可以包含DTO来实现代码复用的功能。PageDTO是一个更好的例子。

```C++
#include "oatpp/core/Types.hpp"

class InnerDTO : public oatpp::DTO {
public:
  DTO_INIT(InnerDTO, DTO)

  DTO_FIELD(Int32, innerField);
};

class OuterDTO : public oatpp::DTO {
public:
  DTO_INIT(OuterDTO, DTO)

  DTO_FIELD(String, outerField);
  DTO_FIELD(Object<InnerDTO>, innerObject);
};

/*
InnerDTO:
{
  "innerField": 123
}

OuterDTO:
{
  "outerField": "example",
  "innerObject": {
    "innerField": 123
  }
}
*/
```

### 3.2 自定义（C++类 -> oatpp类）

​	一般来说，继承自DTO已经能满足大部分需要了，但是在下面的情况可能会需要用到自定义的类，并经过otapp框架改造以使用其便利的功能：

1. **非标准化数据：** 如果你的数据对象不符合常规的 DTO 模式，并且无法简单地通过继承 oatpp 的 DTO 类进行表示，那么使用自定义类是一个合理的选择。你可以根据实际需求设计自己的类结构，并在其中添加必要的方法和属性。
2. **现有类结构：** 如果你已经有了现有的类结构，并且在 oatpp 的对象映射框架中使用它们更为方便，那么就没有必要强制转换成继承自 oatpp 的 DTO 类。你可以选择直接使用现有的类，并手动实现序列化/反序列化方法和其他必要的功能。

---

注意：当使用自定义类时，oatpp 对象映射框架主要关注将其 public 字段转换为对应的映射。

下面是一个自定义类，并将其转换为oatpp对象的演示。

```C++
// 包含所需的头文件
#include "oatpp/core/Types.hpp"
#include "oatpp/core/macro/codegen.hpp"
#include "oatpp/parser/json/mapping/ObjectMapper.hpp"

// 定义结构体 VPoint
struct VPoint {
    v_int32 x; // x 坐标
    v_int32 y; // y 坐标
    v_int32 z; // z 坐标
};

namespace __class {
    class PointClass;
}

// VPoint->Point映射，存储类型信息的类为PointClass
typedef oatpp::data::mapping::type::Primitive<VPoint, __class::PointClass> Point;

namespace __class {

    /**
     * 类型信息
     */
    class PointClass {
    private:

        /**
         * 类型解释器类
         */
        class Inter : public oatpp::Type::Interpretation<Point, oatpp::UnorderedFields<oatpp::Int32>> {
        public:

            // 将自定义类型转换为 oatpp 中的字段集合类型
            oatpp::UnorderedFields<oatpp::Int32> interpret(const Point& value) const override {
                return { {"x", value->x}, {"y", value->y}, {"z", value->z} };
            }

            // 将 oatpp 中的字段集合类型转换回自定义类型
            Point reproduce(const oatpp::UnorderedFields<oatpp::Int32>& map) const override {
                return Point({ map["x"], map["y"], map["z"] });
            }

        };

    public:
        // 声明类的唯一标识符，一般该变量都用CLASS_ID命名来强调说明
        static const oatpp::ClassId CLASS_ID;

        // 获取类型信息的静态方法
        static oatpp::Type* getType() {
            // 创建类型对象，并添加类型解释器，这里的my-types可以改成其它的标志名
            static oatpp::Type type(CLASS_ID, oatpp::Type::Info{"my-types", new Inter()});
            return &type;
        }

    };

    // 定义类的唯一标识符，用于oatpp注册这个类，与上面的解释器相关联
    const oatpp::ClassId PointClass::CLASS_ID("Point");

}
```

​	下面是一个使用 `Point` 类型的示例：

```cpp
// 创建一个 Point 对象
Point myPoint({1, 2, 3});

// 使用 oatpp 对象映射框架的功能操作 Point 对象
oatpp::Object<Point> newPoint = Point::createShared();
newPoint->x = 4;
newPoint->y = 5;
newPoint->z = 6;

// 将 Point 对象转换为 JSON 字符串
oatpp::String json = oatpp::parser::json::mapping::ObjectMapper::createShared()->writeToString(newPoint);

// 从 JSON 字符串中解析 Point 对象
oatpp::Object<Point> parsedPoint = oatpp::parser::json::mapping::ObjectMapper::createShared()->readFromString(json);

// 访问 Point 对象的字段
v_int32 x = parsedPoint->x;
v_int32 y = parsedPoint->y;
v_int32 z = parsedPoint->z;
```

