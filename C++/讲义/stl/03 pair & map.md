---
create: 2024-07-12
---
# pair & map

[TOC]

## 1. pair

​	pair是一个二元组，第一个是first，第二个是second。

```C++
#include <utility>

std::pair<int, int> p;
p.first = 1;
p.second = 2;
```

## 2. map

​	map是一个含有多个二元组的容器。它的底层实现是一个红黑树，也就是说，它的查找效率是O(lgn)。

### 2.1 插入

```C++
#include <map>

std::map<int, int> m;
m.insert({1,2});
```

### 2.2 查找

```C++
#include <map>

std::map<int, int> m{
    {1,2},
    {2,2},
    {3,2},
    {4,2}
};

auto it = m.find(2);
if(it != m.end())
{
    auto res = it->second;
}
```

### 2.3 删除

```C++
m.erase(it);
```

### 2.4 修改

```C++ 
m[2] = 2;
m[3] = 3;
```

### 注意点

#### 1. 如果不能保证存在，使用find获取迭代器

​	使用[]运算符，可以拿到对应的value，但是如果原本这个pair不存在，则会设置一个默认的value进去，并返回。

​	所以使用find，去检测是否存在。

```C++
if(m[2] == 0);		// wrong!

if(auto it = m.find(2); it != m.end());
```

#### 2. 即使不关心是否存在，也使用find获取迭代器

​	使用[]运算符虽然方便，但是每次使用都会进行一次查找，但是使用find只会查找一次，接下来就能直接定位到位置。

```C++
if(m[2] == 0)		//1
{
    m[2]++;			//2
}

if(auto it = m.find(2); it != m.end())	//1
{
    it->second++;
}
else
{
    m.insert({2, 0});
}

```

## 3. unordered_map

​	这个最常用，因为底层是hash表，即查找时间是O(1)。

​	用法与map一致。

## 4. 补充知识点：将pair作为key

​	经常存在需求，需要将pair作为key，例如，根据坐标xy，查找到对应的属性。此处的xy，就是一个典型的pair结构。

### map

​	如果使用了map，则可以直接这样用：

```C++
map<pair<int,int>, int> m;
pair<int,int> p{2,2};
m.find(p);
```

​	因为它的底层的红黑树，通过比较两个pair是否相等，即通过pair的`==运算符`重载进行比较查找，时间复杂度是O(lgn)。

### unordered_map

​	如果使用了unordered_map，则不可以直接使用，因为它的底层是hash表，查找方式是对数据结构去计算它的hash值，而pair并没有规定如何计算它的hash值。

​	但是unordered_map的性能是O(1)，非常优越，因此可以手写一个pair的hash值计算来辅助使用，下面是boost库实现的源代码：

```C++
#include <functional>
// from boost (functional/hash):
// see http://www.boost.org/doc/libs/1_35_0/doc/html/hash/combine.html template
template <typename T>
inline void hash_combine(std::size_t &seed, const T &val) {
    seed ^= std::hash<T>()(val) + 0x9e3779b9 + (seed << 6) + (seed >> 2);
}
// auxiliary generic functions to create a hash value using a seed
template <typename T> inline void hash_val(std::size_t &seed, const T &val) {
    hash_combine(seed, val);
}
template <typename T, typename... Types>
inline void hash_val(std::size_t &seed, const T &val, const Types &... args) {
    hash_combine(seed, val);
    hash_val(seed, args...);
}

template <typename... Types>
inline std::size_t hash_val(const Types &... args) {
    std::size_t seed = 0;
    hash_val(seed, args...);
    return seed;
}

struct pair_hash {
    template <class T1, class T2>
    std::size_t operator()(const std::pair<T1, T2> &p) const {
        return hash_val(p.first, p.second);
    }
};


#include <unordered_map>
#include <unordered_set>
using namespace std;
using ll = long long;

int main() {
    unordered_map<pair<ll, ll>, ll, pair_hash> slopeCount;
    unordered_set<pair<ll, ll>, pair_hash> seen;
    return 0;
}
```

