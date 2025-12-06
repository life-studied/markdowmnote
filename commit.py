import os

import scripts.git_utils as g_utils
import scripts.yaml_front_matter_utils as y_utils
import scripts.keywords_kv_utils as kv_utils


def main(prefix):
    prefix = prefix.replace("\\", "/")

    if not os.path.exists(prefix):
        print("menu path not exist:", prefix)
        return

    if prefix.endswith("/"):
        prefix = prefix[:-1]

    if not os.path.exists(os.path.join(prefix, kv_utils.get_keywords_kv_file_name())):
        print("keywords.kv not exist in path:", prefix)
        print("Please run install script first.")
        exit(1)

    # 获取git更新：新增、修改、删除文件
    new_files, modified_files = g_utils.get_exist_diff_files(prefix)
    deleted_files = g_utils.get_deleted_diff_files(prefix)
    print("new files:", *new_files, sep="\n\t")
    print()
    print("modified files:", *modified_files, sep="\n\t")

    # 筛选出 md 文件
    new_files = [file_name for file_name in new_files if file_name.endswith(".md")]
    modified_files = [file_name for file_name in modified_files if file_name.endswith(".md")]
    deleted_files = [file_name for file_name in deleted_files if file_name.endswith(".md")]

    # 预检查所有改动的 keywords 字段，提示缺失
    y_utils.pre_check_field_keywords(prefix, new_files + modified_files)

    # 自动更新 yaml front matter
    for file_path in new_files + modified_files:
        y_utils.main(prefix + "/" + file_path)

    # 自动更新本地缓存数据库
    kv_utils.main(prefix, new_files, modified_files, deleted_files)

    # 将脚本更新的内容，刷进暂存区
    if new_files or modified_files:
        g_utils.update_repo_stage(prefix, new_files + modified_files)
        print("Staged updated files.")

if __name__ == "__main__":
    # 获取脚本所在位置，切换工作目录到此位置
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    print("work dir:", os.getcwd())
    try:
        main(os.getcwd())
    except Exception as e:
        exit(1)
