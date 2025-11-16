---
create: '2025-11-16'
modified: '2025-11-16'
---

# pwsh添加启动配置$PROFILE

```powershell
# 创建配置文件（如果没有）
if (!(Test-Path -Path $PROFILE)) { New-Item -ItemType File -Path $PROFILE -Force }

# 打开文件
code $PROFILE
```