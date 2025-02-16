---
create: '2025-02-14'
modified: '2025-02-14'
---

# co_return | co_yield | co_await

协程体当中会存在 co_await、co_yield、co_return 三种协程特有的调用，其中

- co_await 我们前面已经介绍过，用来将协程挂起。
- co_yield 则是 co_await 的一个马甲，用于挂起时传值给上层调用者。
- co_return 则用来协程结束时退出或者返回一个值。

## co_return

co_return 用于从协程体结束协程，直接退出或者返回一个值。

### return_value

对于返回一个值的情况，需要在 promise_type 当中定义一个函数：

```cpp
??? return_value();
```

例如：

```cpp
struct Result {
    struct promise_type {

        void return_value(int value) {
            ...
        }

        ...

    };
};
```

此时，我们的 Coroutine 函数就需要使用 co_return 来返回一个整数了：

```cpp
Result Coroutine() {
    ...
    co_return 1000;
};
```

1000 会作为参数传入，即 return_value 函数的参数 value 的值为 1000。这个值可以存到 promise_type 对象当中，外部的调用者可以获取到。

### return_void

除了返回值的情况以外，C++ 协程当然也支持返回 void。只不过 `promise_type` 要定义的函数就不再是 `return_value` 了，而是 `return_void` 了：

```cpp
struct Result {
    struct promise_type {

        void return_void() {
            ...
        }

        ...
    };
};
```

这时，协程内部就可以通过 co_return 来退出协程体了：

```cpp
Result Coroutine() {
    ...
    co_return;
};
```

### unhandled_exception

协程体除了正常返回以外，也可以抛出异常。异常实际上也是一种结果的类型，因此处理方式也与返回结果相似。我们只需要在 promise_type 当中定义一个函数，在异常抛出时这个函数就会被调用到：

```cpp
struct Result {
    struct promise_type {

        void unhandled_exception() {
            exception_ = std::current_exception();
            std::rethrow_exception(exception_);
        }

        ...
    };
};
```

## co_yield

`co_yield`专门用于从协程中挂起并直接返回一个值。它简化了co_await的操作，更加直观简洁地实现了返回值的效果。

### yield_value

`co_yield`通过`yield_value`来将返回值传递出去。

```C++
struct Result {
    struct promise_type {

        void yield_value(int val) {
			
        }

        ...
    };
};

Result Coroutine() {
    int i = 0;
    while (true) {
        co_yield i++;
    }
};
```