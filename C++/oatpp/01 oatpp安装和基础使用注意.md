# oatpp安装和基础使用注意

## 下载

```shell
git clone git@github.com:oatpp/oatpp.git
cd oatpp
mkdir build
cd build
cmake ..
```

## 安装

​	在`build`下以管理员身份打开powershell/cmd，运行

```shell
cmake --build . --target INSTALL
```

## 使用

### 附加包含目录

​	在vs中附加包含目录`C:\Program Files %28x86%29\oatpp\include\oatpp-1.3.0\oatpp`

### 附加库目录

​	在vs中附加库目录`C:\Program Files %28x86%29\oatpp\lib\oatpp-1.3.0`

### 附加依赖项

​	不要忘了网络库。

```
oatpp.lib
ws2_32.lib
```

## 测试

### handler.h

```C++
// handler.h
#ifndef HANDLER_H
#define HANDLER_H

#include "oatpp/web/server/HttpRequestHandler.hpp"

#define O_UNUSED(x) (void)x;

// 自定义请求处理程序
class Handler : public oatpp::web::server::HttpRequestHandler
{
public:
    // 处理传入的请求，并返回响应
    std::shared_ptr<OutgoingResponse> handle(const std::shared_ptr<IncomingRequest>& request) override {
        O_UNUSED(request);

        return ResponseFactory::createResponse(Status::CODE_200, "Hello, World!");
    }
};

#endif // HANDLER_H

```

### main.cpp

```C++
// main.cpp
#include "oatpp/web/server/HttpConnectionHandler.hpp"
#include "oatpp/network/tcp/server/ConnectionProvider.hpp"
#include "oatpp/network/Server.hpp"
#include "handler.h"

void run()
{
    // 为 HTTP 请求创建路由器
    auto router = oatpp::web::server::HttpRouter::createShared();

    // 路由 GET - "/hello" 请求到处理程序
    router->route("GET", "/hello", std::make_shared<Handler>());

    // 创建 HTTP 连接处理程序
    auto connectionHandler = oatpp::web::server::HttpConnectionHandler::createShared(router);

    // 创建 TCP 连接提供者
    auto connectionProvider = oatpp::network::tcp::server::ConnectionProvider::createShared({ "0.0.0.0", 8080, oatpp::network::Address::IP_4 });

    // 创建服务器，它接受提供的 TCP 连接并将其传递给 HTTP 连接处理程序
    oatpp::network::Server server(connectionProvider, connectionHandler);

    // 打印服务器端口
    OATPP_LOGI("MyApp", "Server running on port %s", connectionProvider->getProperty("port").getData());

    // 运行服务器
    server.run();
}

int main()
{
    // 初始化 oatpp 环境
    oatpp::base::Environment::init();

    // 运行应用
    run();

    // 销毁 oatpp 环境
    oatpp::base::Environment::destroy();

    return 0;
}

```

