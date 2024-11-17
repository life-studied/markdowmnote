---
create: 2023-10-04
---
# 05 Step-By-Step-Guide解读（上）

​	Step-By-Step-Guide主要演示了两个层面的使用，从简单到复杂：

* 简单使用：使用裸`HttpRequestHandler`为每个`Endpoint`创建
* 完整使用：使用`Api Controller`管理`Endpoint`。

## 1. 简单使用

### 1.1 基础

​	下面演示了如何创建一个基本的http服务，不带任何处理接口。

```cpp
#include "oatpp/web/server/HttpConnectionHandler.hpp"

#include "oatpp/network/Server.hpp"
#include "oatpp/network/tcp/server/ConnectionProvider.hpp"

void run() {

  /* 创建用于HTTP请求路由的路由器 */
  auto router = oatpp::web::server::HttpRouter::createShared();

  /* 创建带有路由器的HTTP连接处理程序 */
  auto connectionHandler = oatpp::web::server::HttpConnectionHandler::createShared(router);

  /* 创建TCP连接提供者 */
  auto connectionProvider = oatpp::network::tcp::server::ConnectionProvider::createShared({"localhost", 8000, oatpp::network::Address::IP_4});

  /* 创建服务器，它接受提供的TCP连接并将它们传递给HTTP连接处理程序 */
  oatpp::network::Server server(connectionProvider, connectionHandler);

  /* 打印有关服务器端口的信息 */
  OATPP_LOGI("MyApp", "服务器运行在端口 %s", connectionProvider->getProperty("port").getData());

  /* 运行服务器 */
  server.run();
}

int main() {

  /* 初始化oatpp环境 */
  oatpp::base::Environment::init();

  /* 运行应用程序 */
  run();

  /* 销毁oatpp环境 */
  oatpp::base::Environment::destroy();

  return 0;

}

```

