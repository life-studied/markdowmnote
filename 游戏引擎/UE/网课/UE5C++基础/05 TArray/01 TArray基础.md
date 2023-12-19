# TArray基础

## 初始化

```C++
// 大括号
TArray<int32> AInt32s = {10, 20, 30, 40, 50};

// 数组
int32 cArray[1024] = {0};
TArray<int32> AInt32s(cArray, 1024);
```

## 拷贝赋值

```C++
TArray<int32> AInt32s = {10, 20, 30, 40, 50};

// copy 容器
TArray<int32> AInt32s1 = AInt32s;
TArray<int32> AInt32s2(AInt32s);
```

