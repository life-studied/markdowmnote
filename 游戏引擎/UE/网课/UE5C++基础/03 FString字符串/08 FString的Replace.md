---
create: 2023-12-05
---
# 08 FString的Replace

## 1. Replace

​	对源字符串无影响。

```C++
FString s = TEXT("Hello World");
// 源
// 目的
// 忽略大小写（default）
// 返回值：替换后的string
auto newS = s.Replace(TEXT("llo"), TEXT("aaa"), ESearchCase::IgnoreCase);
```

## 2. ReplaceInline

​	内部替换

```C++
FString s = TEXT("Hello World");
// 源
// 目的
s.ReplaceInline(TEXT("llo"), TEXT("aaa"));
```

## 3. ReplaceCharWithEscapedChar

​	将字符串中所有转义字符，变为字面显式版本。

