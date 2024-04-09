# download and install

## download

​	在boost官网下载Windows版本的zip文件。

![image-20240409141439022](./assets/image-20240409141439022.png)

## install

### bat

​	双击bat文件，生成编译可执行文件`b2.exe`

![image-20240409141542713](./assets/image-20240409141542713.png)

### b2

​	使用生成的b2.exe执行lib的编译。

```shell
.\b2.exe toolset=msvc		# linux: ./b2.exe toolset=gcc
```

![image-20240409142158849](./assets/image-20240409142158849.png)

## header

​	包含目录为boost的根目录：`E:\3rdparty\boost_1_84_0`

![image-20240409143806894](./assets/image-20240409143806894.png)

## lib

​	lib目录为根目录下的stage/lib:`E:\3rdparty\boost_1_84_0\stage\lib`

![image-20240409143918583](./assets/image-20240409143918583.png)

## test

```C++
#include <iostream>
#include <string>
#include "boost/lexical_cast.hpp"
int main()
{
    using namespace std;
    cout << "Enter your weight: ";
    float weight;
    cin >> weight;
    string gain = "A 10% increase raises ";
    string wt = boost::lexical_cast<string> (weight);
    gain = gain + wt + " to ";      // string operator()
    weight = 1.1 * weight;
    gain = gain + boost::lexical_cast<string>(weight) + ".";
    cout << gain << endl;
    system("pause");
    return 0;
}
```

