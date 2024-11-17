---
create: 2023-11-28
---
# 10 优先使用作用域限制的 enmus 而不是无作用域的 enum

## 1. 避免命名冲突

​	一般而言，在花括号里面声明的变量名会限制在括号外的可见性。但是这对于 C++98 风格 的 `enums` 中的枚举元素（`unscoped`）并不成立。枚举元素和包含它的枚举类型**同属一个作用域空间**，这意味着在这个作用域中不能再有同样名字的定义：

```C++
enum Color { black, white, red}; 	// black, white, red 和
									// Color 同属一个定义域
auto white = false; 	// Error！因为 white
						// 在这个定义域已经被声明过
```

​	使用`enum class`能解决这个问题：

```C++
enum class Color { black, white, red}; 	// black, white, red
										// 作用域为 Color

auto white = false; // fine, 在这个作用域内
					// 没有其他的 "white"

Color c = white; 	// 错误！在这个定义域中
					// 没有叫"white"的枚举元素

Color c = Color::white; // fine
auto c = Color::white; 	// 同样没有问题（和条款5
						// 的建议项吻合）
```

## 2. 避免隐式转换

​	无作用域的 `enum` 会将枚举元素隐式的转换为整数类型（从整数出发，还可以转换为浮点类型）。

​	在有作用域的 `enum` 中不 存在从枚举元素到其他类型的隐式转换：

```C++
enum class Color { black, white, red }; // 有作用域的enum
Color c = Color::red; 	// 和前面一样，但是
... 					// 加上一个作用域限定符
if (c < 14.5){ 			// 出错！不能将Color类型
						// 和double类型比较
    auto factors = 		// 出错！不能将Color类型传递给
    primeFactors(c); 	// 参数类型为std::size_t的函数
    ...
}
```

​	如果你就是想将 `Color` 类型转换为一个其他类型，使用类型强制转换（ `cast` ）可以满足需求：

```C++
if(static_cast<double>(c) < 14.5) { // 怪异但是有效的代码
    auto factors = 						// 感觉不可靠
    primeFactors(static_cast<std::size_t(c)); // 但是可以编译
    ...
}
```

## 3. 允许不事先声明

​	因为有定义域的 `enum` 可以被提前声明的，即可以不指定枚举元素而进行声明：

```C++
enum Color; 		// 出错！
enum class Color; 	// 没有问题
```

**减少编译冗余**

​	不能事先声明枚举类型有几个不足。最引人注意的就是会增加编译依赖性。

​	这个枚举体可能会在整个系统中都会被使用到，因此被包含在系统每部分都依赖的一个头文件当中。如果一个新的状态需要被引入，就算一个子系统——甚至只有一个函数！——用到这个新的枚举元素，有可能导致整个系统的代码需要被重新编译。

​	解决方法就是通过`enum class` 进行事先声明：

```C++
enum class Status; // 前置声明
void continueProcessing(Status s); // 使用前置声明的枚举体
```

​	如果 `Status` 的定义被修改，包含这个声明的头文件**不需要重新编译**。

## 4. 指定默认潜在类型

​	对有作用域的枚举体，默认的潜在的类型是 `int` :

```C++
enum class Status; // 潜在类型是int
```

​	如果默认的类型不适用于你，你可重载它：

```C++
enum class Status: std::uint32_t; // Status潜在类型是std::uint32_t —— 来自<cstdint>
```

​	为了给没有作用域的枚举体指定潜在类型，你需要做相同的事情，可能在前置声明处：

```C++
enum Color: std::uint8_t; 	// 没有定义域的枚举体
                            // 的前置声明，潜在类型是
                            // std::uint8_t
```

​	潜在类型的指定也可以放在**枚举体的定义**处：

```C++
enum class Status: std::uint32_t { 
    good = 0,
    failed = 1,
    incomplete = 100,
    corrupt = 200,
    audited = 500,
    indeterminate = 0xFFFFFFFF
};
```

## 5. 无作用域的enum的特殊作用

​	这种情况发生在引用 C++11 的 `std::tuple` 中的某个域时。例如，假设我们有一个元组，元组中保存着姓名， 电子邮件地址，和用户在社交网站的影响力数值：

```C++
using UserInfo = 			// 别名，参见条款9
std::tuple<std::string, 	// 姓名
std::string, 				// 电子邮件
std::size_t>; 				// 影响力
```

​	在遇到获取电子邮件的情况，并不想去记住元组的第一个域对应的是用户的电子邮件地址，由此可以创建一个对应的enum枚举类型：

```C++
enum UserInfoFields {uiName, uiEmail, uiReputation };
```

​	然后用它的隐式类型转换，相比于`enum class`，更为合适：

```C++
auto val = std::get<uiEmail>(uInfo); 

auto val = std::get<static_cast<std::size_t>(UserInfoFields::uiEmail)>(uInfo);
```

​	**注意**： `std::get` 是一个模板，你提供的值是一个模板参数，因此负责将枚举元素转化为 `std::size_t` 的这个函数**必须在编译阶段**就确定它的结果。 这意味着它**必须是一个 `constexpr` 函数**。

---

**使用enum class的方法**

​	如果要使用`enum class`，则可以通过补充一个使用`std::underlying_type_t<T>`的模板函数来完成：

```C++
template<typename E> 					// C++14
constexpr std::underlying_type_t<E> toUType(E enumerator) noexcept
{
	return static_cast<std::underlying_type_t<E>>(enumerator);
}
```

​	接下来使用这种方式去调用它：

```C++
auto val = std::get<toUType(UserInfoFields::uiEmail)>(uInfo);
```

​	这样依然比使用没有定义域的枚举体要复杂，但是它可以**避免命名空间污染**和**不易引起注意的枚举元素的的类型转换**。