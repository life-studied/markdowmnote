import frontmatter
import os
from datetime import datetime


def check_yaml_front_matter(file_path):
    with open(file_path, encoding="utf-8-sig") as file:
        post = frontmatter.load(file)
        if post.metadata:
            return True, post
        else:
            return False, post


def add_yaml_front_matter_to_md_files(file_path):
    if not file_path.endswith(".md"):
        return

    print(f"\nProcessing file: {file_path}:")
    # 获取文件修改时间
    file_modified_time = datetime.fromtimestamp(os.path.getmtime(file_path)).strftime('%Y-%m-%d')

    # 检查yaml front matter
    has_front_matter, post = check_yaml_front_matter(file_path)

    if has_front_matter:
        if post["create"] is None:
            post["create"] = file_modified_time
            print(f"\tAdded 'create' date to {file_path}")
        post["modified"] = file_modified_time
        print(f"\tUpdated 'modified' date in {file_path}")
    else:
        post["create"] = file_modified_time
        post["modified"] = file_modified_time
        print(f"\tAdded 'create' date to {file_path}")
        print(f"\tAdded 'modified' date in {file_path}")
    
    # 将post替换为新的frontmatter
    with open(file_path, "w", encoding="utf-8") as file:
        file.write(frontmatter.dumps(post))

    
