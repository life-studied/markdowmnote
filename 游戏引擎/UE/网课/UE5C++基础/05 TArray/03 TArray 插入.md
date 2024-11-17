---
create: 2023-12-18
---
# TArray 插入

## Insert

```C++
TArray<int32> AInt32s = {1,2,3,4,5};
int32 Index = AInt32s.Insert(6,0);	//插入到第0个
```

## 其它插入

与Add基本一致：

* Insert_GetRef
* InsertDefault
* InsertZeroed
* ...