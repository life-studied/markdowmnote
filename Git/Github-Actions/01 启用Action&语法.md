---
create: 2024-04-24
modified: '2024-11-17'
---

# 启用Action

​	Github-Actions的启动依赖于本仓库下的特定文件：`.github/workflows/xxx.yml`

​	语法：[GitHub Actions 的工作流语法 - GitHub 文档](https://docs.github.com/zh/actions/using-workflows/workflow-syntax-for-github-actions)。

## 1. easy-example

```yaml
name: 打包C++项目
on: push

jobs:
  check version:
    name: 检查版本
    runs-on: ubuntu-latest
    
    steps:
    - name: 检查cmake版本
      run: cmake -v

  Cpp-build:
    name: Cpp-build工作
    runs-on: ubuntu-latest
    
    steps:
    - name: 读取仓库内容
      uses: actions/checkout@v4
      
    - name: 生成cmake build文件
      run: cmake -S . -B build
	
	- name: 开始编译
	  run: cmake --build build
	  
	- name: 运行代码
	  run: cd build && xxx.exe
```

## 2. 关键字

### name

​	`name`字段是 workflow 的名称。如果省略该字段，默认为当前 workflow 的文件名。

​	name可以给不同的字段添加，以指明该任务的名字。

```yaml
name: GitHub Actions Demo
```

### on

#### 简单触发/数组触发

​	`on`字段指定触发 workflow 的条件，通常是某些事件。

```yaml
on: push
on: [push, pull_request]
```

#### 限定触发

指定触发事件时，可以限定分支或标签。

```yaml
on:
  push:
    branches:    
      - master
```

​	上面代码指定，只有`master`分支发生`push`事件时，才会触发 workflow。

### jobs

​	workflow 文件的主体是`jobs`字段，表示要执行的一项或多项任务。

​	`jobs`字段里面，需要写出每一项任务的`job_id`，具体名称自定义。`job_id`里面的`name`字段是任务的说明。

```yaml
jobs:
  my_first_job:
    name: My first job
  my_second_job:
    name: My second job
```

​	上面代码的`jobs`字段包含两项任务，`job_id`分别是`my_first_job`和`my_second_job`。

#### needs

​	`needs`字段指定当前任务的依赖关系，即运行顺序。

```yaml
jobs:
  job1:
  job2:
    needs: job1
  job3:
    needs: [job1, job2]
```

​	上面代码中，`job1`必须先于`job2`完成，而`job3`等待`job1`和`job2`的完成才能运行。因此，这个 workflow 的运行顺序依次为：`job1`、`job2`、`job3`。

### runs-on

`runs-on`字段指定运行所需要的虚拟机环境。它是必填字段。目前可用的虚拟机如下。

* `ubuntu-latest`，`ubuntu-18.04`或`ubuntu-16.04`
* `windows-latest`，`windows-2019`或`windows-2016`
* `macOS-latest`或`macOS-10.14`

下面代码指定虚拟机环境为`ubuntu-18.04`。

```yaml
runs-on: ubuntu-18.04
```

### steps

`steps`字段指定每个 Job 的运行步骤，可以包含一个或多个步骤。每个步骤都可以指定以下三个字段。

> - `jobs.<job_id>.steps.name`：步骤名称。
> - `jobs.<job_id>.steps.run`：该步骤运行的命令或者 action。
> - `jobs.<job_id>.steps.env`：该步骤所需的环境变量。

### uses

​	见Action市场。



## 参考资料

* [GitHub Actions 的工作流语法 - GitHub 文档](https://docs.github.com/zh/actions/using-workflows/workflow-syntax-for-github-actions)
* [GitHub Actions 入门教程 - 阮一峰的网络日志 (ruanyifeng.com)](https://www.ruanyifeng.com/blog/2019/09/getting-started-with-github-actions.html)