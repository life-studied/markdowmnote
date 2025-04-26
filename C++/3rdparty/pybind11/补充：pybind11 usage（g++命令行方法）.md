---
create: '2025-04-25'
modified: '2025-04-25'
---

# 补充：pybind11 usage（g++命令行方法）

这个方法用于简单的C++使用pybind11去进行开发，或者在不方便使用cmake环境等情况下。

可以直接调用g++命令行去编译出C++ pybind11的so。

## 案例（Linux）

在很多情况下，我们需要调试python和C++的混合程序。这个时候如果想要快速生成一个带调试信息的so，就可以使用g++直接build：

```shell
g++ -g -Wall -std=c++17 -fPIC $(python3.6 -m pybind11 --include) example.cpp -o example$(python3.11-config --extension-suffix)
```

上面需要在linux上安装python-dev以保证能正常使用`python3.11-config`命令。

## Win

在win上的python的扩展名应为`.pyd`，（linux上为`.so`）。

```shell
g++ -g -Wall -std=c++17 -fPIC $(python -m pybind11 --include) example.cpp -o example.pyd
```

输出的pyd名字应该是`module.$(python3.11-config --extension-suffix).pyd`或者是简化版本的`module.pyd`，其它名字在我的测试下似乎不行。

例如：`HelloWorld.pyd`或者`HelloWorld.cp311-win_amd64.pyd`，后者是pybind11自己生成的命名，我没在Windows上为什么叫这个名字的规则。