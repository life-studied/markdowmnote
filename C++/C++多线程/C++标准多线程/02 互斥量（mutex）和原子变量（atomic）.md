## 02 互斥量（mutex）和原子变量（atomic）

[TOC]

​	关键字：thread;mutex;lock_guard;unique_lock;atomic

### 1.问题引入

​	下面的代码会导致结果不为0。

​	原因：存在竞态条件，在读写同一变量时没有加锁。

```C++
#include <iostream>
#include <thread>

int globalVariable = 0;

void task1()
{
    for(int i  = 0;i < 1000000; i++)
    {
        globalVariable ++;
        globalVariable --;
    }
}

int main()
{
	std::thread t1(task1);
    std::thread t2(task1);
    
    t1.join();
    t2.join();
    
    std::cout << "current value is " << globalVariable;
}
```

### 2. 方案1：使用lock来管理临界区

#### 2.1 用mutex解决问题

​	使用互斥量`mutex`这个原语能够对代码段进行上锁。解决该简单问题。

```C++
#include <iostream>
#include <thread>
#include <mutex>

std::mutex mtx;
int globalVariable = 0;

void task1()
{
    for(int i  = 0;i < 1000000; i++)
    {
        mtx.lock();
        globalVariable ++;
        globalVariable --;
        mtx.unlock();
    }
}

int main()
{
	std::thread t1(task1);
    std::thread t2(task1);
    
    t1.join();
    t2.join();
    
    std::cout << "current value is " << globalVariable;
}
```

#### 2.2 新问题与解决：代码中途return或throw（std::lock_guard/std::unique_lock）

##### 2.2.1 std::lock_guard

​	对于代码在解锁前`return`或`throw`，解锁操作就不能很好的执行。

​	解决方法：使用`std::lock_guard<std::mutex>`对象来自动管理`mutex`对象的生命周期。

```C++
#include <iostream>
#include <thread>
#include <mutex>

std::mutex mtx;
int globalVariable = 0;

void task1()
{
    for(int i  = 0;i < 1000000; i++)
    {
        std::lock_guard<std::mutex> lock(mtx);		//自动管理锁
        globalVariable ++;
        
        //if(...)
        //throw ...
        
        //if(...)
        //return ...
        
        globalVariable --;
    }
}

int main()
{
	std::thread t1(task1);
    std::thread t2(task1);
    
    t1.join();
    t2.join();
    
    std::cout << "current value is " << globalVariable;
}
```

##### 2.2.2 unique_lock

​	`std::guard_lock`自带的功能仅有创建和析构，对加锁部分的粒度在当前整个作用域，不是很灵活。

​	因此可以使用`std::unique_lock`中的`unlock`函数来为临界区设置更细致的粒度。（还有其它函数来使用）

```C++
#include <iostream>
#include <thread>
#include <mutex>

std::mutex mtx;
int globalVariable = 0;

void task1()
{
    for (int i = 0; i < 1000000; i++)
    {
        std::unique_lock<std::mutex> lock(mtx);		//自动管理锁
        globalVariable++;

        //if(...)
        //throw ...

        //if(...)
        //return ...

        lock.unlock();          //减小锁的粒度
        globalVariable--;
    }
}

int main()
{
    std::thread t1(task1);
    std::thread t2(task1);

    t1.join();
    t2.join();

    std::cout << "current value is " << globalVariable;
}
```

#### 2.3 新问题与解决：因为上锁顺序导致的死锁（std::lock）

​	问题：

1. 存在两个锁mtx1和mtx2。
2. 线程1先上锁mtx1，再上锁mtx2；线程2相反。导致死锁发生。



​	解决方法：

		1. 两个线程都按顺序进行分别上锁；
		2. 或者**推荐使用**`std::lock`对两把锁直接进行上锁。而不是分开上锁。

```C++
#include <iostream>
#include <thread>
#include <mutex>

std::mutex mtx1;
std::mutex mtx2;
int globalVariable = 0;

void task1()
{
    for(int i  = 0;i < 1000000; i++)
    {
        std::lock(mtx1, mtx2);
        globalVariable ++;
        globalVariable --;
        mtx1.unlock();
        mtx2.unlock();
    }
}

void task2()
{
    for(int i  = 0;i < 1000000; i++)
    {
        std::lock(mtx1, mtx2);			//传入两个锁参数，直接全部上锁
        globalVariable ++;
        globalVariable --;
        mtx1.unlock();
        mtx2.unlock();
    }
}

int main()
{
	std::thread t1(task1);
    std::thread t2(task2);
    
    t1.join();
    t2.join();
    
    std::cout << "current value is " << globalVariable;
}
```

### 3. 方案2：使用atomic设置原子变量

```C++
#include <iostream>
#include <thread>
#include <atomic>

std::atomic<int> globalVariable = 0;		//使用atomic模板化一个原子变量

void task1()
{
    for (int i = 0; i < 1000000; i++)
    {
        globalVariable++;					//当成int使用即可
        globalVariable--;
    }
}

int main()
{
    std::thread t1(task1);
    std::thread t2(task1);

    t1.join();
    t2.join();

    std::cout << "current value is " << globalVariable;
}
```

