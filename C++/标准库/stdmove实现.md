---
create: 2023-09-13
---
# std::move实现

​	`std::move`是一个函数模板，返回值是一个右值引用，实现方法是通过一个中间结构体模板的辅助。

## 1.定义

```C++
template <typename T>
typename remove_reference<T>::type&& move(T&& t)
{
	return static_cast<typename remove_reference<T>::type&&>(t);
}
```

## 2.`remove_reference<T>`

​	`remove_reference`通过三种模板实现，来获取到T的原始类型，将其命名为`type`。

```C++
//原始的，最通用的版本
template <typename T> struct remove_reference{
    typedef T type;  //定义T的类型别名为type
};
 
//部分版本特例化，将用于左值引用和右值引用
template <class T> struct remove_reference<T&> //左值引用
{ typedef T type; }
 
template <class T> struct remove_reference<T&&> //右值引用
{ typedef T type; }   
```

## 3.解释

​	`std::move`通过`remove_reference`模板获取到具体类型`type`，再将其补上`&&`代表其右值引用版本，最后将传入的参数`t`转换为其右值引用类型返回。

```C++
template <typename T>
typename remove_reference<T>::type&& move(T&& t)
{
	return static_cast<typename remove_reference<T>::type&&>(t);
}
```

