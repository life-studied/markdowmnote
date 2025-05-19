---
create: '2025-05-20'
modified: '2025-05-20'
---

# OOP

## 1. 面向对象编程和面向过程

面向对象编程和面向过程编程是两种编程的范式。它们之间没有孰优孰劣之分，只是编程的思想上不同。一般使用面向对象编程，都是使用到class的概念：

用一个类来抽象现实世界里的一个真实的物体，而这个抽象出来的class，会拥有物体的属性和行为。

## 2. class

### quick start（最简单的class示例）

```C++
class A {
public:
    A() {}
    ~A() {}
    
private:
    int data;
};
```

* `class`关键字：声明一个类
* `public`和`private`：用于修饰下面部分的内容是公开还是私有的，公开是指外部可以访问，私有是指只有自己可以访问。
* 构造函数`A()`：在对象被构造一瞬间进行的操作函数，常用来初始化
* 析构函数`~A()`：对象过期时，程序会自动调用一个特殊的成员函数——析构函数。
    * 离开作用域：函数作用域，块作用域
    * `delete`时

```C++
int main() {
    A a;	// 栈上
    A* a2 = new A();	// 堆上
}
```

### 构造函数

构造函数一般是用来将类中的数据初始化的。

* 构造函数的名称与类名相同，无需声明类型（即无返回值），原型位于类声明的公共部分。

* 构造函数如果没有设置，编译器会默认提供一个。
* 构造函数可以定义多个，只要参数列表不同，用于应对不同的需求。
* 一旦定义了一个，编译器就不会自动生成。

```C++
#include <string>
#include <iostream>

class A {
public:
	A() : data(1) {}
	A(int data_) : data(data_) {}
	A(std::string data_) : data(atoi(data_.c_str())) {}

	void print() { std::cout << data << std::endl; }
private:
	int data;
};

int main() {
	A a1;
	A a2(2);
	A a3("3");

	a1.print();
	a2.print();
	a3.print();
}
```

#### 构造函数使用

方法1：显式的调用构造函数

```c++
A a1 = A(1);
```

方法2：隐式的调用构造函数

```c++
A a2(2);
```

使用new的方法：

```c++
A *pa = new A(3);
```

### 析构函数

析构函数是用来释放类内部的资源的。

* 析构函数名字固定，不能有多个。`~XXX()`
* 析构函数如果没有设置，编译器会默认提供一个。
* 析构函数是默认调用的，不用手动调用。

```C++
#include <string>
#include <iostream>

class A {
public:
	A() : data(new int(1)) {}
	A(int data_) : data(new int(data_)) {}
	A(std::string data_) : data(new int(atoi(data_.c_str()))){}

	void print() { std::cout << *data << std::endl; }

	~A() {
		delete data;
		std::cout << "资源已释放" << std::endl;
	}
private:
	int* data;
};

int main() {
	A a1;
	A a2(2);
	A a3("3");

	a1.print();
	a2.print();
	a3.print();
}
```

### 访问控制符

* public外部可访问
* private仅内部访问

```C++
#include <string>
#include <iostream>
#include <chrono>
#include <iomanip>
#include <ctime>

class A {
public:
	A() : data(new int(1)) {}
	A(int data_) : data(new int(data_)) {}
	A(std::string data_) : data(new int(atoi(data_.c_str()))) {}

	void print() {
		print_time();
		std::cout << *data << std::endl;
	}

	~A() {
		delete data;
		std::cout << "资源已释放" << std::endl;
	}
private:
	void print_time() {
		auto now = std::chrono::system_clock::now();
		std::time_t time_t_now = std::chrono::system_clock::to_time_t(now);
		std::tm* ltm = std::localtime(&time_t_now);
		std::cout << std::put_time(ltm, "%Y-%m-%d %H:%M:%S") << '\t';
	}

	int* data;
};

int main() {
	A a1;
	A a2(2);
	A a3("3");

	a1.print();
	a2.print();
	a3.print();

	// a1.print_time();	// 不可访问
	// a2.print_time();	// 不可访问
	// a3.print_time();	// 不可访问

}
```

### 类静态函数和类静态变量

`static`关键字在`class`中的作用，是提供属于整个类的属性和行为。

* 全局类可调用，并不是属于某个对象

比如说，有一个兔子类，我们想要知道全局已经有多少个兔子了：

```C++
#include <iostream>

class rabbit {
public:
	rabbit() {
		num++;
	}

	static void print_total_num() {
		std::cout << "全局兔子的数量：" << num << std::endl;
	}

private:
	static int num;	// 声明
};

int rabbit::num = 0;	// 定义

int main()
{
	rabbit r1;
	r1.print_total_num();
    rabbit::print_total_num();

	rabbit r2;
	r2.print_total_num();
	rabbit::print_total_num();

	return 0;
}
```

### this指针

this指针是用来获取对象的地址。

* 用于明确指示数据成员
* 用于获取自己的地址

```C++
#include <iostream>

class A {
public:
	A() : data(1) {}
	void print() {
		std::cout << this->data << std::endl;
		std::cout << this << std::endl;
	}

private:
	int data;
};

int main()
{
	A a;
	a.print();
	return 0;
}
```

## 3. 运算符重载

运算符重载是C++的一个很有特色的做法，任意运算符都可以被重载后使用。

举个例子：

