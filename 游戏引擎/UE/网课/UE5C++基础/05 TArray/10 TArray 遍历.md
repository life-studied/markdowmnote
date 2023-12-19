# TArray 遍历

## auto for-loop

```C++
for(const auto& i : TestArray)
{
}
```

## tradition for-loop

```C++
for(int32 i = 0;i < TestArray.Num();i++)
{
}
```

## iterator for-loop

```C++
for(auto it = TestArray.CreateIterator();it;++it)
{
}
```

