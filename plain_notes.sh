#!/bin/bash

SCRIPT_DIR=$(dirname "$0")

find . ! -name '.git' -exec rm -rf {} +

git pull origin master

git reset --hard HEAD

find . -type f -name "*.md" -exec mv {} "$SCRIPT_DIR" \;

echo "����.md�ļ����ƶ����ű�����Ŀ¼��"