```C++
#include <string>
#include <iostream>

int main() {
	std::string s("Hello World");
	std::cout << s[0];
}
```

### 创建运算符，以[]为例

```C++
#include <cstring>
#include <iostream>

class MyString {
public:
	MyString(const char* str) {
        if(str == nullptr) {
            std::cerr << "input string is null" << std::endl;
            return;
        }
		len = std::strlen(str);
		str_internal = new char[len + 1];
		std::strcpy(str_internal, str);
	}

	char& operator[](int index) {
        if(index >= len) {
			std::cerr << "unreachable data" << std::endl;
            exit(-1);
        }
		return str_internal[index];
	}

	int size() {
		return len;
	}

private:
	char* str_internal;	// 内部实际的字符串
	int len;
};

int main()
{
	MyString s("Hello World");
	std::cout << s[0] << std::endl;

	std::cout << s.size() << std::endl;
	return 0;
}
```

* 合理的检查不可缺少：空指针传递、数组越界
* 多使用标准库提供的函数：`strlen`、`strcpy`等
* 运算符重载返回引用，目的是为了可读可写：`s[0] = 'A'`

## 4. 拷贝构造函数和赋值运算符

这两个虽然一个是构造函数，另一个是运算符，但是由于用起来十分接近，因此会放到一起：

* 拷贝构造函数本质上还是一个构造函数，只是参数非常特殊，是另一个同类的对象。
* 赋值运算符本质上还是一个运算符重载函数，只是参数非常特殊，是另一个同类对象。
* 这两者都有一种复制的语义在里面。

```C++
#include <iostream>

class A {
public:
	A() : data(1) {}
	A(int data_) : data(data_) {}

	A(const A& other) { this->data = other.data; }
    
	A& operator=(const A& other) {
		if (this == &other) {
			return *this;
		}

		this->data = other.data;
	}

	void print() {
		std::cout << data << std::endl;
	}
private:
	int data;
};

int main()
{
	A a(3);
	A a2(a);

	A a3;
	a3 = a2;

	a3.print();
	return 0;
}
```

补充问题：这里调用的是什么？

```C++
A a4 = a3;	// 拷贝构造函数？赋值运算符？
```

## 5. 移动构造函数

什么是移动？移动就是将类里面的资源全部移动到另一个地方。

它与复制的区别在于，移动会把原来类中的资源给别人，然后自己清理掉自己的资源；而复制会拷贝一份出来。

讲移动构造之前，必须先讲移动的意义是什么。比如我有一个很大的对象，例如一个视频：

```C++
class video {
    ...
private:
    char* data[1024*1024];	// 1M
}
```

如果我只是想让另一个地方去播放它，我不希望这个数据在内存里又被拷贝一份。那么这个时候，移动就非常适合。

移动在什么时候发生？

试想，如果一个资源，在后续还会被用到，那么我把这个资源里面的内容移动了，里面的内容就会被清空，非常危险。因此，移动一般发生在这个资源后续我们不会使用了，但是这个资源别人需要的时候。

### 左值和右值

在C++中，一个值如果可以在等号的右边，则可以看成是左值，否则就是右值。

比如：

```C++
int a = 1;
```

* a是左值，因为它可以被修改
* 1是右值，因为你不可以用1=a，1已经是一个写死的值，你不能修改它

### 左值引用和右值引用

如何表示一个右值的类型？在C++中，用右值引用来表示一个右值：`int&&`。

一个&代表指向的是左值，而两个&代表指向的是右值。

```C++
int main()
{
	int data = 1;
    
	int& a = data;	// 左值
	int&& b = 1;	// 右值

	//int& a2 = 1;	// 左值引用不能绑定右值
	//int&& b2 = a;	// 右值引用不能绑定左值
	return 0;
}
```

### 移动构造

移动构造是一个工具，用于让我们转移数据。

```C++
#include <iostream>
#include <fstream>
#include <string>
#include <filesystem>

const int file_max_len = 1024 * 1024 * 1024;	// 1G

class video {
public:
	video(std::string filename): success(false), data(nullptr) {
		
		if (!std::filesystem::exists(filename)) {
			std::cerr << "文件不存在" << std::endl;
			return;
		}

		if (std::filesystem::path(filename).extension() != ".mp4") {
			std::cerr << "不支持的扩展名：" << std::filesystem::path(filename).extension().string() << std::endl;
			return;
		}

		std::ifstream ifs(filename, std::ios::binary | std::ios::ate);
		if (!ifs.is_open()) {
			std::cerr << "文件无法打开" << std::endl;
			return;
		}

		int file_len = ifs.tellg();
		if (file_len > file_max_len) {
			std::cerr << "文件过大" << std::endl;
			return;
		}
		
		// 读取文件
		ifs.seekg(0, std::ios::beg);
		data = new char[file_len];
		memset(data, 0, file_len);
		ifs.read(data, file_len);
		success = true;
	}

	video(video&& other) {
		data = other.data;
		success = true;

		other.data = nullptr;
		other.success = false;

		std::cout << "数据成功转移" << std::endl;
	}

	bool ready() {
		return success;
	}

private:

	char * data;
	bool success;
};


int main()
{
	video v1("1.mp4");

	if (v1.ready()) {
		video v2(std::move(v1));
	}
	
	return 0;
}
```

## 3. 练习：手写string

学习完class，最常见的要求就是自己完成一个string类。