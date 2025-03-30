---
create: '2025-03-30'
modified: '2025-03-30'
---

# C++直接调用Python

## 创建python解释器

```C++
#include <pybind11/pybind11.h>
#include <pybind11/embed.h>

namespace py = pybind11;
using namespace std;

int main() {
    py::scoped_interpreter guard{}; // 初始化 Python 解释器
    
    py::exec(R"(print("Hello, Python!"))");
    
    py::object print = py::eval("print");	// 获取print对象
    print("Hello", "from", "C++");
}
```

## 调用Python

懒得写了，干脆把所有的调用Python的方式写在这里。

```C++
#include <pybind11/pybind11.h>
#include <pybind11/embed.h>

namespace py = pybind11;
using namespace std;

int main() {
    py::scoped_interpreter guard{}; // 初始化 Python 解释器
    
	try {
        // 导入模块并调用函数
        {
            py::object example_module = py::module_::import("example");
            py::object add_function = example_module.attr("add");

            int result = add_function(2, 3).cast<int>();
            std::cout << "Result from Python: " << result << std::endl;
        }
        
        // 创建类对象
        {
            // 导入模块
            py::object my_module = py::module_::import("my_module");
            // 获取类
            py::object MyClass = my_module.attr("MyClass");
            // 创建类的实例
            py::object my_instance = MyClass(42);

            // 调用实例的方法
            int value = my_instance.attr("get_value")().cast<int>();
            std::cout << "Value from Python instance: " << value << std::endl;
        }
        
        // 创建python基本类型对象
        {
            // 导入 copy 模块
            py::object copy_module = py::module_::import("copy");
            py::object original_obj = py::dict("key"_a = "value");
            // 使用 copy.copy() 复制对象
            py::object copied_obj = copy_module.attr("copy")(original_obj);

            // 修改原始对象，验证复制对象是否独立
            original_obj["key"] = "new_value";

            // 打印结果
            std::cout << "Original object: " << original_obj["key"].cast<std::string>() << std::endl;
            std::cout << "Copied object: " << copied_obj["key"].cast<std::string>() << std::endl;
        }
        
    } catch (const std::exception& e) {
        std::cerr << e.what() << std::endl;
    }
}
```