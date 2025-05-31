---
create: '2025-05-31'
modified: '2025-05-31'
---

# 创建日志器Logger

## root

logging模块中，`root`作为根日志器，是所有日志器最终的根节点。

## basicConfig()

`basicConfig()`是用来设置`root`的属性的。在小脚本或应用程序里直接用`root`+ `basicConfig()`就能完成日志的任务。而大型工程里，一般都会用`getLogger()`创建自己的日志器。

### 属性

* Handler：配置一个或多个Handler

* level：配置日志级别

    * logging默认提供5个日志等级：DEBUG、INFO、WARNING、ERROR、CRITICAL

* format：配置日志格式

    * 默认值：`'%(levelname)s:%(name)s:%(message)s'`

    * 属性：`%(xxx)s`会被实际的属性替代，常用属性如下：

        * 基础：

            * | `%(levelname)s` | 日志级别的名称（例如，DEBUG、INFO、WARNING、ERROR、CRITICAL）。 |
                | --------------- | ------------------------------------------------------------ |
                | `%(name)s`      | 日志记录器的名称（logger 的名字）。                          |
                | `%(message)s`   | 日志消息本身。                                               |

        * 溯源：

            * | `%(pathname)s` | 调用日志记录函数的模块的完整路径名。 |
                | -------------- | ------------------------------------ |
                | `%(filename)s` | 调用日志记录函数的模块的文件名。     |
                | `%(module)s`   | 调用日志记录函数的模块名。           |
                | `%(funcName)s` | 调用日志记录函数的函数名。           |
                | `%(lineno)d`   | 调用日志记录函数的代码所在的行号。   |

        * 时间：

            * | `%(created)f`         | 当前时间的时间戳（从 1970 年 1 月 1 日午夜开始的秒数）。  |
                | --------------------- | --------------------------------------------------------- |
                | `%(asctime)s`         | 当前时间的可读形式（默认格式为 `%Y-%m-%d %H:%M:%S,%f`）。 |
                | `%(msecs)d`           | 当前时间的毫秒部分。                                      |
                | `%(relativeCreated)d` | 自日志记录器被创建以来的毫秒数。                          |

        * 进程与线程号：

            * | `%(thread)d`     | 线程的 ID。  |
                | ---------------- | ------------ |
                | `%(threadName)s` | 线程的名称。 |
                | `%(process)d`    | 进程的 ID。  |

### 输出到文件

常见需求是将日志输出到文件中：

```python
logging.basicConfig(
    level=logging.DEBUG,  # 设置日志级别
    format='%(asctime)s - %(name)s - %(levelname)s : %(message)s',  # 设置日志格式
    datefmt='%Y-%m-%d %H:%M:%S',  # 设置日期时间格式
    filename='app.log',  # 将日志输出到文件
    filemode='w',  # 以写模式打开文件
    encoding='utf-8',  # 指定文件编码
    errors='ignore',  # 指定错误处理方式
    style='%'  # 指定格式化风格
)
```

### 输出到流

也可以将日志输出到stream流中，不过这与`filename`参数是冲突的。

```python
logging.basicConfig(
    level=logging.DEBUG,  # 设置日志级别为 DEBUG
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',  # 设置日志格式
    datefmt='%Y-%m-%d %H:%M:%S',  # 设置日期时间格式
    stream=sys.stdout  # 将日志输出到标准输出
)
```

## 自定义：getLogger()

`logging`模块创建日志器的核心函数是`getLogger()`。它既是日志器的创建者，也是日志器的查找者。

* 在`logging`中，所有的日志器都以一个`str`作为唯一标识。通过传入`name`，得到日志器。
    * `root`：无参得到logger中的根日志器，root
    * 创建日志器`name`：
        * 传入str，创建日志器，父日志器为root
        * 如果已存在则返回`name`对应的日志器
    * 创建子日志器`a.b`：传入以.分割的str，得到子日志器`b`，父日志器为`a`

```python
root_logger = getLogger()	# 默认无参，得到root

core_logger = getLogger('core')	# 自定义的Logger core，parent=root
subcore1 = getLogger('core.sub1')	# sub1，parent=core
subcore2 = getLogger('core.sub2')
subcore3 = getLogger('core.sub3')
```