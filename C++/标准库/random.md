---
create: 2023-09-04
---
# random

## 1.简介

​	在C语言中，使用`srand`和`rand`函数生成随机数，但是生成的随机数具有的最大值范围根据实现来决定的，在使用中可以出现下面的情况：

```C
int num = rand() % 10000;	//范围：1-32767 取余后导致生成的随机不均匀
```

​	因此，使用C++的随机生成库`<random>`来生成随机数。

## 2.均匀随机数生成

### 1.生成size_t整数

```C++
#include <random>
#include <iostream>

int main()
{
    std::random_device rnd;		// 使用硬件熵作为随机数种子
    std::mt19937 rng(rnd());	// 使用 Mersenne Twister 算法作为原始数生成器，种子为读取的硬件熵
    std::uniform_int_distribution<int> uni(90,100);		// 创建一个int均匀分布对象，输入上下限
    int num = uni(rng);	//生成数字
    std::cout << num << std::endl;
    return 0;
}
```

### 2.生成float小数

```C++
#include <random>
#include <iostream>

int main()
{
    std::random_device rnd;	// 使用硬件熵作为随机数种子
    std::mt19937 rng(rnd());	//使用 Mersenne Twister 算法作为原始数生成器，种子为读取的硬件熵
    std::uniform_real_distribution<float> unf(0.0f,1.0f);		//创建一个float均匀分布对象，输入上下限
    float num = unf(rng);	//生成数字
    std::cout << num << std::endl;
    return 0;
}
```

## 3.高斯分布随机数生成

```C++
#include <iostream>
#include <random>

int main() {
    std::random_device rd; // 使用硬件熵作为随机数种子
    std::mt19937 gen(rd()); // 使用 Mersenne Twister 算法作为原始数生成器，种子为读取的硬件熵

    // 创建一个double类型的高斯分布对象
    double mean = 0.0; // 均值
    double stddev = 1.0; // 标准差
    std::normal_distribution<double> dist(mean, stddev);

    // 生成随机数
    for (int i = 0; i < 10; ++i) { // 生成10个随机数
        double random_num = dist(gen);
        std::cout << random_num << std::endl;
    }

    return 0;
}

```

