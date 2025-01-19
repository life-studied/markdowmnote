---
create: '2025-01-18'
modified: '2025-01-18'
---

# 基本API-socket同步读写

> 在同步读写之前，确保socket已经完成了前期的流程（见上一章内容socket流程）。

[TOC]

## 读写常用API

read和write是更通用的文件流写法，而send和recv是socket的成员写法。

> 关于buffer是什么类型，可以详见[本章节](./客户端服务器实战/01 asio socket的创建与连接.md)的4.基础：buffer

```C++
// 写
template <typename ConstBufferSequence>
std::size_t send(const ConstBufferSequence& buffers);

// 读
template <typename MutableBufferSequence>
std::size_t receive(const MutableBufferSequence& buffers)
```

### 读：read和recv

```C++
const size_t BUFF_SIZE = 7;
char buffer_receive[BUFF_SIZE + 1]; // +1 用于字符串终止符

try {
    int recv_length = sock.receive(asio::buffer(buffer_receive, BUFF_SIZE));
    // int recv_length = read(sock, asio::buffer(buffer_receive,BUFF_SIZE));
    if (recv_length > 0) {
        buffer_receive[recv_length] = '\0'; // 确保字符串终止
        cout << "Received: " << buffer_receive << endl;
    } else {
        cout << "recv failed!!!" << endl;
    }
} catch (const boost::system::system_error& e) {
    cout << "Exception during receive: " << e.what() << endl;
}

```

### 写：write和send

```C++
try {
    std::string buf = "My name is ClientA";
    int send_length = sock.send(buffer(buf.c_str(), buf.length()));
    // int send_length = write(sock, buffer(buf.c_str(), buf.length()));
    if (send_length != buf.length())
    {
        cout << "Warning: Not all data sent, only " << send_length << " bytes sent." << endl;
    }
} catch (const boost::system::system_error& e) {
    cout << "Exception during send: " << e.what() << endl;
}
```

## 读写的非阻塞方式

使用非阻塞方式的好处是，可以在信息接收和发送一半需要等待的时候，利用等待时间干点别的，最大程度提升性能。但是缺点是代码编写麻烦，如果不是特别有必要，还是用上面的简单方法。

### read_some

```C++
const unsigned char SIZE = 10;
char buff[SIZE];
std::size_t total_read_bytes = 0;

// 循环读取=>等价read
while (total_read_bytes != SIZE) {
    total_read_bytes = total_read_bytes + socket.read_some(boost::asio::buffer(buff + total_read_bytes, SIZE - total_read_bytes));
}
```

### write_some

```C++
std::string buff("hello world");
std::size_t total_write_bytes = 0;

// 循环发送=>等价write
while (total_write_bytes != buff.length()) {
    total_write_bytes = total_write_bytes + socket.write_some(boost::asio::buffer(buff.c_str() + total_write_bytes, buff.length() - total_write_bytes));
}
```