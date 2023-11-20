git add .
current_date=$(date +"%Y-%m-%d")
commitmsg="update on $current_date"
git commit -m "$commitmsg"
git push markdownnote master
read -n 1 -s -r -p "按任意键结束..."