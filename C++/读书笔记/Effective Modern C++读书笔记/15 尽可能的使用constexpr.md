# 15 尽可能的使用`constexpr`

​	`constexpr`和`const`一样，它们是编译期可知的。但是`const`不提供`constexpr`所能保证之事，因为`const`对象不需要在编译期初始化它的值。

​	简而言之，所有`constexpr`对象都是`const`，但不是所有`const`对象都是`constexpr`。

## constexpr函数

​	如果实参是编译期常量，这些函数将产出编译期常量；如果实参是运行时才能知道的值，它们就将产出运行时值。

### 标准迭代

**C++11**

​	C++11中，`constexpr`函数的代码**不超过一行语句：一个`return`**。

​	扩展：第一，使用三元运算符“`?:`”来代替`if`-`else`语句，第二，使用递归代替循环。

```C++
constexpr int pow(int base, int exp) noexcept
{
    return (exp == 0 ? 1 : base * pow(base, exp - 1));
}
```

**C++14**

​	`constexpr`函数限制为只能获取和返回**字面值类型**。

```c++
constexpr int pow(int base, int exp) noexcept   //C++14
{
    auto result = 1;
    for (int i = 0; i < exp; ++i) result *= base;
    
    return result;
}
```

## constexpr对象（字面值类型）

​	类的构造函数被声明为`constexpr`类型。即如果构造的入参是`constexpr`，产生的类对象就是`constexpr`。

```C++
class Point {
public:
    constexpr Point(double xVal = 0, double yVal = 0) noexcept
    : x(xVal), y(yVal)
    {}

    constexpr double xValue() const noexcept { return x; } 
    constexpr double yValue() const noexcept { return y; }

    void setX(double newX) noexcept { x = newX; }
    void setY(double newY) noexcept { y = newY; }

private:
    double x, y;
};
```

​	`Point`的构造函数可被声明为`constexpr`，因为如果传入的参数在编译期可知，`Point`的数据成员也能在编译器可知。因此这样初始化的`Point`就能为`constexpr`：

```cpp
constexpr Point p1(9.4, 27.7);  //没问题，constexpr构造函数
                                //会在编译期“运行”
constexpr Point p2(28.8, 5.3);  //也没问题
```