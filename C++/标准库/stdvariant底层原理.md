---
create: '2025-05-02'
modified: '2025-05-02'
---

# std::variant实现原理

## traits.h

```C++
#pragma once
#include <type_traits>

// T => id : Position<Ts...> 从 Ts... 找到 T类型的下标
template <int id, typename U, typename T, typename... Ts>
struct Position {
	constexpr static int pos = std::is_same_v<U, T> ? id : Position<id + 1, U, Ts...>::pos;
};
template <int id, typename U, typename T>
struct Position<id, U, T> {
	constexpr static int pos = id;
};

// id => T : Type_element<idx,Ts...> 返回下标为 idx 的类型
template <int idx, typename T, typename... Ts>
struct Type_element {
	using Type = typename Type_element<idx - 1, Ts...>::Type;
};
template <typename T, typename... Ts>
struct Type_element<0, T, Ts...> {
	using Type = T;
};

// MaxSize<Ts...>  Ts... 最大的一个
template <typename T, typename... Ts>
struct MaxSize {
	constexpr static int size = sizeof(T) > MaxSize<Ts...>::size ? sizeof(T) : MaxSize<Ts...>::size;
};
template <typename T>
struct MaxSize<T> {
	constexpr static int size = sizeof(T);
};
```

## variant和visit

本质上和std::any的实现是类似的，都是通过编译期将类型信息保存在函数里，在运行时调用即可。

稍有不同的是，std::any通过函数指针来保存函数，而std::variant通过type_idx来记录函数类型。

```C++
#pragma once
#include "traits.h"
#include <string>
#include <iostream>

template <class... Ts>
struct Variant {
public:

	using destroy_func_t = void (*)(void*);
	using copy_func_t = void(*)(void*, void*);

public:
	Variant() {};
	
	Variant(const Variant<Ts...>& rhs) {
		type = rhs.type;
		copy_func_t[type](data, rhs.data);
	}

	void operator=(const Variant<Ts...>& rhs) {
		type = rhs.type;
		copy_func_t[type](data, rhs.data);
	}
	
	template <typename T>
	Variant(T&& rhs) {
		type = Position<0, T, Ts...>::pos;
		*reinterpret_cast<T*>(data) = std::forward<T>(rhs);
	}
	
	template <typename T>
	void operator=(T&& rhs) {
		if (type != -1) {
			destroy_func[type](data);
		}
		type = Position<0, T, Ts...>::pos;
		
		*reinterpret_cast<T*>(data) = std::forward<T>(rhs);
	}
	
	~Variant() {
		if (type != -1) {
			destroy_func[type](data);
		}
	}

public:

	template <typename T>
	T& get() {
		return *reinterpret_cast<T*>(data);
	}
	
	template <int id>
	auto& get() {
		using T = typename Type_element<id, Ts...>::Type;
		return *reinterpret_cast<T*>(data);
	}

public:

	template <typename Func, typename Var, typename realtype>
	static auto _Visit_helper(Func&& func, Var& var) {
		return func(std::forward<Var>(var).template get<realtype>());
	}

	template <typename Func>
	auto Visit(Func&& func) {
		using arg0 = typename Type_element<0, Ts...>::Type;
		using Ret = typename std::invoke_result_t<Func, arg0>;
		using Vartype = Variant<Ts...>;
		using fn_type = Ret(*)(Func&&, Vartype&);
		constexpr static fn_type table[] = { _Visit_helper<Func, Vartype, Ts>... };
		return table[type](std::forward<Func>(func), *this);
	}

private:

	template <class T>
	static void destroy(void* data) {
		reinterpret_cast<T*>(data)->~T();
	}

	template <typename T>
	static void copy(void* lhs, void* rhs) {
		*reinterpret_cast<T*>(lhs) = *reinterpret_cast<T*>(rhs);
	}
private:

	int type = -1;
	char data[MaxSize<Ts...>::size] = { 0 };
	constexpr static destroy_func_t destroy_func[] = { destroy<Ts>... };
	constexpr static copy_func_t copy_func[] = { copy<Ts>... };
};

template <typename Func, typename Vs>
auto Visit(Func&& func, Vs&& var) {
	return std::forward<Vs>(var).Visit(std::forward<Func>(func));
}

int main()
{
	Variant<int, double, std::string> v = std::string("Hello World");
	auto f = [](auto&& arg) {
		std::cout << arg << std::endl;
		};
	Visit(f, v);
	return 0;
}
```