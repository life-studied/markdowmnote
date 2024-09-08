#!/bin/bash

# 脚本所在目录
SCRIPT_DIR=$(dirname "$0")

# 移动所有.md文件到脚本所在目录
find . -type f -name "*.md" -exec mv {} $SCRIPT_DIR \;

# 删除脚本所在目录下除了.md文件以外的所有文件和文件夹
find $SCRIPT_DIR -type f ! -name "*.md" -delete
find $SCRIPT_DIR -mindepth 1 -type d -exec rm -rf {} +

echo "所有.md文件已移动到脚本所在目录。"