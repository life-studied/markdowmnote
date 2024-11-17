---
create: 2023-09-14
---
# STL中at和[]运算符的区别

## 1. vector

### 1.1 基础

​	在`vector`中，`[]`和`at`都会返回一个引用，因此在返回值上是相同的。

​	但是，`at`会**额外帮你检查是否越界**的问题，如果越界，会抛出一个`std::out_of_range`的异常。

```C++
std::vector<int> v{1,2,3};

//read
cout << v[0]; 		//OK
cout << v.at(0);	//OK

//write
v[0] = 6;		//OK
v.at(0) = 7;	//OK
v.operator[](2) = 8;	//OK

//ub:out of range
cout << v[3];		//ub
cout << v[-1];		//ub

//exception: out_of_range
cout << v.at(3);	//throw exception
```

### 1.2 const xxx& operator[]

​	以`int`类型为例。假设有下面这个类，它有两个`operator[]`的版本，`const`和`no const`：

```C++
class MyVec
{
public:
    MyVec(int n): data_{n} {}
    
    int& operator[](int index) {
        return data_[index];
    }
    
    const int& operator[](int index) const {
        return data_[index];
    }

private:
    vector<int> data_;
}
```

​	什么时候调用第一个，什么时候调用第二个？

```C++
//no const
MyVec v1(3);
cout << v1[2] << endl;
v1[2] = 6;

//const 
const MyVec& v2 = v1;
cout << v2[2] <<endl;	//only read
```

## 2. map

### 2.1 []的问题

​	在`map`中，`[]`和`at`的区别更大。

#### 2.1.1 默认赋值

```C++
std::map<char, int> m{{'a', 65}, {'b', 66}, {'c', 67}};

//read exists
cout << m['a'];		//OK
cout << m.at('a');	//OK

//read not exists
cout << m['x'];		//OK??? given a default value, create extra space for it
cout << m.at('x');	//throw exception: out_of_range

//now: m{{'a', 65}, {'b', 66}, {'c', 67}, {'x', 0}}
```

#### 2.2.2 不支持const map

```C++
const std::map<char, int> m{{'a', 65}, {'b', 66}, {'c', 67}};
cout << m.at('a');		//OK
cout << m['a'];			//doesn't compile, [] is not a const function
```

### 2.2 合适的用法

```C++
cout << m.count('x') ? m['x'] : -1;		//OK, but two queries

//one query version
auto it = m.find('x');
cout << it != m.end() ? it->second : -1;
```

