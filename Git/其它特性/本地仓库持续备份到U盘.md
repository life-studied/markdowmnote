---
create: '2025-07-27'
modified: '2025-07-27'
---

# 本地仓库持续备份到U盘

## 在U盘中克隆本地仓库

```shell
cd <u-pan-repo-path>
git clone <local-repo-path> <u-pan-repo-path>

# git clone C:/c-note  C:/code_space/badcode/test_c_note
# windows下使用/，否则会出错
```

## 持续更新

```shell
cd <u-pan-repo-path>
git pull origin
```