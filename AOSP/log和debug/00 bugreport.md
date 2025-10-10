---
create: '2025-10-10'
modified: '2025-10-10'
---

# bugreport

## bugreport是什么

1. bugreport 本身并不是一个“超级诊断工具”，它只是一个脚本/入口（在 Android 源码里叫 dumpstate 可执行文件 + shell 脚本）。
2. 运行时它会**依次拉起**系统里已有的诊断命令，把输出全部收集起来，最后压成一个 ZIP。被拉起的典型命令有：
   - logcat（Java/Kotlin 崩溃、系统消息）
   - dumpsys（所有系统服务快照：Activity、Window、MemInfo、CPU、Binder、GPU……）
   - dumpstate（内核日志、进程列表、文件描述符、anr trace、oom 分数、网络状态、property、last_kmsg 等）
   - bugreportz（把上面所有东西再打包、加时间戳、版本号、systrace 可选）
3. 因此你拿到的那份 ZIP 其实是一个“快照合集”，而不是某个新工具产生的额外信息；它**不分析**，只**汇总**。
4. 好处是“一键”就能把当时系统里能看的几乎都抓下来，省得你手动敲十几条 adb 命令；坏处是体积大（几十～几百 MB），且敏感信息（账号、通知内容、部分内存镜像）都在里面，上传前需要脱敏。

## bugreport使用方式

```shell
adb bugreport <保存路径>	# 如果未指定路径，报告会保存在当前目录。
```