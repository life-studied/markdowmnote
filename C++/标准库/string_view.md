# string_view

​	string_view代表了字符串的视图，其内部仅有字符串的长度和字符串的指针。

​	不拥有字符串的所有权，不为字符串分配和析构内存。

## 1. 应用场景

​	string_view是视图，这就意味着它是只读的。它的API与string是一致的，可以用于替代`const string&`传参，将其改为string_view，极大程度地优化字符串copy构造的成本。

```C++
void output_str(string_view sv)
{
    cout << sv << endl;
}

int main()
{
    output_str("Hello world");
}
```

## 2. 构造

### 基本构造

```C++
//默认构造函数
constexpr basic_string_view() noexcept;
//拷贝构造函数
constexpr basic_string_view(const string_view& other) noexcept = default;
//直接构造，构造一个从s所指向的字符数组开始的前count个字符的视图
constexpr basic_string_view(const CharT* s, size_type count);
//直接构造，构造一个从s所指向的字符数组开始，到\0之前为止的视图，不包含空字符
constexpr basic_string_view(const CharT* s);
```

### 从string构造

​	由于`std::string`类重载了从`string`到`string_view`的转换操作符：

```text
operator std::basic_string_view<CharT, Traits>() const noexcept;
```

​	因此可以通过`std::string`来构造一个`std::string_view`：

```text
std::string_view foo(std::string("LionLong"));
```

这个过程其实包含三步：

1. 构造`std::string`的临时对象`a`；
2. 通过转换操作符将临时对象`a`转换为`string_view`类型的临时对象`b`；
3. 调用`std::string_view`的拷贝构造函数。

## 3. 成员函数

​	`string_view`有一个`data()`成员函数，但是不推荐使用，因为它是根据`\0`来判断字符串结尾的，结果不可控。需要配合`size()`一起使用来保证安全。

---

​	下面演示了不同构造方法，同时对`string_view`直接输出和`data()`输出进行比较。

​	需要注意的是：使用sv后缀的字符串构造和直接通过字符串构造的区别——直接构造遇到\0就停止了。

```C++
#include <string_view>

using namespace std::string_view_literals;

int main() {
    std::string_view sv("hello, LionLong");
    std::cout << "sv = " << sv
        << ", size() = " << sv.size()
        << ", data() = " << sv.data() << std::endl;

    std::string_view sv2 = sv.substr(0, 5);
    std::cout << "sv2 = " << sv2
        << ", size() = " << sv2.size()
        << ", data() = " << sv2.data() << std::endl;

    std::string_view sv3 = "hello\0 LionLong"sv;
    //std::string_view sv4("hello\0 LionLong"sv)
    std::cout << "sv3 = " << sv3
        << ", size() = " << sv3.size()
        << ", data() = " << sv3.data() << std::endl;

    std::string_view sv4("hello\0 LionLong");
    std::cout << "sv4 = " << sv4
        << ", size() = " << sv4.size()
        << ", data() = " << sv4.data() << std::endl;
}
```

输出：

```cpp
sv = hello, LionLong, size() = 14, data() = hello, LionLong
sv2 = hello, size() = 5, data() = hello, LionLong
sv3 = hello LionLong, size() = 14, data() = hello
sv4 = hello, size() = 5, data() = hello
```