---
create: '2025-01-19'
modified: '2025-01-23'
---

# 基本API-steady_timer超时检测

## API说明

* `asio::steady_timer(io_context, timeout)`：构造函数，创建一个定时器，设定超时时间并绑定到io_context上。
* `async_wait(handler)`：成员函数，添加异步定时任务到io_context上。

示例：

```C++
// 创建timer(io_context, timeout)
asio::steady_timer timer(socket.get_executor(), asio::chrono::seconds(1));

// 创建定时任务
timer.async_wait(std::bind(&Session::timer_call_back, shared_from_this(), std::placeholders::_1));
```

## 使用案例：socket超时检测

```C++
class Session : std::enable_shared_from_this<Session>{
    
    Session() : timer(socket.get_executor(), asio::chrono::seconds(1)) {}
    
    // ...
    
    void add_timeout_check() {
        timer.async_wait(std::bind(&Session::timer_call_back, shared_from_this(), std::placeholders::_1));
    }
    
    void time_call_back(asio::error_code ec) {
        if(!ec) {
            socket.close(ec);
        }
    }
private:
    // ...
    asio::steady_timer timer;
    asio::ip::tcp::socket socket;
}
```