# asio异步读写

[TOC]

## 异步写入

​	异步写入有时序的问题，需要额外考虑。

​	常用API为`async_write_some`和`async_send`。工程中常用后者，简单方便，后者底层调用了前者，两者不要混合使用。

### async_write_some

​	异步发送，完成后调用WriteToken回调。

​	调用WriteToken时，传入错误码和发送了的字节数

> 由于TCP的缓冲区会小于用户的缓冲区，因此发送的字节数可能会小于需要发送的数量。
>
> 因此设计该回调函数时，需要检测是否再次调用async_write_some来继续发送。

* 参数

  * buffer：发送缓冲区，ConstBufferSequence模板类型

  * token：完成时的回调函数，WriteToken函数类型

  * > `WriteToken:void(boost::system::error_code, std::size_t);`

* 返回值：

```C++
BOOST_ASIO_COMPLETION_TOKEN_FOR(void (boost::system::error_code,
        std::size_t)) WriteToken
          BOOST_ASIO_DEFAULT_COMPLETION_TOKEN_TYPE(executor_type)>
  BOOST_ASIO_INITFN_AUTO_RESULT_TYPE_PREFIX(WriteToken,
      void (boost::system::error_code, std::size_t))
  async_write_some(const ConstBufferSequence& buffers,
      BOOST_ASIO_MOVE_ARG(WriteToken)token
        BOOST_ASIO_DEFAULT_COMPLETION_TOKEN(executor_type))
```

**实践**

​	本实践封装了一个Session和一个MsgNode。

​	注意：本示例设计不够好，但是演示了asio异步发送的使用和注意点，在队列设计和函数递归使用上可以优化和简化。

* Session：内置一个socket，表示一个会话连接
* MsgNode：发送一次信息的结构体，包含了发送的数据

```C++
struct MsgNode
{
    MsgNode(const char* msg) : _total_len(strlen(msg)), _msg(new char[_total_len]), _cur_len(0) 
    {
        memcpy(_msg, msg, _total_len);
    }
    
    MsgNode(const char* msg, size_t len) : _total_len(len), _msg(new char[_total_len]), _cur_len(0)
    {
        memcpy(_msg, msg, _total_len);
    }
    
    ~MsgNode()
    {
        delete[] _msg;
    }
    size_t _total_len;
    size_t _cur_len;
    char* _msg;
}

class Session
{
public:
    // 回调函数
    void WriteCallBackErr(const boost::system::error_code& ec, std::size_t bytes_transferred, std::shared_ptr<MsgNode> node);
    // 封装async_write_some函数
    void WriteToSocketErr(const std::string &buf);
private:
    std::queue<std::shared_ptr<MsgNode>> _send_queue;		// 待发送的queue
    std::shared_ptr<asio::ip::tcp::socket> _socket;
    bool _send_pending;		// 是否正在发送中
};
```

#### 危险发送（多个async顺序错乱）

​	在这个情况下，如果用户调用了两次`WriteToSocketErr`，那么很可能在回调函数中隐式发生了调用导致发送顺序错乱。

​	例如，第一次发送“Hello World"，发送了Hello，未发送完，触发了回调，继续发送World。在发送World之前，用户再次调用了该函数发送”Hello World"，那么就发生了：“Hello Hello WorldWorld”的情况。

```C++
//用户发送数据
WriteToSocketErr("Hello World!");
//用户无感知下层调用情况又一次发送了数据
WriteToSocketErr("Hello World!");
```

---

​	用户调用封装好的异步发送函数`WriteToSocketErr`，函数中的回调函数保存了MsgNode指针，防止被析构。

```C++
void Session::WriteToSocketErr(const std::string& buf) {
    _send_node = make_shared<MsgNode>(buf.c_str(), buf.length());
    //异步发送数据，因为异步所以不会一下发送完
    this->_socket->async_write_some(asio::buffer(_send_node->_msg, 
        _send_node->_total_len),
        std::bind(&Session::WriteCallBackErr,
            this, std::placeholders::_1, std::placeholders::_2, _send_node));
}
```

​	回调函数检查是否全部发送，再次调用异步发送以保证发送完全。

