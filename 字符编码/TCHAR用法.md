# TCHAR用法

#### 1.char与wchar_t

##### 1.1char

char :单字节变量类型，最多表示256个字符

##### 1.2wchar_t

wchar_t :宽字节变量类型，用于表示Unicode字符

(定义在<string.h>里：typedef unsigned short wchar_t)

**注**：为了让编译器识别Unicode字符串，必须以在前面加一个“L”,定义宽字节类型方法如下：

```c++
wchar_t c = 'A';
wchar_t * p = L"Hello!";
wchar_t a[] = L"Hello!";
```

其中，宽字节类型每个变量占用2个字节

#### 2.TCHAR和_T()

##### 2.1TCHAR

如果在程序中既包括ANSI又包括Unicode编码，需要包括头文件tchar.h。TCHAR是定义在该头文件中的宏，它视你是否定义了`_UNICODE`宏而定义： 
定义了`_UNICODE`：  typedef wchar_t TCHAR ; 
没有定义_UNICODE： typedef char TCHAR ;

```c++
#ifdef UNICODE 
typedef char TCHAR; 
#else 
typede wchar_t TCHAR; 
#endif 
```

##### 2.2_T()

`_T( )`也是定义在该头文件中的宏，视是否定义了`_UNICODE`宏而定义： 
定义了`_UNICODE`：  `#define _T(x) L##x `
没有定义`_UNICODE`： `#define _T(x) x `

> 可以把一个引号引起来的字符串，根据你的环境设置，使得编译器会根据编译目标环境选择合适的（Unicode还是ANSI）字符处理方式。

> 如果你定义了UNICODE，那么`_T`宏会把字符串前面加一个L。这时 `_T("ABCD") `相当于 `L"ABCD" `，这是宽字符串。 
> 如果没有定义，那么`_T`宏不会在字符串前面加那个L，`_T("ABCD") `就等价于` "ABCD" `

注意：如果在程序中使用了TCHAR，那么就不应该使用ANSI的strXXX函数或者Unicode的wcsXXX函数了，而必须使用tchar.h中定义的_tcsXXX函数

#### 3.字符串前+L的作用

如 L"我的字符串"  表示将ANSI字符串转换成unicode的字符串，就是每个字符占用两个字节。 

 ```c++
 strlen("asd")  =  3;  
 strlen(L"asd")  =  6;
 ```

> 有时为了移植性，不能确定是否使用L时，可以用`_T`来处理。

#### 4.总结

> 总是用TCHAR代替char和wchar_t
>
> 总是用_T()来处理字符串，而不是用L或空置