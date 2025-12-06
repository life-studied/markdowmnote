import scripts.yaml_front_matter_utils as y_utils


def get_keywords_kv_file_name():
    return "keywords.kv"


def main(prefix, new_files_name, modify_files_name, deleted_files_name=[]):
    new_files_name = [file_name for file_name in new_files_name if file_name.endswith(".md")]
    modify_files_name = [file_name for file_name in modify_files_name if file_name.endswith(".md")]
    deleted_file_name = [file_name for file_name in deleted_files_name if file_name.endswith(".md")]

    new_files_path = [prefix + "/" + file_name for file_name in new_files_name]
    modify_files_path = [prefix + "/" + file_name for file_name in modify_files_name]
    # 读取keywords.kv文件
    with open("keywords.kv", "r", encoding="utf-8") as kv_file:
        kv_list = []
        # 读取键值对k=v，一行一个
        for line in kv_file:
            kv = []
            key = line.split('=')[0].strip()
            values = line.split('=')[1].strip().split(',')
            kv.append(key)
            kv.append(values)
            kv_list.append(kv)

    # new keywords
    new_keywords = {}
    for new_file_name, new_file_path in zip(new_files_name, new_files_path):
        post = y_utils.get_yaml_front_matter_from(new_file_path)
        key = new_file_name
        values = post.get("keywords", [])
        new_keywords[key] = values

    # modify keywords
    modify_keywords = {}
    for modify_file_name, modify_file_path in zip(modify_files_name, modify_files_path):
        post = y_utils.get_yaml_front_matter_from(modify_file_path)
        key = modify_file_name
        values = post.get("keywords", [])
        modify_keywords[key] = values
    
    # 更新kv_list: new
    for file_name in new_files_name:
        kv_list.append([file_name, new_keywords[file_name]])

    # 更新kv_list: modify
    for file_name in modify_files_name:
        if file_name in new_keywords:
            for kv in kv_list:
                if kv[0] == file_name:
                    kv[1] = new_keywords[file_name]
                    break
    
    # 更新kv_list: delete
    for file_name in deleted_file_name:
        for kv in kv_list:
            if kv[0] == file_name:
                kv_list.remove(kv)
                break

    # 写回keywords.kv文件
    with open("keywords.kv", "w", encoding="utf-8") as kv_file:
        for kv in kv_list:
            line = kv[0] + " = " + ",".join(kv[1]) + "\n"
            kv_file.write(line)
    print(f"Updated keywords.kv file.")




        




