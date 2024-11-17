---
create: 2024-08-29
---
# Server

* 继承`ServiceName::Service`创建`serviceImplementation`
* 覆盖RPC方法，实现逻辑
* 创建服务器监听



```C++
#include <string>

#include <grpcpp/grpcpp.h>
#include "mathtest.grpc.pb.h"

using grpc::Server;
using grpc::ServerBuilder;
using grpc::ServerContext;
using grpc::Status;

using mathtest::MathTest;
using mathtest::MathRequest;
using mathtest::MathReply;

class MathServiceImplementation final : public MathTest::Service {
    Status sendRequest(
            ServerContext* context,
            const MathRequest* request,
            MathReply* reply
    ) override {
        int a = request->a();
        int b = request->b();

        reply->set_result(a * b);

        return Status::OK;
    }
};

void Run() {
    std::string address("0.0.0.0:5000");
    MathServiceImplementation service;

    ServerBuilder builder;

    builder.AddListeningPort(address, grpc::InsecureServerCredentials());
    builder.RegisterService(&service);

    std::unique_ptr<Server> server(builder.BuildAndStart());
    std::cout << "Server listening on port: " << address << std::endl;

    server->Wait();
}

int main(int argc, char** argv) {
    Run();

    return 0;
}
```