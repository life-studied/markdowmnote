---
create: '2024-12-14'
modified: '2025-11-08'
---

# coroutine函数

在C++20中，协程与普通的函数是一样的，都是以一种函数的形式展现；标准规定，如果一个函数的**返回类型符合协程的规则**的类型，那么这个函数就是一个协程。

```C++
Result f() {
    ...
}
```

## quick view

```C++
struct Result {
    struct promise_type {
		
        Awaiter initial_suspend() {
            ...
            return Awaiter{};
        }
        
        Awaiter final_suspend() {
            ...
            return Awaiter{};
        }
        
        Result get_return_object() {
            // 创建 Result 对象
            return {};
        }
        
        // co_return 返回值（见co_return一节）
        void return_value(int value) {
            ...
            return;
        }
        
        // co_return 不返回值
        //void return_void() {
        //
        //    return;
    	//}

        ...
    };
};
```

## 1. 协程函数的返回值类型的要求（`Ret::promise_type`）

规定：这个返回值类型应该拥有一个`promise_type`的内部类。

```C++
// 1. 正常情况
struct Result {
    struct promise_type {
        ...
    };
};

// 2. 利用using的情况
struct MyPromise {
    ...
};

struct Result {
    using promise_type = MyPromise;
};
```

区别一个函数是不是协程，是通过它的返回值类型来判断的。如果它的返回值类型满足协程的规则（内含`promise_type`），那这个函数就会**被编译成**协程。

> 规则如下：
>
> ```C++
> template <class _Ret, class = void>
> struct _Coroutine_traits {};
> 
> template <class _Ret>
> struct _Coroutine_traits<_Ret, void_t<typename _Ret::promise_type>> {
>     using promise_type = typename _Ret::promise_type;
> };
> 
> template <class _Ret, class...>
> struct coroutine_traits : _Coroutine_traits<_Ret> {};
> ```

简单来说，就是返回值类型 `_Ret` 能够找到一个类型 `_Ret::promise_type` 与之相匹配。（这个 `promise_type` 既可以是直接定义在 `_Ret` 当中的类型，也可以通过 `using` 指向已经存在的其他外部类型。）

## 2. 协程函数的返回值从哪里创建（`get_return_object`）

可以看到，协程函数并不显式地创建返回值：

```C++
Result Coroutine(int start_value) {
    std::cout << start_value << std::endl;
    co_await std::suspend_always{};
    std::cout << start_value + 1 << std::endl;
}
```

协程的返回值并非函数中显式创建并返回，而是在`promise_type`中通过`Result get_return_object()`函数获得，并且协程函数会在一开始时就自动调用它创建一个返回值对象。

```C++
struct Result {
    struct promise_type {

        Result get_return_object() {
            // 1. 创建 Result 对象，调用默认构造
            return {};
            
            // 2. 也可以这样，让Result调用句柄参数的构造函数（如果它有的话）
            // 获得协程句柄存储在内部：
            // return std::coroutine_handle<promise_type>::from_promise(*this);
        }

        ...
    };
    // 1.
    Result() = default;
    // 2.
    // Result(std::std::coroutine_handle<promise_type> coroutine)
    //    : mCoroutine(coroutine) {}

    // std::coroutine_handle<promise_type> mCoroutine;
};
```

> 再次强调：函数刚进入，整个协程的状态被创建出来之后，会立即构造 `promise_type` 对象，进而调用 `get_return_object` 来创建返回值对象。

另外关于`promise_type`的**构造函数**：`promise_type` 类型的构造函数参数来源于协程函数的参数。如果参数类型不匹配，就通过默认无参构造函数来构造 `promise_type`。

## 补充：协程的返回值/coroutine_handle/promise_type三者关系，如何理解

上面说了协程返回值的要求，即拥有一个`promise_type`的内置类型。

对于上层函数而言，其调用了协程函数，并拥有了这个返回值，相当于间接拥有了这个协程的句柄。

`coroutine_handle`一般需要手动存储在返回值里，要么就要存储`promise_type`的引用。这是因为`coroutine_handle`提供了成员函数`promise()`和静态函数`from_promise(promise)`，用于在handle和promise之间互相转换。

因此他们在使用上是等价的，只需要拥有一个就能拿到另一个。

而协程的返回值则是给上层函数看的东西，它把内部细节、句柄和promise包装了起来，简化提供给上层关心的接口和内容。

## 3. 协程函数的初始化和退出

### 等待体（awaiter）是什么

`awaiter`是最原始的对象，作为`co_await`的参数，用于给`co_await`运算符进行调度判断。

> （它需要满足的条件可以在下一节看到）

而`awaitable`则是更加广义上的等待体，重载了`co_await`运算符。它与awaiter的关系，可以类比成`原始指针`和`重载了operator->的类`。

### initial_suspend

为了方便灵活扩展，协程体正式执行的第一步是隐式地调用 `co_await promise.initial_suspend()`。

正如上面所说，`co_await`的参数是一个`awaiter`，因此`initial_suspend` 的返回值就是一个等待体对象（awaiter），如果返回值满足挂起的条件，则协程体在最一开始就立即挂起。

```C++
struct Result {
    struct promise_type {
		
        Awaiter initial_suspend() {
            ...
            return Awaiter{};
        }
        
        Result get_return_object() {
            // 创建 Result 对象
            return {};
        }

        ...
    };
};
```

### final_suspend

当协程执行完成或者抛出异常之后会先清理局部变量，接着调用 `co_await promise.final_suspend()` 来方便开发者在协程结束时，自行处理资源的销毁逻辑。
它的返回值规则和`initial_suspend`一致，因此也要返回一个awaiter。

`final_suspend` 也可以返回一个使得当前协程挂起的等待体，但之后当前协程应当通过 `coroutine_handle` 的 `destroy` 函数来直接销毁协程体，而不是再次 `resume`。

```C++
struct Result {
    struct promise_type {
		
        Awaiter initial_suspend() {
            ...
            return Awaiter{};
        }
        
        Awaiter final_suspend() {
            ...
            return Awaiter{};
        }
        
        Result get_return_object() {
            // 创建 Result 对象
            return {};
        }

        ...
    };
};
```