- 代码中所使用的类：

  - [HttpRouter](https://oatpp.io/api/latest/oatpp/web/server/HttpRouter/) - HTTP请求的路由器。它将URL映射到端点处理程序。由于我们的应用程序目前尚未声明任何端点，因此服务器将始终返回`404未找到`。

  - [HttpConnectionHandler](https://oatpp.io/api/latest/oatpp/web/server/HttpConnectionHandler/) - 这是一个简单的HTTP连接处理程序。它以多线程方式处理传入连接，为每个连接创建一个线程。

  - [ConnectionProvider](https://oatpp.io/api/latest/oatpp/network/tcp/server/ConnectionProvider/) - `TCP`连接的提供者。它绑定到指定的端口。

  - [Server](https://oatpp.io/api/latest/oatpp/network/Server/) - 服务器运行一个循环，从`ConnectionProvider`接受连接并将它们传递给`ConnectionHandler`。

### 1.2.0 响应工厂（ResponseFactory.hpp）

​	从`class ResponseFactory`可以看出，可以有三种响应格式：

* 状态码
* 状态码+文本
* 状态码+DTO+JSON序列化器

> 它们的本质都是将数据转换成文本响应。

```C++
class ResponseFactory {
public:
  
  /**
   * Create &id:oatpp::web::protocol::http::outgoing::Response; without a &id:oatpp::web::protocol::http::outgoing::Body;.
   * @param status - &id:oatpp::web::protocol::http::Status;.
   * @return - `std::shared_ptr` to &id:oatpp::web::protocol::http::outgoing::Response;.
   */
  static std::shared_ptr<Response> createResponse(const Status &status);

  /**
   * Create &id:oatpp::web::protocol::http::outgoing::Response; with &id:oatpp::web::protocol::http::outgoing::BufferBody;.
   * @param status - &id:oatpp::web::protocol::http::Status;.
   * @param text - &id:oatpp::String;.
   * @return - `std::shared_ptr` to &id:oatpp::web::protocol::http::outgoing::Response;.
   */
  static std::shared_ptr<Response> createResponse(const Status& status, const oatpp::String& text);

  /**
   * Create &id:oatpp::web::protocol::http::outgoing::Response; with &id:oatpp::web::protocol::http::outgoing::DtoBody;.
   * @param status - &id:oatpp::web::protocol::http::Status;.
   * @param dto - see [Data Transfer Object (DTO)](https://oatpp.io/docs/components/dto/).
   * @param objectMapper - &id:oatpp::data::mapping::ObjectMapper;.
   * @return - `std::shared_ptr` to &id:oatpp::web::protocol::http::outgoing::Response;.
   */
  static std::shared_ptr<Response> createResponse(const Status& status,
                                                  const oatpp::Void& dto,
                                                  const std::shared_ptr<data::mapping::ObjectMapper>& objectMapper);
  
};
```

### 1.2 返回文本格式响应

​	新增`class Handler`和第27行代码。

```C++
#include "oatpp/web/server/HttpConnectionHandler.hpp"

#include "oatpp/network/Server.hpp"
#include "oatpp/network/tcp/server/ConnectionProvider.hpp"

/** 
 * Custom Request Handler
 */
class Handler : public oatpp::web::server::HttpRequestHandler {
public:

  /**
   * Handle incoming request and return outgoing response.
   */
  std::shared_ptr<OutgoingResponse> handle(const std::shared_ptr<IncomingRequest>& request) override {
    return ResponseFactory::createResponse(Status::CODE_200, "Hello World!");
  }

};

void run() {

  /* Create Router for HTTP requests routing */
  auto router = oatpp::web::server::HttpRouter::createShared();
  
  /* Route GET - "/hello" requests to Handler */
  router->route("GET", "/hello", std::make_shared<Handler>());

  /* Create HTTP connection handler with router */
  auto connectionHandler = oatpp::web::server::HttpConnectionHandler::createShared(router);

  /* Create TCP connection provider */
  auto connectionProvider = oatpp::network::tcp::server::ConnectionProvider::createShared({"localhost", 8000, oatpp::network::Address::IP_4});

  /* Create server which takes provided TCP connections and passes them to HTTP connection handler */
  oatpp::network::Server server(connectionProvider, connectionHandler);

  /* Priny info about server port */
  OATPP_LOGI("MyApp", "Server running on port %s", connectionProvider->getProperty("port").getData());

  /* Run server */
  server.run();
}

int main() {

  /* Init oatpp Environment */
  oatpp::base::Environment::init();

  /* Run App */
  run();

  /* Destroy oatpp Environment */
  oatpp::base::Environment::destroy();

  return 0;

}

```

### 1.3 返回JSON格式响应

​	新增了DTO类。

​	修改了handle返回的格式类型。

​	修改了router->route的参数。

```C++
#include "oatpp/parser/json/mapping/ObjectMapper.hpp"

#include "oatpp/web/server/HttpConnectionHandler.hpp"

#include "oatpp/network/Server.hpp"
#include "oatpp/network/tcp/server/ConnectionProvider.hpp"

#include "oatpp/core/macro/codegen.hpp"

/* Begin DTO code-generation */
#include OATPP_CODEGEN_BEGIN(DTO)

/**
 * Message Data-Transfer-Object
 */
class MessageDto : public oatpp::DTO {

  DTO_INIT(MessageDto, DTO /* Extends */)

  DTO_FIELD(Int32, statusCode);   // Status code field
  DTO_FIELD(String, message);     // Message field

};

/* End DTO code-generation */
#include OATPP_CODEGEN_END(DTO)

/**
 * Custom Request Handler
 */
class Handler : public oatpp::web::server::HttpRequestHandler {
private:
  std::shared_ptr<oatpp::data::mapping::ObjectMapper> m_objectMapper;
public:

  /**
   * Constructor with object mapper.
   * @param objectMapper - object mapper used to serialize objects.
   */
  Handler(const std::shared_ptr<oatpp::data::mapping::ObjectMapper>& objectMapper)
    : m_objectMapper(objectMapper)
  {}

  /**
   * Handle incoming request and return outgoing response.
   */
  std::shared_ptr<OutgoingResponse> handle(const std::shared_ptr<IncomingRequest>& request) override {
    auto message = MessageDto::createShared();
    message->statusCode = 1024;
    message->message = "Hello DTO!";
    return ResponseFactory::createResponse(Status::CODE_200, message, m_objectMapper);
  }

};

void run() {

  /* Create json object mapper */
  auto objectMapper = oatpp::parser::json::mapping::ObjectMapper::createShared();

  /* Create Router for HTTP requests routing */
  auto router = oatpp::web::server::HttpRouter::createShared();

  /* Route GET - "/hello" requests to Handler */
  router->route("GET", "/hello", std::make_shared<Handler>(objectMapper /* json object mapper */ ));

  /* Create HTTP connection handler with router */
  auto connectionHandler = oatpp::web::server::HttpConnectionHandler::createShared(router);

  /* Create TCP connection provider */
  auto connectionProvider = oatpp::network::tcp::server::ConnectionProvider::createShared({"localhost", 8000, oatpp::network::Address::IP_4});

  /* Create server which takes provided TCP connections and passes them to HTTP connection handler */
  oatpp::network::Server server(connectionProvider, connectionHandler);

  /* Priny info about server port */
  OATPP_LOGI("MyApp", "Server running on port %s", connectionProvider->getProperty("port").getData());

  /* Run server */
  server.run();
}

int main() {

  /* Init oatpp Environment */
  oatpp::base::Environment::init();

  /* Run App */
  run();

  /* Destroy oatpp Environment */
  oatpp::base::Environment::destroy();

  return 0;

}
```

