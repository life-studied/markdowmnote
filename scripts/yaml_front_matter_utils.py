import frontmatter
import os
from datetime import datetime


def get_yaml_front_matter_from(file_path):
    with open(file_path, encoding="utf-8-sig") as file:
        post = frontmatter.load(file)
    return post


def get_file_modify_time_from(file_path):
    return datetime.fromtimestamp(os.path.getmtime(file_path)).strftime('%Y-%m-%d')


def add_field_create_to(post, file_modified_time):
    if post.get("create") is None:
        post["create"] = file_modified_time
        print(f"\tAdded 'create' field")


def add_field_modify_to(post, file_modified_time):
    post["modified"] = file_modified_time
    print(f"\tUpdated 'modified' field")


def check_field_keywords(post):
    if post.get("keywords") is None:
        print(f"\tWarning, 'keywords' missing")

def pre_check_field_keywords(prefix, new_and_modify_files_name):
    keywords_missing = False
    # 预检查 md 文件是否有keywords字段
    for file_name in new_and_modify_files_name:
        file_path = prefix + "/" + file_name
        post = get_yaml_front_matter_from(file_path)
        if post.get("keywords") is None:
            print(f"Warning: 'keywords' field missing in {file_name}")
            keywords_missing = True
    
    if keywords_missing:
        if os.environ.get('SKIP_KEYWORD_CHECK'):
            print("Warning: keywords missing, but SKIP_KEYWORD_CHECK set, continue.")
        else:
            print("Error: 'keywords' field missing in some files.")
            print("Set SKIP_KEYWORD_CHECK=1 to skip this check.")
            exit(1)


def write_yaml_front_matter_to(file_path, post):
    with open(file_path, "w", encoding="utf-8") as file:
        file.write(frontmatter.dumps(post))
        print(f"\tWrote updated front matter to {file_path}")


def process_fields(post, meta_data):

    file_modified_time = meta_data["file_modified_time"]

    # 处理yaml front matter字段
    add_field_create_to(post, file_modified_time)
    add_field_modify_to(post, file_modified_time)
    check_field_keywords(post)

    
def main(file_path):
    if not file_path.endswith(".md"):
        return
    
    print(f"Processing file: {file_path}")

    # 后续依赖的元数据结构体
    meta_data = {}
    meta_data["file_modified_time"] = get_file_modify_time_from(file_path)

    # 读取yaml front matter
    post = get_yaml_front_matter_from(file_path)

    # 处理yaml front matter字段
    process_fields(post, meta_data)

    # 写回yaml front matter到文件
    write_yaml_front_matter_to(file_path, post)

    print(f"Finished processing file: {file_path}\n")
    

