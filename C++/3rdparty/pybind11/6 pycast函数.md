---
create: '2025-01-05'
modified: '2025-01-05'
---

# py::cast函数

## quick view

```C++
#include <pybind11/pybind11.h>
#include <pybind11/stl.h>
#include <pybind11/functional.h>
#include <vector>
#include <map>
#include <tuple>
#include <memory>

namespace py = pybind11;

struct MyStruct {
    int value;
    
    MyStruct(int value) : value(value) {py::print("value: create MyStruct instance");} // 构造函数
    MyStruct() : value(0) {py::print("default: create MyStruct instance");} // 默认构造函数
    MyStruct(const MyStruct& other) : value(other.value) {py::print("copy MyStruct instance");} // 拷贝构造函数
    MyStruct(MyStruct&& other) : value(other.value) {py::print("move MyStruct instance");} // 移动构造函数
    MyStruct& operator=(const MyStruct& other) {value = other.value; py::print("assign MyStruct instance"); return *this;} // 赋值运算符
    
    ~MyStruct() {py::print("delete MyStruct instance");} // 析构函数
};

PYBIND11_MODULE(example, m) {
    m.doc() = "pybind11 cast example plugin";

    // 如果不指定接管类型（智能指针）,pybind11自动选择std::unique_ptr
    // 注意，每个类仅能与一个持有者类型关联。如果一个类已经与std::shared_ptr关联，那么不能再与std::unique_ptr关联
    py::class_<MyStruct, std::shared_ptr<MyStruct>>(m, "MyClass")
        .def(py::init<>())
        .def_readwrite("value", &MyStruct::value);

    // py::cast可能的某个场景，混合编程
    m.def("append_to_list", [](py::list list_to_append) {
        MyStruct *cls = new MyStruct(42);
		py::object obj = py::cast(cls);
        list_to_append.append(obj);
        // 其实直接丢进去也行：list_to_append.append(cls);
    });
    m.def("delete_from_list", [](py::list list_to_delete) {
        py::object obj = list_to_delete[0];
        MyStruct *cls = py::cast<MyStruct *>(obj);	// 或者是obj.cast<MyStruct *>()
        delete cls;
    });
    
    // 可以但没必要的做法：pybind11会自动进行隐式类型转换；py::cast()应该用于C++函数体内的混合编程，将C++类型快速转换为Python类型
    m.def("cast_int", []() { return py::cast(42); });
    m.def("cast_double", []() { return py::cast(3.14); });
    m.def("cast_bool", []() { return py::cast(true); });
    m.def("cast_string", []() { return py::cast(std::string("hello")); });
    m.def("cast_vector", []() { return py::cast(std::vector<int>{1, 2, 3}); });
    m.def("cast_map", []() { return py::cast(std::map<std::string, int>{{"one", 1}, {"two", 2}}); });
    m.def("cast_tuple", []() { return py::cast(std::make_tuple(1, 2.0, "three")); });
    m.def("cast_pair", []() { return py::cast(std::make_pair(1, "one")); });
    m.def("cast_shared_ptr", []() { return py::cast(std::make_shared<MyStruct>()); });
    
    // 应该这么做：直接返回类型，让pybind11自动接管隐式类型转换
    m.def("test_shared_ptr", []() { return std::make_shared<MyStruct>(); });
    // 其它同理
    // ...
}
```

## 附录

## py::cast=>类型转换表

`py::cast` 函数可以支持多种 C++ 类型进行转换，并将其转换为相应的 Python 类型。用于C++代码中进行混合编程。

以下是一些常见的 C++ 类型及其转换后的 Python 类型：

| C++ 类型                               | 转换后的 Python 类型       | 说明 |
| :------------------------------------- | :------------------------- | ---- |
| `int`                                  | `int`                      |      |
| `double`                               | `float`                    |      |
| `bool`                                 | `bool`                     |      |
| `std::string`                          | `str`                      |      |
| `const char*`                          | `str`                      |      |
| `std::vector<T>`                       | `list`                     |      |
| `std::map<Key, Value>`                 | `dict`                     |      |
| `std::tuple<Args...>`                  | `tuple`                    |      |
| `std::pair<First, Second>`             | `tuple`                    |      |
| `std::shared_ptr<T>`                   | `object` 指向解引用的数据T |      |
| `std::unique_ptr<T>`                   | `object` 指向解引用的数据T |      |
| `T*`                                   | `object` 指向解引用的数据T |      |
| 自定义类类型（通过 `py::class_` 绑定） | `object` (自定义类型)      |      |

### 解释

- 基本类型：
  - `int`、`double`、`bool` 等基本类型会直接转换为相应的 Python 基本类型。
- 字符串类型：
  - `std::string` 和 `const char*` 会转换为 Python 的 `str` 类型。
- 容器类型：
  - `std::vector<T>` 会转换为 Python 的 `list` 类型。
  - `std::map<Key, Value>` 会转换为 Python 的 `dict` 类型。
  - `std::tuple<Args...>` 会转换为 Python 的 `tuple` 类型。
  - `std::pair<First, Second>` 会转换为 Python 的 `tuple` 类型。
- 智能指针类型：
  - `std::shared_ptr<T>` 和 `std::unique_ptr<T>` 会转换为 Python 的 `object` 类型，内部引用指向数据块T，通常用于自定义类型（需要通过`py::class_`定义）。
- 自定义类类型：
  - 自定义类类型需要通过 `py::class_` 进行绑定，转换后会成为 Python 的 `object` 类型。

## py::cast=>返回值策略

以下是根据不同的返回值策略和类型选择实际策略的规则表格（默认首选`return_value_policy::automatic_reference`，这意味着由C++管理生命周期）：

| 返回值策略                                 | T 类型条件                           | 实际使用的策略                        |
| :----------------------------------------- | :----------------------------------- | :------------------------------------ |
| `return_value_policy::automatic`           | `std::is_pointer<no_ref_T>::value`   | `return_value_policy::take_ownership` |
| `return_value_policy::automatic`           | `std::is_lvalue_reference<T>::value` | `return_value_policy::copy`           |
| `return_value_policy::automatic`           | 其他情况                             | `return_value_policy::move`           |
|                                            |                                      |                                       |
| `return_value_policy::automatic_reference` | `std::is_pointer<no_ref_T>::value`   | `return_value_policy::reference`      |
| `return_value_policy::automatic_reference` | `std::is_lvalue_reference<T>::value` | `return_value_policy::copy`           |
| `return_value_policy::automatic_reference` | 其他情况                             | `return_value_policy::move`           |

### 解释

- `return_value_policy::automatic`：
  - 如果 `T` 是指针类型，则使用 `return_value_policy::take_ownership`。
  - 如果 `T` 是左值引用类型，则使用 `return_value_policy::copy`。
  - 其他情况使用 `return_value_policy::move`。
- `return_value_policy::automatic_reference`：
  - 如果 `T` 是指针类型，则使用 `return_value_policy::reference`。
  - 如果 `T` 是左值引用类型，则使用 `return_value_policy::copy`。
  - 其他情况使用 `return_value_policy::move`。

## 参考资料

* [10. 智能指针 - 《pybind11 中文文档》 - 极客文档](https://geekdaxue.co/read/pybind11-CN/Smart-Pointers.md)
* [官方文档：C++与Python互相转换：用于混合编程](https://pybind11.readthedocs.io/en/stable/advanced/pycpp/object.html#casting-back-and-forth)