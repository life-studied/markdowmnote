# GoogleTest Primer

## 创建测试套件

​	使用`TEST()`宏创建测试套件，它是一个不带返回值的C++函数

* TestSuiteName：测试套件名称，用于命名一组测试
* TestName：当前测试名称，用于命名当前测试
* 上述两个参数应以C++标准进行命名，并要求不带`_`

```C++
TEST(TestSuiteName, TestName) {
  ... test body ...
}
```

**案例**

​	对于下面的函数

```C++
int Factorial(int n);  // Returns the factorial of n
```

​	创建Test：

```c++
// Tests factorial of 0.
TEST(FactorialTest, HandlesZeroInput) {
  EXPECT_EQ(Factorial(0), 1);
}

// Tests factorial of positive numbers.
TEST(FactorialTest, HandlesPositiveInput) {
  EXPECT_EQ(Factorial(1), 1);
  EXPECT_EQ(Factorial(2), 2);
  EXPECT_EQ(Factorial(3), 6);
  EXPECT_EQ(Factorial(8), 40320);
}
```

## 创建Test Fixtures（类）：为多个测试配置相同的数据

### 1. 继承testing::Test类

​	public继承testing::Test，并在protected中使用`SetUp()`完成资源的初始化，使用`TearDown()`完成资源的释放。

```C++
class QueueTest : public testing::Test {
 protected:
  void SetUp() override {
     // q0_ remains empty
     q1_.Enqueue(1);
     q2_.Enqueue(2);
     q2_.Enqueue(3);
  }

  // void TearDown() override {}

  Queue<int> q0_;
  Queue<int> q1_;
  Queue<int> q2_;
};
```

### 2. 使用TEST_F()创建相关的测试套件

```C++
TEST_F(QueueTest, IsEmptyInitially) {
  EXPECT_EQ(q0_.size(), 0);
}

TEST_F(QueueTest, DequeueWorks) {
  int* n = q0_.Dequeue();
  EXPECT_EQ(n, nullptr);

  n = q1_.Dequeue();
  ASSERT_NE(n, nullptr);
  EXPECT_EQ(*n, 1);
  EXPECT_EQ(q1_.size(), 0);
  delete n;

  n = q2_.Dequeue();
  ASSERT_NE(n, nullptr);
  EXPECT_EQ(*n, 2);
  EXPECT_EQ(q2_.size(), 1);
  delete n;
}
```

## 断言

### ASSERT_*

​	`ASSERT_*`如果失败，会终止当前函数。

### EXPECT_*

​	`EXPECT_*`如果失败，不会终止当前函数。