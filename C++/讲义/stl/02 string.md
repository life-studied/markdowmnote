# string

​	string是C++中用于处理字符串的类。

## 1. quick to start

```C++
#include <string>

int main()
{
    std::string s1{"hello"};
    std::string s2{"world"};
    std::string s = s1 + ' '  +  s2;
    
    size_t index = s.find("hello");
    
    if(index != std::string::npos)
    {
        std::cout << "hello is at " << index << " position" << '\n';
    }
        
}
```

## 2. 遍历

​	string作为STL的容器之一，也是有迭代器的。一样可以通过begin()和end()来获取。

​	但是不常用。一般最常用的就是通过下标去遍历。如果有时候不需要用到下标，还可以用C++11的for-loop语法糖。

```C++
for(int i = 0; i < s.size(); i++)
{
    // ...
}

for(auto& i : s)
{
    std::cout << i;
}
```

## 3. find

​	string的优点在于，find算法不用自己写，而是内置了一套find的函数族。

​	在string中，有一个特殊的变量：`npos`表示未找到。

#### 1. string的find函数族

* `find`：在一个字符串中，查找一个指定的字符或字符串，返回首次匹配的开始位置
* `find_first_of`：在一个字符串中，查找一个字符组中任一字符，返回首次匹配的开始位置
* `find_last_of`：在一个字符串中，查找一个字符组中任一字符，返回最后一个匹配的位置
* `find_first_not_of`：在一个字符串中，查找与一个字符组中任一字符都不匹配的字符，返回首次不匹配的开始位置
* `find_last_not_of`：在一个字符串中，查找与一个字符组中任一字符都不匹配的字符，返回最后一次不匹配的位置
* `rfind`：find从后往前找

### 2. substr

​	通过substr，可以获取字符串的子字符串。

​	参数：起始下标， 个数

```C++
std::string s = "1234";

s.substr(2, 2);		// 34
```

### 3. 案例：

#### 校验密码合法

​	已知密码必须由大写，小写和数字组成，请校验密码的合法性：

```C++
#include <string>
#include <iostream>

bool isValid(std::string& str)
{
    std::string valid_str = "1234567890";
    for(char ch = 'a'; ch <= 'z'; ch++)
        valid_str += ch;
    
    for(char ch = 'A'; ch <= 'Z'; ch++)
        valid_str += ch;
    
    return str.find_first_not_of(valid_str) == std::string::npos;
}

int main()
{
    std::string password = "jfopanfpasherpofjawpofhHpohJOAPSDIJFOPSNDFOPASDJHFOJWEPFA2134DAS56F7SFDGBA8S9HF08QNNX0392QYNX0Q2976823CN90QNMX231N0NNnr8902q3n9c203m|x30-2497mcn";
    
    auto res = isValid(password);
}
```

#### 分割字符串

​	下面演示了一个例子，分割由分号分隔的单词。

```C++
#pragma once
#include <cstddef>
#include <string>
#include <vector>
#include <iostream>

using std::size_t;
using std::string;
using std::vector;

class RparseTest
{
    vector<string> strings;

public:
    void parseForData()
    {
        string s("now;sense;make;to;going;is;This");

        int last = s.size();
        size_t current = s.rfind(';');

        while (current != string::npos)
        {
            ++current;
            strings.push_back(s.substr(current, last - current));
            current -= 2;
            last = current + 1;
            current = s.rfind(';', current);
        }
        strings.push_back(s.substr(0, last));
    }

    void show()
    {
        for (const auto& i : strings)
        {
            std::cout << i << std::endl;
        }
    }
};

int main(int argc, char const* argv[])
{
    RparseTest t;
    t.parseForData();
    t.show();
    return 0;
}
```

## 4. 反转

​	字符串反转是常见的题目，与其它容器一样，一般用reverse来操作。

```C++
#include <string>
#include <iostream>

int main()
{
	std::string s = "1234";

	reverse(s.begin(), s.end());
	std::cout << s;
}
```



题目：[151. 反转字符串中的单词 - 力扣（LeetCode）](https://leetcode.cn/problems/reverse-words-in-a-string/description/?envType=study-plan-v2&envId=top-interview-150)