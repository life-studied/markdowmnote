---
create: 2023-09-24
---
# 04 FString插入与移除

## 4.1 插入

```C++
FString s = TEXT("hello world");
s.InsertAt(2,'x');
s.InsertAt(2,"hello");
s.InsertAt(2,TEXT("hello"));

const ANSICHAR* p = "f";
const WIDECHAR* p2 = "g";
s.InsertAt(2,p);
s.InsertAt(2,p2);
```

## 4.2 移除

```C++
FString s = TEXT("hello world");
s.RemoveAt(2,3,true);			//index count isShrink

s.RemoveFromStart(TEXT("hello"));		//find from start, if exists, remove it
s.RemoveFromEnd(TEXT("world"));			//find from end, if exists, remove it

s.RemoveSpacesInline();		//remove all space
s.Shrink();					//clear space
```

