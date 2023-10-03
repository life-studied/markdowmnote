#!/bin/bash

md_extension=".md"
xmind_extension=".xmind"

countmd=$(find "$(pwd)" -type f -name "*$md_extension" | wc -l)
countxmind=$(find "$(pwd)" -type f -name "*$xmind_extension" | wc -l)
total=$((countmd + countxmind))

echo "在当前目录及其子文件夹中找到了 $total 个笔记。"
echo "markdown笔记: $countmd 个。"
echo "xmind笔记   : $countxmind 个。"
read -n 1 -s -r -p "按任意键结束..."