from git import Repo
from datetime import datetime

def commit_push(repo_path='notes'):
    repo = Repo(repo_path)

    # 暂存所有更改的文件
    repo.git.add(A=True)

    # 提交更改，添加提交信息
    commit_message = "Update On " + datetime.now().strftime("%Y-%m-%d")
    repo.git.commit(m=commit_message)

    # 推送到远程仓库，默认是origin分支
    remote = repo.remote(name='origin')
    remote.push()