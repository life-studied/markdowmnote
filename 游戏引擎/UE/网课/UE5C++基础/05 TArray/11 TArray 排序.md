# TArray 排序

## `Sort()`

```C++
TestArray.Sort();		//默认升序排序
```



## `Sort([](){})`

```C++
TestArray.Sort([](const auto& lhs, const auto & rhs){ return lhs<rhs;});		//默认升序排序
```

## StableSort

​	稳定排序，同Sort