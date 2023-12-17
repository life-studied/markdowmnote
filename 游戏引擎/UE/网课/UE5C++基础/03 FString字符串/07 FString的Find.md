# 07 FString的Find

## 1. Find

```C++
FString s1 = TEXT("Hello World");

// 查找字符串
// 是否忽略大小写
// 搜索逆序/顺序
// 起始index
auto i = s1.Find(TEXT("lo"), ESearchCase::IgnoreCase, ESearchDir::FromStart, INDEX_NONE);
```

## 2. FindChar

```C++
FString s1 = TEXT("Hello World");

// 查找字符
// 返回下标
// 返回值：bool
int32 Index = 0;
auto isFind = s1.FindChar(TEXT('o'), Index);
```

## 3. FindLastChar

​	逆序找Char

```C++
FString s1 = TEXT("Hello World");

// 查找字符
// 返回下标
// 返回值：bool
int32 Index = 0;
auto isFind = s1.FindLastChar(TEXT('o'), Index);
```

## 4. FindLastCharByPredicate

​	自定义条件搜索（lambda），从后往前

```C++
FString s1 = TEXT("Hello World");

// 自定义搜索
// 返回值：index
auto index = s1.FindLastCharByPredicate([](TCHAR Ch)
                                         {
                                             return Ch == TEXT('o');
                                         });
```

