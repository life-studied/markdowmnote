---
create: '2025-07-16'
modified: '2025-07-16'
---

# 补充：禁止对同一个socket进行并发读和并发写

在使用asio的io_context时，如果设计将同一个io_context给多线程并发使用，很容易出现对同一个socket的read操作并发，或者write操作并发。

这些并发，在asio设计中明确不允许的。

## 1. 操作系统层面Socket的线程安全性 (POSIX/Windows)

- POSIX (Linux/macOS/etc.): 根据POSIX标准，对同一个socket描述符进行并发的`read`/`write`/`recv`/`send`等操作的结果是**未定义的**。
- Windows: 官方文档明确说明，使用IOCP时，对同一个socket发起多个并发的重叠读或写操作的结果也是**未定义的**。
- 安全的方式是确保同一时间只有一个读操作和一个写操作在进行（且它们可以并行）。

## 2. ASIO的规则与设计

ASIO作为高层库，其设计严格遵守了底层Socket API的约束。ASIO的核心规则是：

对于同一个`socket`对象（或更精确地说，对于同一个底层socket描述符）：

- **不能同时有多个未完成的异步读操作 (`async_read`, `async_read_some`)**。
- **不能同时有多个未完成的异步写操作 (`async_write`, `async_write_some`)**。
- **读操作和写操作可以同时存在且并行进行**（这是安全的，也是常见模式）。
- **其他操作（如`async_accept`, `async_connect`, `close`）也需要与当前正在进行的读写操作协调**，通常也需要通过`strand`或确保在同一个顺序执行上下文中调用。