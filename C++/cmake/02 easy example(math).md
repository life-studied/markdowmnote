# 02 easy example(math)

[TOC]

​	关键字：cmake;project;add_executable;

---

## 2.1 source file

​	下面是源文件。

### add.c

```C++
#include <stdio.h>
#include "head.h"

int add(int a, int b)
{
    return a+b;
}
```

### sub.c

```C++
#include <stdio.h>
#include "head.h"

// 你好
int subtract(int a, int b)
{
    return a-b;
}
```

### mult.c

```C++
#include <stdio.h>
#include "head.h"

int multiply(int a, int b)
{
    return a*b;
}
```

### div.c

```C++
#include <stdio.h>
#include "head.h"

double divide(int a, int b)
{
    return (double)a/b;
}
```

### head.h

```C++
#ifndef _HEAD_H
#define _HEAD_H
// 加法
int add(int a, int b);
// 减法
int subtract(int a, int b);
// 乘法
int multiply(int a, int b);
// 除法
double divide(int a, int b);
#endif
```

### main.c

```C++
#include <stdio.h>
#include "head.h"

int main()
{
    int a = 20;
    int b = 12;
    printf("a = %d, b = %d\n", a, b);
    printf("a + b = %d\n", add(a, b));
    printf("a - b = %d\n", subtract(a, b));
    printf("a * b = %d\n", multiply(a, b));
    printf("a / b = %f\n", divide(a, b));
    return 0;
}
```

## 2.2 dictionary

​	下面是目录结构。

```shell
$ tree .
├── add.c
├── div.c
├── head.h
├── main.c
├── mult.c
└── sub.c
```

## 2.3 CMakeLists.txt

​	下面是CMakeLists.txt文件。

```cmake
cmake_minimum_required(VERSION 3.0)
project(CALC)
add_executable(app add.c div.c main.c mult.c sub.c)
```

## 2.4 cmake命令解释

​	本部分对上面的命令进行解释。

- `cmake_minimum_required`：指定使用的 cmake 的最低版本

  - **可选，非必须，如果不加可能会有警告**

- `project`：定义工程名称，并可指定工程的版本、工程描述、web主页地址、支持的语言（默认情况支持所有语言），如果不需要这些都是可以忽略的，只需要指定出工程名字即可。

  ```CMAKE
  # PROJECT 指令的语法是：
  project(<PROJECT-NAME> [<language-name>...])
  project(<PROJECT-NAME>
         [VERSION <major>[.<minor>[.<patch>[.<tweak>]]]]
         [DESCRIPTION <project-description-string>]
         [HOMEPAGE_URL <url-string>]
         [LANGUAGES <language-name>...])
         
  # 例子
  project(MyAwesomeProject
          VERSION 1.0.0
          DESCRIPTION "A cool project showcasing CMake"
          HOMEPAGE_URL "https://github.com/username/MyAwesomeProject"
          LANGUAGES CXX)
  ```

- `add_executable`：定义工程会生成一个可执行程序

  ```CMAKE
  add_executable(可执行程序名 源文件名称)
  ```

  - 这里的可执行程序名和`project`中的项目名没有任何关系

  - 源文件名可以是一个也可以是多个，如有多个可用空格或`;`间隔

    ```CMAKE
    # 样式1
    add_executable(app add.c div.c main.c mult.c sub.c)
    # 样式2
    add_executable(app add.c;div.c;main.c;mult.c;sub.c)
    ```