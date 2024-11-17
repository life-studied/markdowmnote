---
create: 2023-12-18
---
# TArray 移除

## RemoveAt

​	根据下标移除。

```C++
//...
```

## Remove

​	根据值移除，返回下标

```C++
TArray<int32> TInt32s = {1,2,3,4};

auto index = TInt32s.Remove(3);	//移除3
```

## RemoveAll

​	移除所有符合条件的元素。

```C++
TArray<int32> TInt32s = {1,2,2,3};

auto index = TInt32s.RemoveAll([](auto& data){ return data==2;});	//移除所有2
```

## Empty

​	清空所有。

