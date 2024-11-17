---
create: 2023-09-23
---
# 02 FString本质和遍历

## 2.1 FString遍历

```C++
FString s1 = TEXT("Hello");
for(auto &i : s1)
{
	UE_LOG(LogTestCpp, Display, TEXT("%c"), t);
}

TCHAR SC = s1[3];
TArray<TCHAR>& s2 = s1.GetCharArray();

for (auto& i : s2)
{
	UE_LOG(LogTestCpp, Display, TEXT("%c"), i);
}
```

## 2.2 常用Api

```C++
SIZE_T n = s1.GetAllocatedSize();		//分配了多少字节
int32 n2 = s1.Len();		//字符串长度

s1.Empty(100);				//将数据和内存一起清空，如果有参数，则分配对应参数的size
s1.IsEmpty();				//判空

s1.Reset();					//只清空数据
s1.Shrink();				//清空没用到的空间
```

