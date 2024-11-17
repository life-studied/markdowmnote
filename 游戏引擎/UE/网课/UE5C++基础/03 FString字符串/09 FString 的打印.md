---
create: 2023-12-15
---
# 09 FString 的打印

## 使用Printf

```C++
FString S1 = TEXT("UE4");
int32 a = 10;
float f = 100.f;

FString S3 = FString::Printf(TEXT("Hello %s,%i,%f"),*S1,a,f);	//打印同时赋值
```

## 使用Appendf

​	在原有字符串后进行格式化合并。

```C++
FString S1 = TEXT("UE4");
int32 a = 10;
float f = 100.f;

FString S3 = TEXT("Hello ");
FString S4 = S3.Appendf(TEXT("World %s,%i,%f"),*S1,a,f);	//Hello World UE4,10,100.0
```

## Format

```C++
FString S5 = FString::Format(TEXT("sai = {0}{1}"), { S2, S3 });
```

