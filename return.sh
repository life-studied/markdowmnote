#!/bin/bash

# 列出需要恢复修改时间的文件
find . -name '*.md' | while read file; do touch -d "$(git log -1 --pretty="@%ct" -- "$file")" "$file";
done;
