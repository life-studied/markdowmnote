import os

import scripts.find_diff as fd
import scripts.check_fronter as cf
import scripts.commit_push as commit_push


def main(prefix="notes"):
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

    # 提交到git
    commit_push.commit_push(prefix)


if __name__ == "__main__":
    main(".")
