# 01 五个基类

## 1.1 UObject类

​	UObject是提供了如下功能的类：

* 垃圾回收（GC）
* 引用自动更新（Reference updating）
* Reflection 反射
* Serialization 序列化
* Automatic updating of default property changes自动检测默认变量的更改
* Automatic property initialization 自动变量初始化
* Automatic editor integration 和虚幻引擎编辑器的自动交互
* Type information available at runtime 运行时类型识别
* Network replication 网络复制

### GC的两种方案

​	对于**继承自UObject类**的对象，如果有一个指针指向它，并且它被UPROPERTY标记了，那么会被自动GC

​	对于**非UObject对象**，才能选择**智能指针**进行自动内存释放。

### 反射

​	反射可以在**运行时获取对象的类信息，成员信息**等。

### 序列化

​	将对象的信息**保存到磁盘**，下次再完好无损地加载。

### 与Editor的交互

​	允许类的变量能够**被Editor简单编辑**

### 运行时类型检测

​	注意：虚幻引擎打开了`/GR-`编译器参数。即，**不允许使用C++标准的`dynamic_cast`**。

​	使用`Cast<>`模板函数来完成RTTI的功能。

### 网络复制

​	在网络游戏的c/s模式下，可以**自动**将被**宏**标记的变量，从**服务器复制到客户端**。

### 其它注意

#### 构造函数与DefaultObject

​	由于`UObject`会在引擎加载阶段创建一个**`DefaultObject`**，因此构造函数会被默认调用一次。

#### 构造函数与UWorld

​	构造函数被调用时，`UWorld`不一定存在，因此**不要在构造函数中调用`GetWorld()`**

## 1.2 Actor类

​	Actor类的标志是：**能够挂载组件**。（它可以没有坐标与旋转量，因为它可以不存在实体。）

​	组件：组件不是`Actor`，而是`UObject`。

​	如果需要挂载组件，例如`SceneComponent`（提供坐标与旋转量），`StaticMeshComponent`（被渲染）...选择Actor。

## 1.3 Pawn/Character/Controller（控制与被控制）

### 1.3.1 Pawn

​	`Pawn`代表傀儡，即一个**可以被控制的对象**（无论是AIController或者玩家Controller）。

### 1.3.2 Character

​	Character代表角色，**特点**在于：

​	它提供了**一个特殊的组件`Character Movement`**。这个组件提供了一个基础的、基于胶囊体的**角色移动功能**。包括移动和跳跃，以及如果你需要， 还能自行扩展出更多，例如蹲伏和爬行。

#### 注意

​	如果用不上这类移动（例如飞船类的移动），请继承`Pawn`而不要去继承`Character`。

### 1.3.3 Controller

​	Controller用于操纵Pawn或者Character的行为。它可以是AIController（行为树/EQS环境查询系统），或者玩家Controller。通常在这个类中**绑定输入**，然后转化为对Pawn/Character方法的调用（例如：前进、开火、跳跃等肉体执行的方法）。

> 抽象出Controller的原因：不同的怪物可能会共享同一套Controller，从而获得类似的行为。

​	灵魂可以通过`Possess/UnPossess`来控制一个肉体，或者从一个肉体上离开。

