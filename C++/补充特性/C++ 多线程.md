---
create: 2023-07-08
---
## C++ 多线程

#### 1.线程

##### 1.库`<Thread>`

##### 2.对象std::thread

> 在对象创建时，线程就开始执行，使用join或detach来确认主线程和子线程之间的关系。

* thread()
	* 默认构造函数
	* 创建一个线程，什么都不做
* template<class Fn, class ... Arg> thread(Fn && fn,Args && ... args)
	* 初始化构造函数
	* 创建一个线程，以args为参数，执行fn函数

##### 3.常用成员函数

* void join()
	* 阻塞等待线程结束并清理资源
* void detach()
	* 将线程与调用其的线程分离，彼此独立执行
	* 注意：如果需要该函数，必须在线程被创建时立刻调用。
* bool joinable()
	* 返回线程是否能执行join函数
	* 当线程执行过join或者detach，则不能再次被执行
* std::thread::id get_id()
	* 获取线程id

