## 05 packaged_task和async

[TOC]

​	关键字：packaged_task;async;future;

#### 1. async

​	为了简化`promise`和`future`的使用，可以用`async`让函数直接返回值，并用`future`获取。

​	注意：`async`不一定会创建一个线程，而是默认采用自动决定（`std::launch::deffered | std::launch::async`）的；可以使用第一个参数`std::launch::async/deffered`来强制决定**开启一个线程**还是**延迟调用**。

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>
int task(int a, int b)
{
	int ret_a = a * a;
	int ret_b = b * 2;
	
	return ret_a + ret_b;
}

int main()
{
	std::future<int> f = std::async(/*std::launch::async,*/ task, 1, 2);

	std::cout << "return value is " << f.get();		//阻塞等待，直到返回值
}
```

#### 2. packaged_task

​	和async类似，packaged_task需要传入一个函数的返回值和形参列表来实例化模板，通过调用执行后，可以拿到future对象。

​	注意：即使可以通过`std::bind`来打包函数和参数，也必须通过调用才能执行。

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>
int task(int a, int b)
{
	int ret_a = a * a;
	int ret_b = b * 2;
	
	return ret_a + ret_b;
}

int main()
{
	std::packaged_task<int(int, int)> t(task);
	
	t(1, 2);

	std::cout << "return value is " << t.get_future().get();		//阻塞等待，直到返回值

    /*
    std::packaged_task<int()> t(std::bind(task, 1, 2));
    t();
    std::cout << "return value is " << t.get_future().get();		//阻塞等待，直到返回值
    */
}
```

