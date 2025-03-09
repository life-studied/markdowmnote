---
create: '2025-03-09'
modified: '2025-03-09'
---

# TFieldIterator

## FProperty和UFunction

类内部的 **反射数据成员** 和**反射成员函数** ，都会给生成对应的`FProperty` 和`UFunction`对象（见基础概念），用来运行时访问到它们，因此通过`TFieldIterator<T>`查找我们需要的字段类型：

```C++
UObject* u = xxx;	// 某个继承UObject

for (TFieldIterator<FProperty> it(u->GetClass()); it; ++it)	// 数据成员
{
    FProperty* PropertyIns = *it;
    UE_LOG(LogTemp, Log, TEXT("Property Name: %s"), *PropertyIns->GetName());
}
for (TFieldIterator<UFunction> it(u->GetClass()); it; ++it)	// 函数成员
{
    UFunction* PropertyIns = *it;
    UE_LOG(LogTemp, Log, TEXT("Function Name: %s"), *PropertyIns->GetName());
}
```

## 访问数据成员

> C++ 有一个特性叫做 **指向类成员的指针**，本质上就是描述了当前数据成员在类布局内的偏移值。FProperty 做的就是类似的事情，记录反射数据成员的类内偏移信息，UE 中的实现也是通过 **指向成员的指针** 来实现的。这里只介绍使用方法。

通过 `FProperty` 获取对象中值的方式，需要通过调用 `FProperty` 的`ContainerPtrToValuePtr`来实现：

```C++
for(TFieldIterator<FProperty> it(GetClass());it;++it)
{
    FProperty* PropertyIns = *it;
    if(PropertyIns->GetName().Equals(TEXT("ival")))		// 通过GetName()成员函数获取字段名
    {
        int32* i32 = PropertyIns->ContainerPtrToValuePtr<int32>(this);		// 传入this实例，获取该实例中字段的值
        UE_LOG(LogTemp,Log,TEXT("Property %s value is %d"),*PropertyIns->GetName(),*i32);
    }
}
```

## 附：TFieldIterator实现详解

```C++
//
// For iterating through a linked list of fields.
//
template <class T>
class TFieldIterator
{
private:
    /** 指向传入的UStruct的指针 */
	const UStruct* Struct;
	/** 当前所在的Field，Field内部有Next指针，自成一个list */
	typename T::BaseFieldClass* Field;
	/** 记录当前迭代到第i个字段 */
	int32 InterfaceIndex;
    
	...		// 此处还省略了一些迭代器的属性，用于筛选字段

public:
    /** 构造函数 */
	TFieldIterator(const UStruct* InStruct, EFieldIterationFlags InIterationFlags = EFieldIterationFlags::Default)	... {}
    
    /** 重载的各种迭代器的运算符，其它省略 */
    ...
    inline void operator++()
	{
		checkSlow(Field);
		Field = Field->Next;
		IterateToNext();
	}
    
	inline T* operator*()
	{
		checkSlow(Field);
		return (T*)Field;
	}
	
protected:
    // ++运算符的核心逻辑
	inline void IterateToNext()
	{
		...
	}
};
```

### 基本说明

可以看到，迭代器的实现本质就是依赖于`Field*`进行的。迭代的本质就是遍历`Field`指针形成的链表（前提是`Field`内部有`Field* Next`的成员）。

### T::BaseFieldClass* Field（Field的类型到底是什么）

因为UE在演进的过程中，引入了`FField`的轻量类型替代`UField`，从而绕开了`UObject`的继承机制。（见基础概念）

同时为了保证代码的兼容性，`TFieldIterator`需要对这两个类型都能做到遍历，因此在两者的代码中都有一个`BaseFieldClass`的别名描述指向自身（继承这两个`Field`类的也会继承这个`typedef`）：

```C++
class UField : public UObject {
    typedef UField BaseFieldClass;
    ...
    UField*	Next;
}

class FField {
    typedef FField BaseFieldClass;	
    ...
    FField* Next;
}
```

其本质就是为了给类似于`TFieldIterator`的上层工具提供兼容性的。

### IterateToNext（为什么需要这个函数？）

这是由于单纯的`Field = Field->Next`操作在不同的类型特化下不能满足需求，同时`Field`字段本身存在一些属性，在某些情况下字段不符合我们设置的属性需要，也会在这个里面被跳过：

1. **筛选字段类型**（核心）：`IterateToNext` 函数根据传入的类型要求进行检查（例如，是 `UProperty` 或 `UFunction`）。如果当前字段不符合要求，它会继续查找下一个字段。
2. **处理父类字段**（默认`IncludeSuper`）：如果迭代器配置为包含父类字段（`bIncludeSuper`），`IterateToNext` 会递归地检查父类的字段，而不仅仅是当前类的字段。
3. **处理接口字段**（默认`IncludeInterfaces`）：如果迭代器配置为包含接口字段（`bIncludeInterface`），`IterateToNext` 会检查类实现的接口，并迭代接口中的字段。
4. **处理过时字段**（默认`IncludeDeprecated`）：如果迭代器配置为不包含过时字段（`bIncludeDeprecated`），`IterateToNext` 会跳过标记为过时的字段。

## 参考资料

* [UE 反射实现分析：基础概念 | 虚幻社区知识库](https://ue5wiki.com/wiki/12624/)