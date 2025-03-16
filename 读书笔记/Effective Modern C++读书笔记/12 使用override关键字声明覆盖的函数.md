---
create: 2023-11-29
modified: '2024-11-17'
---

# 12 使用override关键字声明覆盖的函数

​	覆盖父类的虚函数的条件：

* 基类中的函数被声明为虚的。
* 基类中和派生出的函数必须是完全一样的（出了虚析构函数）。
* 基类中和派生出的函数的参数类型必须完全一样。
* 基类中和派生出的函数的常量特性必须完全一样。
* 基类中和派生出的函数的常量特性必须完全一样。
* 基类中和派生出的函数的返回值类型和异常声明必须使兼容的。

---

* 函数的**引用修饰符**必须完全一样。（`C++11`）

```C++
class Widget{
public:
    ...
    void doWork() &; 	// 只有当 *this 为左值时
                        // 这个版本的 doWorkd()
                        // 函数被调用
    void doWork() &&; 	// 只有当 *this 为右值
                        // 这个版本的 doWork()
                        // 函数被调用
};

Widget makeWidget(); 	// 工厂函数，返回右值
Widget w; 				// 正常的对象（左值）
...
w.doWork(); 			// 为左值调用 Widget::doWork()
						//（即 Widget::doWork &）
makeWidget().doWork(); 	// 为右值调用 Widget::doWork()
						//（即 Widget::doWork &&）
```

## 使用override减少错误

​	对于下面的代码，编译能正常进行，但是并不符合预期：

```C++
class Base {
public:
    virtual void mf1() const;
    virtual void mf2(int x);
    virtual void mf3() &;
    void mf4() const;
};

class Derived: public Base {
public:
    virtual void mf1();
    virtual void mf2(unsigned int x);
    virtual void mf3() &&;
    void mf4() const;
};
```

​	使用`override`关键字来让编译器发现错误：

```C++
class Base {
public:
    virtual void mf1() const;
    virtual void mf2(int x);
    virtual void mf3() &;
    virtual void mf4() const;
};

class Derived: public Base {
public:
    virtual void mf1() const override;
    virtual void mf2(int x) override;
    virtual void mf3() & override;
    void mf4() const override; 	// 加上"virtual"也可以
    							// 但是不是必须的
};
```