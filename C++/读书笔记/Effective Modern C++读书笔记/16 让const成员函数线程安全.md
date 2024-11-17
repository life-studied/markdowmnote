---
create: 2023-12-01
---
# 16 让`const`成员函数线程安全

​	这个条款是基于，多个线程可以同时在一个对象上执行一个`const`成员函数这个假设的。如果你可以**保证**在一个对象上永远不会有多个线程执行该成员函数——该函数的线程安全是无关紧要的。

---

​	在多线程环境下，成员函数如果应该是一个`const`，应该保证线程安全。

>mutable：mutable只能作用在类成员上，指示其数据总是可变的。不能和const 同时修饰一个成员，但能配合使用：const修饰的方法中，mutable修饰的成员数据可以发生改变，除此之外不应该对类/对象带来副作用。

​	解决这个问题最普遍简单的方法就是——使用`mutex`（互斥量）：

```c++
class Polynomial {
public:
    using RootsType = std::vector<double>;
    
    RootsType roots() const
    {
        std::lock_guard<std::mutex> g(m);       //锁定互斥量
        
        if (!rootsAreValid) {                   //如果缓存无效
            // ...                              //计算/存储根值
            rootsAreValid = true;
        }
        
        return rootsVals;
    }                                           //解锁互斥量
    
private:
    mutable std::mutex m;
    mutable bool rootsAreValid { false };
    mutable RootsType rootsVals {};
};
```