```C++
void Session::WriteCallBackErr(const boost::system::error_code& ec, 
    std::size_t bytes_transferred, std::shared_ptr<MsgNode> msg_node) {
    if (bytes_transferred + msg_node->_cur_len 
        < msg_node->_total_len) {
        _send_node->_cur_len += bytes_transferred;
        this->_socket->async_write_some(asio::buffer(_send_node->_msg+_send_node->_cur_len,
            _send_node->_total_len-_send_node->_cur_len),
            std::bind(&Session::WriteCallBackErr,
                this, std::placeholders::_1, std::placeholders::_2, _send_node));
    }
}
```

#### 安全发送（队列保证发送顺序）

​	安全发送主要用一个队列存储数据，所有的发送都先写入到队列中。并且用一个bool检测是否有正在发送的数据，有则不进行发送，让正在发送的函数将写入队列的数据顺便发送出去。在此期间应该是要加锁以保证安全（代码中未加锁）。

---

​	用户调用封装好的异步发送函数`WriteToSocketErr`，函数中的回调函数保存了MsgNode指针，防止被析构。

```C++
void Session::WriteToSocket(const std::string& buf){
    //插入发送队列
    _send_queue.emplace(new MsgNode(buf.c_str(), buf.length()));
    //pending状态说明上一次有未发送完的数据
    if (_send_pending) {
        return;
    }
    //异步发送数据，因为异步所以不会一下发送完
    this->_socket->async_write_some(asio::buffer(buf), std::bind(&Session::WriteCallBack, this, std::placeholders::_1, std::placeholders::_2));
    _send_pending = true;
}
```

​	回调函数检查是否全部发送，再次调用异步发送以保证发送完全。同时检查发送队列中的MsgNode是否还有，有就发送下一个node。

```C++
void Session::WriteCallBack(const boost::system::error_code & ec,  std::size_t bytes_transferred){
    if (ec.value() != 0) {
        std::cout << "Error , code is " << ec.value() << " . Message is " << ec.message();
        return;
    }
    //取出队首元素即当前未发送完数据
    auto & send_data = _send_queue.front();
    send_data->_cur_len += bytes_transferred;
    //数据未发送完， 则继续发送
    if (send_data->_cur_len < send_data->_total_len) {
        this->_socket->async_write_some(asio::buffer(send_data->_msg + send_data->_cur_len, send_data->_total_len-send_data->_cur_len),
            std::bind(&Session::WriteCallBack,
            this, std::placeholders::_1, std::placeholders::_2));
        return;
    }
    //如果发送完，则pop出队首元素
    _send_queue.pop();
    //如果队列为空，则说明所有数据都发送完,将pending设置为false
    if (_send_queue.empty()) {
        _send_pending = false;
    }
    //如果队列不是空，则继续将队首元素发送
    if (!_send_queue.empty()) {
        auto& send_data = _send_queue.front();
        this->_socket->async_write_some(asio::buffer(send_data->_msg + send_data->_cur_len, send_data->_total_len - send_data->_cur_len),
            std::bind(&Session::WriteCallBack,
                this, std::placeholders::_1, std::placeholders::_2));
    }
}
```

### async_send

​	async_send相比于async_write_some，保证会将数据全部发送出去。

​	在发送的长度未达到我们要求的长度时就不会触发回调，所以**触发回调函数时要么时发送出错了要么是发送完成了**。

**实践**

​	用户调用封装好的异步发送函数`WriteAllToSocket`。

​	用队列存储发送数据，发送前检测是否有其它异步发送操作，如果有，则让正在发送者去发送队列中的数据，自己直接return。

```C++
void Session::WriteAllToSocket(const std::string& buf) {
    //插入发送队列
    _send_queue.emplace(new MsgNode(buf.c_str(), buf.length()));
    //pending状态说明上一次有未发送完的数据
    if (_send_pending) {
        return;
    }
    //异步发送数据，因为异步所以不会一下发送完
    this->_socket->async_send(asio::buffer(buf), 
        std::bind(&Session::WriteAllCallBack, this,
            std::placeholders::_1, std::placeholders::_2));
    _send_pending = true;
}
```

​	本次发送完成，在回调中检测队列中是否有新的发送数据需要发送，如果有，再次进入async_send的流程。

