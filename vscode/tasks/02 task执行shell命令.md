---
create: '2025-05-30'
modified: '2025-05-30'
---

# task执行shell命令

非常常见的情况就是task执行一个shell命令：

有多种方式编写：

* command里一行命令
* command里一组命令数组
* command是可执行文件名，后续用arg跟参数

```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "echo",
            "type": "shell",
            "command": "echo Hello"
        },
        {
            "label": "download dependency",
            "type": "shell",
            "command": [
                "curl",
                "https://test/dependency.zip",
                "-o",
                "${workspaceFolder}/lib/dependency.zip"
            ]
        },
        {
            "label": "Client Build",
            "command": "npm",
            "args": ["run", "build"]
        }
    ]
}
```