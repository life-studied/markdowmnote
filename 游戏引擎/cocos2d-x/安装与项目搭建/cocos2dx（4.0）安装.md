# cocos2dx安装

## 1. 下载cocos2d-x压缩文件（选择4.0版本）

[Cocos2d-x - 成熟、轻量、开放的跨平台解决方案](https://www.cocos.com/cocos2dx-download)

## 2. 下载python2.7.10

* 添加环境变量
* 记住bin目录，并填入下面的脚本中

## 3. 解压并安装cocos2d-x文件

* 将文件解压到某个目录下
* 在cmd中执行`python ./setup.py`

## 4. 创建项目

* 选择一个项目目录（此时可以撤销python2.7环境变量，以免影响你的电脑中的高版本python）

* 创建一个`newCocos.bat`文件，里面放入以下代码，并修改python2.7的bin目录

  ```shell
  @echo off
  
  if "%~1"=="" (
      echo 请提供参数作为TestProject的替换值。
      exit /b
  )
  
  REM 保存原始的 Python 路径
  set ORIGINAL_PYTHONPATH=%PYTHONPATH%
  set ORIGINAL_PATH=%PATH%
  
  REM 设置要用的 Python 版本路径（此处需要修改）
  set PYTHONPATH=D:\UsefulTools\Python2-7-10
  set PATH=%PYTHONPATH%;%PATH%
  
  REM 设置项目名称
  set project_name=%~1
  
  REM 使用call防止进程意外终止
  call cocos new %project_name% -l cpp -p com.%project_name%.games
  
  cd %project_name%
  cd proj.win32
  
  cmake .. -G"Visual Studio 17 2022" -A win32
  
  REM 脚本退出后，恢复原始的 Python 路径
  set PYTHONPATH=%ORIGINAL_PYTHONPATH%
  set PATH=%ORIGINAL_PATH%
  
  ```

* 创建cocos项目（cmd中）

  ```shell
  newCocos.bat helloworld
  ```

* 打开项目：`proj.win32`下
