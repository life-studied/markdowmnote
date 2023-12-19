# TArray 栈操作

## Push & Pop

​	Push本质上是在TArray最后Push一个

​	Pop本质上是弹出最后一个，用的拷贝的方法。

```C++
TArray<int32> AInt32s = {1,2,3,4,5};
AInt32s.Push(6);
auto data = AInt32s.Pop();	//最后一个消失
```

## Top

​	获取栈顶引用

```C++
TArray<int32> AInt32s = {1,2,3,4,5};
auto &data = AInt32s.Top();		
```

