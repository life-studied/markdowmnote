# stack & queue

​	栈与队列，是非常常见的数据结构。

​	一般讲的时候会把queue和stack放在一起讲，因为它们底层的数据结构都是双端队列deque，它允许左进左出，那就是stack，也允许右进右出，还是stack；还允许左进右出、右进左出，就是queue。

## stack

​	栈。先进后出。比较简单，用到的场景也比较简单。

```C++
#include <stack>
std::stack<int> s;
```

### 插入

​	插入一个到栈顶。

```C++
s.push(1);
```

### 获取

​	注意了，C++中的数据结构，一般不允许边获取边删除这种东西。

​	获取就是获取，不会有额外的操作。

```C++
auto data = s.top();		// return a ref -> int&
```

### 删除

​	删掉栈顶的元素。

```C++
s.pop();
```

## queue

​	队列，先进后出，一般常用场景是树的层序遍历或者图的广搜，在工程上常用于多个线程或异步操作之间的资源管理和共享。用起来也很简单。

```C++
#include <queue>

std::queue<int> q;
```

### 插入

```C++
q.push(1);
```

### 获取

```C++
auto data = q.front();		// return a ref -> int&
							// 注意，queue是front，而stack是top
							// 因为逻辑模型上，queue是横着放的，stack是竖着放的（笑）
```

### 删除

```C++
q.pop();
```

## priority_queue

​	优先级队列，放入数据后，自动在队列里排序。

​	队首的是优先级最高的。优先级可以是大，也可以是小。

​	底层数据结构是堆，堆是一种基于完全二叉树的数据结构，插入时在内部排序的操作被成为“堆化”。大的在前的叫做大顶堆，小的在前的叫做小顶堆。

​	排序的时间复杂度是`O(logn)`。

### Usage

​	使用方法与队列一样。除了获取是用`top`，因为它本质是一棵树，从树的顶上拿一个元素。

​	优先级队列的创建，需要设计优先级规则。不写就是默认是大的优先级高，放在前面。

```C++
#include <queue>
#include <iostream>
using namespace std;

priority_queue<int> q;		// default，大的在前

priority_queue<int, vector<int>, greater<int>> q1;	// 小的在前
priority_queue<int, vector<int>, less<int>> q2;	// 大的在前

int main()
{
	for (int i = 0; i < 10; i++)
	{
		q1.push(i);
		q2.push(i);
	}

	for (int i = 0; i < 10; i++)
	{
		cout << q1.top();
		q1.pop();
	}
	cout << '\n';
	for (int i = 0; i < 10; i++)
	{
		cout << q2.top();
		q2.pop();
	}
	return 0;
}
```

## 自定义

​	当代码写多了，自然会遇到需要自定义的情况。

​	比如，我希望我有一个优先级队列，里面还是正常排序，但是，我希望每个元素里面除了优先级本身，还顺便挂载了一个我想存进去的东西，它只是和这个元素有关，但是不影响排序。

​	这样一个东西，不适合存储在外部，所以只能和优先级一起存，取出来的时候也就能直接拿到了。

```C++
#include <queue>
#include <iostream>
using namespace std;
struct package
{
	package(int _l, double _d) :level(_l), data(_d) {}
	int level;
	double data;
};

bool operator<(const package& lhs, const package& rhs)
{
	return lhs.data < rhs.data;
}

int main()
{
	priority_queue<package, vector<package>, less<package>> q;
	q.push({1,2.0});
	auto data = q.top();
	cout << data.data;
	q.pop();
	return 0;
}
```



