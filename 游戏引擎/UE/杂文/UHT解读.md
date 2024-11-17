---
create: 2024-01-30
---
# UHT解读

​	一般UBT会收集头文件中的信息，并传递给UHT，来生成对应的`classname.generated.h`。一般生成的地址不在和源代码同级，而是在`Intermediate\Build\Win64\UnrealEditor\Inc\MODULE_NAME\UHT`下。

## UHT生成的文件

* MODULE_NAMEClasses.h：无用
* MyObject.generated.h：xxx的生成头文件
* MyObject.gen.h：包含MyObject.h和xxx.generated.h
* MyObject.generated.cpp：该项目的实现编译单元。

​	其生成的文件初看起来很多很复杂，但其实比较简单，不过就是一些宏替换而已。生成的函数大都也以`Z_`开头，笔者开始也在猜想`Z_`前缀的缩写含义，感谢NetFly向Epic的人求证之后的回答：

> The 'Z_' prefix is not part of any official naming convention, and it
> doesn't really mean anything. Some generated functions were named this way
> to avoid name collisions and so that these functions will appear together at the
> bottom of intelisense lists.

简而言之，没什么特别含义，就是简单为了避免命名冲突，用Z是为了字母排序总是出现在智能感知的最下面，尽量隐藏起来。

* [《InsideUE4》UObject（四）类型系统代码生成 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/25098685)

## UHT如何引入代码(GENERATE_BODY宏)

​	UHT一般根据收获的标记信息，生成对应的代码，一般是一堆宏定义。在头文件`include`的最后包含的就是生成文件，并通过`GENERATE_BODY()`宏引进生成的宏代码，引入的代码会自动内嵌在类的内部。该宏是重中之重，其他的`UCLASS`宏只是提供信息，不参与编译，而`GENERATED_BODY`正是把声明和元数据定义关联到一起的枢纽。

​	关于`GENERATE_BODY()`宏：这个宏会根据所在文件和行数生成一个独有的ID宏，这个独有的ID宏实际将由UHT定义在`classname.generated.h`中。

>加了一个__LINE__宏的目的是为了支持在同一个文件内声明多个类，比如在MyClass.h里接着再声明UMyClass2，就可以支持生成不同的宏名称。

```C++
#define GENERATED_BODY(...) BODY_MACRO_COMBINE(CURRENT_FILE_ID,_,__LINE__,_GENERATED_BODY);

// example: generated in UHT header
#define FID_codeSpace_IDE_Project_UEProjects_MyProject_Source_MyProject_test_h_13_GENERATED_BODY \
...
```

### gen.h中的宏定义

#### CURRENT_FILE_ID_LINE_ENHANCED_CONSTRUCTORS

```C++
#define FID_codeSpace_IDE_Project_UEProjects_Poison_Source_Poison_Public_MyObject_h_15_ENHANCED_CONSTRUCTORS \
	/** Standard constructor, called after all reflected properties have been initialized */ \
	NO_API UMyObject(const FObjectInitializer& ObjectInitializer = FObjectInitializer::Get()); \
private: \
	/** Private move- and copy-constructors, should never be used */ \
    /* 禁用了移动和拷贝构造函数*/  \
	NO_API UMyObject(UMyObject&&); \
	NO_API UMyObject(const UMyObject&); \
public: \
	DECLARE_VTABLE_PTR_HELPER_CTOR(NO_API, UMyObject); \
	DEFINE_VTABLE_PTR_HELPER_CTOR_CALLER(UMyObject); \
	DEFINE_DEFAULT_OBJECT_INITIALIZER_CONSTRUCTOR_CALL(UMyObject) \
	NO_API virtual ~UMyObject();
```

​	它提供了以`FObjectInitializer`为参数的构造函数，同时禁用了移动和拷贝构造函数。

​	继续查看`DEFINE_DEFAULT_OBJECT_INITIALIZER_CONSTRUCTOR_CALL`的定义：

```C++
#define DEFINE_DEFAULT_OBJECT_INITIALIZER_CONSTRUCTOR_CALL(TClass) \
	static void __DefaultConstructor(const FObjectInitializer& X) { new((EInternal*)X.GetObj())TClass(X); }
```

​	该声明定义了一个构造函数包装器。需要这么做的原因是，在根据名字反射创建对象的时候，需要调用该类的构造函数。可是类的构造函数并不能用函数指针指向，因此这里就用一个static函数包装一下，变成一个"平凡"的函数指针，而且所有类的签名一致，就可以在UClass里用一个函数指针里保存起来。（此处的`__DefaultConstructor`在《浅析StaticClass的实现》一文中提到）

