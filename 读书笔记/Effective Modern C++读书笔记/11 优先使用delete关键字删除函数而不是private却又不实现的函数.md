---
create: 2023-11-28
modified: '2024-11-17'
---

# 11 优先使用delete关键字删除函数而不是private却又不实现的函数

​	在`C++98`标准中，通过使用`private`成员函数的方法，将其设置为不可用，但是在`C++11`下，应该使用`delete`关键字来设置：

```C++
// In C++98
template <class charT, class traits = char_traits<charT> >
class basic_ios :public ios_base {
public:
    ...
private:
    basic_ios(const basic_ios& ); // 没有定义
    basic_ios& operator(const basic_ios&); // 没有定义
};

// In C++11
template <class charT, class traits = char_traits<charT> >
class basic_ios :public ios_base {
public:
    ...

    basic_ios(const basic_ios& ) = delete
    basic_ios& operator(const basic_ios&) = delete;
};
```

​	这样做的好处在于：

* `delete`的函数一定不会被调用，即使存在友元。
* 编译器检查到使用`delete`函数时，能产生更易读的错误信息
* **允许删除非成员函数，来消除不想要的隐式转换**

## 消除非成员函数不必要的参数隐式转换

​	下面的`isLucky`函数，会允许接受double甚至char类型的参数，但是这明显不符合语义：

```C++
bool isLucky(int number);
```

​	对此，可以使用`delete`来删除不想要出现的情况：

```C++
bool isLucky(int number); 		// 原本的函数
bool isLucky(char) = delete; 	// 拒绝char类型
bool isLucky(bool) = delete; 	// 拒绝bool类型
bool isLucky(double) = delete; 	// 拒绝double和float类型
```

---

## 消除模板函数的特化版本

​	对于一些指针参数模板，如果实参是一个`void*`，可能不能出现满足需要的解引用，自增自减等操作，因此可以使用`delete`来**显式删除这个特化模板**。

```C++
template<typename T>
void processPointer(T* ptr);

template<>
void processPointer<void>(void*) = delete;
```

## 消除成员模板函数的特化版本

​	如果`class`中定义了一个模板函数，如果想要专门删除一个类成员模板函数的特化版本，使用`private`处理会报错：

```C++
class Widget{
public:
    ...
    template<typename T>
    void processPointer(T* ptr)
    { ... }
private:
    template<> 	// 错误！
    void processPointer<void>(void*)
};
```

​	赋予一个成员函数模板的某种特殊情况下，**拥有不同于模板主体的访问权限是不可能**。因为模板的特殊情况必须要写在命名空间的作用域内，而不是类的作用域内，使用`delete`能完美解决这个问题：

```C++
class Widget{
public:
    ...
    template<typename T>
    void processPointer(T* ptr)
    { ... }
    ...
};

template<> // 仍然是公用的，但是已被删除
void Widget::processPointer<void>(void*) = delete;
```