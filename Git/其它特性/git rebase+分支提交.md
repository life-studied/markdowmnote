---
create: '2025-04-26'
modified: '2025-04-26'
---

# git rebase+分支/提交

git rebase后可以跟两种模式：

1. git rebase <branch-name>
2. git rebase <commit-hash>

## 核心

git rebase，后面跟的参数，就是所谓的基（commit-base）；

从这一点来看，无论后面跟的是分支名还是提交hash，本质上都是：

1. 将基指定为：branch间接指向的commit-hash，或者是直接的commit-hash
2. 将此commit-base的基础上，将HEAD所在的commit-hash与commit-base，找到公共祖先commit-ancestor
3. 将从公共祖先commit-ancestor开始，一直到HEAD为止的所有commit，放到commit-base后

```shell
git rebase main			# 从HEAD到main的公共祖先之间
git rebase HEAD~4		# 从HEAD到HEAD~4之间
git rebase 890abcdef	# 从HEAD到890abcdef之间
```

## 交互式

通过添加-i，可以实现交互式rebase，从而更加灵活地：合并、丢弃、交换 commit，形成更加好看的提交历史。

```shell
pick 1234567 First commit
reword 890abcdef Second commit
pick fedcba9 Third commit
```

* p：pick，采用本次提交
* r：reword，重写message
* s：squash，合并本次提交到上一次
* d：drop，丢弃提交，及其修改的内容