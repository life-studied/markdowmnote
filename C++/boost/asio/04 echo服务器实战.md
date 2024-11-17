---
create: 2024-04-14
---
# echo服务器实战

## 简单裸指针版本

### header

```C++
class Session
{
public:
    Session(boost::asio::io_context& ioc):_socket(ioc){
    }
    tcp::socket& Socket() {
        return _socket;
    }
    void Start();
private:
    void handle_read(const boost::system::error_code & error, size_t bytes_transfered);
    void handle_write(const boost::system::error_code& error);
    tcp::socket _socket;
    enum {max_length = 1024};
    char _data[max_length];
};

class Server {
public:
    Server(boost::asio::io_context& ioc, short port);
private:
    void start_accept();
    void handle_accept(Session* new_session, const boost::system::error_code& error);
    boost::asio::io_context& _ioc;
    tcp::acceptor _acceptor;
};
```

### Session

#### start

```C++
void Session::Start(){
    memset(_data, 0, max_length);
    _socket.async_read_some(boost::asio::buffer(_data, max_length),
        std::bind(&Session::handle_read, this, placeholders::_1,
            placeholders::_2)
    );
}
```

#### handle_read

```C++
void Session::handle_read(const boost::system::error_code& error, size_t bytes_transfered) {
    if (!error) {
        cout << "server receive data is " << _data << endl;
        boost::asio::async_write(_socket, boost::asio::buffer(_data, bytes_transfered), 
            std::bind(&Session::handle_write, this, placeholders::_1));
    }
    else {
        delete this;
    }
}
```

#### handle_write

```C++
void Session::handle_write(const boost::system::error_code& error) {
    if (!error) {
        memset(_data, 0, max_length);
        _socket.async_read_some(boost::asio::buffer(_data, max_length), std::bind(&Session::handle_read,
            this, placeholders::_1, placeholders::_2));
    }
    else {
        delete this;
    }
}
```

### Server

```C++
Server::Server(boost::asio::io_context& ioc, short port) :_ioc(ioc),
_acceptor(ioc, tcp::endpoint(tcp::v4(), port)) {
    start_accept();
}
void Server::start_accept() {
    Session* new_session = new Session(_ioc);
    _acceptor.async_accept(new_session->Socket(),
        std::bind(&Server::handle_accept, this, new_session, placeholders::_1));
}
void Server::handle_accept(Session* new_session, const boost::system::error_code& error) {
    if (!error) {
        new_session->Start();
    }
    else {
        delete new_session;
    }
    start_accept();
}
```

### main

```C++
int main(int argc, char* argv[])
{
  try
  {
    if (argc != 2)
    {
      std::cerr << "Usage: async_tcp_echo_server <port>\n";
      return 1;
    }

    boost::asio::io_context io_context;

    Server s(io_context, std::atoi(argv[1]));

    io_context.run();
  }
  catch (std::exception& e)
  {
    std::cerr << "Exception: " << e.what() << "\n";
  }

  return 0;
}
```

​	`io_context.run` 是一个阻塞（blocking）调用，姑且把它想象成一个 loop（事件循环），直到所有异步操作完成后，loop 才结束，`run` 才返回。

### 客户端

```C++
#include <iostream>
#include <boost/asio.hpp>
using namespace std;
using namespace boost::asio::ip;
const int MAX_LENGTH = 1024;
int main()
{
    try {
        //创建上下文服务
        boost::asio::io_context   ioc;
        //构造endpoint
        tcp::endpoint  remote_ep(address::from_string("127.0.0.1"), 10086);
        tcp::socket  sock(ioc);
        boost::system::error_code   error = boost::asio::error::host_not_found; ;
        sock.connect(remote_ep, error);
        if (error) {
            cout << "connect failed, code is " << error.value() << " error msg is " << error.message();
            return 0;
        }
        std::cout << "Enter message: ";
        char request[MAX_LENGTH];
        std::cin.getline(request, MAX_LENGTH);
        size_t request_length = strlen(request);
        boost::asio::write(sock, boost::asio::buffer(request, request_length));
        char reply[MAX_LENGTH];
        size_t reply_length = boost::asio::read(sock,
            boost::asio::buffer(reply, request_length));
        std::cout << "Reply is: ";
        std::cout.write(reply, reply_length);
        std::cout << "\n";
    }
    catch (std::exception& e) {
        std::cerr << "Exception: " << e.what() << endl;
    }
    return 0;
}
```

* [恋恋风辰官方博客 (llfc.club)](https://llfc.club/category?catid=225RaiVNI8pFDD5L4m807g7ZwmF#!aid/2ODYV1A2xbhTjWr0FJ1ZS22ijZO)

## 通用的智能指针版本

> ​	上面的版本，虽然没有问题，但是不够通用：因为在异步读的回调中调用了异步写，异步写的回调中调用了异步读，一读一写，形成串行的操作，不会使得最后的delete析构多次。

> 但是实际的场景中，异步读中会接着读并且写，导致出错后delete多次，造成资源出错。或者其它管理难度导致的出错。
>
> ​	解决方案：构造伪闭包传递智能指针，保证资源在最后一次delete时析构。

### 具体思路

#### 1. 使用shared_ptr管理

```C++
auto ptr = make_shared<Session>(ioc);
boost::asio::async_read(...);
```

#### 2. 通过uuid来唯一标识Session

```C++
_sessions.insert({_uuid, ptr});
```

#### 3. 使用std::bind传递智能指针

```C++
std::bind(&Session::handle_write, this, std::placeholder::_1, _self_shared);
```

#### 4. 连接结束，从连接池中删除

```C++
_sessions.erase(session->GetUuid());
```

### 其它

#### 1. 创建uuid

```C++
boost::uuids::uuid  a_uuid = boost::uuids::random_generator()();
/*string*/ _uuid = boost::uuids::to_string(a_uuid);
```

#### 2. 继承enabled_from_this

```C++
class Session : public std::enable_shared_from_this<Session>
```

* [恋恋风辰官方博客 (llfc.club)](https://llfc.club/category?catid=225RaiVNI8pFDD5L4m807g7ZwmF#!aid/2OEQEc6p4k79cXsTr6dOVfZbo79)

