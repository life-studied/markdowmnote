---
create: '2025-01-03'
modified: '2025-01-03'
---

# PYBIND11_MODULE

pybind11最核心的宏。

```C++
#include <pybind11/pybind11.h>

int add(int i, int j) {
    return i + j;
}

PYBIND11_MODULE(HelloWorld, m) {
    m.doc() = "pybind11 example plugin"; // optional module docstring

    m.def("add", &add, "A function that adds two numbers");
}
```

* \#define PYBIND11_MODULE(name, variable, ...)  

  无须关心宏内部定义了一堆东西，只用知道**最后定义了一个函数头，和后面的函数体拼接起来形成了一个函数**即可

  * name：

    * 模块名，用于标注导出的模块名称

    * 这个名称会在python中得到使用（`import HelloWorld`），因此需要一致

      ```python
      import HelloWorld as hw
      
      hw.add(1,2)
      ```

  * variable：

    * 变量名，实际上是函数的参数名，代表模块对象在C++中的形参。

    * 这个名字可以任意取，只要在函数内也保持一致就好，比如下面改成`aaaa`：

      ```C++
      PYBIND11_MODULE(HelloWorld, aaaa) {
          aaaa.doc() = "pybind11 example plugin"; // optional module docstring
      
          aaaa.def("add", &add, "A function that adds two numbers");
      }
      ```