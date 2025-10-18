:: copy hooks file (in current dir) to .git\hooks
@echo off
setlocal enabledelayedexpansion
set SCRIPT_DIR=%~dp0
set HOOKS_DIR=%SCRIPT_DIR%hooks
set GIT_HOOKS_DIR=%SCRIPT_DIR%..\.git\hooks

:: check if hooks source directory exists
if not exist "%HOOKS_DIR%" (
    echo Hooks source directory not found: %HOOKS_DIR%
    exit /b 1
)

:: check if hooks directory exists
if not exist "%GIT_HOOKS_DIR%" (
    echo Git hooks directory not found: %GIT_HOOKS_DIR%
    exit /b 1
)

:: copy each hook script
for %%f in ("%HOOKS_DIR%\*") do (
    set FILENAME=%%~nxf
    echo Installing hook: !FILENAME!
    copy /Y "%%f" "%GIT_HOOKS_DIR%\!FILENAME!" >nul
)
echo All hooks installed successfully.