---
create: '2025-01-18'
modified: '2025-01-19'
---

# 基本API-socket

asio 有三种类型的套接字类，在各自的namespace下：ip::tcp, ip::udp 和 ip::iocmp。

```C++
tcp::socket sock(ioc);	// 一般初始化就将其绑定到io_context上
udp::socket sock(ioc);
```

---

## 总结

客户端API流程

```C++
// 客户端
string ip = "192.168.1.12";
int port = 8080;
tcp::endpoint ep(address::from_string(ip), port);

io_context ioc;
tcp::socket sock(ioc, ep.protocol());
sock.connect(ep);

// 后续就可以通信了
```

服务器API流程

```C++
const int BACKLOG_SIZE = 30;
int server_port = 8080;
tcp::endpoint s_ep(address_v4::any(), server_port);

// 创建用于接收客户端连接的 socket 类型，即 acceptor
io_context ic;
tcp protocol = tcp::v4();
tcp::acceptor acceptor(ic);
acceptor.open(protocol);

// 绑定、监听、接收客户端连接
acceptor.bind(s_ep);
acceptor.listen(BACKLOG_SIZE);

tcp::socket c_socket(ic);
acceptor.accept(c_socket);

// 后续就可以和客户端通信了
```

## API介绍

### 非常规API：

- `assign(protocol, socket)`：**（底层）**这个函数接收了一个原生的 socket 类型给这个 socket 实例。当对接老（旧）程序时会使用它（也就是说，原生socket已经被建立了）

  ```c++
  int native_socket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
  // 将裸socket分配给asio的socket
  socket.assign(asio::ip::tcp::v4(), native_socket);
  ```

### 常规流程API：

下面是依次按照流程进行介绍的API：

- `open(protocol)`：这个函数用给定的 IP 协议（v4 或者 v6）打开一个 socket。你主要在 UDP/iocMP socket，或者服务端 socket 上使用

  ```C++
  // 服务器（创建用于接收客户端连接的 socket）
  io_context ioc;
  tcp::acceptor acceptor(ioc);
  acceptor.open(tcp::v4());
  
  // 客户端（创建用于通信的 socket）
  io_context ioc;
  tcp::socket sock(ioc);
  sock.open(tcp::v4());
  ```

- `bind(endpoint)`：这个函数绑定到一个地址（服务器）

  ```C++
  // 服务端需要创建的端点
  int server_port = 8080;
  tcp::endpoint s_ep(address_v4::any(), server_port);
  
  // 创建用于接收客户端连接的 socket 类型，即 acceptor
  io_context ioc;
  tcp::acceptor acceptor(ioc);
  acceptor.open(tcp::v4());
  
  // 绑定 端点和acceptor
  acceptor.bind(s_ep);
  ```

- `connect(endpoint)`：这个函数用同步的方式连接到一个地址（客户端）

  ```C++
  //  创建端点，记录服务器的信息   
  string ip = "192.168.1.12";
  int port = 8080;
  tcp::endpoint ep(address::from_string(ip), port);
  
  //  连接到服务器
  io_context ioc;
  tcp::socket sock(ioc, ep.protocol());
  sock.connect(ep);
  
  // 后续就可以通信了
  ```

- `listen(int size)`：这个函数用于监听是否有连接（服务器）

  ```C++
  const int BACKLOG_SIZE = 30;
  int server_port = 8080;
  tcp::endpoint s_ep(address_v4::any(), server_port);
  
  // 创建用于接收客户端连接的 socket 类型，即 acceptor
  io_context ic;
  tcp protocol = tcp::v4();
  tcp::acceptor acceptor(ic);
  acceptor.open(protocol);
  
  // 绑定、监听、接收客户端连接
  acceptor.bind(s_ep);
  acceptor.listen(BACKLOG_SIZE);
  
  tcp::socket c_socket(ic);
  acceptor.accept(c_socket);
  
  // 后续就可以和客户端通信了
  ```

- async_connect(endpoint)：这个函数用异步的方式连接到一个地址

### 其它API：

- `is_open()`：如果套接字已经打开，这个函数返回 true
- `close()`：这个函数用来关闭套接字。调用时这个套接字上任何的异步操作都会被立即关闭，同时返回 error::operation_aborted 错误码
- `shutdown(type_of_shutdown)`：这个函数立即使 send 或者 receive 操作失效，或者两者都失效
- `cancel()`：这个函数取消套接字上所有的异步操作。这个套接字上任何的异步操作都会立即结束，然后返回 error::operation_aborted 错误码