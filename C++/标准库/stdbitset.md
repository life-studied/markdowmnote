# std::bitset

[TOC]

---

​	bitset容器其实就是个01串。可以被看作是一个bool数组。它比bool数组更优秀的优点是：节约空间，节约时间，支持基本的位运算。在bitset容器中，8位占一个字节，相比于bool数组4位一个字节的空间利用率要高很多。同时，n位的bitset在执行一次位运算的复杂度可以被看作是n/32，这都是bool数组所没有的优秀性质。

## 1. 类模板

```C++
template <size_t N> class bitset;
```

1. `bitset`从名字就可以看出来，是一个(比特)二进制(0和1)的集合

2. 使用`bitset`必须指定类模板参数N，N表示bitset有几位

3. bitset在内存中是以4或8个字节为单位存储的
   1. `sizeof(bitset<1>) = 4`：1位的时候占4个字节
   2. `sizeof(bitset<32>) = 4`： 32位的时候占4个字节
   3. `sizeof(bitset<33>) = 8`： 33位的时候占8个字节
   4. `sizeof(bitset<65>) = 16`： 65位的时候占个16字节 (2倍增长动态分配内存空间)

## 2. 构造函数
### **bitset 的定义和初始化：**
1. `bitset<size> b;`　　//b 有 size 位，每位都是默认值 0
2. `bitset<size> b(num);`　　//b 是 unsigned long 型 num 的一个副本
3. `bitset<size> b(str);`　　//b 是 string 对象 str 中的位串副本，即 01 字符串
4. `bitset<size> b(str, pos);`　　//b 是 str 从位置 pos 开始的位串的副本 （从左往右）
5. `bitset<size> b(str, pos, n);`　　//b 是 str 中从位置 pos 开始，向后 n 位的副本


### bitset 的几个注意点：

　　1、bitset 最右边为下标开始，而 string 最左边！！！

　　2、bitset 支持位运算

　　3、可以通过下标来访问

　　4、包括函数：all(), any(), none(), test(), set([], []), reset([]), flip([]), _size(), count(), to_string(zero, one), to_ullong(), to_ulong()
　　
## 3. 重载运算符[]

**运算符重载**[],支持下标从0开始访问,与数组类似

**注意 :下标小的是小端**

```C++
bitset<4> a;	//a:0000(默认构造函数)
a[0] = 1;
a[2] = 1;
//a:0101

bitset<7> b("1001101");
for (int i = 0; i < 7; i++)
    cout << b[i] << ' ';//输出:1011001
```

## 4. 成员函数

### **count** 

​	返回`bitset`中 1 的个数

```C++
//成员函数声明
size_t count() const;
//用例
bitset<6> a("011101");
cout << a.count() // 4
```

### **size** 

​	返回size大小

```C++
//成员函数声明
size_t size() const;
//用例：
bitset<6> a("011101");
cout << a.size(); // 6
```

### **test** 

​	返回某一位(下标)是否为1

```C++
//成员函数声明
bool test (size_t pos) const;
//用例：
bitset<6> a("011101");
cout << a.test(0) << endl;	//1	(true)
cout << a.test(1) << endl;	//0	(false)
cout << a.test(5) << endl;	//0 (false)
```

### **any** 

​	只要有一位是1,就返回true,否则返回false

```C++
//成员函数声明
bool any() const;
//用例：
cout << bitset<4>("0001").any() << endl;	//1	(true)
cout << bitset<4>("0000").any() << endl;	//0	(false)
```

### **none** 

​	若全为0,返回true,否则返回false

```C++
//成员函数声明
bool none() const;
//用例：
cout << bitset<4>("0000").none() << endl;	//1	(true)
cout << bitset<4>("0001").none() << endl;	//0	(false)
```

### **all** 

​	若全为1,返回true,否则返回false

```C++
//成员函数声明
bool all() const noexcept;
//用例：
cout << bitset<4>("1111").all() << endl;	//1	(true)
cout << bitset<4>("1101").all() << endl;	//0	(false)
```

### **set** 

​	set()函数的作用是把bitset全部置为1. 特别地，set()函数里面可以传参数。set(u,v)的意思是把bitset中的第u位变成v,v∈0/1。

```C++
//成员函数声明
bitset& set() noexcept;
//用例：
//1.
bitset<6> a("011101");
a.set();
cout << a << endl;	//输出:111111

//2.set也可以指定参数,第一个参数是索引,第二个true表示置1,false表示置0
bitset& set (size_t pos, bool val = true);
//
bitset<6> a("011101");
a.set(0,0);
a.set(5,1);
cout << a << endl;	//输出:111100
```

### **reset** 

​	与set()函数相对地，reset()函数将bitset的所有位置为0。而reset()函数只传一个参数，表示把这一位改成0。

```C++
//成员函数声明
bitset& reset();
bitset& reset (size_t pos);
//用例：
bitset<6> a("011101");
a.reset();
cout << a << endl;	//输出:000000
//也可以指定参数,单独将某一位置0
bitset<6> a("011101");
a.reset(0);
cout << a << endl;	//输出:011100
```

### **flip** 

​	flip()与前两个函数不同，它的作用是将整个bitset容器按位取反。同上，其传进的参数表示把其中一位取反。

```C++
//成员函数声明
bitset& flip();	
bitset& flip (size_t pos);
//用例：
bitset<6> a("011101");
a.flip();
cout << a << endl;	//输出:100010
//也可以指定参数,单独将某一位取反
bitset<6> a("011101");
a.flip(0);
a.flip(1);
cout << a << endl;	//输出:011110
```

### **to_string( )** 

​	转换为字符串

```C++
bitset<6> a("011101");
auto x = a.to_string();
cout << x ;		//	011101
```

### **to_ulong( )** 

​	转换为无符号long类型

```C++
bitset<6> a("011101");
auto x = a.to_ulong();
cout << x ; // 输出:29 	1 + 4 + 8 + 16 = 29
```

### **to_ullong( )** 

​	转换为无符号long long类型

```C++
bitset<6> a("011101");
auto x = a.to_ullong();	//同上
cout << x ;   //29
```

## 参考资料
* https://www.zhihu.com/question/508249175/answer/2671905018?utm_id=0
* https://www.cnblogs.com/lemonyam/p/10631787.html
