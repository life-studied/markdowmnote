#!/bin/bash

# 列出需要恢复修改时间的.md文件
find . -name '*.md' | while read file; do
    touch -d "$(git log -1 --pretty="@%ct" -- "$file")" "$file";
done;

# 列出需要恢复修改时间的.xmind文件
find . -name '*.xmind' | while read file; do
    touch -d "$(git log -1 --pretty="@%ct" -- "$file")" "$file";
done;