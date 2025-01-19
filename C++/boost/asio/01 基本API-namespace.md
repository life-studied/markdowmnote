---
create: '2025-01-18'
modified: '2025-01-18'
---

# 基本API-namespace

下面是asio的常见namespace里的内容：

- `boost::asio`：这是核心类和函数所在的地方。重要的类有 `io_service` 和 `streambuf`。类似`read, read_at, read_until`方法，它们的异步方法，它们的写方法和异步写方法等自由函数也在这里
- `boost::asio::ip`：这是网络通信部分所在的地方。重要的类有 `address, endpoint, tcp, udp`和 `icmp`，重要的自由函数有 `connect` 和 `async_connect`。要注意的是在 `boost::asio::ip::tcp::socket`中间，`socket`只是 `boost::asio::ip::tcp` 类中间的一个`typedef`关键字
- `boost::asio::error`：这个命名空间包含了调用 I/O 例程时返回的错误码
- `boost::asio::ssl`：包含了 SSL 处理类的命名空间
- `boost::asio::local`：这个命名空间包含了 POSIX 特性的类
- `boost::asio::windows`：这个命名空间包含了 Windows 特性的类

## 参考资料

* [第一章：Asio 建立网络连接基本 API |](https://xiaoyangst.github.io/2024/08/30/第一章：Asio-建立网络连接基本-API/)