# std::bitset

[TOC]

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

### 2.1 默认构造函数

```C++
bitset<10> a;			//a:0000000000
```

### 2.2 数值构造函数

1. 当用一个数值去构造的时候，其实就是将数值在内存中的存储方式显示出来。(数值在内存中是以补码形式存储的)
2. 若bitset的位数n小于数值的位数,只取数值(小端的)前n位初始化给bitset

```C++
bitset<4> a(-16);		//-16的补码为11111111.....10000,a有4位,因此a:0000

bitset<5> a(17);		 //17的补码为00000000.....10001,a有5位,因此a:10001

bitset<6> a(-8);		//-8的补码为 11111111.....11000,a有6位,因此a:111000

bitset<7> a(8);			 //8的补码为 00000000.....01000,a有7位,因此a:0001000
```

### 2.3 字符串构造函数

​	用字符串`string` 或者 `char[]`初始化。以`string`为例，`char[]`与其用法相同：

```C++
string b = "100101111";	//这里特别注意，bitset的size和字符串长度不匹配的时候如何构造

bitset<3> a(b);			//a:100			  //当bitset的size小于等于字符串长度，取字符串的前size位

bitset<6> a(b);			//a:100101

bitset<9> a(b);			//a:100101111

bitset<12> a(b);		//a:000100101111  //当bitset的size大于字符串长度，进行补零
```

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

​	全部置1,或者某一位置1或0

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

​	全部置0,或者某一位置0

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

​	全部取反,或者某一位取反

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