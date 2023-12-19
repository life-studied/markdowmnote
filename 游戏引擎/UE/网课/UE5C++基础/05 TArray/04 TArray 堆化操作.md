# TArray 堆化操作

​	将数组堆化，要保证其中元素支持`<运算符`。

## Heapify

```c++
struct FTest
{
    int32 a;
    float b;
    FString c;
    bool operator<(FTest& rhs)
    {
        return this->a < rhs.a;
	}
}

// init
TArray<FTest> FArray;
FArray.AddDefault(10);

// 堆化
FArray.Heapify();
```

## HeapPush & HeapPop

```C++
struct FTest
{
    int32 a;
    float b;
    FString c;
    bool operator<(FTest& rhs)
    {
        return this->a < rhs.a;
	}
    FTest() = default;
    FTest(int32 _a);
}

// init
TArray<FTest> FArray;
FArray.AddDefault(10);

// 堆化
FArray.Heapify();

// 添加进堆
FArray.HeapPush(FTest(20));

// 弹出到外部
FTest v;
FArray.HeapPop(v);
```

## HeapTop

​	获取顶部内容

```C++
auto& top = FArray.HeapTop();
```