```C++
void Session::WriteAllCallBack(const boost::system::error_code& ec, std::size_t bytes_transferred){
    if (ec.value() != 0) {
        std::cout << "Error occured! Error code = "
            << ec.value()
            << ". Message: " << ec.message();
        return;
    }
    //如果发送完，则pop出队首元素
    _send_queue.pop();
    //如果队列为空，则说明所有数据都发送完,将pending设置为false
    if (_send_queue.empty()) {
        _send_pending = false;
    }
    //如果队列不是空，则继续将队首元素发送
    if (!_send_queue.empty()) {
        auto& send_data = _send_queue.front();
        this->_socket->async_send(asio::buffer(send_data->_msg + send_data->_cur_len, send_data->_total_len - send_data->_cur_len),
            std::bind(&Session::WriteAllCallBack,
                this, std::placeholders::_1, std::placeholders::_2));
    }
}
```

## 异步读取

​	异步读取不用考虑时序，因为读的时候已经排好序了。

​	主要的API是`async_read_some`和`async_receive`。同样不要混用。在工程上更偏向于前者，因为效率更高，不会等待过长时间。

### async_read_some

​	与`async_write_some`类似，不能保证一次性读取到需要的字节数。

```C++
class Session {
public:
    void ReadFromSocket();
    void ReadCallBack(const boost::system::error_code& ec, std::size_t bytes_transferred);
private:
    std::shared_ptr<asio::ip::tcp::socket> _socket;
    std::shared_ptr<MsgNode> _recv_node;
    bool _recv_pending;
};
```

​	封装了`async_read_some`，绑定了回调函数。

```C++
void Session::ReadFromSocket() {
    if (_recv_pending) {
        return;
    }
    //可以调用构造函数直接构造，但不可用已经构造好的智能指针赋值
    /*auto _recv_nodez = std::make_unique<MsgNode>(RECVSIZE);
    _recv_node = _recv_nodez;*/
    _recv_node = std::make_shared<MsgNode>(RECVSIZE);
    _socket->async_read_some(asio::buffer(_recv_node->_msg, _recv_node->_total_len), std::bind(&Session::ReadCallBack, this,
        std::placeholders::_1, std::placeholders::_2));
    _recv_pending = true;
}
```

​	回调函数，一次未读取完成，就接着调用`async_read_some`进行读取。

```C++
void Session::ReadCallBack(const boost::system::error_code& ec, std::size_t bytes_transferred){
    _recv_node->_cur_len += bytes_transferred;
    //没读完继续读
    if (_recv_node->_cur_len < _recv_node->_total_len) {
        _socket->async_read_some(asio::buffer(_recv_node->_msg+_recv_node->_cur_len,
            _recv_node->_total_len - _recv_node->_cur_len), std::bind(&Session::ReadCallBack, this,
            std::placeholders::_1, std::placeholders::_2));
        return;
    }
    //将数据投递到队列里交给逻辑线程处理，此处略去
    // ...
    
    //如果读完了则将标记置为false
    _recv_pending = false;
    //指针置空
    _recv_node = nullptr;    
}
```

### async_receive

​	与async_write类似，一次性读取完需要的字节数。

```C++
void Session::ReadAllFromSocket(const std::string& buf) {
    if (_recv_pending) {
        return;
    }
    //可以调用构造函数直接构造，但不可用已经构造好的智能指针赋值
    /*auto _recv_nodez = std::make_unique<MsgNode>(RECVSIZE);
    _recv_node = _recv_nodez;*/
    _recv_node = std::make_shared<MsgNode>(RECVSIZE);
    _socket->async_receive(asio::buffer(_recv_node->_msg, _recv_node->_total_len), std::bind(&Session::ReadAllCallBack, this,
        std::placeholders::_1, std::placeholders::_2));
    _recv_pending = true;
}
void Session::ReadAllCallBack(const boost::system::error_code& ec, std::size_t bytes_transferred) {
    _recv_node->_cur_len += bytes_transferred;
    //将数据投递到队列里交给逻辑线程处理，此处略去
    //如果读完了则将标记置为false
    _recv_pending = false;
    //指针置空
    _recv_node = nullptr;
}
```



## 参考资料

* [恋恋风辰官方博客 (llfc.club)](https://llfc.club/category?catid=225RaiVNI8pFDD5L4m807g7ZwmF#!aid/2O0QikntG7ktdgARRndYzNg8R55)
* [C++ asio网络编程(4)异步读写api介绍和使用_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Vm4y1B7KN/?spm_id_from=333.880.my_history.page.click&vd_source=7ea28e304f19f399517ee153057d1f10)