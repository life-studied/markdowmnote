# 对pair进行hash化

​	`std::map`底层采用红黑树，因此它会调用对象的`operator<`进行排序放置。

​	而`std::unordered_map`底层采用哈希表，因此对象类型必须与`std::hash`兼容才能进行使用。

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

#include <bits/stdc++.h>
using namespace std;
using ll = long long;

int main() {
    unordered_map<pair<ll, ll>, ll, pair_hash> slopeCount;
    unordered_set<pair<ll, ll>, pair_hash> seen;
    return 0;
}
```

## 参考资料

* [pair 作为 unordered_map unordered_set 的键值 C++ | YoungForest's blog](https://youngforest.github.io/2020/05/27/unordered-map-hash-pair-c/)
* 《C++ 标准库（第二版）》