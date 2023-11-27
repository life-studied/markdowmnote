# 02 理解auto类型推导

​	`auto`的类型推导，与**条款1的类型推导几乎一致**。例如：

```C++
auto x = 27; 		// 情况3（x既不是指针也不是引用）
const auto cx = x; 	// 情况3（cx二者都不是）
const auto& rx = x; // 情况1（rx是一个非通用的引用）

auto&& uref1 = x; 	// x是int并且是左值
					// 所以uref1的类型是int&
auto&& uref2 = cx; 	// cx是int并且是左值
					// 所以uref2的类型是const int&
auto&& uref3 = 27; 	// 27是int并且是右值
					// 所以uref3的类型是int&&

const char name[] = // name的类型是const char[13]
"R. N. Briggs";
auto arr1 = name; 	// arr1的类型是const char*
auto& arr2 = name; 	// arr2的类型是const char (&)[13]

void someFunc(int, double); // someFunc是一个函数，类型是
							// void (*)(int, double)
auto& func2 = someFunc; 	// func1的类型是
							// void (&)(int, double)
```

## 唯一的例外——大括号初始化

​	唯一的例外在于，用**大括号初始化**会导致出现`initializer_list`的存在。

```C++
auto x1 = 27; 		// 类型时int，值是27
auto x2(27); 		// 同上

auto x3 = { 27 }; 	// 类型是std::intializer_list<int>
					// 值是{ 27 }
auto x4{ 27 }; 		// 同上
```

​	但是，这仅在声明变量时有效，而以下场景却无效：

* 返回值auto推导：

  * ```C++
    auto createInitList()
    {
    	return { 1, 2, 3 }; // 编译错误：不能推导出{ 1, 2, 3 }的类型
    }
    ```

* lambda里函数参数auto推导

  * ```C++
    std::vector<int> v;
    // ...
    auto resetV = [&v](const auto& newValue) { v = newValue; } // C++14
    // ...
    resetV({ 1, 2, 3 }); // 编译错误，不能推导出{ 1, 2, 3 }的类型
    ```

