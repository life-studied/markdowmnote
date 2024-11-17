---
create: 2024-04-11
---
# asio同步读写read/write（from buffer）

​	读写是通过`socket`的成员函数或者全局函数完成的，总是会用到socket。

## 写入

### 1. socket::write_some

​	用于写入buffer中的一部分字符到socket中，数量不定。

​	由于是**非阻塞的写入**，因此一次可能发不完，常常接受了返回值配合循环使用。

* 参数：被读取的buffer
* 返回值：写入的字节数。

```C++
template <typename Stream>
template <typename ConstBufferSequence>
std::size_t buffered_write_stream<Stream>::write_some(const ConstBufferSequence& buffers);
```

**实践**

```C++
void write_to_socket(socket& s, std::string& buf)
{
    size_t total_written_bytes = 0;
    while(total_written_bytes != buf.length())
    {
        total_written_bytes += s.write_some(asio::buffer(buf.c_str() + total_written_bytes, buf.length() - total_written_bytes));
    }
}
```

### 2. socket::send

​	用于写入buffer到socket中，**阻塞直到全部写入**。

* 参数：被读取的buffer
* 返回值：
  * `<0`：系统级错误
  * `=0`：对方关闭
  * `>0`：正常，buffer的size

```C++
template <typename ConstBufferSequence>
  std::size_t send(const ConstBufferSequence& buffers)
```

**实践**

```C++
void write_to_socket(socket& s, std::string& buf)
{
    int send_length = s.send(asio::buffer(buf.c_str(), buf.length()));
    if(send_length <= 0)	return;
}
```

### 3. asio::write

​	同send，但用的是全局函数。

```C++
void write_to_socket(socket& s, std::string& buf)
{
    int send_length = asio::write(s, asio::buffer(buf.c_str(), buf.length()));
    if(send_length <= 0)	return;
}
```

## 读取

### 1.  socket::read_some

​	非阻塞读，每次读取<=buffer.size的不确定大小字节，返回读取的数量。可以配合循环使用。

* 参数：被写入的buffer
* 返回值：读取字节数

```C++
template <typename MutableBufferSequence>
  std::size_t read_some(const MutableBufferSequence& buffers);
```

**实践**

```C++
std::string read_from_socket(asio::ip::tcp::socket& s)
{
    constexpr uint8_t message_size = 7;
    char buf[message_size];
    size_t total_bytes_readed = 0;
    while(total_bytes_readed != message_size)
    {
        total_bytes_readed += s.read_some(asio::buffer(buf + total_bytes_readed, message_size - total_bytes_readed));
    }
    
    return {buf, total_bytes_readed};
}
```

### 2. socket::receive

​	用于读取data到buffer中，**阻塞直到写满buffer**。

* 参数：被写入的buffer
* 返回值：
  * `<0`：系统级错误
  * `=0`：对方关闭
  * `>0`：buffer的size

```C++
template <typename MutableBufferSequence>
  std::size_t receive(const MutableBufferSequence& buffers);
```

**实践**

```C++
int read_data_by_recv()
{
    std::string raw_ip_addr = "127.0.0.1";
    unsigned short port_num = 3333;
    try
    {
        asio::ip::tcp::endpoint ep(asio::ip::address::from_string(raw_ip_addr), port_num);
        asio::io_context ioc;
        asio::ip::tcp::socket s(ioc, ep.protocal());
        s.connect(ep);
        
        constexpr uint8_t message_size = 7;
    	char buf[message_size];
        
        int recv_length = s.receive(asio::buffer(buf, message_size));
        if(recv_length <= 0)
        {
            std::cout << "Error!" << "code = " << recv_length << '\n';
            return recv_length;
        }
        return recv_length;
    }
    catch(system::system_error& e)
    {
        std::cout << "Error occured! Error code:" << e.code() <<
            ".Error Message:" << e.what() << '\n';
        return e.code().value();
	}
}
```

### 3. asio::read

​	同receive，但用的是全局函数。

```C++
int read_length = asio::read(s, asio::buffer(buf, message_size));
```

