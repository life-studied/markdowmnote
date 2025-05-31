---
create: '2025-05-30'
modified: '2025-05-30'
---

# vscode使用ssh免密连接服务器

## 1. 生成ssh

如果本地的`~/.ssh/`下没有id_rsa和id_rsa.pub，则生成ssh：

```shell
ssh-keygen -t rsa -b 4096
```

## 2. 使用id_rsa.pub

将id_rsa.pub里的内容，拷贝到远程服务器的`~/.ssh/authorized_keys`中，一行一个公钥，放在最后一行就行。

```shell
cat ~/.ssh/id_rsa.pub
```

## 3. 设置vscode的ssh config

在vscode的ssh config里添加下面的配置，核心是`IdentityFile`，指向本地`id_rsa`的私钥路径。

```
Host aliyun-ssh
    HostName xxx.xxx.xxx.xxx
    User root
    IdentityFile C:\Users\Username\.ssh\id_rsa
```