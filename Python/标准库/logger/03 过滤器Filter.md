---
create: '2025-05-31'
modified: '2025-05-31'
---

# 过滤器Filter

## LogRecord类

实际上所有的日志信息一开始都以LogRecord类的形式存储，当需要输出时，将`Record`传给`Formatter`进行字符串格式化，然后再交给`Handler`。

### 属性

因此，LogRecord中存储了日志的各种属性：

| 属性名称          | 描述                                                         |
| :---------------- | :----------------------------------------------------------- |
| `name`            | 日志记录器（Logger）的名称。                                 |
| `levelno`         | 日志级别的数值（例如，`DEBUG` 是 10，`INFO` 是 20）。        |
| `levelname`       | 日志级别的名称（例如，`DEBUG`、`INFO`、`WARNING`、`ERROR`、`CRITICAL`）。 |
| `pathname`        | 调用日志记录函数的模块的完整路径名。                         |
| `filename`        | 调用日志记录函数的模块的文件名。                             |
| `module`          | 调用日志记录函数的模块名。                                   |
| `lineno`          | 调用日志记录函数的代码所在的行号。                           |
| `funcName`        | 调用日志记录函数的函数名。                                   |
| `created`         | 当前时间的时间戳（从 1970 年 1 月 1 日午夜开始的秒数）。     |
| `asctime`         | 当前时间的可读形式（默认格式为 `%Y-%m-%d %H:%M:%S,%f`）。    |
| `msecs`           | 当前时间的毫秒部分。                                         |
| `relativeCreated` | 自日志记录器被创建以来的毫秒数。                             |
| `thread`          | 线程的 ID。                                                  |
| `threadName`      | 线程的名称。                                                 |
| `process`         | 进程的 ID。                                                  |
| `processName`     | 进程的名称（Python 3.2+）。                                  |
| `message`         | 日志消息本身（经过格式化后的最终消息）。                     |
| `args`            | 传递给日志方法的参数（如果日志消息是通过字符串格式化生成的）。 |
| `exc_info`        | 如果日志记录时发生了异常，包含异常信息的元组（`type`、`value`、`traceback`）。 |
| `exc_text`        | 异常信息的文本表示（如果 `exc_info` 存在）。                 |
| `stack_info`      | 堆栈信息（如果在日志记录时启用了堆栈跟踪）。                 |

### 添加属性

LogRecord支持在输出日志时，使用`extra`字典添加属性，并在格式化时获取内容：

```python
# 创建logger
logger = logging.getLogger('example_logger')

# 创建handler
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.DEBUG)
logger.addHandler(console_handler)

# 设置formatter，获取extra传入的参数并格式化
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s - %(user)s - %(ip)s')
console_handler.setFormatter(formatter)

# 使用 extra 参数传递额外信息
logger.info("这是一条日志消息", extra={"user": "Alice", "ip": "192.168.1.1"})
```

## 过滤日志Filter

过滤日志作为一种很实用的手段，常用于区分各种模块的日志输出，在日志混合在一起时。

这是一种后处理方式，便于在一个成熟的代码中，为了不修改代码中的日志相关内容，同时又需要筛选日志的做法。

核心做法是通过重载`filter`函数，参数是`record`，如果返回`True`则过滤（允许通过）。

### 关键字过滤器 KeywordFilter

通过关键字过滤日志是很常见的需求：

```python
import logging

# 创建 Logger
logger = logging.getLogger('simple_filter_logger')
logger.setLevel(logging.DEBUG)

# 创建 Handler
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.DEBUG)

# 创建简单的过滤器
class KeywordFilter(logging.Filter):
    def __init__(self, keyword):
        super().__init__()
        self.keyword = keyword

    def filter(self, record):
        # 检查日志消息中是否包含关键字
        return self.keyword in record.getMessage()

# 添加过滤器到 Handler
keyword_filter = KeywordFilter(keyword="重要")
console_handler.addFilter(keyword_filter)

# 设置日志格式
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
console_handler.setFormatter(formatter)

# 将 Handler 添加到 Logger
logger.addHandler(console_handler)

# 测试日志
logger.info("这是一条普通日志")
logger.info("这是一条包含关键字'重要'的日志")
logger.warning("这是一条警告日志")
```

### 模块日志过滤器

用模块前缀来过滤日志，也是一种常见的需求：

```python
import logging

# 创建 Logger
logger = logging.getLogger('module_filter_logger')
logger.setLevel(logging.DEBUG)

# 创建 Handler
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.DEBUG)

# 创建基于模块名的过滤器
class ModuleFilter(logging.Filter):
    def __init__(self, module_name):
        super().__init__()
        self.module_name = module_name

    def filter(self, record):
        # 检查日志是否来自指定模块
        return record.module == self.module_name

# 添加过滤器到 Handler
module_filter = ModuleFilter(module_name="example")
console_handler.addFilter(module_filter)

# 设置日志格式
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(module)s - %(message)s')
console_handler.setFormatter(formatter)

# 将 Handler 添加到 Logger
logger.addHandler(console_handler)

# 测试日志
logger.info("这是一条来自 main 模块的日志")
logger.info("这是一条来自 example 模块的日志", extra={"module": "example"})
```

### 日志级别过滤器

根据日志级别来过滤也非常常用：

```python
import logging

# 创建 Logger
logger = logging.getLogger('level_filter_logger')
logger.setLevel(logging.DEBUG)

# 创建 Handler
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.DEBUG)

# 创建基于日志级别的过滤器
class LevelRangeFilter(logging.Filter):
    def __init__(self, min_level, max_level):
        super().__init__()
        self.min_level = min_level
        self.max_level = max_level

    def filter(self, record):
        # 检查日志级别是否在指定范围内
        return self.min_level <= record.levelno <= self.max_level

# 添加过滤器到 Handler
level_filter = LevelRangeFilter(min_level=logging.INFO, max_level=logging.WARNING)
console_handler.addFilter(level_filter)

# 设置日志格式
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
console_handler.setFormatter(formatter)

# 将 Handler 添加到 Logger
logger.addHandler(console_handler)

# 测试日志
logger.debug("这是一条 DEBUG 级别的日志")
logger.info("这是一条 INFO 级别的日志")
logger.warning("这是一条 WARNING 级别的日志")
logger.error("这是一条 ERROR 级别的日志")
```