---
create: '2025-06-30'
modified: '2025-06-30'
---

# wsl中vscode不能修改root权限文件

经过查阅相关链接，测试后没有一个通用且好用的：

[Visual Studio Code - WSL 中的 VSCode：如何 sudo 根文件以便我可以编辑它 - Stack Overflow](https://stackoverflow.com/questions/58980356/vscode-in-wsl-how-to-sudo-a-root-file-so-i-can-edit-it)

要么测试下来不能使用，要么不通用，还要根据具体情况去修改对应的命令，比较麻烦。

因此建议使用wsl自带的nano去做这种修改：

```shell
sudo nano xxx.conf
```