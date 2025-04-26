---
create: '2025-04-18'
modified: '2025-04-18'
---

# VS-多字节字符集和Unicode字符集

## 字符集差别

Unicode字符集就是宽字符集，也就是wchar_t。在Windows上，wchar_t类型就是char16_t，也就是UTF16。（Linux和MacOS上则是char32_t，也就是UTF32）

多字节字符集就是用多个char来存储Unicode字符。

## 使用案例

例如，需要读取路径，而路径中存在中文。使用两种字符集都可以，但是处理起来不一样。

* 如果是Unicode字符集，那么读取到的路径就会存储到wchar_t[]中，每个字符占一个wchar_t。
* 如果是多字节字符集，那么读取到的路径就会存储到char[]中，每个字符可能占1个字节，也可能占多个字节。

```C++
#include <iostream>

#include <io.h>
#include <fcntl.h>

int main() {
	// 多字节字符集
	char szChar4[4] = "我"; //前两个字节存放汉字'我'，;后两个字节存放字符串结束符\0
	std::cout << szChar4 << "的字节长度 : " << strlen(szChar4) * sizeof(char) << std::endl;

	// 宽字节字符集
	_setmode(_fileno(stdout), _O_U16TEXT); // 设置标准输出为 UTF-16 模式
	wchar_t wcChar1 = L'我';
	std::wcout << wcChar1 << L"的字节长度 : " << sizeof(wchar_t) << std::endl;
	return 0;
}
```

## 影响

VS集成开发环境，字符集选择“使用多字节字符集”和“使用Unicode字符集”的直接区别就是：编译器是否增加了宏定义`UNICODE`。当选择“使用Unicode字符集”时，编译器会增加宏定义`UNICODE`；而选择“使用多字节字符集”时，编译器则不会增加宏定义`UNICODE`。

```C++
WINUSERAPI
int
WINAPI
MessageBoxA(
    __in_opt HWND hWnd,
    __in_opt LPCSTR lpText,
    __in_opt LPCSTR lpCaption,
    __in UINT uType);
WINUSERAPI
int
WINAPI
MessageBoxW(
    __in_opt HWND hWnd,
    __in_opt LPCWSTR lpText,
    __in_opt LPCWSTR lpCaption,
    __in UINT uType);
#ifdef UNICODE
#define MessageBox  MessageBoxW
#else
#define MessageBox  MessageBoxA
#endif // !UNICODE
```