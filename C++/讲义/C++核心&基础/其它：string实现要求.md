---
create: '2025-05-20'
modified: '2025-05-20'
---

# string实现要求

## 1.构造

* 支持字符数组或char指针构造
* 支持复制构造
* 支持移动构造
* 支持赋值构造

## 2.支持运算符

* `[]`运算符
* `+`运算符
* `==`运算符
* `!=`运算符
* `>`运算符
* `<`运算符
* `<<`运算符（标准输出流）
* `>>`运算符（标准输入流）

## 3.支持函数

1. `length()`或者`size()`: 返回字符的数量。
2. `empty()`: 检查字符串是否为空。
3. `clear()`: 清除字符串中的所有字符。
4. `c_str()`: 返回以C风格的字符串表示的`const char*`指针。
5. `substr(pos, len)`: 返回从指定位置开始的指定长度的子字符串。
6. `append(str)`: 在字符串的末尾添加另一个字符串。
7. `insert(pos, str)`: 在指定位置插入另一个字符串。
8. `erase(pos, len)`: 删除指定位置开始的指定个数的字符。
9. `replace(pos, len, str)`: 用另一个字符串替换指定位置开始的指定长度的字符。
10. `find(str, pos)`: 在字符串中查找第一个出现的指定子字符串，并返回其位置。
11. `rfind(str, pos)`: 在字符串中从后向前查找第一个出现的指定子字符串，并返回其位置。
12. `compare(str)`: 将字符串与另一个字符串进行比较，返回比较结果。
13. 补充：`split(str)`：将字符串切割成数个字符串（标准库未提供实现，作为拓展可自行思考如何实现）

## 4.其它拓展技术

1. 高级find实现：
    1. KMP算法（Knuth-Morris-Pratt Algorithm）：KMP算法是一种用于字符串匹配的高效算法。它利用前缀和后缀的信息，避免不必要的回溯操作，从而加快匹配速度。通过使用KMP算法，可以在查找子字符串时减少比较次数，提高效率。
    2. Boyer-Moore算法：Boyer-Moore算法也是一种用于字符串匹配的高效算法。它基于两个启发式规则：坏字符规则和好后缀规则。这些规则可以帮助我们在每一步中跳过更多的比较操作，从而减少匹配时间。
    3. 使用哈希函数：在字符串查找中，可以使用哈希函数来计算子串的哈希值。通过将子串的哈希值与目标字符串中的子串进行比较，可以快速判断是否存在匹配。这样可以减少比较的次数，提高性能。注意，哈希函数应该具有良好的散列性质以减少哈希冲突。
    4. 多线程并行查找：对于较长的字符串，可以考虑使用多线程并行查找的方式来加快搜索速度。将字符串划分为多个子串，每个线程负责在不同的子串中进行查找，并将结果合并。这样可以利用多核处理器的优势来提高性能。
    5. 优化边界条件：在实现`find()`函数时，注意对于一些特殊情况的边界条件进行优化。例如，如果要查找的子字符串为空字符串，则直接返回0；如果目标字符串的长度小于子字符串的长度，则无需进行查找。
2. 内存池（Memory Pool）：内存池是一种预分配固定大小的内存块并管理它们的技术。使用内存池可以减少频繁的内存分配和释放操作，从而提高性能。你可以使用内存池来管理字符串类中字符数组的内存，并在需要时从池中获取内存块，避免频繁的new和delete操作。
3. 短字符串优化（Small String Optimization）：短字符串优化是一种策略，当字符串较短时，将字符串的字符数组直接存储在字符串对象本身的栈空间或内部缓冲区中，而不是在堆上分配内存。这样可以避免对堆内存的动态分配，提高效率。
4. 引用计数（Reference Counting）：引用计数是一种技术，用于跟踪有多少个对象共享同一块内存。可以通过实现引用计数机制来处理字符串的拷贝和赋值操作，以减少内存拷贝的次数。只有在必要的情况下才进行内存拷贝，从而提高性能。
5. Copy-on-Write（COW）：COW是一种延迟复制的策略，在字符串被修改之前，共享相同内容的多个字符串对象引用同一块内存。只有在修改操作发生时，才会执行实际的内存复制操作，以保持数据的一致性。这可以减少不必要的内存复制，提高性能。

## 5.测试案例

```C++
#include <iostream>
#include "MyString.h" // 假设自定义的string类的头文件为"MyString.h"

int main() {
    // 创建一个字符串对象
    MyString myString("Hello, World!");

    // 测试长度函数
    std::cout << myString.length() << std::endl; // 输出：13

    // 测试索引访问函数
    std::cout << myString[0] << std::endl; // 输出：H
    std::cout << myString[7] << std::endl; // 输出：W

    // 测试切片函数
    std::cout << myString.substr(7) << std::endl; // 输出：World!
    std::cout << myString.substr(0, 5) << std::endl; // 输出：Hello

    // 测试字符串拼接函数
    MyString newString = myString + " Welcome!";
    std::cout << newString << std::endl; // 输出：Hello, World! Welcome!

    // 测试字符串重复函数
    MyString repeatedString = myString * 3;
    std::cout << repeatedString << std::endl; // 输出：Hello, World!Hello, World!Hello, World!

    // 测试字符串比较函数
    MyString otherString("Hello");
    std::cout << (myString == otherString) << std::endl; // 输出：0 (false)
    std::cout << (myString != otherString) << std::endl; // 输出：1 (true)
    std::cout << (myString > otherString) << std::endl;  // 输出：1 (true)
    std::cout << (myString < otherString) << std::endl;  // 输出：0 (false)

    // 测试字符串查找函数
    std::cout << myString.find("World") << std::endl;    // 输出：7
    std::cout << myString.find("Goodbye") << std::endl;  // 输出：-1

    // 测试字符串替换函数
    MyString replacedString = myString.replace("World", "Universe");
    std::cout << replacedString << std::endl; // 输出：Hello, Universe!

    // 测试字符串分割函数
    std::vector<MyString> splitStrings = myString.split(",");
    for (const auto& str : splitStrings) {
        std::cout << str << std::endl;
    }
    // 输出：
    // Hello
    //  World!

    return 0;
}

```