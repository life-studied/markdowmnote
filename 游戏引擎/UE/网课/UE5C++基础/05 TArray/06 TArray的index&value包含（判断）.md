# TArray的index&value包含（判断）

## IsValidIndex

```C++
bool valid = FArray.IsValidIndex(5);		//判断是否合法
```

## Contains

```C++
bool hasIt = FArray.Contains(100);			//判断100是否存在
```

## ContainsByPredicate

​	自定义lambda，检测是否存在

```C++
bool hasIt = FArray.ContainsByPredicate([](auto* data){ return true;});			
```

