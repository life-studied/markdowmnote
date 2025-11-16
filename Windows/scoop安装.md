---
create: '2025-11-16'
modified: '2025-11-16'
---

# scoop安装

基于gitee镜像代理完成。

## 安装scoop

[scoop本体安装](https://gitee.com/scoop-installer-mirrors)

复制下面的命令即可。

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
irm https://gitee.com/scoop-installer-mirrors/Install/releases/download/archive/install.ps1 -OutFile install.ps1

.\install.ps1
```

## 添加bucket

确保7zip和git在电脑上。

```powershell
# 设置scoop仓库
scoop config scoop_repo https://gitee.com/scoop-installer-mirrors/Scoop

# 添加bucket: main extras
scoop bucket add main https://gitee.com/scoop-installer-mirrors/Main
scoop bucket add extras https://gitee.com/scoop-installer-mirrors/Extras

# 优化scoop性能
scoop config use_sqlite_cache true
scoop config aria2-enabled true
```

### 添加abyss bucket

```powershell
scoop bucket add abyss https://gitee.com/abgox/abyss
```

### 软件换源

如果有梯子可以不用换源（github的release一般下载很快，但是clone就算开了梯子也不太行）

通过`scoop-install`代替原有的scoop install：

```powershell
scoop install abyss/abgox.scoop-install
scoop install abyss/abgox.scoop-update
scoop config abgox-scoop-install-url-replace-from "^https://github.com|||^https://raw.githubusercontent.com"
scoop config abgox-scoop-install-url-replace-to "https://gh-proxy.com/github.com|||https://gh-proxy.com/raw.githubusercontent.com"
```

### 安装测试

```powershell
scoop-install sudo
```

## 参考资料

* 视频：https://www.bilibili.com/video/BV11YnDzsEQG
* Gitee：[scoop-installer-mirrors](https://gitee.com/scoop-installer-mirrors)