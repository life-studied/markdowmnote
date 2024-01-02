# 一文搞懂StaticClass、GetClass和ClassDefaultObject

## UClass和反射

​	**UClass存储了类的信息，用于反射。**

### GetClass

​	`GetClass()`是UObject自带的反射功能（成员函数），用于获取当前实例的UClass信息，返回一个UClass对象。

```C++
UObject * a = xxx;
UObject * b = yyy;
auto uc1 = a->GetClass();
auto uc2 = b->GetClass();
auto res = uc1 == uc2;		// true or false
```

### StaticClass

​	StaticClass是在没有类实例的情况下，可以通过类静态函数的方式获取对应类的UClass信息。

```C++
UObject *a = xxx;
auto res = a -> GetClass() == A::StaticClass();	// true or false
```

### 判断实例的类型

​	如果要判断一个实例是否是某一类，可以通过上面两个函数进行。

```C++
UObject* a = xxx;
auto result = a->GetClass() == A::GetStaticClass();
UE_LOG(LogTemp, Warning, TEXT("the result is %d"), result);
```

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

