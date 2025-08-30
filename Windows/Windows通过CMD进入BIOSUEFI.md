---
create: '2025-08-30'
modified: '2025-08-30'
---

# Windows通过CMD进入BIOS/UEFI

```cmd
shutdown /r /fw /t 0
```

* shutdown：这个命令的作用是关机或重启。 
* /r：这个参数是 reboot 的缩写，表示重启计算机。 
* /fw：这个参数是 firmware 的缩写，它告诉系统在重启时进入固件界面，也就是我们常说的 UEFI BIOS 界面。 
* /t 0：这个参数是 timeout 的缩写，后面的 0 表示在 0 秒后立即执行上述操作，也就是没有延迟。