# 03 FString字符串拼接

## 3.1 字符串拼接

* 使用+运算符
* 使用Append函数

```C++
FString s1 = TEXT("Hello World");
FString s2 = TEXT(" and you");
FString s3 = s1 + s2;	//拼接

const ANSICHAR* p = "string 2";

const TCHAR* p2 = TEXT("string 3");
int32 Len = FCString::Strlen(p2);		//strlen of TCHAR

s1.AppendChars(p, 4);		//char string
s1.AppendChars(p2, 4);		//TCHAR	string
s1.Append(s2);				//FString

s1.AppendChar(L'c');		//one wide char
s1.AppendChar('c');			//one char
```

