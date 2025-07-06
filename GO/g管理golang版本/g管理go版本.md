---
create: '2025-04-12'
modified: '2025-07-06'
---

# g管理go版本

* [Releases · voidint/g](https://github.com/voidint/g/releases)

## 查看golang版本

### 本地版本

```shell
C:\Users\10654>g ls
* 1.23.2
```

### 所有remote版本

```shell
C:\Users\10654>g ls-remote
...
```

## 安装golang版本

```shell
C:\Users\10654>g install 1.23.2
```

## 切换golang版本

```shell
C:\Users\10654>g use 1.21.5
```

## 卸载golang版本

```shell
C:\Users\10654>g uninstall 1.14.7
```

## 将go添加到环境变量

以软链接的形式，下面的目录：

```
~\.g\go\bin
```