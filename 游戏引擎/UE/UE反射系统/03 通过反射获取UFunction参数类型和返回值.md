---
create: '2025-03-09'
modified: '2025-03-09'
---

# 通过反射获取UFunction参数类型和返回值

## 通过TFieldIterator

```C++
void get_func()
{
    for(TFieldIterator<UFunction> It(MyFunction); It; ++It)
    {
        auto* f = *It;
        if(f->GetName().Equal(TEXT("myfunc")))
        {
            get_type_param_and_ret(f);
        }
    }
}

void get_type_param_and_ret(UFunction* MyFunction)
{
    for (TFieldIterator<FProperty> It(MyFunction); It; ++It)
    {
        FProperty* Property = *It;
        if (Property->HasAnyPropertyFlags(CPF_Parm)) 				// 检查是否是参数
        {
            UE_LOG(LogTemp, Log, TEXT("Parameter: %s"), *Property->GetName());
        }
        else if (Property->HasAnyPropertyFlags(CPF_ReturnParm)) 	// 检查是否是返回值
        {
            UE_LOG(LogTemp, Log, TEXT("Return Value: %s"), *Property->GetName());
        }
    }
}
```

## 通过成员函数获取ret

`UFunction` 提供了 `GetReturnProperty` 方法，用于直接获取返回值的 `FProperty`。例如：

```cpp
FProperty* ReturnProperty = MyFunction->GetReturnProperty();
if (ReturnProperty)
{
    UE_LOG(LogTemp, Log, TEXT("Return Property: %s"), *ReturnProperty->GetName());
}
```