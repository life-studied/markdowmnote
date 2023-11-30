# 防多开（C++）(Windows)

## 1. 实现描述

​	使用Windows Api实现，函数为`CreateEventW`，在头文件`<Windows.h>`中。（或者用`<synchapi.h>`+链接`Kernel32.lib`）

## 2. 代码

```C++
#include <Windows.h>
#include <iostream>

bool AutoExite()
{
    HANDLE hEvent = CreateEvent(NULL, FALSE, FALSE, L"project-name-yunyin-window-event");
    if(hEvent)
        SetEvent(hEvent);
    if (ERROR_ALREADY_EXISTS == GetLastError())
        return true;
    else
        return false;
}

int main()
{
    if (AutoExite())
        exit(0);
    std::cout << "Hello World!\n";
    system("pause");
}

```

