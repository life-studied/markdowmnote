# TEST宏

>本页列出了 GoogleTest 提供的用于编写测试程序的工具。 要使用它们，请添加 .`#include <gtest/gtest.h>`

## TEST

​	使用`TEST()`宏创建测试套件，它是一个不带返回值的C++函数

* TestSuiteName：测试套件名称，用于命名一组测试
* TestName：当前测试名称，用于命名当前测试
* 上述两个参数应以C++标准进行命名，并要求不带`_`

```C++
TEST(TestSuiteName, TestName) {
  ... statements ...
}
```

## TEST_F

​	使用`TEST_F()`宏创建测试套件，它是一个不带返回值的C++成员函数，基于`TestFixtureName`类。

```
TEST_F(TestFixtureName, TestName) {
  ... statements ...
}
```

## TEST_P