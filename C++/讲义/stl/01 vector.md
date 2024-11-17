---
create: 2024-07-12
---
# vector

​	vector是一个模板类，底层是数组，采用动态内存分配，分配策略是2倍增长。 

## 1. quick to start

```C++
#include <vector>
#include <iostream>

int main()
{
    std::vector<int> v;
    v.push_back(1);
    std::cout << v[0];
}
```

## 2. 容量

### size

​	使用size方法获取到当前vector中元素的数量。

```C++
size_t v_size = v.size();
```

### capacity

​	使用capacity方法获取到当前vector底层的数组分配内存的大小。

```C++
size_t v_cap = v.capacity();
```

---

### 预分配与适配

#### reserve

​	在少数情况下，知道一个vector的最终大小不会超过某个值，可以直接使用reserve预分配内存大小，某些情况下能明显提高速度。(一般会分配一个大于参数的第一个2的幂，例如：传入20分配32；传入7分配8)

```C++
v.reserve(20);
```

#### resize

​	很多时候，resize很容易和reserve混淆。它的作用是调整size()的结果，但是不一定会对内存进行操作。用的不多。

​	例如，v中原本有3个元素，resize传入5，就会添加两个默认值进去。

​	而v中原本有6个元素，resize传入3，就会丢弃末尾的3个元素。

```C++
v.resize(8);

v.resize(16, -1);	// 指定默认值
```

## 3. 插入与删除

​	vector底层作为数组，推荐只在末尾进行插入和删除，时间复杂度是O(1)，否则时间复杂度是O(n)。

### push_back

​	最常用的方法，插入一个元素在末尾。

### pop_back

​	从末尾删除一个元素。不常用其实。

### insert

​	很多人喜欢用insert在中间进行插入。但是高手如果要用到这个，就会开始考虑换个容器不用vector了。

### erase

​	同理，中间删除。

## 4. 迭代器

​	STL中大多数迭代器都是使用begin和end函数获取。vector也不例外。

​	一般使用it作为迭代器的名字，用auto自动推断类型，因为它的类型名很长，大家都懒得写：`std::vector<T>::iterator`。

```C++
auto it = v.begin();		// 这样就拿到了指向第一个元素的迭代器。*it就能获取值了
```

​	第二个注意点是，end指向的是容器末尾的第一个非法地址。一般用于判断loop结束，或者是判断find查找没找到。

```C++
for(auto it = v.begin(); it != v.end(); it++)
{
    // ...
}

for(int i = 0; i < v.size(); i++)
{
    // ...
}

auto it = std::find(v.begin(), v.end(), 1);
if(it != v.end())
{
    // ...
}
```

## 5. 查找

​	虽然vector的查找效率是O(n)，但是用它进行查找是很多时候需要的。

### 无序查找

​	一般来说，除非你意识到了某个代码能够使用排序显著减少解决的难度和时间，否则你会选择直接开始查。

​	你可以选择自己手写for-loop，因为有时候太复杂了，使用标准库的算法不能满足你的需要，否则使用下面的标准方法，它能极大地降低你的代码错误率：

```C++
auto it = std::find(v.begin(), v.end(), 1);

auto it = std::find_if(v.begin(), v.end(), [](auto& element){ return element % 3 == 0;});
```

### 有序查找

​	这个建立在数组有序的情况下。

​	你当然可以手写二分查找，但是最好使用标准库的：

* `std::lower_bound`函数返回一个指向大于或等于给定值的第一个元素的迭代器。如果没有找到符合条件的元素，它将返回指向容器末尾的迭代器。
* `std::upper_bound`函数返回一个指向大于给定值的第一个元素的迭代器。如果没有找到符合条件的元素，它将返回指向容器末尾的迭代器。

```C++
std::lower_bound(v.begin(), v.end(), 1);
```

​	注意了，二分查找不一定会找到目标值，而是会找到一个适合插入的地方。如果你想要判断这个值是否存在，就要加一个：

```C++
auto it = std::lower_bound(v.begin(), v.end(), 1);

if(it == v.end())
{
    // ...
}
else
{
    if(*it == 1)
    {
        // ...
    }
}
```

## 6. 排序

​	没什么好讲的，直接用sort。

```C++
std::sort(v.begin(), v.end());
```

​	反向排序：

```C++
std::sort(v.begin(), v.end(), std::greater<int>());
```

## 7.  逆序

​	有时候需要对vector翻个方向：

```C++
std::reverse(v.begin(), v.end());
```

