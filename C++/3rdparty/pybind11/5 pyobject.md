---
create: '2025-01-05'
modified: '2025-01-05'
---

# pyobject

这一章所有内容都可以通过查看`pybind11/pytypes.h`源码获取信息。

## py::object

`py::object` 类是 `pybind11` 中用于管理 Python 对象引用的一个包装类，也是所有 Python 类型在C++中的基类（例如`py::list`）。它继承自 handle 类，并在其基础上增加了引用计数自动管理功能（拷贝、移动、析构时自动管理）。

object本身不存储数据，数据存储在基类的 handle 中，是一个 PyObject 的指针（`PyObject *m_ptr = nullptr;`）。

## py::handle

`py::handle` 类是 `pybind11` 中用于管理 Python 对象引用的一个基础类。它是一个轻量级的包装器，围绕一个任意的 Python 对象（即 Python C API 中的 `PyObject *`）提供基本的 C++ 接口。与 `object` 类不同，handle 类不执行任何自动引用计数管理，只提供引用计数增减的接口。

`py::handle` 类还提供了一个cast模板成员函数，用于将底层的 `PyObject*` 转换为给定的C++类型（因此所有 Python 类型都继承了这个转换功能），失败则会抛出 `cast_error` 异常：

```C++
template <typename T>
T handle::cast() const {
    return pybind11::cast<T>(*this);
}
```

## 常见的Python对象（in C++）

pybind11提供了Python中常见对象在C++中的包装类。所有主要的Python类型通过简单C++类封装公开出来了，可以当做参数参数来使用。包括： `handle`, `object`, `bool_`, `int_`, `float_`, `str`, `bytes`, `tuple`, `list`, `dict`, `slice`, `none`, `capsule`, `iterable`, `iterator`, `function`, `buffer`, `array`, 和`array_t`.

## py::list

对应于 Python 中的 list。

```C++
void add_list(py::list my_list) {
    // 可读可写（通过[]运算符重载）
    for (size_t i = 0; i < my_list.size(); ++i)
        my_list[i] = my_list[i].cast<int>() + 1;		// 每个成员 => 任意类型(int,double,...)
    
    // 只可读（通过迭代器）
    for (auto & i : my_list)
        std::cout << i << std::endl;
    
    // 其它常用操作
    
    // 创建一个指定大小的 py::list
    py::list my_list_with_size(5);

    // 获取列表的大小
    size_t size = my_list.size();

    // 检查列表是否为空
    bool is_empty = my_list.empty();
    
    // 向列表末尾添加元素
    my_list.append(py::int_(10));

    // 在指定位置插入元素
    my_list.insert(1, py::int_(20));

    // 清空列表
    my_list.clear();
}
```

## py::dict

```C++
py::dict dict_example() {
    // 创建一个空的 py::dict
    py::dict my_dict;

    // 使用关键字参数创建 py::dict
    using namespace pybind11::literals;
    py::dict my_dict_with_args("key1"_a = 1, "key2"_a = 2);

    // 获取字典的大小
    size_t size = my_dict.size();

    // 检查字典是否为空
    bool is_empty = my_dict.empty();

    // 迭代字典元素
    for (auto it = my_dict.begin(); it != my_dict.end(); ++it) {
        std::cout << "Key: " << it->first.cast<std::string>() << ", Value: " << it->second.cast<int>() << std::endl;
    }

    // 清空字典
    my_dict.clear();

    // 检查字典是否包含某个键
    bool contains_key = my_dict.contains("key1");

    // 向字典添加元素
    my_dict["key1"] = py::int_(42);
    my_dict[py::int_(2)] = py::int_(43);

    // 访问字典元素
    py::object value = my_dict["key1"];

    // 删除字典元素（没有提供删除接口，但是可以通过attr来调用Python中的函数接口）
    auto discarded_val = my_dict.attr("pop")("key1");

    return my_dict;
}
```

## 附录

### C++ 类型封装表

| C++ 类型封装 | Python 类型                  |
| ------------ | ---------------------------- |
| handle       | 无直接对应，用于底层对象处理 |
| object       | object                       |
| bool_        | bool                         |
| int_         | int                          |
| float_       | float                        |
| str          | str                          |
| bytes        | bytes                        |
| tuple        | tuple                        |
| list         | list                         |
| dict         | dict                         |
| slice        | slice                        |
| none         | None                         |
| capsule      | ctypes.capsule               |
| iterable     | iterable                     |
| iterator     | iterator                     |
| function     | function                     |
| buffer       | buffer                       |
| array        | array                        |
| array_t      | numpy.ndarray                |

请注意，`handle` 类型在 Python 中没有直接对应，它通常用于底层的对象处理，而 `capsule` 类型在 Python 中对应于 `ctypes.capsule`，这是一个用于封装 C 数据的类型。其他类型则直接对应于 Python 中的同名类型。

## 参考资料

* [12. Python C++接口 - 《pybind11 中文文档》 - 极客文档](https://geekdaxue.co/read/pybind11-CN/12-Python-C++-interface.md)
* [Python 类型 - pybind11 documentation](https://pybind11.readthedocs.io/en/stable/advanced/pycpp/object.html)