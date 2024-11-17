---
create: 2023-09-23
---
# 01 FString中文问题和基础赋值问题          

## 1.1 赋值

FString兼容：

* TCHAR
* char

```C++
FString S = TEXT("hello");		//L""
FString S1 = S;
FString S2(S);

const TCHAR* p = TEXT("!@#$%^&*()");
S1 = p;

FString S3(5,p);		//仅拷贝前5个
FString S4(p,5);		//S4.MAX = p.MAX + 5

const char* ptr = "hello";
FString S6(ptr);
```

## 1.2 编码转换

```C++
char *ptr = "你好";
TCHAR* CCC = UTF8_TO_TCHAR(ptr);	//ascii to wide byte
FString s(CCC);
```

