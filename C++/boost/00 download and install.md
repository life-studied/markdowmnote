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

### 更详细的b2编译

```shell
.\b2.exe install --toolset=msvc-14.2 --build-type=complete --prefix="D:\cppsoft\boost_1_84_0" link=static runtime-link=shared threading=multi debug release
```

1. install可以更改为stage, stage表示只生成库(dll和lib), install还会生成包含头文件的include目录。一般来说用stage就可以了，我们将生成的lib和下载的源码包的include头文件夹放到项目要用的地方即可。
2. toolset 指定编译器，`gcc`用来编译生成linux用的库，`msvc-14.2（VS2019）`用来编译windows使用的库，版本号看你的编译器比如`msvc-10.0（VS2010）`，我的是`VS2019`所以是`msvc-14.2`。
3. 如果选择的是install 命令，指定生成的库文件夹要用`--prefix`，如果使用的是stage命令，需要用`--stagedir`指定。
4. `link` 表示生成动态库还是静态库，`static`表示生成lib库，`shared`表示生成dll库。
5. `runtime-link` 表示用于指定运行时链接方式为静态库还是动态库，指定为`static`就是`MT`模式，指定`shared`就是`MD`模式。`MD` 和 `MT` 是微软 Visual C++ 编译器的选项，用于指定运行时库的链接方式。这两个选项有以下区别：
   - `/MD`：表示使用多线程 DLL（Dynamic Link Library）版本的运行时库。这意味着你的应用程序将使用动态链接的运行时库（MSVCRT.dll）。这样的设置可以减小最终可执行文件的大小，并且允许应用程序与其他使用相同运行时库版本的程序共享代码和数据。
   - `/MT`：表示使用多线程静态库（Static Library）版本的运行时库。这意味着所有的运行时函数将被静态链接到应用程序中，使得应用程序不再依赖于动态链接的运行时库。这样可以确保应用程序在没有额外依赖的情况下独立运行，但可能会导致最终可执行文件的体积增大。

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

## 参考资料

* [visualstudio配置boost · 恋恋风辰的编程笔记 (llfc.club)](https://gitbookcpp.llfc.club/sections/cpp/project/day03.html)
