# TMap 添加

## 初始化

```C++
TMap<FName, FString> m1;
TMap<FName, FString> m2 = {{"1", "Hello"}, {"2", "World"}};
```

## Add

​	本质上调用了`Emplace`。

```C++
m1.Add("1", "Hello");
m1.Add("2", "World");

// 实际上是添加了一个TPair
m1.Add(TPair<FName, FString>("2", "World"));
```

## Emplace

​	与`Add`用法一致。

```C++
m1.Emplace("1", "Hello");
m1.Emplace("2", "World");
```

## FindOrAdd

​	如果没有找到，则添加

```C++
auto& value = m2.FindOrAdd("5");
```

## AddByHash

​	通过哈希值放入容器。

```C++
const uint32 hash = GetTypeHash("77");

m2.AddByHash(hash, "77", "sdfasdfasdf");
```

## FindOrAddByHash

​	查找对应的哈希值和key。如果没有找到，则添加进去。

```C++
const uint32 hash = GetTypeHash("77");
auto& value = m2.FindOrAddByHash(hash, "77");
```

## Append

​	合并两个map，存在相同key则覆盖value

```C++
m1.Append(m2);
```

