---
create: '2025-05-31'
modified: '2025-05-31'
---

# 多目标输出Handler

`Handler`用于将Logger的日志消息发送到指定的目的地，例如控制台、文件、网络等，一个Logger可以设置多个输出。

* `StreamHandler`：将日志输出到控制台。

- `FileHandler`：将日志输出到文件。
- `SocketHandler`：将日志发送到网络服务器。
- `MemoryHandler`：将日志存储在内存中。

通过`addHandler()`为Logger添加一个输出：

```python
logger = logging.getLogger('multi-logger')
logger.setLevel(logging.DEBUG)

# 创建 StreamHandler（输出到控制台）
console_handler = logging.StreamHandler()	# 默认sys.stderr
console_handler.setLevel(logging.INFO)  # 控制台只输出 INFO 及以上级别的日志
console_formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
console_handler.setFormatter(console_formatter)
logger.addHandler(console_handler)

# 创建 FileHandler（输出到文件）
file_handler = logging.FileHandler('example.log', mode='w', encoding='utf-8')
file_handler.setLevel(logging.ERROR)  # 文件只记录 ERROR 级别及以上的日志
file_formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(file_formatter)
logger.addHandler(file_handler)

# 创建 StringHandler（输出到字符串）
string_io = StringIO()
string_handler = logging.StreamHandler(string_io)
string_handler.setLevel(logging.DEBUG)  # 字符串记录所有日志
string_formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
string_handler.setFormatter(string_formatter)
logger.addHandler(string_handler)
```