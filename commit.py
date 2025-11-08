import os

import scripts.find_diff as fd
import scripts.check_fronter as cf


def main(prefix):
    prefix = prefix.replace("\\", "/")

    if not os.path.exists(prefix):
        print("目录不存在")
        return

    if prefix.endswith("/"):
        prefix = prefix[:-1]

    new_files, modified_files = fd.get_diff_files(prefix)
    print("new files:", *new_files, sep="\n")
    print("modified files:", *modified_files, sep="\n")

    # 添加yaml front matter
    for file_path in new_files + modified_files:
        cf.add_yaml_front_matter_to_md_files(prefix + "/" + file_path)

    # 将修改后的文件添加到暂存区
    if new_files or modified_files:
        repo = fd.Repo(prefix)
        repo.index.add(new_files + modified_files)
        print("Staged updated files.")

if __name__ == "__main__":
    # 获取脚本所在位置，切换工作目录到此位置
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    print("work dir:", os.getcwd())
    main(os.getcwd())
