# 05 优先使用auto而非显式类型声明

## 1. 避免未初始化变量

​	`auto` 变量从它们的初始化中推导出其类型，所以它们必须被初始化。

```C++
int x1; 		// potentially uninitialized
auto x2; 		// error! initializer required
auto x3 = 0; 	// fine, x's value is well-defined
```

## 2. 避免类型截断

​	`auto` 的优点除了可以避免未初始化的变量，变量声明引起的歧义，直接持有封装体的能力。 还有一个就是可以避免“类型截断”问题。下面有个例子，你可能见过或者写过：

```C++
std::vector v;
// ... 
unsigned sz = v.size();
```

​	`v.size()` 定义的返回类型是 `std::vector::size_type` ，但是很少有开发者对此十分清楚。

​	 `std::vector::size_type` 被指定为一个非符号的整数类型，因此很多程序员认 为 `unsigned` 类型是足够的，然后写出了上面的代码。这将导致一些有趣的后果：比如说在32 位 Windows 系统上， `unsigned` 和 `std::vector::size_type` 有同样的大小，但是在64位的 Windows 上， `unsigned` 是**32bit**的，而 `std::vector::size_type` 是**64bit**的。

​	这意味着上面的代码在32位 Windows 系统上工作良好，但是在64位 Windows 系统上时有可能不正确，当应用程序从32位移植到64位上时，谁又想在这种问题上浪费时间呢？ 使用 `auto` 可以保证你不必被上面的东西所困扰：

```C++
auto sz = v.size(); 	// sz's type is std::vector<int>::size_type
```

## 3. 避免未知的隐式转换

​	对于下面的代码，看起来正常，实际上发生了不必要甚至危险的隐式转换：

```C++
std::unordered_map<std::string, int> m;
// ...

for (const std::pair<std::string, int>& p : m)
{
	// do something with p
}
```

​	在哈希表中的 `std::pair` 的类型不是 `std::pair<std::string, int>` ，而是 `std::pair<const std::string, int>` 。

​	但是这不是循环体外变量 `p` 的声明类型。后果就是：编译器竭尽全力去找到一种方式，把 `std::pair<const std::string, int>` 对象（正是哈希表中的元素）转化为 `std::pair<std::string, int>` 对象（ p 的声明类型）。

​	这个过程将通过复制 `m` 的每一个元素到一个临时对象，然后将这个临时对象和 p 绑定完成。在每个循环结束的时候，这个临时对象将被销毁。

---

​	解决方案仅仅是使用auto来声明变量：

```C++
for (const auto& p : m)
{
	... // as before
}
```

