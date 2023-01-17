## C++中构造函数的互相调用

> 假设我们有一个写好的构造函数，我们现在想要重载一个构造函数。但是新的构造函数除了部分操作外，其他基本和原来写好的构造函数一致，这就出现了极大的代码重复。因此，引入构造函数的互相调用。

#### 方法一：在函数的初始化列表中调用另一个构造函数。（推荐）

```C++
int b = 2;
class Test
{
public:
    Test() : Test(b) {} //在初始化列表调用Test(int a)
    Test(int a) : p(a) {}
    ~Test() {}

    int get() { return p; }
private:
    int p;
};
```

#### 方法二：用this指针显示调用构造函数（g++不支持，vs支持）

```C++
int b = 2;
class Test
{
public:
    Test()
    {
        this->Test::Test(b);    //通过this指针显式调用Test(int a)
    }
    Test(int a) : p(a) {}
    ~Test() {}

    int get() { return p; }
private:
    int p;
};
```

#### 方法三：在原始内存覆盖

```C++
int b = 2;
class Test
{
public:
    Test()
    {
        //使用new (void*p) Type(…)，这种语句的意思是不重新分配内存，而是直接覆盖在原内存上。
        new (this) Test(b);
    }
    Test(int a) : p(a) {}
    ~Test() {}

    int get() { return p; }
private:
    int p;
};
```