#### CURRENT_FILE_ID_LINE_INCLASS_NO_PURE_DECLS

```C++
#define FID_codeSpace_IDE_Project_UEProjects_Poison_Source_Poison_Public_MyObject_h_15_INCLASS_NO_PURE_DECLS \
private: \
	static void StaticRegisterNativesUMyObject(); \
	friend struct Z_Construct_UClass_UMyObject_Statics; \	//一个构造该类UClass对象的辅助函数
public: \
	DECLARE_CLASS(UMyObject, UObject, COMPILED_IN_FLAGS(0), CASTCLASS_None, TEXT("/Script/Poison"), NO_API) \	//声明该类的一些通用基本函数
	DECLARE_SERIALIZER(UMyObject)	//声明序列化函数
```

##### DECALRE_CLASS宏

```C++
#define DECLARE_CLASS( TClass, TSuperClass, TStaticFlags, TStaticCastFlags, TPackage, TRequiredAPI  ) \
```

- TClass：类名 
- TSuperClass：基类名字
- TStaticFlags：类的属性标记，这里是0，表示最默认，不带任何其他属性。读者可以查看EClassFlags枚举来查看其他定义。
- TStaticCastFlags：指定了该类可以转换为哪些类，这里为0表示不能转为那些默认的类，读者可以自己查看EClassCastFlags声明来查看具体有哪些默认类转换。
- TPackage：类所处于的包名
  - 所有的对象都必须处于一个包中，而每个包都具有一个名字，可以通过该名字来查找。
  - 这里是"/Script/Hello"，指定是Script下的Hello，Script可以理解为用户自己的实现，不管是C++还是蓝图，都可以看作是引擎外的一种脚本，当然用这个名字也肯定有UE3时代UnrealScript的影子。Hello就是项目名字，该项目下定义的对象处于该包中。Package的概念涉及到后续Object的组织方式，目前可以简单理解为一个大的Object包含了其他子Object。
- TRequiredAPI：就是用来Dll导入导出的标记，这里是NO_API，因为是最终exe，不需要导出。

> 类型系统的实际创建见《浅析StaticClass的实现》。

## UHT标记

​	简单判断是否是UHT标记的方法就是，检查这个标记的定义是否是空宏。(`Runtime\CoreUObject\Public\ObjectMacros.h`)

```C++
// Runtime\CoreUObject\Public\ObjectMacros.h
// ...
// These macros wrap metadata parsed by the Unreal Header Tool, and are otherwise
// ignored when code containing them is compiled by the C++ compiler
#define UPROPERTY(...)
#define UFUNCTION(...)
#define USTRUCT(...)
#define UMETA(...)
#define UPARAM(...)
#define UENUM(...)
#define UDELEGATE(...)

// This pair of macros is used to help implement GENERATED_BODY() and GENERATED_USTRUCT_BODY()
#define BODY_MACRO_COMBINE_INNER(A,B,C,D) A##B##C##D
#define BODY_MACRO_COMBINE(A,B,C,D) BODY_MACRO_COMBINE_INNER(A,B,C,D)

// Include a redundant semicolon at the end of the generated code block, so that intellisense parsers can start parsing
// a new declaration if the line number/generated code is out of date.
#define GENERATED_BODY_LEGACY(...) BODY_MACRO_COMBINE(CURRENT_FILE_ID,_,__LINE__,_GENERATED_BODY_LEGACY);
#define GENERATED_BODY(...) BODY_MACRO_COMBINE(CURRENT_FILE_ID,_,__LINE__,_GENERATED_BODY);

#define GENERATED_USTRUCT_BODY(...) GENERATED_BODY()
#define GENERATED_UCLASS_BODY(...) GENERATED_BODY_LEGACY()
#define GENERATED_UINTERFACE_BODY(...) GENERATED_BODY_LEGACY()
#define GENERATED_IINTERFACE_BODY(...) GENERATED_BODY_LEGACY()

#if UE_BUILD_DOCS || defined(__INTELLISENSE__)
#define UCLASS(...)
#else
#define UCLASS(...) BODY_MACRO_COMBINE(CURRENT_FILE_ID,_,__LINE__,_PROLOG)
#endif

#define UINTERFACE(...) UCLASS()
// ...
```

## UHT生成时机

​	相比于C++标准编译流程而言，UHT在**所有的流程之前生成**，也就是说，在**预处理之前**就已经完成了代码的生成。

​	注意：因此，不能用宏来包含UHT的标记宏，例如`GENERATE_BODY()`，`UPROPERTY()`等。