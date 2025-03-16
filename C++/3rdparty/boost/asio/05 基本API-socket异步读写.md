---
create: '2025-01-19'
modified: '2025-01-19'
---

# 基本API-socket异步读写

回调函数类型：`void (asio::error_code, std::size_t)`

## 异步

### 异步写

```C++
async_write_some(buffer, handler);

async_send(buffer, handler);
```

* `async_write_some`：函数不保证回调函数触发时，发送的长度为总长度，我们每次都要在回调函数判断发送数据是否完成。

* `async_send`：asio 提供了一个更简单的发送函数 async_send，这个函数在发送的长度未达到我们要求的长度时就不会触发回调，所以触发回调函数时要么时发送出错了要么是发送完成了，无需判断发送长度的问题。

其内部的实现原理就像不断的调用 async_write_some 直到完成发送，所以 async_send 不能和 async_write_some 混合使用。

### 异步读

与上面一致。

```C++
async_read_some(buffer, handler);

async_receive(buffer, handler);
```