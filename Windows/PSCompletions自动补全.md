---
create: '2025-11-16'
modified: '2025-11-16'
---

# PSCompletions自动补全

前置准备：[scoop安装](./scoop安装.md)

```powershell
scoop install abyss/abgox.PSCompletions
Import-Module PSCompletions
psc add scoop scoop-install scoop-update

# 添加i18n中文提示
scoop install abyss/abgox.scoop-i18n
```