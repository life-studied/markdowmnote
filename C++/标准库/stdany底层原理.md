# std::any底层原理

最有意思的地方在于，manager在编译期，通过不同type的实例化隐式地存储了类型信息。

而到了运行时，即使我不知道void*里真正存储的是什么类型，我也可以通过manager代理处理，它一定能正确处理对应的类型，哪怕我不知道这是什么类型，但是manager是对应上data的类型的。

```C++
#include <iostream>
#include <typeinfo>

class any {
public:
	any() : data(nullptr), helperfunc(nullptr) {}
	~any() {
		if (data) {
			helperfunc(Destory, *this, nullptr);
			helperfunc = nullptr;
		}
	}
	template <typename T>
	any(const T& value) {
		data = new T(value);
		helperfunc = &any::manager<std::decay_t<T>>;
	}
	any& operator=(const any& other) {
		if (this != &other) {
			if (helperfunc != nullptr) {
				helperfunc(Destory, *this, nullptr);
			}
			if (other.helperfunc) {
				other.helperfunc(Copy, *this, &other);
			}
		}
		return *this;
	}
	void print() {
		if (helperfunc) {
			helperfunc(Print, *this, nullptr);
		}
		else {
			std::cout << "No Data" << std::endl;
		}
	}

private:
	void* data;
	enum operation {
		Destory,
		Copy,
		AnyCast,
		TypeInfo,
		Print
	};

	using HelperFunction = void* (*)(operation op, any& left, const any* right);
	HelperFunction helperfunc;

private:
	template <typename T>
	static void* manager(operation op, any&left, const any* right) {
		switch (op)
		{
		case any::Destory:
			delete static_cast<T*>(left.data);
			break;
		case any::Copy:
			left.data = new T(*static_cast<const T*>(right->data));
			left.helperfunc = right->helperfunc;
			break;
		case any::AnyCast:
			return static_cast<T*>(left.data);
			break;
		case any::TypeInfo:
			return const_cast<void*>(static_cast<const void*>(&typeid(T)));
			break;
		case any::Print:
			if (left.data) {
				std::cout << "Data: " << *static_cast<T*>(left.data) << std::endl;
			}
			else {
				std::cout << "No Data" << std::endl;
			}
			break;
		default:
			break;
		}
		return nullptr;
	}

private:
	template <typename T>
	friend T any_cast(any& a);
};

template <typename T>
T any_cast(any& a) {
	if (a.helperfunc == nullptr) {
		throw std::bad_cast();
	}
	return *static_cast<T*>(a.helperfunc(any::AnyCast, a, nullptr));
}

int main()
{
	any a = 1;
	a.print();

	std::string str = "Hello World";
	a = str;
	a.print();

	a = 3.14;
	a.print();

	any b(999);
	a = b;
	a.print();

	try
	{
		std::cout << "a = " << any_cast<int>(a) << std::endl;
	}
	catch (const std::bad_cast& e)
	{
		std::cout << "Bad any cast: " << e.what() << std::endl;
	}
	
	return 0;
}
```

