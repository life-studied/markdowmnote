---
create: 2023-08-01
---
# axmol安装

[【Axmol】基于Cocos2d-x 4.0的持续维护的游戏引擎介绍 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/168732477)

脚本：

```shell
@echo off

if "%~1"=="" (
    echo 请提供参数作为TestProject的替换值。
    exit /b
)

REM 保存原始的 Python 路径
set ORIGINAL_PYTHONPATH=%PYTHONPATH%
set ORIGINAL_PATH=%PATH%

REM 设置要用的 Python 版本路径
set PYTHONPATH=D:\ProgrammingSoftware\Python3
set PATH=%PYTHONPATH%;%PATH%

REM 设置项目名称
set project_name=%~1

call axmol new -p com.%project_name%.games -d . -l cpp --portrait %project_name%

cd %project_name%

pwsh .\build.ps1
REM cmake .. -G"Visual Studio 17 2022" -A x64

.\run.bat Debug

REM 脚本退出后，恢复原始的 Python 路径
set PYTHONPATH=%ORIGINAL_PYTHONPATH%
set PATH=%ORIGINAL_PATH%

```

