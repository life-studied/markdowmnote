---
create: '2025-04-04'
modified: '2025-04-04'
---

# Protocol Buffers 文档

## C++ 生成代码指南

本文详细描述了 Protocol Buffers 编译器为给定的协议定义生成的 C++ 代码。文中突出了 proto2 和 proto3 生成代码之间的差异。请注意，这些差异仅存在于本文描述的生成代码中，而不是基础消息类/接口，它们在两个版本中是相同的。在阅读本文之前，建议先阅读 [proto2 语言指南](https://protobuf.dev/programming-guides/proto2/) 和/或 [proto3 语言指南](https://protobuf.dev/programming-guides/proto3/)。

### 编译器调用

当使用 --cpp_out= 命令行标志调用时，Protocol Buffers 编译器会生成 C++ 输出。--cpp_out= 选项的参数是你希望编译器写入 C++ 输出的目录。编译器会为每个输入的 .proto 文件生成一个头文件和一个实现文件。输出文件的命名规则如下：

- 将 .proto 文件的扩展名替换为 .pb.h 或 .pb.cc，分别对应头文件和实现文件。
- 将 .proto 文件路径（通过 --proto_path= 或 -I 命令行标志指定）替换为输出路径（通过 --cpp_out= 指定）。

例如，假设你以以下方式调用编译器：

```
protoc --proto_path=src --cpp_out=build/gen src/foo.proto src/bar/baz.proto
```

编译器将读取 src/foo.proto 和 src/bar/baz.proto 文件，并生成四个输出文件：build/gen/foo.pb.h、build/gen/foo.pb.cc、build/gen/bar/baz.pb.h 和 build/gen/bar/baz.pb.cc。编译器会自动创建 build/gen/bar 目录（如果需要），但不会创建 build 或 build/gen 目录；这些目录必须已经存在。

### 包

如果 .proto 文件中包含 package 声明，则文件中的所有内容都将被放置在对应的 C++ 命名空间中。例如，给定以下 package 声明：

```
package foo.bar;
```

文件中的所有声明都将位于 foo::bar 命名空间中。

### 消息

给定一个简单的消息声明：

```
message Foo {}
```

Protocol Buffers 编译器会生成一个名为 Foo 的类，该类公开继承自 google::protobuf::Message。这个类是一个具体类；没有未实现的纯虚方法。根据优化模式的不同，Message 中的虚方法可能会被 Foo 覆盖，也可能不会。默认情况下，Foo 实现了所有方法的专门版本，以实现最大速度。然而，如果 .proto 文件中包含以下行：

```
option optimize_for = CODE_SIZE;
```

那么 Foo 将只覆盖运行所需的最小方法集，并依赖基于反射的其他方法实现。这显著减少了生成代码的大小，但也会降低性能。或者，如果 .proto 文件中包含：

```
option optimize_for = LITE_RUNTIME;
```

那么 Foo 将包含所有方法的快速实现，但会实现 google::protobuf::MessageLite 接口，该接口仅包含 Message 的一个子集。特别是，它不支持描述符或反射。然而，在这种模式下，生成的代码只需要链接到 libprotobuf-lite.so（在 Windows 上是 libprotobuf-lite.lib），而不是 libprotobuf.so（在 Windows 上是 libprotobuf.lib）。"lite" 库比完整库小得多，更适合资源受限的系统，如移动电话。

你不应该创建自己的 Foo 子类。如果你继承这个类并覆盖一个虚方法，覆盖可能会被忽略，因为许多生成的方法调用被去虚化以提高性能。

Message 接口定义了允许你检查、操作、读取或写入整个消息的方法，包括从二进制字符串解析和序列化。

- bool ParseFromString(::absl::string_view data)：从给定的序列化二进制字符串（也称为线缆格式）解析消息。
- bool SerializeToString(string* output) const：将给定的消息序列化为二进制字符串。
- string DebugString()：返回一个字符串，给出 proto 的文本格式表示（仅应用于调试）。

除了这些方法外，Foo 类还定义了以下方法：

- Foo()：默认构造函数。
- ~Foo()：默认析构函数。
- Foo(const Foo& other)：拷贝构造函数。
- Foo(Foo&& other)：移动构造函数。
- Foo& operator=(const Foo& other)：赋值运算符。
- Foo& operator=(Foo&& other)：移动赋值运算符。
- void Swap(Foo* other)：与另一条消息交换内容。
- const UnknownFieldSet& unknown_fields() const：返回在解析此消息时遇到的未知字段集合。如果在 .proto 文件中指定了 option optimize_for = LITE_RUNTIME，则返回类型变为 std::string&。
- UnknownFieldSet* mutable_unknown_fields()：返回指向在解析此消息时遇到的可变未知字段集合的指针。如果在 .proto 文件中指定了 option optimize_for = LITE_RUNTIME，则返回类型变为 std::string*。

该类还定义了以下静态方法：

- static const Descriptor* descriptor()：返回类型的描述符。它包含有关类型的信息，包括它有哪些字段以及它们的类型是什么。可以使用反射来程序化地检查字段。
- static const Foo& default_instance()：返回一个常量单例实例 Foo，该实例与新构造的 Foo 实例相同（因此所有单个字段都未设置，所有重复字段都为空）。请注意，消息的默认实例可以用作工厂，通过调用其 New() 方法来实现。

### 生成的文件名

在生成的输出中，保留关键字会附加一个下划线。

例如，以下 proto3 定义语法：

```
message MyMessage {
  string false = 1;
  string myFalse = 2;
}
```

生成的部分输出如下：

```
void clear_false_();
const std::string& false_() const;
void set_false_(Arg_&& arg, Args_... args);
std::string* mutable_false_();
PROTOBUF_NODISCARD std::string* release_false_();
void set_allocated_false_(std::string* ptr);

void clear_myfalse();
const std::string& myfalse() const;
void set_myfalse(Arg_&& arg, Args_... args);
std::string* mutable_myfalse();
PROTOBUF_NODISCARD std::string* release_myfalse();
void set_allocated_myfalse(std::string* ptr);
```

### 嵌套类型

可以在另一个消息中声明消息。例如：

```
message Foo {
  message Bar {}
}
```

在这种情况下，编译器会生成两个类：Foo 和 Foo_Bar。此外，编译器还会在 Foo 中生成一个类型定义如下：

```
typedef Foo_Bar Bar;
```

这意味着你可以像使用嵌套类 Foo::Bar 一样使用嵌套类型的类。然而，请注意，C++ 不允许嵌套类型进行前向声明。如果你想在另一个文件中前向声明 Bar 并使用该声明，你必须将其标识为 Foo_Bar。

### 字段

除了上一节中描述的方法外，Protocol Buffers 编译器还会为 .proto 文件中定义的每个字段生成一组访问器方法。这些方法采用小写蛇形命名法，例如 has_foo() 和 clear_foo()。

除了访问器方法外，编译器还会为每个字段生成一个整数常量，其中包含字段编号。常量名称是字母 k，后跟字段名称转换为驼峰命名法，再后跟 FieldNumber。例如，给定字段 optional int32 foo_bar = 5;，编译器将生成常量 static const int kFooBarFieldNumber = 5;。

对于返回常量引用的字段访问器，当对消息进行下一次修改访问时，该引用可能会失效。这包括调用任何非常量字段的访问器、调用从 Message 继承的任何非常量方法或通过其他方式修改消息（例如，通过将消息作为 Swap() 的参数）。相应地，只有在两次访问器调用之间没有对消息进行修改访问的情况下，返回引用的地址才会保持相同。

对于返回指针的字段访问器，无论常量性如何，当对消息进行下一次访问（无论是修改还是非修改）时，该指针可能会失效。这包括调用任何字段的访问器、调用从 Message 继承的任何方法或通过其他方式访问消息（例如，通过使用拷贝构造函数复制消息）。相应地，返回指针的值在两次不同的调用之间永远不会保证相同。

### 可选数值字段（proto2 和 proto3）

对于以下字段定义：

```proto
optional int32 foo = 1;
required int32 foo = 1;
```

编译器将生成以下访问器方法：

- `bool has_foo() const`：如果字段已设置，则返回 `true`。
- `int32 foo() const`：返回字段的当前值。如果字段未设置，则返回默认值。
- `void set_foo(int32 value)`：设置字段的值。调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回 `value`。
- `void clear_foo()`：清除字段的值。调用此方法后，`has_foo()` 将返回 `false`，`foo()` 将返回默认值。

对于其他数值字段类型（包括 `bool`），`int32` 将被替换为对应的 C++ 类型，具体可参考标量值类型表。

### 隐式存在数值字段（proto3）

对于以下字段定义：

```proto
int32 foo = 1;  // 未指定字段标签，默认为隐式存在
```

编译器将生成以下访问器方法：

- `int32 foo() const`：返回字段的当前值。如果字段未设置，则返回 `0`。
- `void set_foo(int32 value)`：设置字段的值。调用此方法后，`foo()` 将返回 `value`。
- `void clear_foo()`：清除字段的值。调用此方法后，`foo()` 将返回 `0`。

对于其他数值字段类型（包括 `bool`），`int32` 将被替换为对应的 C++ 类型，具体可参考标量值类型表。

### 可选字符串/字节字段（proto2 和 proto3）

**注意**：从 2023 版本开始，如果 `features.(pb.cpp).string_type` 设置为 `VIEW`，则会生成 `string_view` API。

对于以下字段定义：

```proto
optional string foo = 1;
required string foo = 1;
optional bytes foo = 1;
required bytes foo = 1;
```

编译器将生成以下访问器方法：

- `bool has_foo() const`：如果字段已设置，则返回 `true`。
- `const string& foo() const`：返回字段的当前值。如果字段未设置，则返回默认值。
- `void set_foo(::absl::string_view value)`：设置字段的值。调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回 `value` 的副本。
- `void set_foo(const string& value)`：设置字段的值。调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回 `value` 的副本。
- `void set_foo(string&& value)`：设置字段的值，从传入的字符串中移动。调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回 `value` 的副本。
- `void set_foo(const char* value)`：使用 C 风格的以空字符结尾的字符串设置字段的值。调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回 `value` 的副本。
- `void set_foo(const char* value, int size)`：使用具有显式大小的字符串设置字段的值，而不是通过查找空终止字节来确定大小。调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回 `value` 的副本。
- `string* mutable_foo()`：返回一个指向存储字段值的可变字符串对象的指针。如果在调用之前字段未设置，则返回的字符串将为空（不是默认值）。调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回写入给定字符串的值。
- `void clear_foo()`：清除字段的值。调用此方法后，`has_foo()` 将返回 `false`，`foo()` 将返回默认值。
- `void set_allocated_foo(string* value)`：将字符串对象设置为字段的值，并释放之前的字段值（如果存在）。如果字符串指针不为 `NULL`，消息将拥有分配的字符串对象的所有权，`has_foo()` 将返回 `true`。消息可以在任何时间删除分配的字符串对象，因此对该对象的引用可能会失效。否则，如果值为 `NULL`，行为与调用 `clear_foo()` 相同。
- `string* release_foo()`：释放字段的所有权并返回字符串对象的指针。调用此方法后，调用者将拥有分配的字符串对象的所有权，`has_foo()` 将返回 `false`，`foo()` 将返回默认值。

### 隐式存在字符串/字节字段（proto3）

**注意**：从 2023 版本开始，如果 `features.(pb.cpp).string_type` 设置为 `VIEW`，则会生成 `string_view` API。

对于以下字段定义：

```proto
string foo = 1;  // 未指定字段标签，默认为隐式存在
bytes foo = 1;
```

编译器将生成以下访问器方法：

- `const string& foo() const`：返回字段的当前值。如果字段未设置，则返回空字符串/空字节。
- `void set_foo(::absl::string_view value)`：设置字段的值。调用此方法后，`foo()` 将返回 `value` 的副本。
- `void set_foo(const string& value)`：设置字段的值。调用此方法后，`foo()` 将返回 `value` 的副本。
- `void set_foo(string&& value)`：设置字段的值，从传入的字符串中移动。调用此方法后，`foo()` 将返回 `value` 的副本。
- `void set_foo(const char* value)`：使用 C 风格的以空字符结尾的字符串设置字段的值。调用此方法后，`foo()` 将返回 `value` 的副本。
- `void set_foo(const char* value, int size)`：使用具有显式大小的字符串设置字段的值，而不是通过查找空终止字节来确定大小。调用此方法后，`foo()` 将返回 `value` 的副本。
- `string* mutable_foo()`：返回一个指向存储字段值的可变字符串对象的指针。如果在调用之前字段未设置，则返回的字符串将为空。调用此方法后，`foo()` 将返回写入给定字符串的值。
- `void clear_foo()`：清除字段的值。调用此方法后，`foo()` 将返回空字符串/空字节。
- `void set_allocated_foo(string* value)`：将字符串对象设置为字段的值，并释放之前的字段值（如果存在）。如果字符串指针不为 `NULL`，消息将拥有分配的字符串对象的所有权。消息可以在任何时间删除分配的字符串对象，因此对该对象的引用可能会失效。否则，如果值为 `NULL`，行为与调用 `clear_foo()` 相同。
- `string* release_foo()`：释放字段的所有权并返回字符串对象的指针。调用此方法后，调用者将拥有分配的字符串对象的所有权，`foo()` 将返回空字符串/空字节。

### 单个字节字段支持 Cord

从 v23.0 开始，增加了对 `absl::Cord` 的支持，用于单个字节字段（包括 oneof 字段）。单个字符串字段、重复字符串字段和重复字节字段不支持使用 Cords。

要将单个字节字段设置为使用 `absl::Cord` 存储数据，可以使用以下语法：

```proto
optional bytes foo = 25 [ctype=CORD];
bytes bar = 26 [ctype=CORD];
```

请注意，重复字节字段不支持使用 cord。对于这些字段，`protoc` 会忽略 `[ctype=CORD]` 设置。

编译器将生成以下访问器方法：

- `const ::absl::Cord& foo() const`：返回字段的当前值。如果字段未设置，在 proto3 中返回空的 Cord，或在 proto2 中返回默认值。

- `void set_foo(const ::absl::Cord& value)`：设置字段的值。调用此方法后，`foo()` 将返回 `value`。

- `void set_foo(::absl::string_view value)`：设置字段的值。调用此方法后，`foo()` 将返回 `value` 作为 `absl::Cord`。

- `void clear_foo()`：清除字段的值。调用此方法后，`foo()` 将返回空

- 的 Cord（在 proto3 中）或默认值（在 proto2 中）。
    - `bool has_foo()`：如果字段已设置，则返回 `true`。

    ### 可选枚举字段（proto2 和 proto3）

    给定枚举类型：

    ```proto
    enum Bar {
      BAR_UNSPECIFIED = 0;
      BAR_VALUE = 1;
      BAR_OTHER_VALUE = 2;
    }
    ```

    对于以下字段定义：

    ```proto
    optional Bar bar = 1;
    required Bar bar = 1;
    ```

    编译器将生成以下访问器方法：

    - `bool has_bar() const`：如果字段已设置，则返回 `true`。
    - `Bar bar() const`：返回字段的当前值。如果字段未设置，则返回默认值。
    - `void set_bar(Bar value)`：设置字段的值。调用此方法后，`has_bar()` 将返回 `true`，`bar()` 将返回 `value`。在调试模式下（即未定义 `NDEBUG`），如果 `value` 不匹配 `Bar` 中定义的任何值，此方法将终止进程。
    - `void clear_bar()`：清除字段的值。调用此方法后，`has_bar()` 将返回 `false`，`bar()` 将返回默认值。

    ### 隐式存在枚举字段（proto3）

    对于以下字段定义：

    ```proto
    Bar bar = 1;  // 未指定字段标签，默认为隐式存在
    ```

    编译器将生成以下访问器方法：

    - `Bar bar() const`：返回字段的当前值。如果字段未设置，则返回默认值（`0`）。
    - `void set_bar(Bar value)`：设置字段的值。调用此方法后，`bar()` 将返回 `value`。
    - `void clear_bar()`：清除字段的值。调用此方法后，`bar()` 将返回默认值。

    ### 可选嵌套消息字段（proto2 和 proto3）

    给定消息类型：

    ```proto
    message Bar {}
    ```

    对于以下字段定义：

    ```proto
    // proto2
    optional Bar bar = 1;
    required Bar bar = 1;
    
    // proto3
    Bar bar = 1;
    ```

    编译器将生成以下访问器方法：

    - `bool has_bar() const`：如果字段已设置，则返回 `true`。
    - `const Bar& bar() const`：返回字段的当前值。如果字段未设置，则返回一个未设置任何字段的 `Bar`（可能是 `Bar::default_instance()`）。
    - `Bar* mutable_bar()`：返回一个指向存储字段值的可变 `Bar` 对象的指针。如果在调用之前字段未设置，则返回的 `Bar` 将未设置任何字段（即与新分配的 `Bar` 相同）。调用此方法后，`has_bar()` 将返回 `true`，`bar()` 将返回对该实例的引用。
    - `void clear_bar()`：清除字段的值。调用此方法后，`has_bar()` 将返回 `false`，`bar()` 将返回默认值。
    - `void set_allocated_bar(Bar* bar)`：将 `Bar` 对象设置为字段的值，并释放之前的字段值（如果存在）。如果 `Bar` 指针不为 `NULL`，消息将拥有分配的 `Bar` 对象的所有权，`has_bar()` 将返回 `true`。否则，如果 `Bar` 为 `NULL`，行为与调用 `clear_bar()` 相同。
    - `Bar* release_bar()`：释放字段的所有权并返回 `Bar` 对象的指针。调用此方法后，调用者将拥有分配的 `Bar` 对象的所有权，`has_bar()` 将返回 `false`，`bar()` 将返回默认值。

    ### 重复数值字段

    对于以下字段定义：

    ```proto
    repeated int32 foo = 1;
    ```

    编译器将生成以下访问器方法：

    - `int foo_size() const`：返回字段中当前的元素数量。要检查是否为空集，建议使用底层 `RepeatedField` 的 `empty()` 方法，而不是此方法。
    - `int32 foo(int index) const`：返回指定零基索引处的元素。使用超出 `[0, foo_size())` 范围的索引调用此方法将导致未定义行为。
    - `void set_foo(int index, int32 value)`：设置指定零基索引处的元素的值。
    - `void add_foo(int32 value)`：在字段的末尾追加一个新的元素，其值为给定值。
    - `void clear_foo()`：从字段中移除所有元素。调用此方法后，`foo_size()` 将返回 `0`。
    - `const RepeatedField<int32>& foo() const`：返回存储字段元素的底层 `RepeatedField`。此类容器类提供了类似 STL 的迭代器和其他方法。
    - `RepeatedField<int32>* mutable_foo()`：返回一个指向存储字段元素的底层可变 `RepeatedField` 的指针。此类容器类提供了类似 STL 的迭代器和其他方法。

    对于其他数值字段类型（包括 `bool`），`int32` 将被替换为对应的 C++ 类型，具体可参考标量值类型表。

    ### 重复字符串字段

    **注意**：从 2023 版本开始，如果 `features.(pb.cpp).string_type` 设置为 `VIEW`，则会生成 `string_view` API。

    对于以下字段定义：

    ```proto
    repeated string foo = 1;
    repeated bytes foo = 1;
    ```

    编译器将生成以下访问器方法：

    - `int foo_size() const`：返回字段中当前的元素数量。要检查是否为空集，建议使用底层 `RepeatedField` 的 `empty()` 方法，而不是此方法。
    - `const string& foo(int index) const`：返回指定零基索引处的元素。使用超出 `[0, foo_size()-1]` 范围的索引调用此方法将导致未定义行为。
    - `void set_foo(int index, ::absl::string_view value)`：设置指定零基索引处的元素的值。
    - `void set_foo(int index, const string& value)`：设置指定零基索引处的元素的值。
    - `void set_foo(int index, string&& value)`：设置指定零基索引处的元素的值，从传入的字符串中移动。
    - `void set_foo(int index, const char* value)`：使用 C 风格的以空字符结尾的字符串设置指定零基索引处的元素的值。
    - `void set_foo(int index, const char* value, int size)`：使用具有显式大小的字符串设置指定零基索引处的元素的值，而不是通过查找空终止字节来确定大小。
    - `string* mutable_foo(int index)`：返回一个指向存储指定零基索引处元素值的可变字符串对象的指针。使用超出 `[0, foo_size())` 范围的索引调用此方法将导致未定义行为。
    - `void add_foo(::absl::string_view value)`：在字段的末尾追加一个新的元素，其值为给定值。
    - `void add_foo(const string& value)`：在字段的末尾追加一个新的元素，其值为给定值。
    - `void add_foo(string&& value)`：在字段的末尾追加一个新的元素，从传入的字符串中移动。
    - `void add_foo(const char* value)`：使用 C 风格的以空字符结尾的字符串在字段的末尾追加一个新的元素。
    - `void add_foo(const char* value, int size)`：使用具有显式大小的字符串在字段的末尾追加一个新的元素，而不是通过查找空终止字节来确定大小。
    - `string* add_foo()`：在字段的末尾添加一个新的空字符串元素，并返回一个指向该元素的指针。
    - `void clear_foo()`：从字段中移除所有元素。调用此方法后，`foo_size()` 将返回 `0`。
    - `const RepeatedPtrField<string>& foo() const`：返回存储字段元素的底层 `RepeatedPtrField`。此类容器类提供了类似 STL 的迭代器和其他方法。
    - `RepeatedPtrField<string>* mutable_foo()`：返回一个指向存储字段元素的底层可变 `RepeatedPtrField` 的指针。此类容器类提供了类似 STL 的迭代器和其他方法。

    ### 重复枚举字段

    给定枚举类型：

    ```proto
    enum Bar {
      BAR_UNSPECIFIED = 0;
      BAR_VALUE = 1;
      BAR_OTHER_VALUE = 2;
    }
    ```

    对于以下字段定义：

    ```proto
    repeated Bar bar = 1;
    ```

    编译器将生成以下访问器方法：

    - `int bar_size() const`：返回字段中当前的元素数量。要检查是否为空集，建议使用底层 `RepeatedField` 的 `empty()` 方法，而不是此方法。
    - `Bar bar(int index) const`：返回指定零基索引处的元素。使用超出 `[0, bar_size())` 范围的索引调用此方法将导致未定义行为。
    - `void set_bar(int index, Bar value)`：设置指定零基索引处的元素的值。在调试模式下（即未定义 `NDEBUG`），如果 `value` 不匹配 `Bar` 中定义的任何值，此方法将终止进程。
    - `void add_bar(Bar value)`：在字段的末尾追加一个新的元素，其值为给定值。在调试模式下（即未定义 `NDEBUG`），如果 `value` 不匹配 `Bar` 中定义的任何值，此方法将终止进程。
    - `void clear_bar()`：从字段中移除所有元素。调用此方法后，`bar_size()` 将返回 `0`。
    - `const RepeatedField<int>& bar() const`：返回存储字段元素的底层 `RepeatedField`。此类容器类提供了类似 STL 的迭代器和其他方法。
    - `RepeatedField<int>* mutable_bar()`：返回一个指向存储字段元素的底层可变 `RepeatedField` 的指针。此类容器类提供了类似 STL 的迭代器和其他方法。

    ### 重复嵌套消息字段

    给定消息类型：

    ```proto
    message Bar {}
    ```

    对于以下字段定义：

    ```proto
    repeated Bar bar = 1;
    ```

    编译器将生成以下访问器方法：

    - `int bar_size() const`：返回字段中当前的元素数量。要检查是否为空集，建议使用底层 `RepeatedField` 的 `empty()` 方法，而不是此方法。
    - `const Bar& bar(int index) const`：返回指定零基索引处的元素。使用超出 `[0, bar_size())` 范围的索引调用此方法将导致未定义行为。
    - `Bar* mutable_bar(int index)`：返回一个指向存储指定零基索引处元素值的可变 `Bar` 对象的指针。使用超出 `[0, bar_size())` 范围的索引调用此方法将导致未定义行为。
    - `Bar* add_bar()`：在字段的末尾添加一个新的元素，并返回一个指向该元素的指针。返回的 `Bar` 是可变的，并且未设置任何字段（即与新分配的 `Bar` 相同）。
    - `void clear_bar()`：从字段中移除所有元素。调用此方法后，`bar_size()` 将返回 `0`。
    - `const RepeatedPtrField<Bar>& bar() const`：返回存储字段元素的底层 `RepeatedPtrField`。此类容器类提供了类似 STL 的迭代器和其他方法。
    - `RepeatedPtrField<Bar>* mutable_bar()`：返回一个指向存储字段元素的底层可变 `RepeatedPtrField` 的指针。此类容器类提供了类似 STL 的迭代器和其他方法。

    ### Oneof 数值字段

    对于以下 `oneof` 字段定义：

    ```proto
    oneof example_name {
        int32 foo = 1;
        ...
    }
    ```

    编译器将生成以下访问器方法：

    - `bool has_foo() const`：如果 `oneof` 的当前字段是 `foo`，则返回 `true`。
    - `int32 foo() const`：如果 `oneof` 的当前字段是 `foo`，则返回该字段的当前值；否则返回默认值。
    - `void set_foo(int32 value)`：
      - 如果同一 `oneof` 中的其他字段已设置，则调用 `clear_example_name()`。
      - 设置该字段的值，并将 `oneof` 的当前字段设置为 `foo`。
      - 调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回 `value`，`example_name_case()` 将返回 `kFoo`。
    - `void clear_foo()`：
      - 如果 `oneof` 的当前字段不是 `foo`，则不进行任何操作。
      - 如果 `oneof` 的当前字段是 `foo`，则清除该字段的值和 `oneof` 的当前字段。`has_foo()` 将返回 `false`，`foo()` 将返回默认值，`example_name_case()` 将返回 `EXAMPLE_NAME_NOT_SET`。

    对于其他数值字段类型（包括 `bool`），`int32` 将被替换为对应的 C++ 类型，具体可参考标量值类型表。

    ### Oneof 字符串字段

    **注意**：从 2023 版本开始，可能会生成 `string_view` API。

    对于以下 `oneof` 字段定义：

    ```proto
    oneof example_name {
        string foo = 1;
        ...
    }
    ```

    编译器将生成以下访问器方法：

    - `bool has_foo() const`：如果 `oneof` 的当前字段是 `foo`，则返回 `true`。
    - `const string& foo() const`：如果 `oneof` 的当前字段是 `foo`，则返回该字段的当前值；否则返回默认值。
    - `void set_foo(::absl::string_view value)`：
      - 如果同一 `oneof` 中的其他字段已设置，则调用 `clear_example_name()`。
      - 设置该字段的值，并将 `oneof` 的当前字段设置为 `foo`。
      - 调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回 `value` 的副本，`example_name_case()` 将返回 `kFoo`。
    - `void set_foo(const string& value)`：与第一个 `set_foo()` 方法类似，但从 `const string&` 类型的引用中复制。
    - `void set_foo(string&& value)`：与第一个 `set_foo()` 方法类似，但从传入的字符串中移动。
    - `void set_foo(const char* value)`：与第一个 `set_foo()` 方法类似，但从 C 风格的以空字符结尾的字符串中复制。
    - `void set_foo(const char* value, int size)`：与第一个 `set_foo()` 方法类似，但从具有显式大小的字符串中复制，而不是通过查找空终止字节来确定大小。
    - `string* mutable_foo()`：
      - 如果同一 `oneof` 中的其他字段已设置，则调用 `clear_example_name()`。
      - 将 `oneof` 的当前字段设置为 `foo`，并返回一个指向存储该字段值的可变字符串对象的指针。如果在调用之前 `oneof` 的当前字段不是 `foo`，则返回的字符串将为空（不是默认值）。
      - 调用此方法后，`has_foo()` 将返回 `true`，`foo()` 将返回写入给定字符串的值，`example_name_case()` 将返回 `kFoo`。
    - `void clear_foo()`：
      - 如果 `oneof` 的当前字段不是 `foo`，则不进行任何操作。
      - 如果 `oneof` 的当前字段是 `foo`，则释放该字段并清除 `oneof` 的当前字段。`has_foo()` 将返回 `false`，`foo()` 将返回默认值，`example_name_case()` 将返回 `EXAMPLE_NAME_NOT_SET`。
    - `void set_allocated_foo(string* value)`：
      - 调用 `clear_example_name()`。
      - 如果字符串指针不为 `NULL`：将字符串对象设置为该字段的值，并将 `oneof` 的当前字段设置为 `foo`。消息将拥有分配的字符串对象的所有权，`has_foo()` 将返回 `true`，`example_name_case()` 将返回 `kFoo`。
      - 如果字符串指针为 `NULL`，则 `has_foo()` 将返回 `false`，`example_name_case()` 将返回 `EXAMPLE_NAME_NOT_SET`。
    - `string* release_foo()`：
      - 如果 `oneof` 的当前字段不是 `foo`，则返回 `NULL`。
      - 清除 `oneof` 的当前字段，释放该字段的所有权，并返回该字段的指针。调用此方法后，调用者将拥有分配的字符串对象的所有权，`has_foo()` 将返回 `false`，`foo()` 将返回默认值，`example_name_case()` 将返回 `EXAMPLE_NAME_NOT_SET`。

    ### Oneof 枚举字段

    给定枚举类型：

    ```proto
    enum Bar {
      BAR_UNSPECIFIED = 0;
      BAR_VALUE = 1;
      BAR_OTHER_VALUE = 2;
    }
    ```

    对于以下 `oneof` 字段定义：

    ```proto
    oneof example_name {
        Bar bar = 1;
        ...
    }
    ```

    编译器将生成以下访问器方法：

    - `bool has_bar() const`：如果 `oneof` 的当前字段是 `bar`，则返回 `true`。
    
    - `Bar bar() const`：如果 `oneof` 的当前字段是 `bar`，则返回该字段的当前值；否则返回默认值。
    
        `void set_bar(Bar value)`：
    
        - 如果同一 `oneof` 中的其他字段已设置，则调用 `clear_example_name()`。
        - 设置该字段的值，并将 `oneof` 的当前字段设置为 `bar`。
        - 调用此方法后，`has_bar()` 将返回 `true`，`bar()` 将返回 `value`，`example_name_case()` 将返回 `kBar`。
        - 在调试模式下（即未定义 `NDEBUG`），如果 `value` 不匹配 `Bar` 中定义的任何值，此方法将终止进程。
    
        `void clear_bar()`：
    
        - 如果 `oneof` 的当前字段不是 `bar`，则不进行任何操作。
        - 如果 `oneof` 的当前字段是 `bar`，则清除该字段的值和 `oneof` 的当前字段。`has_bar()` 将返回 `false`，`bar()` 将返回默认值，`example_name_case()` 将返回 `EXAMPLE_NAME_NOT_SET`。

### Oneof 嵌套消息字段

给定消息类型：

```proto
message Bar {}
```

对于以下 `oneof` 字段定义：

```proto
oneof example_name {
    Bar bar = 1;
    ...
}
```

编译器将生成以下访问器方法：

- `bool has_bar() const`：如果 `oneof` 的当前字段是 `bar`，则返回 `true`。
- `const Bar& bar() const`：如果 `oneof` 的当前字段是 `bar`，则返回该字段的当前值；否则返回一个未设置任何字段的 `Bar`（可能是 `Bar::default_instance()`）。
- `Bar* mutable_bar()`：
    - 如果同一 `oneof` 中的其他字段已设置，则调用 `clear_example_name()`。
    - 将 `oneof` 的当前字段设置为 `bar`，并返回一个指向存储该字段值的可变 `Bar` 对象的指针。如果在调用之前 `oneof` 的当前字段不是 `bar`，则返回的 `Bar` 将未设置任何字段（即与新分配的 `Bar` 相同）。
    - 调用此方法后，`has_bar()` 将返回 `true`，`bar()` 将返回对该实例的引用，`example_name_case()` 将返回 `kBar`。
- `void clear_bar()`：
    - 如果 `oneof` 的当前字段不是 `bar`，则不进行任何操作。
    - 如果 `oneof` 的当前字段是 `bar`，则释放该字段并清除 `oneof` 的当前字段。`has_bar()` 将返回 `false`，`bar()` 将返回默认值，`example_name_case()` 将返回 `EXAMPLE_NAME_NOT_SET`。
- `void set_allocated_bar(Bar* bar)`：
    - 调用 `clear_example_name()`。
    - 如果 `Bar` 指针不为 `NULL`：将 `Bar` 对象设置为该字段的值，并将 `oneof` 的当前字段设置为 `bar`。消息将拥有分配的 `Bar` 对象的所有权，`has_bar()` 将返回 `true`，`example_name_case()` 将返回 `kBar`。
    - 如果指针为 `NULL`，则 `has_bar()` 将返回 `false`，`example_name_case()` 将返回 `EXAMPLE_NAME_NOT_SET`。行为类似于调用 `clear_example_name()`。
- `Bar* release_bar()`：
    - 如果 `oneof` 的当前字段不是 `bar`，则返回 `NULL`。
    - 清除 `oneof` 的当前字段，释放该字段的所有权，并返回 `Bar` 对象的指针。调用此方法后，调用者将拥有分配的 `Bar` 对象的所有权，`has_bar()` 将返回 `false`，`bar()` 将返回默认值，`example_name_case()` 将返回 `EXAMPLE_NAME_NOT_SET`。

### Map 字段

对于以下 Map 字段定义：

```proto
map<int32, int32> weight = 1;
```

编译器将生成以下访问器方法：

- `const google::protobuf::Map<int32, int32>& weight()`：返回一个不可变的 Map。
- `google::protobuf::Map<int32, int32>* mutable_weight()`：返回一个可变的 Map。

`google::protobuf::Map` 是 Protocol Buffers 中用于存储 Map 字段的特殊容器类型。从其接口可以看出，它使用了 `std::map` 和 `std::unordered_map` 常用的一个子集方法。

**注意**：这些 Map 是无序的。

以下是 `google::protobuf::Map` 的模板类定义：

```cpp
template<typename Key, typename T> {
  class Map {
    // 成员类型
    typedef Key key_type;
    typedef T mapped_type;
    typedef MapPair<Key, T> value_type;

    // 迭代器
    iterator begin();
    const_iterator begin() const;
    const_iterator cbegin() const;
    iterator end();
    const_iterator end() const;
    const_iterator cend() const;

    // 容量
    int size() const;
    bool empty() const;

    // 元素访问
    T& operator[](const Key& key);
    const T& at(const Key& key) const;
    T& at(const Key& key);

    // 查找
    bool contains(const Key& key) const;
    int count(const Key& key) const;
    const_iterator find(const Key& key) const;
    iterator find(const Key& key);

    // 修改器
    pair<iterator, bool> insert(const value_type& value);
    template<class InputIt>
    void insert(InputIt first, InputIt last);
    size_type erase(const Key& key);
    iterator erase(const_iterator pos);
    iterator erase(const_iterator first, const_iterator last);
    void clear();

    // 复制
    Map(const Map& other);
    Map& operator=(const Map& other);
  };
}
```

**添加数据的最简单方法**是使用普通的 Map 语法，例如：

```cpp
std::unique_ptr<ProtoName> my_enclosing_proto(new ProtoName);
(*my_enclosing_proto->mutable_weight())[my_key] = my_value;
```

`pair<iterator, bool> insert(const value_type& value)` 方法会隐式地导致 `value_type` 实例的深拷贝。将新值插入到 `google::protobuf::Map` 中的最高效方法如下：

```cpp
T& operator[](const Key& key); // map[new_key] = new_mapped;
```

**使用 `google::protobuf::Map` 与标准 Map**

`google::protobuf::Map` 支持与 `std::map` 和 `std::unordered_map` 相同的迭代器 API。如果你不想直接使用 `google::protobuf::Map`，可以通过以下方式将 `google::protobuf::Map` 转换为标准 Map：

```cpp
std::map<int32, int32> standard_map(message.weight().begin(), message.weight().end());
```

请注意，这将对整个 Map 进行深拷贝。

你也可以通过以下方式从标准 Map 构造一个 `google::protobuf::Map`：

```cpp
google::protobuf::Map<int32, int32> weight(standard_map.begin(), standard_map.end());
```

**解析未知值**

在传输过程中，`.proto` Map 等同于每个键值对的 Map 条目消息，而 Map 本身是一个重复字段的 Map 条目。与普通消息类型一样，解析的 Map 条目消息可能包含未知字段，例如在定义为 `map<int32, string>` 的 Map 中出现 `int64` 类型的字段。

如果在 Map 条目的线缆格式中存在未知字段，这些字段将被丢弃。

如果在 Map 条目的线缆格式中存在未知枚举值，proto2 和 proto3 的处理方式不同。在 proto2 中，整个 Map 条目消息将被放入包含消息的未知字段集中。在 proto3 中，它将被当作已知枚举值放入 Map 字段中。

### Any 字段

给定一个 `Any` 字段，例如：

```proto
import "google/protobuf/any.proto";

message ErrorStatus {
  string message = 1;
  google.protobuf.Any details = 2;
}
```

在生成的代码中，`details` 字段的 getter 方法返回一个 `google::protobuf::Any` 类型的实例。此类型提供了以下特殊方法来打包和解包 `Any` 的值：

```cpp
class Any {
 public:
  // 使用默认类型 URL 前缀 “type.googleapis.com” 将给定消息打包到此 Any 中。
  // 如果序列化消息失败，则返回 false。
  bool PackFrom(const google::protobuf::Message& message);

  // 使用给定的类型 URL 前缀将给定消息打包到此 Any 中。
  // 如果序列化消息失败，则返回 false。
 bool PackFrom(const google::protobuf::Message& message, ::absl::string_view type_url_prefix);

  // 将此 Any 解包到一个 Message 中。如果此 Any 表示的是不同的 Protobuf 类型，或者解析失败，则返回 false。
  bool UnpackTo(google::protobuf::Message* message) const;

  // 如果此 Any 表示给定的 Protobuf 类型，则返回 true。
  template<typename T> bool Is() const;
};
```

### Oneof 字段

给定一个 `oneof` 定义，例如：

```proto
oneof example_name {
    int32 foo_int = 4;
    string foo_string = 9;
    ...
}
```

编译器将生成以下 C++ 枚举类型：

```cpp
enum ExampleNameCase {
  kFooInt = 4,
  kFooString = 9,
  EXAMPLE_NAME_NOT_SET = 0
};
```

此外，它还会生成以下方法：

- `ExampleNameCase example_name_case() const`：返回一个枚举值，指示当前设置的是哪个字段。如果没有字段被设置，则返回 `EXAMPLE_NAME_NOT_SET`。
- `void clear_example_name()`：如果 `oneof` 字段使用指针（消息或字符串），则释放该对象，并将 `oneof` 状态设置为 `EXAMPLE_NAME_NOT_SET`。

### 枚举类型

**注意**：从 2024 版本开始，某些特性设置下可能会生成 `string_view` API。有关详细信息，请参阅 [枚举名称助手](https://protobuf.dev/reference/cpp/cpp-generated/#enum-name-helper)。

给定一个枚举定义，例如：

```proto
enum Foo {
  VALUE_A = 0;
  VALUE_B = 5;
  VALUE_C = 1234;
}
```

Protocol Buffers 编译器将生成一个名为 `Foo` 的 C++ 枚举类型，并包含相同的值集合。此外，编译器还会生成以下函数：

- `const EnumDescriptor* Foo_descriptor()`：返回该类型的描述符，其中包含此枚举类型定义的所有值的信息。
- `bool Foo_IsValid(int value)`：如果给定的数值与 `Foo` 中定义的值之一匹配，则返回 `true`。在上面的例子中，如果输入是 `0`、`5` 或 `1234`，则会返回 `true`。
- `const string& Foo_Name(int value)`：返回给定数值对应的名称。如果不存在这样的值，则返回空字符串。如果多个值具有相同的编号，则返回第一个定义的值。在上面的例子中，`Foo_Name(5)` 将返回 `"VALUE_B"`。
- `bool Foo_Parse(::absl::string_view name, Foo* value)`：如果 `name` 是此枚举的有效值名称，则将其赋值给 `value` 并返回 `true`。否则返回 `false`。在上面的例子中，`Foo_Parse("VALUE_C", &some_foo)` 将返回 `true`，并将 `some_foo` 设置为 `1234`。
- `const Foo Foo_MIN`：枚举类型中最小的有效值（在示例中为 `VALUE_A`）。
- `const Foo Foo_MAX`：枚举类型中最大的有效值（在示例中为 `VALUE_C`）。
- `const int Foo_ARRAYSIZE`：始终定义为 `Foo_MAX + 1`。

**注意事项**：
- 在 proto2 中，将整数强制转换为枚举值时，该整数必须是该枚举的有效值之一，否则结果可能是未定义的。如果不确定，可以使用生成的 `Foo_IsValid()` 函数来测试转换是否有效。将无效的枚举值设置到 proto2 消息的枚举字段中可能会导致断言失败。如果在解析 proto2 消息时读取到无效的枚举值，它将被视为未知字段。
- 在 proto3 中，将任何整数强制转换为枚举值都是安全的，只要它能适应 `int32`。解析 proto3 消息时，无效的枚举值也会被保留，并且可以通过枚举字段访问器返回。
- 在使用 proto3 枚举的 `switch` 语句时需要格外小心。proto3 枚举是开放的枚举类型，可能包含超出指定符号范围的值。解析 proto3 消息时，未识别的枚举值也会被保留，并且可以通过枚举字段访问器返回。如果 `switch` 语句没有默认分支，即使列出了所有已知字段，也可能无法捕获所有情况。这可能导致意外行为，包括数据损坏和运行时崩溃。始终添加默认分支，或者在 `switch` 外显式调用 `Foo_IsValid(int)` 来处理未知枚举值。

你可以将枚举定义在消息类型内部。在这种情况下，Protocol Buffers 编译器会生成代码，使其看起来像是枚举类型确实被声明为嵌套在消息类内部。`Foo_descriptor()` 和 `Foo_IsValid()` 函数被声明为静态方法。实际上，枚举类型本身及其值是在全局作用域中声明的，带有混淆的名称，并通过 `typedef` 和一系列常量定义导入到类的作用域中。这仅仅是为了绕过声明顺序问题。不要依赖这些混淆的顶层名称；假设枚举确实嵌套在消息类中。

### 扩展（仅 proto2）

给定一个带有扩展范围的消息：

```proto
message Foo {
  extensions 100 to 199;
}
```

Protocol Buffers 编译器将为 `Foo` 生成一些额外的方法：`HasExtension()`、`ExtensionSize()`、`ClearExtension()`、`GetExtension()`、`SetExtension()`、`MutableExtension()`、`AddExtension()`、`SetAllocatedExtension()` 和 `ReleaseExtension()`。这些方法的第一个参数都是一个扩展标识符（下文描述），用于标识扩展字段。其余参数和返回值与为普通（非扩展）字段生成的访问器方法完全相同（`GetExtension()` 对应于没有特殊前缀的访问器）。

给定一个扩展定义：

```proto
extend Foo {
  optional int32 bar = 123;
  repeated int32 repeated_bar = 124;
  optional Bar message_bar = 125;
}
```

对于单一扩展字段 `bar`，Protocol Buffers 编译器会生成一个名为 `bar` 的“扩展标识符”，你可以使用它与 `Foo` 的扩展访问器一起访问该扩展字段，例如：

```cpp
Foo foo;
assert(!foo.HasExtension(bar));
foo.SetExtension(bar, 1);
assert(foo.HasExtension(bar));
assert(foo.GetExtension(bar) == 1);
foo.ClearExtension(bar);
assert(!foo.HasExtension(bar));
```

对于消息扩展字段 `message_bar`，如果该字段未设置，`foo.GetExtension(message_bar)` 将返回一个未设置任何字段的 `Bar`（可能是 `Bar::default_instance()`）。

同样，对于重复扩展字段 `repeated_bar`，编译器会生成一个名为 `repeated_bar` 的扩展标识符，你也可以使用它与 `Foo` 的扩展访问器一起使用：

```cpp
Foo foo;
for (int i = 0; i < kSize; ++i) {
  foo.AddExtension(repeated_bar, i);
}
assert(foo.ExtensionSize(repeated_bar) == kSize);
for (int i = 0; i < kSize; ++i) {
  assert(foo.GetExtension(repeated_bar, i) == i);
}
```

（扩展标识符的确切实现比较复杂，涉及模板的神奇用法——不过，你不需要了解扩展标识符的工作原理即可使用它们。）

扩展可以声明在另一个类型的内部。例如，一个常见的模式是像这样操作：

```proto
message Baz {
  extend Foo {
    optional Baz foo_ext = 124;
  }
}
```

在这种情况下，扩展标识符 `foo_ext` 被声明为嵌套在 `Baz` 内部。它可以这样使用：

```cpp
Foo foo;
Baz* baz = foo.MutableExtension(Baz::foo_ext);
FillInMyBaz(baz);
```

### 竞态分配

竞态分配是 C++ 特有的一个功能，它可以帮助你在使用 Protocol Buffers 时优化内存使用并提高性能。在 `.proto` 文件中启用竞态分配会为你的 C++ 生成代码添加与竞态相关的额外代码。有关竞态分配 API 的更多信息，请参阅 [竞态分配指南](https://protobuf.dev/reference/cpp/cpp-generated/#arena-allocation)。

### 服务

如果 `.proto` 文件包含以下行：

```proto
option cc_generic_services = true;
```

那么 Protocol Buffers 编译器将根据文件中的服务定义生成代码，如本节所述。然而，生成的代码可能并不理想，因为它与任何特定的 RPC 系统都不相关，因此需要比针对一个系统的代码更多层次的间接调用。如果你**不希望**生成此代码，请在文件中添加以下行：

```proto
option cc_generic_services = false;
```

如果未给出上述任一行，则该选项默认为 `false`，因为通用服务已被废弃。（注意，在 2.4.0 版本之前，默认值为 `true`。）

基于 `.proto` 语言服务定义的 RPC 系统应该提供插件来生成适用于该系统的代码。这些插件可能需要禁用抽象服务，以便它们可以生成同名的自己的类。

以下部分描述了当启用抽象服务时，Protocol Buffers 编译器会生成什么内容。

#### 接口

给定一个服务定义：

```proto
service Foo {
  rpc Bar(FooRequest) returns(FooResponse);
}
```

Protocol Buffers 编译器将生成一个名为 `Foo` 的类来表示此服务。`Foo` 将为服务定义中定义的每个方法生成一个虚方法。在本例中，`Bar` 方法被定义为：

```cpp
virtual void Bar(RpcController* controller, const FooRequest* request,
                 FooResponse* response, Closure* done);
```

参数与 `Service::CallMethod()` 的参数等效，只是方法参数是隐式的，而 `request` 和 `response` 指定了它们的确切类型。

这些生成的方法是虚方法，但不是纯虚方法。默认实现只是调用 `controller->SetFailed()`，并附带一条错误消息，指出该方法未实现，然后调用 `done` 回调。当你实现自己的服务时，你必须继承这个生成的服务类，并根据需要实现它的方法。

`Foo` 继承自 `Service` 接口。Protocol Buffers 编译器自动生成 `Service` 的方法实现如下：

- `GetDescriptor`：返回服务的 `ServiceDescriptor`。
- `CallMethod`：根据提供的方法描述符确定正在调用的方法，并直接调用它，将请求和响应消息对象向下转型为正确类型。
- `GetRequestPrototype` 和 `GetResponsePrototype`：返回给定方法的请求或响应的默认实例。

还生成以下静态方法：

```cpp
static ServiceDescriptor descriptor();
```

返回类型的描述符，其中包含有关该服务有哪些方法以及它们的输入和输出类型的信息。

#### 代理

Protocol Buffers 编译器还会为每个服务接口生成一个“代理”实现，客户端可以使用它向实现该服务的服务器发送请求。对于上面的 `Foo` 服务，将定义代理实现 `Foo_Stub`。与嵌套消息类型一样，使用 `typedef`，因此 `Foo_Stub` 也可以称为 `Foo::Stub`。

`Foo_Stub` 是 `Foo` 的一个子类，还实现了以下方法：

```cpp
Foo_Stub(RpcChannel* channel);
Foo_Stub(RpcChannel* channel, ChannelOwnership ownership);
```

构造一个在给定通道上发送请求的新代理。如果 `ownership` 是 `Service::STUB_OWNS_CHANNEL`，则当代理对象被删除时，它也会删除通道。

```cpp
RpcChannel* channel();
```

返回代理的通道，即传递给构造函数的通道。

代理还实现了服务的每个方法，作为通道的包装器。调用其中的一个方法只是简单地调用 `channel->CallMethod()`。

Protocol Buffers 库不包含 RPC 实现。但是，它提供了所有必要的工具，以便你可以将生成的服务类连接到你选择的任意 RPC 实现。你只需要提供 `RpcChannel` 和 `RpcController` 的实现。更多详细信息，请参阅 `service.h` 的文档。

### 插件插入点

代码生成器插件如果希望扩展 C++ 代码生成器的输出，可以使用以下插入点名称插入以下类型的代码。每个插入点都出现在 `.pb.cc` 文件和 `.pb.h` 文件中，除非另有说明。

- `includes`：包含指令。
- `namespace_scope`：属于文件的包/命名空间，但不属于任何特定类的声明。出现在所有其他命名空间范围代码之后。
- `global_scope`：属于文件顶级、命名空间外部的声明。出现在文件的最末尾。
- `class_scope:TYPENAME`：属于消息类的成员声明。`TYPENAME` 是完整的 proto 名称，例如 `package.MessageType`。出现在类中所有其他公共声明之后。此插入点仅出现在 `.pb.h` 文件中。

不要生成依赖于标准代码生成器声明的私有类成员的代码，因为这些实现细节可能会在未来版本的 Protocol Buffers 中发生变化。