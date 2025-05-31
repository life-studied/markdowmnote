---
create: '2025-05-30'
modified: '2025-05-30'
---

# 为task设置环境变量

如下所示，通过`env`设置环境变量：

```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "generate pybind11 stubgen",
            "type": "shell",
            "windows": {
                "command": "pybind11-stubgen -o ./typings --ignore-all-errors example",
                "options": {
                    "env": {
                        "PYTHONPATH": "%PYTHONPATH%;${workspaceFolder}"
                    }
                }
            },
            "linux": {
                "command": "pybind11-stubgen -o ./typings --ignore-all-errors example",
                "options": {
                    "env": {
                        "PYTHONPATH": "${env.PYTHONPATH}:${workspaceFolder}"
                    }
                }
            },
        }
    ]
}
```