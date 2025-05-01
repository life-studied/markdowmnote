---
create: 2023-07-08
---
## g++案例

#### 目录结构

![](picture/目录结构.png)

#### 文件内容

```C++
//main.cpp
#include"add.h"
#include<iostream>
using namespace std;
int main(int argc,char* argv[])
{
    int a{};
    a = 1;
    a++;
    a++;
    a = add(1,3);
    cout<<a<<endl;
}


//add.h
#pragma once
int add(int x,int y);


//add.cpp
#include"add.h"
int add(int x,int y)
{
    return x+y;
}
```

#### 编译指令

> 指定头文件目录-I(大写i)
>
> 指定输出目录-o

```shell
g++ .\source\main.cpp .\source\add.cpp -I .\include\ -o ./output/main.exe
```

---



#### 更全面的指令

* -I(大写的i)：指定头文件目录，一般为incl或include
* -l(小写的l)：指定链接的库名，后面直接跟库名，例如libmath.a，为-lmath。
	* （因此库名是去掉lib和后缀的部分）
* -L(大写的L)：指定寻找上述库的目录。当前目录为.

**案例**

```shell
g++ .\source\add.cpp -I .\include -c -o .\midfile\add.o	 
g++ .\source\min.cpp -I .\include -c -o .\midfile\min.o  
ar -r .\lib\libmath.a .\midfile\min.o .\midfile\add.o
g++ .\source\main.cpp -lmath -Llib -I .\include\ -o .\output\main.exe
```

**最终结果**

![](../Linux相关/picture/最终结果.png)

## **使用makefile改进编写**

> 由于某种问题（后面已经解决，详见二和四），make找不到source下的文件，只能将源文件移动到根目录下

```makefile
main.exe:libmath.a main.o
	g++ .\midfile\main.o -lmath -Llib -I .\include\ -o .\output\main.exe
libmath.a:add.o min.o
	ar -r .\lib\libmath.a .\midfile\min.o .\midfile\add.o
add.o:add.cpp
	g++ add.cpp -I .\include -c -o .\midfile\add.o
min.o:min.cpp
	g++ min.cpp -I .\include -c -o .\midfile\min.o
main.o:main.cpp
	g++ main.cpp -I .\include -c -o .\midfile\main.o

clean:			#注意：make.exe调用的不是powershell,而是cmd,因此要用del代替rm命令
	del .\midfile\*.o
	del .\output\main.exe
	del .\lib\*.a
```

**最终结果**

![](../Linux相关/picture/最终结果2.png)

### **将makefile优化（一）**

> 与上图一致，只是将部分路径使用变量表示，并且加入了O2优化指令
>
> 关于头文件：依赖里可加可不加，但是命令里要加，并且要写路径

```makefile
INC = -I .\include
SOURCE = .\source\*.cpp
CFLAGS := -O2 -g
EXE = main.exe

$(EXE):libmath.a main.o
	g++ .\midfile\main.o -lmath -Llib $(INC) $(CFLAGS) -o .\output\main.exe
libmath.a:add.o min.o
	ar -r .\lib\libmath.a .\midfile\min.o .\midfile\add.o
add.o:add.cpp
	g++ add.cpp $(INC) -c $(CFLAGS) -o .\midfile\add.o
min.o:min.cpp
	g++ min.cpp $(INC) -c $(CFLAGS) -o .\midfile\min.o
main.o:main.cpp
	g++ main.cpp $(INC) -c $(CFLAGS) -o .\midfile\main.o

clean:			#注意：make.exe调用的不是powershell,而是cmd,因此要用del代替rm命令
	del .\midfile\*.o
	del .\output\main.exe
	del .\lib\*.a
```

### 将makefile优化（二）

> 解决了寻找source目录的问题，但是没有静态库。得出的结论是
>
> 1. 生成.o文件需要在依赖和命令中都指定路径
> 2. 生成可执行文件，只需要在命令中指定路径，不能指定依赖的路径
>
> 即：除了依赖源文件要加路径指明，其它都不用

```makefile
SRCPATH = .\source
INCPATH = .\include
MIDPATH = .\midfile
OUTPUTPATN = .\output
INC = -I $(INCPATH)
EXE = main.exe
CFLAGS := -O2 -g

$(EXE):main.o add.o min.o
	g++ $(MIDPATH)\main.o $(MIDPATH)\add.o $(MIDPATH)\min.o $(CFLAGS) -o $(OUTPUTPATN)\$(EXE)
main.o:$(SRCPATH)\main.cpp
	g++ $(SRCPATH)\main.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\main.o
add.o:$(SRCPATH)\add.cpp
	g++ $(SRCPATH)\add.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\add.o
min.o:$(SRCPATH)\min.cpp
	g++ $(SRCPATH)\min.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\min.o

clean:
	del .\midfile\*.o
	del .\output\main.exe
	del .\lib\*.a
```

