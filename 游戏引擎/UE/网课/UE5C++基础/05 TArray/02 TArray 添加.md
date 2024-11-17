---
create: 2023-12-18
---
# TArray 添加

## Add

​	检测是否合法，底层调用Emplace进行添加

```C++
TArray<int32> AInt32s = {1,2,3,4,5};
int32 Index = AInt32s.Add(6);
// 一致
int32 Index = AInt32s.Emplace(6);
```

## Add_GetRef

​	添加并返回引用

```C++
TArray<int32> AInt32s = {1,2,3,4,5};
auto& ref = AInt32s.Add_GetRef(6);
```

## AddZeroed

​	添加默认0值，返回下标为起始添加点。

```C++
TArray<int32> AInt32s = {1,2,3,4,5};
int32 Index = AInt32s.AddZeroed();		//index=5

// 添加了100个默认值
int32 Index = AInt32s.AddZeroed(100);	//index=6

int32 Index = AInt32s.AddZeroed();	//index=106
```

## AddDefault

​	添加默认构造

```C++
struct FTest
{
    int32 a;
    float b;
    FString c;
}

TArray<FTest> Atests;
Atest.AddDefault();
```

