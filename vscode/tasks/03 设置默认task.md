---
create: '2025-05-30'
modified: '2025-05-30'
---

# 设置默认task

可以为每个组设置默认的task，例如，为`build`设置默认task。

然后用`ctrl+shift+B`执行`build`组的默认task：

```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "C++ Build",
            "type": "shell",
            "command": "g++",
            "args": [
                "-o", "${workspaceFolder}/bin/app",
                "${workspaceFolder}/src/*.cpp"
            ],
            "group": {
                "kind": "build",
                "isDefault": true	// 默认task
            },
            "problemMatcher": ["$gcc"] // 用于解析编译器输出中的错误和警告
        }
    ]
}
```