![](../Linux相关/picture/最终结果3.png)

### **将makefile优化（三）**

> 使用静态库lib来封装.o文件

```makefile
SRCPATH = .\source
INCPATH = .\include
MIDPATH = .\midfile
OUTPUTPATN = .\output
LIBPATH = .\lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
EXE = main.exe
CFLAGS := -O2 -g

$(EXE):main.o libmath.a
	g++ $(MIDPATH)\main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)\$(EXE)
main.o:$(SRCPATH)\main.cpp
	g++ $(SRCPATH)\main.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\main.o
libmath.a:add.o min.o
	ar -r $(LIBPATH)\libmath.a $(MIDPATH)\min.o $(MIDPATH)\add.o
add.o:$(SRCPATH)\add.cpp
	g++ $(SRCPATH)\add.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\add.o
min.o:$(SRCPATH)\min.cpp
	g++ $(SRCPATH)\min.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\min.o

clean:
	del .\midfile\*.o
	del .\output\main.exe
	del .\lib\*.a
```

![](../Linux相关/picture/最终结果4.png)

### **将makefile优化（四）**

![](../Linux相关/picture/VPATH.png)

> 由此，可以设置VPATH变量，来省略cpp依赖中的路径（命令中的路径依旧要写）

```makefile
VPATH = .\source	#此处加了VPATH的路径
SRCPATH = .\source
INCPATH = .\include
MIDPATH = .\midfile
OUTPUTPATN = .\output
LIBPATH = .\lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
OBJECTS = main.o libmath.a
EXE = main.exe
CFLAGS := -O2 -g

$(EXE):$(OBJECTS)
	g++ $(MIDPATH)\main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)\$(EXE)
main.o:main.cpp		#此处的路径省略了
	g++ $(SRCPATH)\main.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\main.o
libmath.a:add.o min.o
	ar -r $(LIBPATH)\libmath.a $(MIDPATH)\min.o $(MIDPATH)\add.o
add.o:add.cpp		#此处的路径省略了
	g++ $(SRCPATH)\add.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\add.o
min.o:min.cpp		#此处的路径省略了
	g++ $(SRCPATH)\min.cpp -c $(INC) $(CFLAGS) -o $(MIDPATH)\min.o

clean:
	del .\midfile\*.o
	del .\output\main.exe
	del .\lib\*.a
```

### **将makefile优化（五）**

> 使用静态模式直接生成所有.o文件
>
> 使用`$<`作为依赖集合（依赖集合不需要加路径，因为在VPATH里设置了路径）
>
> 使用`$@`作为输出集合（输出集合需要加路径，因为没有一个输出的路径变量）

```makefile
VPATH = .\source		#依赖目录
SRCPATH = .\source
INCPATH = .\include
MIDPATH = .\midfile
OUTPUTPATN = .\output
LIBPATH = .\lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
OBJECTS = main.o add.o min.o
EXE = main.exe
CFLAGS := -O2 -g

$(EXE):main.o libmath.a
	g++ $(MIDPATH)\main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)\$(EXE)
libmath.a:add.o min.o
	ar -r $(LIBPATH)\libmath.a $(MIDPATH)\min.o $(MIDPATH)\add.o
$(OBJECTS):%.o:%.cpp		#静态模式 格式：文件集合：筛选条件：依赖集合
	g++ -c $(CFLAGS) $(INC) $< -o $(MIDPATH)\$@	#使用$<作为依赖，$@作为输出（且有路径）

clean:
	del .\midfile\*.o
	del .\output\main.exe
	del .\lib\*.a
```

### **将makefile优化（六）**

> 主要优化了clean的各种模式，便于选择性清理。

