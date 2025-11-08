from git import Repo

def get_diff_files(repo_path):

    repo = Repo(repo_path)

    new_file_list = []
    modified_file_list = []

    # 1. 把暂存区固化成树对象
    index_tree_sha = repo.index.write_tree()

    # 2. 对比 HEAD → 暂存区树，发生了什么变化
    diff_staged = repo.head.commit.diff(other=index_tree_sha)

    for diff_item in diff_staged:
        # 修改文件
        if diff_item.change_type == 'M':
            modified_file_list.append(diff_item.b_path)
        # 新增文件
        elif diff_item.change_type == 'A':
            new_file_list.append(diff_item.b_path)

    return new_file_list, modified_file_list

    