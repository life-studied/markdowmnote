---
create: 2024-03-02
---
# 算法（Matrix and vector）

[TOC]

## 加法和减法

​	左侧和右侧必须具有相同的行数和列数。它们还必须具有相同的类型`Scalar`。

- 二进制运算符 `+` 如`a+b`
- 二进制运算符 `-` 如`a-b`
- 一元运算符 `-` 如`-a`
- 复合运算符 `+=` 如`a+=b`
- 复合运算符 `-=` 如`a-=b`

```C++
#include <iostream>
#include <Eigen/Dense>
 
int main()
{
  Eigen::Matrix2d a;
  a << 1, 2,
       3, 4;
  Eigen::MatrixXd b(2,2);
  b << 2, 3,
       1, 4;
  std::cout << "a + b =\n" << a + b << std::endl;
  std::cout << "a - b =\n" << a - b << std::endl;
  std::cout << "Doing a += b;" << std::endl;
  a += b;
  std::cout << "Now a =\n" << a << std::endl;
  Eigen::Vector3d v(1,2,3);
  Eigen::Vector3d w(1,0,0);
  std::cout << "-v + w - v =\n" << -v + w - v << std::endl;
}

/*
a + b =
3 5
4 8
a - b =
-1 -1
 2  0
Doing a += b;
Now a =
3 5
4 8
-v + w - v =
-1
-4
-6
*/
```

## 标量乘法和除法

​	标量的乘法和除法也非常简单。

- 二进制运算符 `*` 如`matrix*scalar`
- 二进制运算符 `*` 如`scalar*matrix`
- 二进制运算符 `/` 如`matrix/scalar`
- 复合运算符 `*=` 如`matrix*=scalar`
- 复合运算符 `/=` 如`matrix/=scalar`

```C++
#include <iostream>
#include <Eigen/Dense>
 
int main()
{
  Eigen::Matrix2d a;
  a << 1, 2,
       3, 4;
  Eigen::Vector3d v(1,2,3);
  std::cout << "a * 2.5 =\n" << a * 2.5 << std::endl;
  std::cout << "0.1 * v =\n" << 0.1 * v << std::endl;
  std::cout << "Doing v *= 2;" << std::endl;
  v *= 2;
  std::cout << "Now v =\n" << v << std::endl;
}

/*
a * 2.5 =
2.5   5
7.5  10
0.1 * v =
0.1
0.2
0.3
Doing v *= 2;
Now v =
2
4
6
*/
```

## 转置、共轭和伴随矩阵

​	通过成员函数以完成。

