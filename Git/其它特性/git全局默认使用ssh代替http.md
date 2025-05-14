---
create: '2025-05-05'
modified: '2025-05-05'
---

# git全局默认使用ssh代替http

## 配置

```bash
git config --global url."git@github.com:".insteadOf "https://github.com/"
```

## 验证

```bash
git config --global --get url.git@github.com:.insteadOf
```

> 输出应为：https://github.com/