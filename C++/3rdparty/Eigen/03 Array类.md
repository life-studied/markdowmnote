---
create: 2024-03-02
modified: '2024-11-17'
---

# Array类

> ​	Array类是用于提供通用数组的类，而不是用于线性代数的Matrix类。Array类提供了一种执行系数运算的简单方法，这些运算可能不具有线性代数意义。例如向数组中的每个系数添加一个常数或将两个数组系数相乘。

[TOC]

## 模板说明

​	与Matrix类似，有三个参数：类型，行数和列数。

```C++
Array<typename Scalar, int RowsAtCompileTime, int ColsAtCompileTime>
```

## 基本类型

| Type                          | Typedef  |
| :---------------------------- | :------- |
| Array<float,Dynamic,1>        | ArrayXf  |
| Array<float,3,1>              | Array3f  |
| Array<double,Dynamic,Dynamic> | ArrayXXd |
| Array<double,3,3>             | Array33d |

## 访问元素

​	与Matrix类似，通过()运算符来访问和修改。

```C++
#include <Eigen/Dense>
#include <iostream>
 
int main()
{
  Eigen::ArrayXXf  m(2,2);
  
  // assign some values coefficient by coefficient
  m(0,0) = 1.0; m(0,1) = 2.0;
  m(1,0) = 3.0; m(1,1) = m(0,1) + m(1,0);
  
  // print values to standard output
  std::cout << m << std::endl << std::endl;
 
  // using the comma-initializer is also allowed
  m << 1.0,2.0,
       3.0,4.0;
     
  // print values to standard output
  std::cout << m << std::endl;
}
```

## 加法和减法

### 两个Array

​	与Matrix类似。

```C++
#include <Eigen/Dense>
#include <iostream>
 
int main()
{
  Eigen::ArrayXXf a(3,3);
  Eigen::ArrayXXf b(3,3);
  a << 1,2,3,
       4,5,6,
       7,8,9;
  b << 1,2,3,
       1,2,3,
       1,2,3;
       
  // Adding two arrays
  std::cout << "a + b = " << std::endl << a + b << std::endl << std::endl;
}
```

### 每个系数加减一个常数

​	提供了与Matrix不同的操作。

```C++
#include <Eigen/Dense>
#include <iostream>
 
int main()
{
  Eigen::ArrayXXf a(3,3);
  Eigen::ArrayXXf b(3,3);
  a << 1,2,3,
       4,5,6,
       7,8,9;
  b << 1,2,3,
       1,2,3,
       1,2,3;
    
  // Subtracting a scalar from an array
  std::cout << "a - 2 = " << std::endl << a - 2 << std::endl;
}
```

## Array乘法

​	Array的乘法被解释为：对应系数相乘。

```C++
#include <Eigen/Dense>
#include <iostream>
 
int main()
{
  Eigen::ArrayXXf a(2,2);
  Eigen::ArrayXXf b(2,2);
  a << 1,2,
       3,4;
  b << 5,6,
       7,8;
  std::cout << "a * b = " << std::endl << a * b << std::endl;
}

/*
a * b = 
 5 12
21 32
*/
```

## 其它系数计算函数

* abs：取每个系数的绝对值
* sqrt：取每个系数的平方根
* min：取两个Array中系数更小的那个

```C++
#include <Eigen/Dense>
#include <iostream>
 
int main()
{
  Eigen::ArrayXf a = Eigen::ArrayXf::Random(5);
  a *= 2;
  std::cout << "a =" << std::endl
            << a << std::endl;
  std::cout << "a.abs() =" << std::endl
            << a.abs() << std::endl;
  std::cout << "a.abs().sqrt() =" << std::endl
            << a.abs().sqrt() << std::endl;
  std::cout << "a.min(a.abs().sqrt()) =" << std::endl
            << a.min(a.abs().sqrt()) << std::endl;
}

/*
a =
  1.36
-0.422
  1.13
  1.19
  1.65
a.abs() =
 1.36
0.422
 1.13
 1.19
 1.65
a.abs().sqrt() =
1.17
0.65
1.06
1.09
1.28
a.min(a.abs().sqrt()) =
  1.17
-0.422
  1.06
  1.09
  1.28
*/
```

## 在Matrix和Array之间转换

* `.array()`：将Matrix->Array
* `.matrix()`：将Array->Matrix

注意：

1. 允许直接将数组的计算结果赋值给矩阵。
2. 允许直接将矩阵的计算结果赋值给数组
3. 矩阵可以使用`cwiseProduct`成员方法直接计算系数乘积。

**基本案例**

​	展示了注意事项和基本操作。

```C++
#include <Eigen/Dense>
#include <iostream>
 
using Eigen::MatrixXf;
 
int main()
{
  MatrixXf m(2,2);
  MatrixXf n(2,2);
  MatrixXf result(2,2);
 
  m << 1,2,
       3,4;
  n << 5,6,
       7,8;
 
  result = m * n;
  std::cout << "-- Matrix m*n: --\n" << result << "\n\n";
  result = m.array() * n.array();
  std::cout << "-- Array m*n: --\n" << result << "\n\n";
  result = m.cwiseProduct(n);
  std::cout << "-- With cwiseProduct: --\n" << result << "\n\n";
  result = m.array() + 4;
  std::cout << "-- Array m + 4: --\n" << result << "\n\n";
}
```

**高级案例**

​	展示了更复杂的使用。

```C++
#include <Eigen/Dense>
#include <iostream>
 
using Eigen::MatrixXf;
 
int main()
{
  MatrixXf m(2,2);
  MatrixXf n(2,2);
  MatrixXf result(2,2);
 
  m << 1,2,
       3,4;
  n << 5,6,
       7,8;
  
  result = (m.array() + 4).matrix() * m;
  std::cout << "-- Combination 1: --\n" << result << "\n\n";
  result = (m.array() * n.array()).matrix() * m;
  std::cout << "-- Combination 2: --\n" << result << "\n\n";
}
```