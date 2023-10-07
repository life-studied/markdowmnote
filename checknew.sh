# 拉取远程分支的最新信息
git fetch

# 检查本地的main分支和远程的main分支是否有差异
if git diff --quiet main origin/main; then
  echo "远程main分支没有更新，不执行后续操作"
  exit 0
else
  echo "远程main分支有更新，本地执行更新"
  git pull origin main
  exit 1
fi