---
create: '2024-12-14'
modified: '2025-02-13'
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

## 协程的返回值类型

规定：这个返回值类型应该拥有一个`promise_type`的内部类。

```C++
struct Result {
    struct promise_type {
        ...
    };
};
```

区别一个函数是不是协程，是通过它的返回值类型来判断的。如果它的返回值类型满足协程的规则，那这个函数就会**被编译成**协程。

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

## get_return_object

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
            // 创建 Result 对象
            return {};
        }

        ...
    };
};
```

> 再次强调：函数刚进入，整个协程的状态被创建出来之后，会立即构造 `promise_type` 对象，进而调用 `get_return_object` 来创建返回值对象。

另外关于`promise_type`的**构造函数**：`promise_type` 类型的构造函数参数列表如果与协程的参数列表一致，那么构造 `promise_type` 时就会调用这个构造函数。否则，就通过默认无参构造函数来构造 `promise_type`。

## initial_suspend

为了方便灵活扩展，协程体正式执行的第一步是隐式地调用 `co_await promise.initial_suspend()`，`initial_suspend` 的返回值就是一个等待体对象（awaiter），如果返回值满足挂起的条件，则协程体在最一开始就立即挂起。

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

## final_suspend

当协程执行完成或者抛出异常之后会先清理局部变量，接着调用 `final_suspend` 来方便开发者自行处理其他资源的销毁逻辑。

`final_suspend` 也可以返回一个等待体使得当前协程挂起，但之后当前协程应当通过 `coroutine_handle` 的 `destroy` 函数来直接销毁协程体，而不是再次 `resume`。

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