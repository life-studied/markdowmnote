---
create: 2023-07-22
---
## fmt库教程

[TOC]

​	关键字：fmt;占位符;format;println;

---

下面是使用fmt库的一些常见步骤：

### 1. 包含头文件：

​	在需要使用fmt库的源文件中，包含以下头文件来访问fmt库的功能：

```cpp
#include <fmt/core.h>
```

### 2. 使用fmt库的格式化函数：

​	fmt库提供了一系列格式化函数，可以根据不同的需求选择合适的函数来格式化输出。以下是一些常用的函数示例：

- `fmt::format()`：将格式化字符串写入到一个字符串变量中，并返回该字符串。例如：
  ```cpp
  std::string result = fmt::format("Hello, {}!", "world");
  ```

- `fmt::print()`：直接将格式化的字符串打印到标准输出。例如：
  
  ```cpp
  fmt::print("The answer is {}.", 42);
  ```
  
- `fmt::sprintf()`：将格式化字符串写入到一个字符数组中。例如：
  
  ```cpp
  char buffer[100];
  std::snprintf(buffer, sizeof(buffer), "The answer is {}.", 42);
  ```
  
- `fmt::println()`：与`fmt::print()`类似，但在输出后会添加换行符。例如：
  ```cpp
  fmt::println("Hello, {}!", "world");
  ```

### 3. 使用格式化占位符：

​	fmt库使用花括号`{}`作为格式化字符串中的占位符，可以在占位符中指定参数的类型和格式。以下是一些常用的占位符示例：

- `{}`：默认占位符，根据参数类型自动选择格式。
- `{0}`、`{1}`、`{2}`等：按照索引引用参数。
- `{:d}`：十进制整数。
- `{:f}`：浮点数。
- `{:s}`：字符串。

​	下面是示例：

```C++
#include <fmt/core.h>

int main() {
  int number = 42;
  double pi = 3.14159;
  std::string name = "Alice";
  
  // 默认占位符
  fmt::print("The number is {}. \n", number);
  
  // 按索引引用参数
  fmt::print("{0} + {1} = {2} \n", 2, 3, 2 + 3);
  
  // 十进制整数
  fmt::print("The answer is {:d}. \n", 42);
  
  // 浮点数
  fmt::print("The value of pi is approximately {:.2f}. \n", pi);
  
  // 字符串
  fmt::print("Hello, {:s}! \n", name);

  return 0;
}
```

