from git import Repo

def get_diff_files(repo_path):

    repo = Repo(repo_path)

    new_file_list = []
    modified_file_list = []

    # 获取所有未暂存的更改
    # diff_unstaged = repo.index.diff(None)

    # 获取新增但未暂存的文件
    untracked_files = repo.untracked_files

    # 获取暂存区的更改
    diff_staged = repo.index.diff("HEAD")

    # 新增文件
    for untracked_file in untracked_files:
        new_file_list.append(untracked_file)

    # 修改文件
    for diff_item in diff_staged:
        if diff_item.change_type == 'M':
            modified_file_list.append(diff_item.b_path)

    return new_file_list, modified_file_list

    