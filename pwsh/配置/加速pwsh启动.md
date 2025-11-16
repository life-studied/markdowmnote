---
create: '2025-11-16'
modified: '2025-11-16'
---

# 加速pwsh启动

有时候pwsh的启动太慢了，可以在配置文件中添加下面的内容以加速：

```profile
$env:DOTNET_ReadyToRun=1
$env:DOTNET_TieredCompilation=1
$env:DOTNET_TC_QuickJit=1
```

这些设置分别启用了ReadyToRun预编译、分层编译和快速JIT编译，能够显著减少启动时的编译开销。相关的运行时优化代码可以在[src/powershell-win-core/runtimeconfig.template.json](https://link.gitcode.com/i/59fdfd18f4c3e98906a0f6bcc8a28c77)中找到默认配置。