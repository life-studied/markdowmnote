---
create: 2024-08-29
---
# proto

```protobuf
syntax = "proto3";

option java_package = "ex.grpc";

package mathtest;

// Defines the service
service MathTest {
    // Function invoked to send the request
    rpc sendRequest (MathRequest) returns (MathReply) {}
}

// The request message containing requested numbers
message MathRequest {
    int32 a = 1;
    int32 b = 2;
}

// The response message containing response
message MathReply {
    int32 result = 1;
}
```