# StaticClass和GetClass

> `StaticClass`是类的静态方法，同时还有一个同名的成员方法。`GetClass`是一个成员方法。
>
> 它们都能返回`UClass*`

### GetClass

​	`GetClass()`是UObject自带的反射功能（成员函数），用于获取**当前实例**的UClass信息，返回一个UClass对象。

```C++
UObject * a = xxx;
UObject * b = yyy;
auto uc1 = a->GetClass();
auto uc2 = b->GetClass();
auto res = (uc1 == uc2);		// true or false
```

### StaticClass

#### 静态方法

​	StaticClass是在没有类实例的情况下，可以通过类静态函数的方式获取对应类的UClass信息。

```C++
UObject *a = xxx;
auto res = (a -> GetClass() == A::StaticClass());	// true or false
```

#### 成员函数

​	StaticClass在UObject的内部也有一个对应的成员方法，是UHT自动生成的。这个方法返回的是当前指针类型的`UClass*`

```C++
UObject *a = xxx;
auto res = (a -> StaticClass() == UObject::StaticClass());	// true
```

### 判断实例的类型

​	如果要判断一个实例是否是某一类，可以通过上面的函数进行。

```C++
UObject* a = xxx;
auto result = a->GetClass() == A::GetStaticClass();
UE_LOG(LogTemp, Warning, TEXT("the result is %d"), result);
```

## 蓝图的UClass

​	以上都是纯 C++ 类的验证，如果有个蓝图 `BP_MyActor`，继承自 `MyActor`，那么情况如何呢？

```cpp
void AMyActor::FromBlueprint() const
{
	UClass* BlueprintClass = StaticLoadClass(StaticClass(), nullptr,
	    TEXT("Blueprint'/Game/ObjectTest/BP_MyActor.BP_MyActor_C'"));
	UClass* BlueprintGeneratedClass = UBlueprintGeneratedClass::StaticClass();
	
	UClass* Class = this->GetClass();                //BP_MyActor
	UClass* ClassClass = Class->GetClass();          //UBlueprintGeneratedClass
	UClass* ClassClassClass = ClassClass->GetClass();//UClass

	UClass* StaticClass = this->StaticClass();           //AMyActor
	UClass* MyActorStaticClass = AMyActor::StaticClass();//AMyActor
	
	UClass* ClassStatic = UClass::StaticClass();          //UClass
	UClass* StaticClassClass = StaticClass->GetClass();   //UClass
	UE_LOG(LogTemp,Log,TEXT("[Chelsea]这一行纯粹为了断点"));
}
```

​	把 BP_MyActor 拖入场景，打开关卡蓝图，BeginPlay 的地方调用 `BP_MyActor->FromBlueprint()`；

---

**this->GetClass()**

this->GetClass() 这个结果很好猜，因为我们是把 `BP_MyActor` 这个蓝图资产直接拖入到场景，因此它应该就等于 `Blueprint Class`（就是我们通过路径加载的那个蓝图对象）

**this->GetClass()->GetClass()**

在编辑器的 Content Browser 内你看到的 BP_MyActor 这个蓝图资产对象它的类型是啥呢？加载后可知，它的类型是 UBlueprintGeneratedClass；因此 this->GetClass()->GetClass() 就等于UBlueprintGeneratedClass::StaticClass()

**this->GetClass()->GetClass()->GetClass()**

后面无数层结果都是一致的，都是UObject::StaticClass()；**因为 UClass 对象其本身是一个 UObject 对象**

## IsChildOf

​	UClass中，还存储了该类的父类信息，因此可以用来判断一个对象是否是一个类的子类。

​	由于UClass是UStruct的子类，具体实现在UStruct中：

```C++
bool UStruct::IsChildOf(const UStruct* SomeBase) const
{
	if(SomeBase == nullptr)
    {
        return false;
    }
    
    bool bOldResult = false;
    for(const UStruct* TempStruct = this; TempStruct; TempStruct = TempStruct->GetSuperStruct())
    {
        if(TempStruct == SomeBase)
        {
            bOldResult = true;
            break;
        }
	}
    
    return bOldResult;
}

UStruct* GetSuperStruct() const
{
	return SuperStruct;
}
```

## ClassDefaultObject（CDO）

​	CDO代表一个类的默认对象，即它属性的默认值构造的一个该类对象。

​	例如，下面这个类的声明：

```C++
// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "GameFramework/Character.h"
#include "MyCharacter.generated.h"

UCLASS()
class HOWTO_AUTOCAMERA_API AMyCharacter : public ACharacter
{
	GENERATED_BODY()

public:
	// Sets default values for this character's properties
	AMyCharacter();

	UPROPERTY(BlueprintReadWrite, EditAnywhere)
    int testVal = 1
protected:
	// Called when the game starts or when spawned
	virtual void BeginPlay() override;

public:	
	// Called every frame
	virtual void Tick(float DeltaTime) override;

	// Called to bind functionality to input
	virtual void SetupPlayerInputComponent(class UInputComponent* PlayerInputComponent) override;

};

```

​	获取其默认对象：

```C++
auto defaultInt = AMyCharacter::GetStaticClass()->GetDefaultObject<AMyCharacter>()->testVal;
UE_LOG(LogTemp, Warning, TEXT("DefaultObject int: %d"), defaultInt);
```

