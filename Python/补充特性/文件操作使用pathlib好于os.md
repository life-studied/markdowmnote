---
create: '2025-04-11'
modified: '2025-04-11'
---

# 文件操作使用pathlib好于os

使用os进行文件操作，常有这些痛点：

* 遍历时，`os.listdir()`或者`os.walk()`只给出最后一级的文件名，进行操作还需要使用`os.path.join()`去拼接成完整路径
* 获取文件的后缀名时，使用os需要通过`os.path.splitext(file_path)[-1]`这种方法。对于使用pathlib，它的`Path(file_path).suffix`的语义更加清晰。
* os对于文件的操作往往是基于字符串的，使用时往往需要小心翼翼，而pathlib却更偏向于使用文件对象的概念，更加合理和高级

## 最佳实践

下面给出一些比较好的操作方法。

### 删除文件夹下的所有内容(shutil.rmtree(p))

```python
from pathlib import Path
import shutil

# 直接删除包括整个文件夹的所有内容
p = Path("test")
shutil.rmtree(p)
```

### 递归 删除文件夹下的指定内容(Path(dir_path).rglob())

使用pathlib的`Path(dir_path).rglob()`操作相比于使用os.walk()更加方便。

```python
from pathlib import Path

def rm_all_file_with_suffix(dir_path: str, suffix_list: list[str]):
    """
    删除给定dir下的所有符合suffix_list的文件
    example: 
    	suffix_list: [".txt", ".png", ...]
    """
    # 创建一个路径对象
    p = Path(dir_path)

    # 递归遍历目录
    for item in p.rglob("*"):  # * 表示匹配所有文件和目录
        print(item)  # 输出：目录及其子目录中的所有文件和目录路径
        if item.is_file() and item.suffix in suffix_list:
			item.unlink()	# 删除文件用unlink()，删除目录用rmdir()
```

### 删除文件夹下的直接子项(Path(dir_name).iterdir())

和上面类似，但是不需要递归，因此可以使用`Path(dir_name).iterdir()`进行遍历：

```python
from pathlib import Path

def rm_direct_file_with_suffix(dir_path: str, suffix_list: list[str]):
    """
    删除给定dir下的符合suffix_list的子文件（不包括子目录下的）
    example: 
    	suffix_list: [".txt", ".png", ...]
    """
    # 创建一个路径对象
    p = Path(dir_path)

    # 遍历目录的直接子项
    for item in p.iterdir():
        print(item)
        if item.is_file() and item.suffix in suffix_list:
			item.unlink()	# 删除文件用unlink()，删除目录用rmdir()
```

### 拼接路径和创建文件

```python
from pathlib import Path

# 使用 / 拼接路径
p = Path.cwd() / "your" / "txts" / "file.txt"
print(p)	# win: D:\codeSpace\bad_code\test_py_pathlib\your\txts\file.txt

# 递归创建所在目录路径
p.parent.mkdir(parents=True, exist_ok=True)

# 创建文件
p.touch()
```