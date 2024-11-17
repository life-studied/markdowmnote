---
create: 2023-07-13
---
## 04 promise和future

[TOC]

​	关键字：promise;future;shared_future;

#### 1.问题引入

​	为了解决一个简单的线程同步，下面的代码显得过于复杂：

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>

std::mutex mtx;
std::condition_variable cv;

void task(int a, int b, int& ret)
{
	int ret_a = a * a;
	int ret_b = b * 2;
	std::unique_lock<std::mutex> lock(mtx);
	cv.notify_one();
	ret = ret_a + ret_b;
}

int main()
{
	int ret = 0;
	std::thread t(task, 1, 2, std::ref(ret));

	std::unique_lock<std::mutex> lock(mtx);
	cv.wait(lock);
	std::cout << "return value is " << ret;
	
	t.join();
}
```

#### 2. 解决方法

​	可以使用`promise`和`future`来组合完成线程同步的问题。

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>
void task(int a, int b, std::promise<int>& ret)
{
	int ret_a = a * a;
	int ret_b = b * 2;
	ret.set_value(ret_a + ret_b);
}

int main()
{
	int ret = 0;

	std::promise<int> p;
	std::future<int> f = p.get_future();

	std::thread t(task, 1, 2, std::ref(p));

	std::cout << "return value is " << f.get();		//阻塞等待，直到返回值
	
	t.join();
}
```

#### 3. 其它应用场景

​	延迟传参：创建一个线程，先传入一个参数，在执行过程中再传入一个参数。

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>

void task(int a, std::future<int>& b, std::promise<int>& ret)
{
	int ret_a = a * a;
	int ret_b = b.get() * 2;
	ret.set_value(ret_a + ret_b);
}

int main()
{
	int ret = 0;

	std::promise<int> p_ret;
	std::future<int> f_ret = p_ret.get_future();

	std::promise<int> p_in;
	std::future<int> f_in = p_in.get_future();

	std::thread t(task, 1, std::ref(f_in), std::ref(p_ret));		

	std::this_thread::sleep_for(std::chrono::milliseconds(5000));	//means do something
	p_in.set_value(2);									//pass the param

	std::cout << "return value is " << f_ret.get();		//用get获取promise的value，但只能获取一次
	
	t.join();
}
```

#### 4. std::shared_future

​	`std::future`的值只能被获取一次，如果有多个线程都需要这个值时，应该用`std::shared_future`来完成。

​	注意，std::shared_future可以被复制，因此只需传递值即可。

```C++
#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>
void task(int a, std::shared_future<int> b, std::promise<int>& ret)
{
	int ret_a = a * a;
	int ret_b = b.get() * 2;
	ret.set_value(ret_a + ret_b);
}

int main()
{
	int ret = 0;

	std::promise<int> p_ret0;
	std::promise<int> p_ret1;
	std::promise<int> p_ret2;
	std::promise<int> p_ret3;
	std::future<int> f_ret0 = p_ret0.get_future();
	std::future<int> f_ret1 = p_ret1.get_future();
	std::future<int> f_ret2 = p_ret2.get_future();
	std::future<int> f_ret3 = p_ret3.get_future();

	std::promise<int> p_in;
	std::future<int> f_in = p_in.get_future();

	std::shared_future<int> s_f = f_in.share();			//获取shared

	std::thread t0(task, 1, s_f, std::ref(p_ret0));		//传递shared
	std::thread t1(task, 1, s_f, std::ref(p_ret1));		
	std::thread t2(task, 1, s_f, std::ref(p_ret2));		
	std::thread t3(task, 1, s_f, std::ref(p_ret3));		

	std::this_thread::sleep_for(std::chrono::milliseconds(5000));
	p_in.set_value(2);

	std::cout << "return value is " << f_ret0.get() << std::endl;		//用get获取promise的value，但只能获取一次
	std::cout << "return value is " << f_ret1.get() << std::endl;		//用get获取promise的value，但只能获取一次
	std::cout << "return value is " << f_ret2.get() << std::endl;		//用get获取promise的value，但只能获取一次
	std::cout << "return value is " << f_ret3.get() << std::endl;		//用get获取promise的value，但只能获取一次
	
	t0.join();
	t1.join();
	t2.join();
	t3.join();
}
```

