---
create: '2025-09-29'
modified: '2025-09-29'
---

## 原因

Win11 24H2镜像出货的机型会默认打开“Virtualization-based Security（VBS）基于虚拟化的安全性”功能，从而导致VMWare的虚拟化加速功能不能正常使用。

## 查看

Win+R呼出运行框，输入msinfo32查看VBS的运行状态。

```
基于虚拟化的安全性	正在运行
```

## 关闭

管理员权限执行下面的脚本后，重启电脑，会在开机弹出询问“是否要disable VBS”：

```bat
@echo off

dism /Online /Disable-Feature:microsoft-hyper-v-all /NoRestart
dism /Online /Disable-Feature:IsolatedUserMode /NoRestart
dism /Online /Disable-Feature:Microsoft-Hyper-V-Hypervisor /NoRestart
dism /Online /Disable-Feature:Microsoft-Hyper-V-Online /NoRestart
dism /Online /Disable-Feature:HypervisorPlatform /NoRestart

REM ===========================================

mountvol X: /s
copy %WINDIR%\System32\SecConfig.efi X:\EFI\Microsoft\Boot\SecConfig.efi /Y
bcdedit /create {0cb3b571-2f2e-4343-a879-d86a476d7215} /d "DebugTool" /application osloader
bcdedit /set {0cb3b571-2f2e-4343-a879-d86a476d7215} path "\EFI\Microsoft\Boot\SecConfig.efi"
bcdedit /set {bootmgr} bootsequence {0cb3b571-2f2e-4343-a879-d86a476d7215}
bcdedit /set {0cb3b571-2f2e-4343-a879-d86a476d7215} loadoptions DISABLE-LSA-ISO,DISABLE-VBS
bcdedit /set {0cb3b571-2f2e-4343-a879-d86a476d7215} device partition=X:
mountvol X: /d
bcdedit /set hypervisorlaunchtype off

echo.
echo.
echo.
echo.
echo =======================================================
echo finished. Please restart your computer.
pause > nul
echo.
echo.
```