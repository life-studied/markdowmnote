---
create: 2023-07-23
---
# regex

## 1. 简介

​	C++有一个标准库提供了对正则表达式的支持。这个库叫做`<regex>`，它包含了一些类和函数，可以用于匹配、搜索、替换和分割字符串。

以下是一些常用的`<regex>`库中的类和函数：

- `std::regex`：表示一个正则表达式对象。
- `std::smatch`：保存匹配结果的对象。
- `std::regex_match()`：检查整个字符串是否与正则表达式匹配。
- `std::regex_search()`：在字符串中搜索满足正则表达式的子串。
- `std::regex_replace()`：用指定的替换字符串替换正则表达式匹配到的子串。
- `std::regex_iterator`：迭代器类，用于遍历字符串中的正则表达式匹配项。

## 2. 简单示例

下面是一个简单的示例，演示了如何使用正则表达式库来匹配电话号码：

```cpp
#include <iostream>
#include <regex>

int main() {
    std::string input = "My phone number is 123-456-7890.";
    std::regex regex("\\d{3}-\\d{3}-\\d{4}"); // 匹配###-###-####格式的电话号码

    std::smatch match;
    if (std::regex_search(input, match, regex)) {
        std::cout << "Phone number found: " << match.str() << std::endl;
    } else {
        std::cout << "No phone number found." << std::endl;
    }

    return 0;
}
```

​	上述代码使用正则表达式`\\d{3}-\\d{3}-\\d{4}`匹配字符串中的电话号码，并打印出匹配到的电话号码。请注意，在C++中，由于反斜杠在字符串中是转义字符，所以需要使用双反斜杠来表示一个普通的反斜杠。

## 3.详细介绍

`<regex>`库是C++11引入的，它提供了对正则表达式的支持。使用这个库，你可以进行字符串的匹配、搜索、替换和分割等操作。

以下是该库中一些重要的类和函数：

- `std::regex`：表示一个正则表达式对象。你可以使用构造函数创建一个正则表达式实例，并且还可以使用正则表达式字面值（例如`R"(\d{3}-\d{3}-\d{4})"`）来创建一个正则表达式。
  ```cpp
  std::regex regex("\\d{3}-\\d{3}-\\d{4}"); // 创建一个正则表达式对象来匹配###-###-####格式的电话号码
  ```

- `std::smatch`：保存匹配结果的对象。当你使用正则表达式对一个字符串进行匹配或搜索时，匹配结果将存储在`std::smatch`对象中。你可以通过索引或迭代器来访问匹配到的子串。
  ```cpp
  std::string input = "My phone number is 123-456-7890.";
  std::regex regex("\\d{3}-\\d{3}-\\d{4}");
  std::smatch match;
  
  if (std::regex_search(input, match, regex)) {
      std::cout << "Phone number found: " << match.str() << std::endl;
  }
  ```

- `std::regex_match()`：用于检查整个字符串是否与正则表达式完全匹配。
  ```cpp
  std::string input = "123-456-7890";
  std::regex regex("\\d{3}-\\d{3}-\\d{4}");
  
  if (std::regex_match(input, regex)) {
      std::cout << "The string is a valid phone number." << std::endl;
  }
  ```

- `std::regex_search()`：在字符串中搜索满足正则表达式的子串。它会返回第一个匹配到的子串和它的位置。
  ```cpp
  std::string input = "My phone number is 123-456-7890.";
  std::regex regex("\\d{3}-\\d{3}-\\d{4}");
  std::smatch match;
  
  if (std::regex_search(input, match, regex)) {
      std::cout << "Phone number found: " << match.str() << std::endl;
  }
  ```

- `std::regex_replace()`：用指定的替换字符串替换正则表达式匹配到的子串。它可以替换所有匹配项或只替换第一个匹配项。
  ```cpp
  std::string input = "Hello, World!";
  std::regex regex("[aeiou]"); // 匹配所有小写元音字母
  std::string replacement = "*";
  
  std::string result = std::regex_replace(input, regex, replacement);
  std::cout << result << std::endl; // 输出 "H*ll*, W*rld!"
  ```

- `std::regex_iterator`：迭代器类，用于遍历字符串中的正则表达式匹配项。你可以使用它来逐个访问所有匹配到的子串。
  ```cpp
  std::string input = "Hello, World!";
  std::regex regex("\\w+"); // 匹配一个或多个单词字符
  std::sregex_iterator it(input.begin(), input.end(), regex);
  std::sregex_iterator end;
  
  while (it != end) {
      std::cout << it->str() << std::endl;
      ++it;
  }
  ```

## 4. 其它

​	这只是`<regex>`库的一些基本用法，还有其他更高级的功能，例如捕获组、反向引用和自定义正则表达式标记等。可以参考C++的文档或其他资源，以获得更详细的了解和更复杂的用法。