# Git Commit常用约定

* 使用关键字来表示这个提交的类型。
	* feat: 新功能
	* fix: 修复 bug
	* docs: 文档改进
	* style: 代码风格改进，不影响代码含义
	* refactor: 重构代码，既不修复错误也不添加新功能
	* test: 增加或修改测试代码
	* chore: 构建过程或辅助工具的变动
	* perf：改进性能的代码更改
	* ci：更改我们的持续集成文件和脚本
* 如果需要，可以在提交信息中引用 issue（使用`#`来表示） 或 PR 编号（使用`!`来表示）。
* 如果有必要，可以附加详细的信息，比如代码变更的原因和影响。

## example

### 1.修复bug，bug的issue编号为123

```shell
git commit -m "fix: resolve issue #123"
```

### 2.提交pr，pr的编号为123

```shell
git commit -m "Add a new feature\n\nSee merge request !123"
```

### 3.提交ci，引入新的测试框架

```shell
git commit -m "ci: update TestSuite for easier testing"
```

