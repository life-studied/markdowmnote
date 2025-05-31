---
create: '2025-05-30'
modified: '2025-05-30'
---

# 设置多task依赖执行

下面定义了一个C++的build任务，依赖于两个第三方库。

* 两个第三方库需要进行下载，解压，编译安装。
* 完成后，才会执行C++主代码的build
* 两个第三方库并行安装

```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "Download Dependency 1",
            "type": "shell",
            "command": "curl",
            "args": [
                "-o", "${workspaceFolder}/dependencies/dependency1.zip",
                "https://example.com/dependency1.zip"
            ]
        },
        {
            "label": "Download Dependency 2",
            "type": "shell",
            "command": "curl",
            "args": [
                "-o", "${workspaceFolder}/dependencies/dependency2.zip",
                "https://example.com/dependency2.zip"
            ]
        },
        {
            "label": "Unzip Dependency 1",
            "type": "shell",
            "command": "unzip",
            "args": [
                "${workspaceFolder}/dependencies/dependency1.zip",
                "-d", "${workspaceFolder}/dependencies/dependency1"
            ],
            "dependsOn": ["Download Dependency 1"]
        },
        {
            "label": "Unzip Dependency 2",
            "type": "shell",
            "command": "unzip",
            "args": [
                "${workspaceFolder}/dependencies/dependency2.zip",
                "-d", "${workspaceFolder}/dependencies/dependency2"
            ],
            "dependsOn": ["Download Dependency 2"]
        },
        {
            "label": "Build Dependency 1",
            "type": "shell",
            "command": "cmake",
            "args": [
                "-B", "${workspaceFolder}/dependencies/dependency1/build",
                "-S", "${workspaceFolder}/dependencies/dependency1"
            ],
            "dependsOn": ["Unzip Dependency 1"]
        },
        {
            "label": "Install Dependency 1",
            "type": "shell",
            "command": "cmake",
            "args": [
                "--install", "${workspaceFolder}/dependencies/dependency1/build"
            ],
            "dependsOn": ["Build Dependency 1"]
        },
        {
            "label": "Build Dependency 2",
            "type": "shell",
            "command": "cmake",
            "args": [
                "-B", "${workspaceFolder}/dependencies/dependency2/build",
                "-S", "${workspaceFolder}/dependencies/dependency2"
            ],
            "dependsOn": ["Unzip Dependency 2"]
        },
        {
            "label": "Install Dependency 2",
            "type": "shell",
            "command": "cmake",
            "args": [
                "--install", "${workspaceFolder}/dependencies/dependency2/build"
            ],
            "dependsOn": ["Build Dependency 2"]
        },
        {
            "label": "Build C++ Project",
            "type": "shell",
            "command": "cmake",
            "args": [
                "-B", "${workspaceFolder}/build",
                "-S", "${workspaceFolder}/src"
            ],
            "dependsOn": [
                "Install Dependency 1",
                "Install Dependency 2"
            ],
            "dependsOrder": "parallel"
        }
    ]
}
```