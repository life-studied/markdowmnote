# 01 FText的创建和使用

​	定义FText之前，要指定`LOCTEXT_NAMESPACE`宏，使用完取消宏。

​	同时FText是一种键值对的形式。

```C++
#define LOCTEXT_NAMESPACE "zh"

FText T = LOCTEXT("A","Hello World");
FText T2 = LOCTEXT("1","Hello UE4");

#undef LOCTEXT_NAMESPACE
```

​	或者使用NSLOCTEXT来将宏放在Text定义处：

```C++
FText T = NSLOCTEXT("zh","A","Hello World");
FText T2 = NSLOCTEXT("en","Hello UE4");
```