```makefile
VPATH = .\source
SRCPATH = .\source
INCPATH = .\include
MIDPATH = .\midfile
OUTPUTPATN = .\output
LIBPATH = .\lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
OBJECTS = main.o add.o min.o
EXE = main.exe
CFLAGS := -O2 -g

$(EXE):main.o libmath.a
	g++ $(MIDPATH)\main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)\$(EXE)
	echo exe build success!
libmath.a:add.o min.o
	ar -r $(LIBPATH)\libmath.a $(MIDPATH)\min.o $(MIDPATH)\add.o
$(OBJECTS):%.o:%.cpp
	g++ -c $(CFLAGS) $(INC) $< -o $(MIDPATH)\$@

.PHONY:cleanall
cleanall:cleanobj cleanlib cleanapp
cleanobj:
	del .\midfile\*.o 
cleanlib:
	del .\lib\*.a
cleanapp:
	del $(OUTPUTPATN)\$(EXE)
```

### **将makefile优化（七）**

> 根据不同的操作系统启动不同的代码。
>
> 注意：Windows下路径使用\，而Linux下路径使用/
>
> 又由于不能将变量设置为\（处于行末自动变为换行，已经查过了：就是不能把\设置为变量）
>
> 所以只能写两套代码

[(19条消息) Linux与windows下makefile的不同之处_罗蜜斯丹的博客-CSDN博客_makefile 区分linux和win](https://blog.csdn.net/Langdon1996/article/details/105037904)

```makefile
ifeq ($(OS),Windows)		#Windows下
VPATH = .\source
SRCPATH = .\source
INCPATH = .\include
MIDPATH = .\midfile
OUTPUTPATN = .\output
LIBPATH = .\lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
OBJECTS = main.o add.o min.o
EXE = main.exe
CFLAGS := -O2 -g

$(EXE):main.o libmath.a
	g++ $(MIDPATH)\main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)\$(EXE)
	echo exe build success!
libmath.a:add.o min.o
	ar -r $(LIBPATH)\libmath.a $(MIDPATH)\min.o $(MIDPATH)\add.o
$(OBJECTS):%.o:%.cpp
	g++ -c $(CFLAGS) $(INC) $< -o $(MIDPATH)\$@
else						#Linux下
VPATH = ./source
SRCPATH = ./source
INCPATH = ./include
MIDPATH = ./midfile
OUTPUTPATN = ./output
LIBPATH = ./lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
OBJECTS = main.o add.o min.o
EXE = main.exe
CFLAGS := -O2 -g

$(EXE):main.o libmath.a
	g++ $(MIDPATH)/main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)/$(EXE)
	echo exe build success!
libmath.a:add.o min.o
	ar -r $(LIBPATH)/libmath.a $(MIDPATH)/min.o $(MIDPATH)/add.o
$(OBJECTS):%.o:%.cpp
	g++ -c $(CFLAGS) $(INC) $< -o $(MIDPATH)/$@
endif


.PHONY:cleanall
cleanall:cleanobj cleanlib cleanapp
ifeq ($(OS),Windows_NT)		#Windows下
cleanobj:
	-del .\midfile\*.o 
cleanlib:
	-del .\lib\*.a
cleanapp:
	-del .\output\$(EXE)
else						#Linux下
UNAME := $(shell uname -s)
ifeq ($(UNAME),Linux)
cleanobj:
	-rm -f ./midfile/*.o
cleanlib:
	-rm -f ./lib/*.a
cleanapp:
	-rm -f ./output/$(EXE)
else
endif
endif
```

### **将makefile优化（八）**

> 对于问题七，可以使用两个程序，根据操作系统，来切换makefile里的路径表示符
>
> 但是出现另一个问题：clean模块不需要被替换

> 因此：创建另一个文件来存储clean模块，并在makefile里include这个模块，这样替换时就不会被替换，同时也能调用到clean模块。另外创建transform模块来根据操作系统生成对应的程序并执行。

![](../Linux相关/picture/目录结构8.png)

>注意：经测试，在Windows下（vscode）使用g++编译出现bug，文件读取时最后一行的某些数据会重复，因此为了防止这个bug，将最后一行加入很多空格。（vs和Linux没有问题）
>
>问题解决：使用`ios::binary`，读写都要。

```C++
//L2W.cpp
#include <iostream>
#include <fstream>
#include <algorithm>
#include <cstring>
using namespace std;

int main(int argc, char* argv[]) {
    const int strnum = 1024;
    string file_path = "./makefile";   //文件路径
    ifstream instream(file_path,ios::in|ios::binary);
    if (!instream)
        cout << "input:open " << file_path << " error" << endl;
    char filestr[strnum] = { 0 };
    instream.read(filestr, strnum-2);
    instream.close();
    
    replace(filestr, filestr + strnum, '/', '\\');
    
    ofstream outstream(file_path,ios::out|ios::binary);
    if (!outstream)
        cout << "output:open " << file_path << " error" << endl;
    outstream.write(filestr, strlen(filestr));
    outstream.close();
    return 0;
}

//W2L.cpp
#include <iostream>
#include <fstream>
#include <algorithm>
#include <cstring>
using namespace std;

int main(int argc, char* argv[]) {
    const int strnum = 1024;
    string file_path = "./makefile";   //文件路径
    ifstream instream(file_path,ios::in|ios::binary);
    if (!instream)
        cout << "input:open " << file_path << " error" << endl;
    char filestr[strnum] = { 0 };
    instream.read(filestr, strnum-2);
    instream.close();
    
    replace(filestr, filestr + strnum, '\\', '/');
    
    ofstream outstream(file_path,ios::out|ios::binary);
    if (!outstream)
        cout << "output:open " << file_path << " error" << endl;
    outstream.write(filestr, strlen(filestr));
    outstream.close();
    return 0;
}
```

> makefile

```makefile
VPATH = .\source
SRCPATH = .\source
INCPATH = .\include
MIDPATH = .\midfile
OUTPUTPATN = .\output
LIBPATH = .\lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
OBJECTS = main.o add.o min.o
EXE = main.exe
CFLAGS := -O2 -g -std=c++11
TSFEXE = Transf.exe
$(EXE):main.o libmath.a
	g++ $(MIDPATH)\main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)\$(EXE)
	echo exe build success!
libmath.a:add.o min.o
	ar -r $(LIBPATH)\libmath.a $(MIDPATH)\min.o $(MIDPATH)\add.o
$(OBJECTS):%.o:%.cpp
	g++ -c $(CFLAGS) $(INC) $< -o $(MIDPATH)\$@

include transform.mk

include clean.mk
```

> clean.mk
>
> 注意：clean.mk不能放到文件夹中，否则在include .\mk\clean在Linux下会先进行检查，从而不能执行tsf命令

```makefile
.PHONY:cleanall
cleanall:cleanobj cleanlib cleanapp
ifeq ($(OS),Windows_NT)
cleanobj:
	-del .\midfile\*.o 
cleanlib:
	-del .\lib\*.a
cleanapp:
	-del .\output\$(EXE)
cleantsf:
	-del .\Transf.exe
else
UNAME := $(shell uname -s)
ifeq ($(UNAME),Linux)
cleanobj:
	-rm -f ./midfile/*.o
cleanlib:
	-rm -f ./lib/*.a
cleanapp:
	-rm -f ./output/$(EXE)
else
endif
endif
```

> transform.mk

```makefile
ifeq ($(OS),Windows_NT)
tsf:
	g++ L2W.cpp $(CFLAGS) -o $(TSFEXE)
	.\$(TSFEXE)
	-del .\$(TSFEXE)
else
UNAME := $(shell uname -s)
ifeq ($(UNAME),Linux)
tsf:
	g++ W2L.cpp $(CFLAGS) -o $(TSFEXE)
	./$(TSFEXE)
	rm -f ./$(TSFEXE)
else
endif
endif
```

### **将makefile优化（九）**

> 使用C++替换字符会因为makefile检查文件目录，不能将mk文件放到子目录下。所以使用python脚本代替C++程序，替换字符。

![](picture/目录9.png)

> 注意：在windows下使用python命令，在Linux下使用python3命令

```python
#trans.py
import re
import sys
f=open('./makefile','r')
alllines=f.readlines()
f.close()
f=open('./makefile','w+')

sysname = sys.platform
if sysname == "win32":
    for eachline in alllines:
        a=re.sub('/',r'\\',eachline)
        f.writelines(a)
elif sysname == "linux":
    for eachline in alllines:
        a=re.sub(r'\\','/',eachline)
        f.writelines(a)
f.close()
```

> makefile

```makefile
VPATH = ./source
SRCPATH = ./source
INCPATH = ./include
MIDPATH = ./midfile
OUTPUTPATN = ./output
LIBPATH = ./lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
OBJECTS = main.o add.o min.o
EXE = main.exe
CFLAGS := -O2 -g -std=c++11
TSFEXE = Transf.exe
$(EXE):main.o libmath.a
	g++ $(MIDPATH)/main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)/$(EXE)
	echo exe build success!
libmath.a:add.o min.o
	ar -r $(LIBPATH)/libmath.a $(MIDPATH)/min.o $(MIDPATH)/add.o
$(OBJECTS):%.o:%.cpp
	g++ -c $(CFLAGS) $(INC) $< -o $(MIDPATH)/$@


include ./mk/clean.mk
```

