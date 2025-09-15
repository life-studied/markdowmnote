---
create: '2025-07-27'
modified: '2025-09-15'
---

# pyproject.toml管理

## 1.过去的`pip freeze > requirements.txt`的问题

1. 信息冗余：requirements.txt中不仅有直接依赖的信息，还会有间接依赖。
2. 删除不完全：删除依赖时更新requirements.txt，只会删除直接依赖，但是不会删除间接依赖。

但是这其实不影响正常的使用，只是在开发移植的时候会出现环境的臃肿和不必要的依赖。

## 2.现代的pyproject.toml

新版python（PEP 518和PEP 621）都推荐采用pyproject.toml统一管理项目的元信息，包含了该项目的依赖。

> * [PEP 518 – Specifying Minimum Build System Requirements for Python Projects | peps.python.org](https://peps.python.org/pep-0518/)
> * [PEP 621 – Storing project metadata in pyproject.toml | peps.python.org](https://peps.python.org/pep-0621/)

### 一个pyproject.toml的示例

```toml
[project]
name = "myproject"
version = "0.1.0"
dependencies = [
	"Flask==3.1.1"	# 只有直接依赖
]
```

这样做统一了配置文件，同时又省去了不必要的依赖冗余。

### pip使用pyproject.toml

```shell
# 将toml安装到venv中，-e是因为还会同时安装项目本身的代码，为了避免源代码拷贝后不能更新到venv，-e会创建软链接，代替拷贝项目本身
pip install -e .
```

pyproject.toml虽然解决了一些问题，但是编写pyproject.toml需要手动编写，因此现代都会选择使用uv或者Poetry这样的工具。

## 3. uv

```shell
# new
uv init -p 3.12
uv add pytorch

# use toml
uv sync
```

### 全局管理python版本

```shell
uv puthon list	# 列出所有版本

uv python install cpython-3.12	# 安装cpyhon3.12
```

### init项目并指定py版本

```shell
uv init -p 3.12
```

### 安装依赖（自动创建venv）

```shell
uv add pytorch
uv add pytest --dev		# 不会被打包，仅在开发阶段使用的依赖
```

### 卸载依赖

```shell
uv remove pytorch
uv remove pytest --dev
```

### 安装全局工具

在需要一些开发中需要（检查类型等），但是代码里没有直接用到的库，可以这么做。

```shell
uv tool install ruff	# 全局工具，不会被安装到venv中，处处可用
```

### 打包

使用uv可以打包库或者可执行脚本，然后使用上述的安装方式，安装到venv或者全局。

#### 1.库打包

直接使用uv build，就会把当前的库打包成whl文件：

```shell
uv build
```

#### 2.可执行脚本打包

需要在pyproject.toml中添加一个脚本，用于自定义入口：

```toml
[project.scripts]
script_name = "module_name:function_name"
# eg. ai = "main:ai_script_entry"
```

接着打包：

```shell
uv build
```