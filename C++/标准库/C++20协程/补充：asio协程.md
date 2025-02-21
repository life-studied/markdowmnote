---
create: '2025-02-18'
modified: '2025-02-18'
---

# asio协程

```C++
#include <asio/co_spawn.hpp>
#include <asio/detached.hpp>
#include <asio/io_context.hpp>
#include <asio/ip/tcp.hpp>
#include <asio/signal_set.hpp>
#include <asio/write.hpp>
#include <cstdio>
#include <iostream>
#include <future>

using asio::ip::tcp;
using asio::awaitable;
using asio::co_spawn;
using asio::detached;
using asio::use_awaitable;
namespace this_coro = asio::this_coro;

awaitable<void> echo(tcp::socket socket)
{
    try
    {
        char data[1024];
        std::size_t used = 0;
        for (;;)
        {
            std::size_t n = co_await socket.async_read_some(asio::buffer(data+used, sizeof(data)-used), use_awaitable);
            
            (void)std::async(std::launch::async, [&, n=n, used=used]() {
                std::cout << std::string(data+used).substr(n) << std::endl;
            });
            
            used+=n;
            co_await async_write(socket, asio::buffer(data, n), use_awaitable);
        }
    }
    catch (std::exception& e)
    {
        std::printf("echo Exception: %s\n", e.what());
    }
}

awaitable<void> listener()
{
    auto executor = co_await this_coro::executor;
    tcp::acceptor acceptor(executor, { tcp::v4(), 10086 });
    for (;;)
    {
        tcp::socket socket = co_await acceptor.async_accept(use_awaitable);
        co_spawn(executor, echo(std::move(socket)), detached);
    }
}

int main()
{
    try
    {
        asio::io_context io_context(1);
        asio::signal_set signals(io_context, SIGINT, SIGTERM);
        signals.async_wait([&](auto, auto) { io_context.stop(); });
        co_spawn(io_context, listener(), detached);
        io_context.run();   // epoll_wait
    }
    catch (std::exception& e)
    {
        std::printf("Exception: %s\n", e.what());
    }
}
```