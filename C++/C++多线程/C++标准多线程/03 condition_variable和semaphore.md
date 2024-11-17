---
create: 2023-07-12
---
## 03 condition_variable和semaphore

[TOC]

​	关键字：condition_variable;虚假唤醒;notify_one;mutex;semaphore

### 3.1 condition_variable

#### 3.1.1 条件变量condition_variable使用

​	`condition_variable`允许阻塞等待并释放互斥锁，直到另一个线程给出信号再重新获取互斥锁，然后再执行。

​	一般两个线程的`wait(lock)`和`notify_one`配合使用。

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <atomic>
#include <deque>
#include <condition_variable>

std::mutex mtx;					//互斥锁
std::deque<int> q;				//队列
std::condition_variable cv;		

// producer
void task1()
{
    int i = 0;
    while (true)
    {
        std::unique_lock<std::mutex> lock(mtx);

        q.push_back(i);
        cv.notify_one();		//允许一个线程继续
        
        if (i < 99999999)
        	i++;
        else
        	i = 0;
        
    }
}

// consumer
void task2()
{
    int data = 0;
    while (true)
    {
        std::unique_lock<std::mutex> lock(mtx);

        if (q.empty())
        {
            cv.wait(lock);		//阻塞，并释放互斥锁，在被唤醒后重新获取互斥锁
        }

        data = q.front();
        q.pop_front();
        std::cout << "Get value from que:" << data << std::endl;
    }
}

int main()
{
    std::thread t1(task1);
    std::thread t2(task2);

    t1.join();
    t2.join();
}
```

#### 3.1.2 虚假唤醒问题（一对多模型）

##### 问题产生：

​	当出现了两个消费者，则会产生虚假唤醒的问题：队列中一开始有一个数据。第一个消费者被唤醒后，拿到数据，紧接着队列再次放入了一个数据，该消费者再一次准备获取`data`；同时第二个消费者因为生产者的唤醒，同时也通过了队空的检测。接下来就会产生一个数据被两个消费者抢夺，导致`front`报异常。

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <atomic>
#include <deque>
#include <condition_variable>

std::mutex mtx;					//互斥锁
std::deque<int> q;				//队列
std::condition_variable cv;		

// producer
void task1()
{
    int i = 0;
    while (true)
    {
        std::unique_lock<std::mutex> lock(mtx);

        q.push_back(i);
        cv.notify_one();		//允许一个线程继续
        
        if (i < 99999999)
        	i++;
        else
        	i = 0;
        
    }
}

// consumer
void task2()
{
    int data = 0;
    while (true)
    {
        std::unique_lock<std::mutex> lock(mtx);

        if (q.empty())
        {
            cv.wait(lock);		//阻塞，并释放互斥锁，在被唤醒后重新获取互斥锁
        }

        data = q.front();
        q.pop_front();
        std::cout << "Get value from que:" << data << std::endl;
    }
}

int main()
{
    std::thread t1(task1);
    std::thread t2(task2);
    std::thread t3(task2);		//第二个消费者

    t1.join();
    t2.join();
    t3.join();
}
```

##### 解决方法：

​	将`if`改成`while`去判断，这样在被唤醒之后，会进行第二次检验，来避免前置判空的失误。

​	注意：由于两个消费者使用的是同一个互斥锁，因此第二个消费者即使被唤醒了，也要等待第一个消费者获取完仅存的资源（此时队空），并释放锁之后的空隙，才能重新获取到互斥锁，并继续执行。在此时如果再次判断队空，则可以再次进入休眠，避免虚假唤醒。

---

​	**新的方法**是必须使用`unique_lock`才能使用：将`cv.wait(lock)`添加lambda表达式，变为`cv.wait(lock, [](){ return !q.empty();});`。

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <atomic>
#include <deque>
#include <condition_variable>

std::mutex mtx;					//互斥锁
std::deque<int> q;				//队列
std::condition_variable cv;

// producer
void task1()
{
    int i = 0;
    while (true)
    {
        std::unique_lock<std::mutex> lock(mtx);

        q.push_back(i);
        cv.notify_one();		//允许一个线程继续

        if (i < 99999999)
            i++;
        else
            i = 0;

    }
}

// consumer
void task2()
{
    int data = 0;
    while (true)
    {
        std::unique_lock<std::mutex> lock(mtx);

        // 或者使用cv.wait(lock, [](){ return !q.empty();});
        // 在被唤醒时，会检测lambda表达式是否满足条件
        	// 如果不满足，则会重新解锁，并进入休眠
        	// 如果满足，则会解除对资源的持有，进入处理程序
        while (q.empty())
        {
            cv.wait(lock);		//阻塞，并释放互斥锁，在被唤醒后重新获取互斥锁
        }

        data = q.front();
        q.pop_front();
        std::cout << "Get value from que:" << data << std::endl;
    }
}

int main()
{
    std::thread t1(task1);
    std::thread t2(task2);
    std::thread t3(task2);		//第二个消费者

    t1.join();
    t2.join();
    t3.join();
}
```

### 3.2 信号量semaphore（C++20）

#### 3.2.1 std::counting_semaphore模板

​	这个模板需要一个`int`类型的参数，来指定最大的信号数目，例如`std::counting_semaphore<6>`。如果为1，则等效于`std::binary_semaphore`。

​	该模板存在两个成员函数：

* release：相当于V操作
* acquire：相当于P操作

##### std::binary_semaphore

```C++
#include <iostream>
#include <semaphore>
#include <thread>

std::counting_semaphore<6> csem(0);
std::binary_semaphore bsem(0);		//计数器初始0，最大1

void task()
{
	std::cout << "ready to recv signal: acquire\n" << std::flush;
	bsem.acquire();		//等待信号
	std::cout << "acquire end\n" << std::flush;
}

int main()
{
	std::thread t(task);
	std::cout << "ready to signal: release\n" << std::flush;
	bsem.release(1);	//唤醒1个线程
	std::cout << "signal end\n" << std::flush;
	t.join();
}
```

##### std::counting_semaphore

```C++
#include <iostream>
#include <semaphore>
#include <thread>

std::counting_semaphore<3> csem(0);		//计数器初始0，最大3
std::binary_semaphore bsem(0);

void task()
{
	std::cout << "ready to recv signal: acquire\n" << std::flush;
	csem.acquire();		//等待信号
	std::cout << "acquire end\n" << std::flush;
}

int main()
{
	std::thread t0(task);
	std::thread t1(task);
	std::thread t2(task);
	std::thread t3(task);
	std::thread t4(task);
	std::cout << "ready to signal: release\n" << std::flush;
	csem.release(2);		//唤醒2个线程，唤醒数不能超过计数器的最大值
	std::cout << "signal end\n" << std::flush;
	t0.join();t1.join();t2.join();t3.join();t4.join();
}

/*
输出：
ready to signal: release
signal end
ready to recv signal: acquire
ready to recv signal: acquire
ready to recv signal: acquire
ready to recv signal: acquire
acquire end
acquire end
ready to recv signal: acquire
*/
```

