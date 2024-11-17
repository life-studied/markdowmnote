---
create: 2023-12-18
---
# TArray Find

## Find

```C++
bool bFind = TestArray.Find(v, Index);
```

## FindByKey

```C++
auto* data = TestArray.FindByKey(v);
```

## FindByPredicate

```C++
auto* data = TestArray.FindByPredicate([](auto& _data){ return _data==1;});
```

## FindItemByClass

​	通过类型来寻找。(TArray中存储的是指针)

```C++
```

## FindLast

​	反向寻找。使用同Find。

## IndexOfByPredicate

​	通过lambda返回index