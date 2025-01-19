---
create: '2025-01-18'
modified: '2025-01-18'
---

# 基本API-ip address和endpoint

## IP address

下面的API用于创建各种`ip::address`类型：

- `ip::address(v4_or_v6_address)`：**（常用）**这个函数把一个v4或者v6的地址转换成 ip::address

  ```C++
  /// Construct an address from an IPv4 address.
  ASIO_DECL address(const asio::ip::address_v4& ipv4_address) noexcept;
  
  /// Construct an address from an IPv6 address.
  ASIO_DECL address(const asio::ip::address_v6& ipv6_address) noexcept;
  ```

- `ip::address:from_string(str)`：**（常用）**这个函数根据一个IPv4地址或者一个IPv6地址创建一个地址

  ```C++
  inline address address::from_string(const std::string& str)
  {
      return asio::ip::make_address(str);	// 内部依然是通过先创建address_v6或者address_v4再构造address，也就是上面的构造函数API（见源码：ddress make_address(const char* str,asio::error_code& ec) noexcept）
  }
  ```

- `ip::address_v4::broadcast([addr, mask])`：这个函数创建了一个广播地址

- `ip::address_v4::any()`：这个函数返回一个能表示任意地址的地址

- `ip::address_v4::loopback(), ip_address_v6::loopback()`：这个函数返回环路地址（为v4/v6协议）

辅助API：

- `ip::host_name()`：这个函数用 string 数据类型返回当前的主机名
- `ip::address::to_string()` ：这个函数返回这个地址的字符串

注：不支持传递域名

## 终端端点endpoint

所谓endpoint就是**用来通信的端对端的节点**，可以通过ip地址和端口构造，其它的endpoint可以连接这个endpoint进行通信。不同类型的 socket 有它自己的 endpoint 类，比如 ip::tcp::endpoint、 ip::udp::endpoint 和 ip::icmp::endpoint。

- `endpoint()`：这是默认构造函数，某些时候可以用来创建UDP/ICMP socket
- `endpoint(const InternetProtocol& internet_protocol, port_type port_num)`：**（常用）**这个方法通常用来创建可以接受新连接的**服务器端端点**
- `endpoint(const asio::ip::address& addr, port_type port_num)`：**（常用）**这个方法创建了一个连接到某个地址和端口的**客户端端点**

```C++
using namespace std;
using namespace boost::asio::ip;

// 客户端可以通过对端地址和端口构造一个endpoint，用这个endpoint和其通信
string ip = "192.168.1.12";	
int port = 8080;
tcp::endpoint c_ep(address::from_string(ip),port);

// 服务端只需根据本地地址绑定就可以生成endpoint
int server_port = 8080;	
tcp::endpoint s_ep(address_v4::any(), server_port);
```

给定一个端点，可以获得他的地址，端口和IP协议（v4或者v6）：

```C++
cout << s_ep.address().to_string() << ":" << s_ep.port() << "/" << s_ep.protocol().protocol() << endl;
```

注：因为 `s_ep.protocol()` 返回的 `T::protocol_type` 类型（即`tcp`/`udp`）没有与 `std::cout` 流兼容的 `<<` 运算符重载，调用tcp类的成员函数protocol()就能得到对应的协议在asio中的int编号（socket_types.hpp）

## 参考资料

* [第一章：Asio 建立网络连接基本 API |](https://xiaoyangst.github.io/2024/08/30/第一章：Asio-建立网络连接基本-API/)