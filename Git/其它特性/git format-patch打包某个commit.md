---
create: '2025-11-08'
modified: '2025-11-08'
---

# git format-patch打包某个commit

## 打包

```shell
# patch成多文件
git format-patch -<number> <commit-hash> -o <out-dir>
# patch成单文件
git format-patch -<number> <commit-hash> --stdout > <out-file>

# 打包从HEAD追溯的共3个commit
git format-patch -3 HEAD -o ../patches
git format-patch -3 HEAD --stdout > ../3-patches.patch
```

## 应用

```shell
git am patches/*.patch
git am 3-patches.patch
```