#!/bin/bash

file_extension=".md"

count=$(find "$(pwd)" -type f -name "*$file_extension" | wc -l)

echo "在当前目录及其子文件夹中找到了 $count 个 .md 文件。"
read -n 1 -s -r -p "按任意键结束..."