git add .
current_date=$(date +"%Y-%m-%d")
commitmsg="update on $current_date"
git commit -m "$commitmsg"
git push markdownnote master