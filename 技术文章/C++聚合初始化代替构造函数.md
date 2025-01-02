---
create: '2024-12-25'
modified: '2024-12-25'
---

# 聚合初始化代替构造函数

## 场景

存在一个纯粹的数据对象，使用vector管理。为了能灵活地设置值，使用聚合初始化（`{.a = 1, .b = 2, .c = "hello"}`）的方式。

```C++
struct node
{
	enum file_type {
		file,
		folder
	};
	std::string name;
	std::string patientname;
	file_type type;
	std::list<node> children;
	std::string content;
	node* parent = nullptr;
}

class fs
{
private:
    std::vector<node> nodes_;
}
```

## 新的需求

给该数据对象添加一个mutex来保证并发安全：

* 在node内部添加一个mutex

  ```C++
  struct node
  {
  	enum file_type {
  		file,
  		folder
  	};
  	std::string name;
  	std::string patientname;
  	file_type type;
  	std::list<node> children;
  	std::string content;
  	node* parent = nullptr;
      std::mutex mtx;
  }
  ```

* 在外部的全局管理器中添加一个`node*->mutex`的映射

  ```C++
  class fs
  {
  private:
      std::vector<node> nodes_;
      std::unordered_map<node*, std::mutex> mutexs_;
  }
  ```

### 方案一

由于std::mutex不可复制，且不可移动，导致vector的push_back不能正常调用node默认生成的复制构造。因此需要额外添加这部分代码来避开std::mutex的复制。

另一方面，新添加的代码导致结构体失去了聚合初始化的能力，导致只能对具体的场景设计构造函数。

### 方案二

采用映射管理mutex可行，但是同样由于mutex不可复制，且不可移动，需要使用std::shared_ptr包裹一层来保证可用。

## 新的架构

为了解决这种糟糕的问题，对于某个数据成员众多，使用方式复杂灵活的类，可以采用下面的方式进行封装。核心思路是独立出一个纯数据成员用于聚合初始化，同时内部使用std::shared_ptr管理来优化使用开销。

```C++
class A
{
public:
    struct Data
    {
        int a;
        int b;
        int c;
	};
    A(std::shared_ptr<Data> _data) : data(_data) {}
private:
    std::shared_ptr<Data> data;
};

int main()
{
    A a(new A::Data{.a = 100, .b = 200, .c = 300});
}
```



```C++
class A
{
public:
    struct Data
    {
        int a;
        int b;
        int c;
        std::mutex mtx;
	};
    A(Data&& _data) : data(std::move(_data)) {}
private:
    Data&& data;
};

class manager{
    std::vector<A> As;
};

int main()
{
    A a(A::Data{.a = 100, .b = 200, .c = 300});
    
    As.push_back(a);
}
```