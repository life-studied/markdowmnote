# 06 FString切割字符串

## 6.1 左右切割

### 6.1.1 保留一边

```C++
FString s1 = TEXT("Hello World");
FString s2 = TEXT("namespace");

FString h1 = s1.Left(3);	//Hel
FString h2 = s1.Right(3);	//rld

s1.LeftInline(3);	//Hel
s2.RightInline(3);	//rld
```

### 6.1.2 去除另一边（路径常用）

```C++
FString s1 = TEXT("Hello World");
FString s2 = TEXT("namespace");

FString h1 = s1.LeftChop(3);	//Hello Wo
FString h2 = s1.RightChop(3);	//lo World

s1.LeftChopInline(3);	//Hello Wo
s1.RightChopInline(3);	//lo World
```



## 6.2 