* 转置：[transpose()](https://eigen.tuxfamily.org/dox/classEigen_1_1DenseBase.html#a43cbcd866a0737eb56642c2e992f0afd)
* 共轭：[conjugate()](https://eigen.tuxfamily.org/dox/group__TutorialMatrixArithmetic.html)
* 伴随：[adjoint()](https://eigen.tuxfamily.org/dox/classEigen_1_1MatrixBase.html#afacca1f88da57e5cd87dd07c8ff926bb)

```C++
MatrixXcf a = MatrixXcf::Random(2,2);
cout << "Here is the matrix a\n" << a << endl;

cout << "Here is the matrix a^T\n" << a.transpose() << endl;

cout << "Here is the conjugate of a\n" << a.conjugate() << endl;

cout << "Here is the matrix a^*\n" << a.adjoint() << endl;
 
/*
Here is the matrix a
 (-0.211,0.68) (-0.605,0.823)
 (0.597,0.566)  (0.536,-0.33)
Here is the matrix a^T
 (-0.211,0.68)  (0.597,0.566)
(-0.605,0.823)  (0.536,-0.33)
Here is the conjugate of a
 (-0.211,-0.68) (-0.605,-0.823)
 (0.597,-0.566)    (0.536,0.33)
Here is the matrix a^*
 (-0.211,-0.68)  (0.597,-0.566)
(-0.605,-0.823)    (0.536,0.33)
*/
```

### 不要在转置的同时赋值同一对象

​	因为会在结果写入的同时计算转置，结果与预期是不一致的。

```C++
Matrix2i a; a << 1, 2, 3, 4;
cout << "Here is the matrix a:\n" << a << endl;
 
a = a.transpose(); // !!! do NOT do this !!!
cout << "and the result of the aliasing effect:\n" << a << endl;

/*
Here is the matrix a:
1 2
3 4
and the result of the aliasing effect:
1 2
2 4
*/
```

### 使用[transposeInPlace()](https://eigen.tuxfamily.org/dox/classEigen_1_1DenseBase.html#ac501bd942994af7a95d95bee7a16ad2a)代替赋值

​	如果需要对某一矩阵进行对应操作，应该使用InPlace函数。

```C++
MatrixXf a(2,3); a << 1, 2, 3, 4, 5, 6;
cout << "Here is the initial matrix a:\n" << a << endl;
 
a.transposeInPlace();
cout << "and after being transposed:\n" << a << endl;

/*
Here is the initial matrix a:
1 2 3
4 5 6
and after being transposed:
1 4
2 5
3 6
*/
```

## 混叠问题

​	混叠问题是指，在原矩阵边写入某些元素的计算结果，边使用计算结果进行其它元素的计算。通常发生在对某一矩阵进行操作时，将其赋值给其本身。例如：转置操作，伴随操作等。

```C++
Matrix2i a; a << 1, 2, 3, 4;
cout << "Here is the matrix a:\n" << a << endl;
 
a = a.transpose(); // !!! do NOT do this !!!
cout << "and the result of the aliasing effect:\n" << a << endl;
```

### 乘法除外

​	乘法操作会被编译过程中使用一个临时变量存储以避免**混叠问题**：

```C++
m = m * m;
// ==
tmp = m * m;
m = tmp;
```

### 加速乘法

​	乘法为了防止混叠问题，有额外的开销。如果目标矩阵与计算矩阵无关，可以使用noalias()函数直接计算并赋值。

```C++
c.noalias() += a * b;
c.noalias() -= 2 * a.adjoint() * b;
```

## 点乘和叉乘（dot&cross）

```C++
#include <iostream>
#include <Eigen/Dense>
 
int main()
{
	Eigen::Vector3d v(1,2,3);
    Eigen::Vector3d w(0,1,2);
 
	std::cout << "Dot product: " << v.dot(w) << std::endl;
	double dp = v.adjoint()*w; // automatic conversion of the inner product to a scalar
	std::cout << "Dot product via a matrix product: " << dp << std::endl;
	std::cout << "Cross product:\n" << v.cross(w) << std::endl;
}
```

## 基本算法操作

```C++
#include <iostream>
#include <Eigen/Dense>
 
using namespace std;
int main()
{
  Eigen::Matrix2d mat;
  mat << 1, 2,
         3, 4;
  cout << "Here is mat.sum():       " << mat.sum()       << endl;	// 加和
  cout << "Here is mat.prod():      " << mat.prod()      << endl;	// 乘积
  cout << "Here is mat.mean():      " << mat.mean()      << endl;	// 平均
  cout << "Here is mat.minCoeff():  " << mat.minCoeff()  << endl;	// 最小值
  cout << "Here is mat.maxCoeff():  " << mat.maxCoeff()  << endl;	// 最大值
  cout << "Here is mat.trace():     " << mat.trace()     << endl;	// 迹
}
```

## 获取最值元素及其位置

```C++
  Matrix3f m = Matrix3f::Random();
  std::ptrdiff_t i, j;
  float minOfM = m.minCoeff(&i,&j);
  cout << "Here is the matrix m:\n" << m << endl;
  cout << "Its minimum coefficient (" << minOfM 
       << ") is at position (" << i << "," << j << ")\n\n";
 
  RowVector4i v = RowVector4i::Random();
  int maxOfV = v.maxCoeff(&i);
  cout << "Here is the vector v: " << v << endl;
  cout << "Its maximum coefficient (" << maxOfV 
       << ") is at position " << i << endl;

/*
Here is the matrix m:
  0.68  0.597  -0.33
-0.211  0.823  0.536
 0.566 -0.605 -0.444
Its minimum coefficient (-0.605) is at position (2,1)

Here is the vector v:  1  0  3 -3
Its maximum coefficient (3) is at position 2
*/
```

