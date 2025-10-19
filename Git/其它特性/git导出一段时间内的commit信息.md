---
create: '2025-06-04'
modified: '2025-10-19'
---

# git导出一段时间内的commit信息

此内容目的是将git的commit相关内容导出成文本形式，供AI阅读分析。

## shell

```shell
#!/bin/bash

# 执行 git log 命令并获取输出
git --no-pager log --since="2025-05-01" --until="2025-06-01" --format="%h %s" | while read -r line; do
    # 提取每行开头的 commit id
    commit_id=$(echo "$line" | awk '{print $1}')
    
    # 使用 git show 来显示该 commit 的详细信息
    echo "-----------------------------"
    echo "Commit ID: $commit_id"
    echo "-----------------------------"
    git --no-pager show "$commit_id"
    echo "-----------------------------"
done
```

## pwsh

```powershell
# 定义 Git 日志命令
$gitLogCommand = "git --no-pager log --since=`"2025-05-01`" --until=`"2025-06-01`" --format=`"%h %s`""

# 执行 Git 日志命令并逐行读取输出
Invoke-Expression $gitLogCommand | ForEach-Object {
    # 提取每行开头的 commit id
    $commitId = ($_ -split ' ')[0]

    # 输出分隔符
    Write-Host "-----------------------------" -ForegroundColor Cyan
    Write-Host "Commit ID: $commitId" -ForegroundColor Green
    Write-Host "-----------------------------" -ForegroundColor Cyan

    # 使用 git show 显示该 commit 的详细信息
    Invoke-Expression "git --no-pager show $commitId"
    Write-Host "-----------------------------" -ForegroundColor Cyan
}
```