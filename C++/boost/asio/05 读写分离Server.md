---
create: 2024-04-16
---
# 读写分离的Server
### 线程安全的队列写数据
	使用读写分离的Server，通过队列来写数据。队列需要保证线程的安全性。
### Send
	发送时，加锁后检测队列是否还有消息，如果有，说明有信息正在发送中，直接将数据塞入队列。
	让正在发送的去处理队列中的数据。
```C++
std::lock_graud<std::mutex> lock(mtx);
_send_que.push(MsgNode{_msg, max_length});
if(_send_que.size() >= 1)
{
	return;
}

boost::asio::async_write(_socket, boost::asio::buffer(_msg, max_length),
				   std::bind(&Server::HandleWrite, this, std::placeholder::_1, std::placeholder::_2, shared_from_this()));	
```
### HandleWrite
	发送完毕后，检测队列中是否还有数据，如果有，继续异步发送。
```C++
if(err)
{
	std::cout << "handle write error:" << err.what() << '\n';
	_server.ClearSession(_uuid);
	return;
}

lock_graud<std::mutex> lock(mtx);

_send_que.pop();
if(!_send_que.empty())
{
	auto &Node = _send_que.front();
	auto _msg = Node._msg;
	auto max_length = Node.max_length;
	boost::asio::async_write(_socket, boost::asio::buffer(_msg, max_length),
				   std::bind(&Server::HandleWrite, this, std::placeholder::_1, std::placeholder::_2, shared_from_this()));
}
```

