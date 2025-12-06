# .githooks/pre-commit.ps1

# 定位脚本自身目录，并切换过去
$hookDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $hookDir

# 检查是否位于 .git/hooks
if (-not $hookDir.EndsWith('.git\hooks')) {
    Write-Host "Error: This script must be run from a directory ending with .git/hooks." -ForegroundColor Red
    Write-Host "Current directory: $hookDir"
    exit 1
}

# 检查 python 是否存在
if (-not (Get-Command python -ErrorAction SilentlyContinue)) {
    Write-Host "python could not be found" -ForegroundColor Red
    exit 1
}

# 回到仓库根目录
Set-Location ..\..\

Write-Host "Running pre-commit checks..." -ForegroundColor Cyan
python commit.py
$pyExit = $LASTEXITCODE

if ($pyExit -ne 0) {
    Write-Host "Pre-commit checks failed." -ForegroundColor Red
    exit $pyExit      # 把 Python 的退出码原样交给 Git
}

Write-Host "Pre-commit checks completed." -ForegroundColor Green
exit 0