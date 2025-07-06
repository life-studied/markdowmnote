---
create: '2025-07-06'
modified: '2025-07-06'
---

# 安装go tools

使用vscode拓展安装go tools时经常会出现网络问题，因此建议使用goproxy安装：

```powershell
$env:GO111MODULE = "on"
$env:GOPROXY = "https://goproxy.cn"

# 安装 gopls
go install golang.org/x/tools/gopls@latest

# 安装 gotests
go install github.com/cweill/gotests/gotests@latest

# 安装 impl
go install github.com/josharian/impl@latest

# 安装 goplay
go install github.com/haya14busa/goplay/cmd/goplay@latest

# 安装 dlv (Delve)
go install github.com/go-delve/delve/cmd/dlv@latest

# 安装 staticcheck
go install honnef.co/go/tools/cmd/staticcheck@latest
```