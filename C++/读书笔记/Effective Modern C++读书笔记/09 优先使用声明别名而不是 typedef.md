# 09 优先使用声明别名而不是 typedef

## 1. using更好理解函数指针

​	相比于`typedef`，`using`能更好分辨函数指针的名字和类型。

```C++
// FP等价于一个函数指针，这个函数的参数是一个int类型和
// std::string常量类型，没有返回值
typedef void (*FP)(int, const std::string&); // typedef

using FP = void (*)(int, const std::string&); // 声明别名
```

## 2. using更好创建模板别名（ alias template ）

### 2.1 typedef的情况

#### typedef创建模板别名（C++98）

​	使用`typedef`就不得不将模板别名嵌入到一个结构体里，才能成功创建：

```C++
template<typename T> 						// MyAllocList<T>::type
struct MyAllocList { 						// 等同于
	typedef std::list<T, MyAlloc<T>> type; 	// std::list<T,
}; 											// MyAlloc<T>>

MyAllocList<Widget>::type lw; // 终端代码
```

#### 作为数据成员使用（typename）

​	使用通过`typedef`创建的模板别名，必须使用`typename`来表明它是一种类型，而非一个变量。

```C++
template<typename T> 	// Widget<T> 包含
class Widget{ 			// 一个 MyAloocList<T>
private: 				// 作为一个数据成员
	typename MyAllocList<T>::type list;
	...
};

```

### 2.2 using

```C++
template<typname T>
using MyAllocList = std::list<T, MyAlloc<T>>; 	// 和以前一样

template<typename T>
class Widget {
private:
	MyAllocList<T> list; 	// 没有typename
	... 					// 没有::type
};
```

## 3. using处理type_traits相关

​	在过去，为了处理`TMP`（模板元编程）中相关类型转，C++11 提供了工具来完成这类工作，表现的形式是 `type_traits` ,它是`<type_traits>`中的一个模板的分类工具。

​	在这个头文件中有数十个类型特征，给定一个你想竞选类型转换的类型 T ，得到的类型是 `std::transformation<T>::type` 。

​	例如：

```C++
std::remove_const<T>::type 			// 从 const T 得到 T
std::remove_reference<T>::type 		// 从 T& 或 T&& 得到 T
std::add_lvalue_reference<T>::type 	// 从 T 得到 T&
```

​	别名有一个统一的形式：对于 C++11 中的每个类型转换 `std::transformation::type` ，有一个对应的 C++14 的模板别名`std::transformation_t` 。用例子来说明我的意思：

```C++
std::remove_const<T>::type 	// C++11: const T -> T
std::remove_const_t<T> 		// 等价的C++14
    
std::remove_reference<T>::type 	// C++11: T&/T&& -> T
std::remove_reference_t<T> 		// 等价的C++14
    
std::add_lvalue_reference<T>::type 	// C++11: T -> T&
std::add_lvalue_reference_t<T> 		// 等价的C++14
```

