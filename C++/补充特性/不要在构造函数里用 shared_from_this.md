---
create: '2025-06-11'
modified: '2025-06-11'
---

# 不要在构造函数里用 `shared_from_this`

## 一、问题概述
在C++中，用`shared_from_this`的前提是当前对象已经被`shared_ptr`托管。因此**在构造函数中使用 `shared_from_this()` 是不安全的**，会导致 `std::bad_weak_ptr` 异常。

下面这个看起来正确的代码，实际上会报错：

```C++
#include <iostream>
#include <vector>
#include <memory>

struct node : std::enable_shared_from_this<node> {
	node() = default;
	node(std::shared_ptr<node> parent) : parent_(parent) {
		parent->nodes_.push_back(shared_from_this());	// error here
	}

	std::vector<std::shared_ptr<node>> nodes_;
	std::weak_ptr<node> parent_;

	~node() {
		std::cout << "node deleted" << std::endl;
	}
};

int main()
{
	auto p = std::make_shared<node>();
	auto c1 = std::make_shared<node>(p);	// error!
	auto c2 = std::make_shared<node>(p);
	auto c3 = std::make_shared<node>(p);
	return 0;
}
```

## 二、原因分析

继承了`shared_from_this`的时候实际上继承了一个`weak_ptr`，用`weak_ptr->lock`拿到`shared_ptr`，而这个`weak_ptr`是由`shared_ptr`的构造函数初始化的。但此时对象还没有被任何 `std::shared_ptr` 管理，即外部的c1还未管理该对象，因此 `shared_from_this()` 无法正常工作。

总结问题：构造函数里的`std::enable_shared_from_this` 依赖于`this`已经被`shared_ptr`托管，即外部的c1被创建完毕，而外部的c1又依赖于构造函数执行完毕，因此出现了矛盾。

## 三、解决方案
### 使用辅助函数
如果需要在构造时设置某些依赖于 `shared_from_this()` 的逻辑，可以使用辅助函数。例如：
```cpp
#include <iostream>
#include <vector>
#include <memory>

struct node : std::enable_shared_from_this<node> {
	node() = default;

	// 修改构造函数，不直接调用 shared_from_this()
	void setParent(std::shared_ptr<node> parent) {
		parent_ = parent;
		parent->nodes_.push_back(shared_from_this());
	}

	std::vector<std::shared_ptr<node>> nodes_;
	std::weak_ptr<node> parent_;

	~node() {
		std::cout << "node deleted" << std::endl;
	}
};

int main() {
	auto p = std::make_shared<node>();

	// 使用辅助函数 setParent 来设置父节点
	auto c1 = std::make_shared<node>();
	c1->setParent(p);

	auto c2 = std::make_shared<node>();
	c2->setParent(p);

	auto c3 = std::make_shared<node>();
	c3->setParent(p);

	return 0;
}
```