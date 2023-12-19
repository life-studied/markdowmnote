# TMap Find

## Find

```C++
const auto* v = m1.Find(TEXT("1"));
if(v!=nullptr)
{
    //...
}
```

## []

```C++
auto& v = m1[TEXT("1")];	// not safe
							// crash when not find
```

### FindKey

​	根据值寻找键

```C++
const auto* Key = m1.FindKey(TEXT("xxx"));
```

## FindRef

​	返回一个复制

```C++
auto v = m1.Find(TEXT("1"));
```

