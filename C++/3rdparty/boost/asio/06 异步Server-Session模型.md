---
create: '2025-01-19'
modified: '2025-01-19'
---

# 异步Server-Session模型

## Session-Server模型

* Session：将socket抽象成一个Session
* Server：将io_context和acceptor抽象成一个Server
* 数据头：在数据头部添加short大小的长度
* 单线程：单线程使用，无需加锁，全程只有ioc.run()在运行
* 循环依赖：使用时请将server和session的h和cpp分离编写，否则会有循环依赖的问题（这边为了方便写在了一起）

```C++
int main() {
    try
    {
        Server server(12345);
        server.run();
    }
    catch(const std::exception& e)
    {
        std::cerr << e.what() << '\n';
    }
}
```

### Session

* 生命周期：通过shared_from_this()在函数中自持
* 读：先读头部，在回调中再读body，再在回调中真正地处理整个数据包
* 写：使用发送队列去写，每次保证只有一个async_send在io_context中，负责代发所有的数据包（带头部）

```C++
class Server; // 前向声明

class Session : public std::enable_shared_from_this<Session> {
public:
    Session(io_context& ioc) : socket(ioc) {}
    tcp::socket& get_socket() { return socket; }
    void Start() {
        // TODO: 此处编写业务逻辑
        // 例如：开始读取数据的头部，然后读取数据的body
        // memset(data_, 0, MAX_LENGTH); header_ = 0;
        // socket.async_receive(buffer((char*)header_, HEADER_LENGTH), std::bind(&Session::read_header_call_back, shared_from_this(), std::placeholders::_1, std::placeholders::_2));
    }

    void send_with_header() {
        auto& data = send_queue.front();
        // 添加数据包头部
        short data_len = data.size();
        data_len = asio::detail::socket_ops::host_to_network_short(data_len);
        data.insert(0, (char*)&data_len, sizeof(short));
        
        // 发送数据包
        socket.async_send(buffer(data), std::bind(&Session::write_call_back, shared_from_this(), std::placeholders::_1, std::placeholders::_2));
        send_queue.pop();
    }
    
    // 发送函数
    void write(const std::string& buf) {
        send_queue.push(buf);
        if(is_sending) {
            return;	// 让io_context中的async_send代发
        }
        
        // io_context中不存在async_send，新建一个去发送数据
        is_sending = true;
        send_with_header();
    }
    
    // 发送完成的回调函数
    void write_call_back(asio::error_code ec, std::size_t bytes_transferred) {
        if(ec) {
            return;
        }
        (void)bytes_transferred;
        
        if(send_queue.empty()) {
            is_sending = false;
            return;
        }
        
        // 继续新建一个async_send，代发send_queue中的数据
        send_with_header();
    }

    // 接收header完成的回调函数
    void read_header_call_back(asio::error_code ec, std::size_t bytes_transferred) {
        if(ec) {
            return;
        }

         // 转换为本地字节序
        int data_len = asio::detail::socket_ops::network_to_host_short(header_);

        // 读取数据包body
        socket.async_receive(buffer(data_, data_len), std::bind(&Session::read_body_call_back, shared_from_this(), std::placeholders::_1, std::placeholders::_2));
    }

    // 接收body完成的回调函数
    void read_body_call_back(asio::error_code ec, std::size_t bytes_transferred) {
        if(ec) {
            return;
        }
        (void)bytes_transferred;

        // TODO: 此处添加业务逻辑，处理接收到的数据，然后发送响应数据
        // write("response");


        // 继续接收下一个数据包（取决于业务需要）
        // memset(data_, 0, MAX_LENGTH); header_ = 0;
        // socket.async_receive(buffer((char*)header_, HEADER_LENGTH), std::bind(&Session::read_header_call_back, shared_from_this(), std::placeholders::_1, std::placeholders::_2));

        // 或者停止接收数据，关闭连接
        socket.close();
        if(server.expired()) {
            return;
        }
        server.lock()->remove_session(shared_from_this());
    }
private:
    std::weak_ptr<Server> server;	// 保存server的view

    ip::tcp::socket socket;
    std::queue<std::string> send_queue;
    bool is_sending = false;	// 标识io_context中是否有async_send

    static constexpr int MAX_LENGTH = 1024;
    static constexpr int HEADER_LENGTH = sizeof(short);
    short header_ = 0;	    // 数据包头部，表示数据包长度
    char data_[MAX_LENGTH];	// 数据包body，接收缓冲区
};
```

### Server

```C++
class Server
{
public:
	Server(short port) : acceptor_(ioc_, tcp::endpoint(tcp::v4(), port)) {
        start_accept();
    }

    void run() {
        ioc_.run();
    }

    void remove_session(std::shared_ptr<Session> session) {
        sessions_.erase(session);
        cur_conn_num--;
    }
private:
	void start_accept() {
        auto new_session = std::make_shared<Session>(ioc_);
        acceptor_.async_accept(new_session->get_socket(),
		    std::bind(&Server::accept_call_back, this, new_session, std::placeholders::_1));
    }

	void accept_call_back(std::shared_ptr<Session> new_session,const asio::error_code& ec) {
        if (ec) {
            return;
        }

        // 先创建任务：异步监听下一个连接
	    start_accept();

        // 再检查并连接数++
        if(cur_conn_num >= max_conn_num) {
            return;
        }
        cur_conn_num++;

        // 连接数正常的情况下：Session才开始启动，否则丢弃
        sessions_.insert(new_session);
        new_session->Start();
    }
	asio::io_context ioc_;
	asio::ip::tcp::acceptor acceptor_;

    int cur_conn_num = 0;   // 当前连接数
    int max_conn_num = 100; // 最大连接数

    std::unordered_set<std::shared_ptr<Session>> sessions_;	// 存储所有连接的session
};
```