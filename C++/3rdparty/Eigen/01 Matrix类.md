---
create: 2024-03-02
modified: '2024-11-17'
---

# Matrix类

[TOC]

## 基础模板参数说明

​	Matrix有6个模板参数，其中前三个是常用的参数，分别代表了

* 值类型
* 行数
* 列数

```C++
Matrix<typename Scalar, int RowsAtCompileTime, int ColsAtCompileTime>;
```

## 常见矩阵类型

​	本质上是Matrix类，使用了typedef的别名类型。

​	命名规则是：

* 方阵：`Matrix##num##Type`
* 行向量：`RowVector##num##Type`
* 列向量：`Vector##num##Type`

### 方阵

| 定义      | 说明                |
| --------- | ------------------- |
| Matrix2f  | 2*2的float矩阵      |
| Matrix3f  | 3*3的float矩阵      |
| Matrix4f  | 4*4的float矩阵      |
| Matrix2i  | 2*2的int矩阵        |
| Matrix3i  | 3*3的int矩阵        |
| Matrix4i  | 4*4的int矩阵        |
| Matrix2d  | 2*2的double矩阵     |
| Matrix3d  | 3*3的double矩阵     |
| Matrix4d  | 4*4的double矩阵     |
|           |                     |
| Matrix2cf | 2*2的复数float矩阵  |
| Matrix2cd | 2*2的复数double矩阵 |
| ......    | ......              |

### 行向量

​	行向量是特殊的Matrix。

| 定义        | 说明      |
| ----------- | --------- |
| RowVector2f | 1*2行向量 |
| ......      | ......    |

### 列向量

​	列向量是特殊的Matrix。

| 定义     | 说明      |
| -------- | --------- |
| Vector2f | 2*1列向量 |
| ......   | ......    |

## 特殊值：Dynamic

​	当行数和列数在编译期不确定时，可以使用特殊值`Dynamic`作为模板参数。

```C++
Matrix<double, Dynamic, Dynamic> md;
```

### 常见别名

​	命名规则：

* 矩阵：`Matrix##X##Type`
* 列向量：`Vector##X##Type`
* 行向量：`RowVector##X##Type`

| 定义        | 说明                               |
| ----------- | ---------------------------------- |
| MatrixXd    | `Matrix<double, Dynamic, Dynamic>` |
| VectorXi    | `Matrix<int, Dynamic, 1>`          |
| RowVectorXi | `Matrix<int, 1, Dynamic>`          |

## 构造

### 默认构造

```C++
Matrix3f a;
MatrixXf b;
```

- `a`是一个 3×3 矩阵，具有一个未初始化系数的普通浮点数 [9] 数组，
- `b`是一个动态大小矩阵，其大小当前为 0×0，其系数数组尚未分配。

### size构造

```C++
MatrixXf a(10,15);
VectorXf b(30);

// 合法 but no ops
Matrix3f a(3,3);
```

## 初始化

### old-way

```C++
Vector2d a(5.0, 6.0);
Vector3d b(5.0, 6.0, 7.0);
Vector4d c(5.0, 6.0, 7.0, 8.0);
```

### initializer_list

```C++
Matrix<int, 5, 1> b {1, 2, 3, 4, 5};
Matrix<int, 1, 5> c = {1, 2, 3, 4, 5};

MatrixXi a {      // construct a 2x2 matrix
      {1, 2},     // first row
      {3, 4}      // second row
};
Matrix<double, 2, 3> b {
      {2, 3, 4},
      {5, 6, 7},
};

VectorXd a {{1.5, 2.5, 3.5}};             // A column-vector with 3 coefficients
RowVectorXd b {{1.0, 2.0, 3.0, 4.0}};     // A row-vector with 4 coefficients
```

### 逗号初始化

​	可以使用如下方法进行逗号初始化。

```C++
Matrix3f m;
m << 1, 2, 3,
     4, 5, 6,
     7, 8, 9;
```

##  元素赋值与访问

```C++
#include <iostream>
#include <Eigen/Dense>
 
int main()
{
  Eigen::MatrixXd m(2,2);
  m(0,0) = 3;
  m(1,0) = 2.5;
  m(0,1) = -1;
  m(1,1) = m(1,0) + m(0,1);
  std::cout << "Here is the matrix m:\n" << m << std::endl;
  Eigen::VectorXd v(2);
  v(0) = 4;
  v(1) = v(0) - 1;
  std::cout << "Here is the vector v:\n" << v << std::endl;
}

/*
Here is the matrix m:
  3  -1
2.5 1.5
Here is the vector v:
4
3
*/
```

## Resize（调整大小）

​	只能调整`Dynamic`矩阵。禁止改变固定矩阵的大小。

```C++
#include <iostream>
#include <Eigen/Dense>
 
int main()
{
  Eigen::MatrixXd m(2,5);
  m.resize(4,3);
  std::cout << "The matrix m is of size "
            << m.rows() << "x" << m.cols() << std::endl;
  std::cout << "It has " << m.size() << " coefficients" << std::endl;
    
  Eigen::VectorXd v(2);
  v.resize(5);
  std::cout << "The vector v is of size " << v.size() << std::endl;
  std::cout << "As a matrix, v is of size "
            << v.rows() << "x" << v.cols() << std::endl;
}

/*
The matrix m is of size 4x3
It has 12 coefficients
The vector v is of size 5
As a matrix, v is of size 5x1
*/
```

## 矩阵赋值（operator=）

​	`=`会改变矩阵的大小和元素。

```C++
MatrixXf a(2,2);
std::cout << "a is of size " << a.rows() << "x" << a.cols() << std::endl;
MatrixXf b(3,3);
a = b;
std::cout << "a is now of size " << a.rows() << "x" << a.cols() << std::endl;

/*
a is of size 2x2
a is now of size 3x3
*/
```

## 可选模板参数

```C++
Matrix<typename Scalar,
       int RowsAtCompileTime,
       int ColsAtCompileTime,
       int Options = 0,
       int MaxRowsAtCompileTime = RowsAtCompileTime,
       int MaxColsAtCompileTime = ColsAtCompileTime>
```

* Options：存储模式。即元素在实际的数组中是按照行优先存储还是列优先存储。此机制会影响到[PlainObjectBase::data()](https://eigen.tuxfamily.org/dox/classEigen_1_1PlainObjectBase.html#ad12a492bcadea9b65ccd9bc8404c01f1)的表现。

  * ```C++
    Matrix<int, 3, 4, ColMajor> Acolmajor;
    Acolmajor << 8, 2, 2, 9,
                 9, 1, 4, 4,
                 3, 5, 4, 5;
    cout << "The matrix A:" << endl;
    cout << Acolmajor << endl << endl; 
     
    cout << "In memory (column-major):" << endl;
    for (int i = 0; i < Acolmajor.size(); i++)
          cout << *(Acolmajor.data() + i) << "  ";
    cout << endl << endl;
     
    Matrix<int, 3, 4, RowMajor> Arowmajor = Acolmajor;
    cout << "In memory (row-major):" << endl;
    for (int i = 0; i < Arowmajor.size(); i++)
    	cout << *(Arowmajor.data() + i) << "  ";
    cout << endl;
    
    /*
    The matrix A:
    8 2 2 9
    9 1 4 4
    3 5 4 5
    
    In memory (column-major):
    8  9  3  2  1  5  2  4  4  9  4  5  
    
    In memory (row-major):
    8  2  2  9  9  1  4  4  3  5  4  5 
    */
    ```

  * [Eigen](https://eigen.tuxfamily.org/dox/namespaceEigen.html) 中的默认值为 `column-major`

* MaxRowsAtCompileTime：虽然未知矩阵的具体大小，但是可以确定不会超过一个值，以此来**避免动态内存分配**。

  ```C++
  Matrix<float, Dynamic, Dynamic, 0, 3, 4>
  ```