---
create: 2024-04-24
---
# Actions市场

​	github提供了actions市场，提供给使用者封装好的一系列actions：[GitHub Marketplace · Actions to improve your workflow](https://github.com/marketplace?type=actions)。

​	原理是：uses可以访问其它仓库的.github/workflows/xxx.yaml文件并执行。

## 1. uses语法

​	使用以下语法之一：

- 用于公共和专用存储库中的可重用工作流：`{owner}/{repo}/.github/workflows/{filename}@{ref}`
- 用于同一存储库中的可重用工作流：`./.github/workflows/{filename}`

​	在第一个选项中，`{ref}` 可以是 SHA、发布标记或分支名称。 如果发布标记和分支具有相同的名称，则发布标记优先于分支名称。 出于稳定性和安全性考虑，使用提交 SHA 是最稳妥的选项。

​	如果使用第二个语法选项（不带 `{owner}/{repo}` 和 `@{ref}`），则调用的工作流来自与调用方工作流相同的提交。 不允许使用 `refs/heads` 和 `refs/tags` 等引用前缀。

### uses示例

```yaml
jobs:
  call-workflow-1-in-local-repo:
    uses: octo-org/this-repo/.github/workflows/workflow-1.yml@172239021f7ba04fe7327647b213799853a9eb89
  call-workflow-2-in-local-repo:
    uses: ./.github/workflows/workflow-2.yml
  call-workflow-in-another-repo:
    uses: octo-org/another-repo/.github/workflows/workflow.yml@v1
```

## 2. actions市场

​	github提供了一系列官方的actions使用：

### 2.1 actions/checkout

​	`actions/checkout`是一个github仓库，也是一个action，用于clone该仓库的源码到工作流中。

​	最好使用@以指定版本名字。

```yaml
- uses: actions/checkout@v4
```

### 2.2 peaceiris/actions-gh-pages

​	这是将静态文件部署到 **GitHub Pages** 的 **GitHub 操作**。 此部署操作可以简单自由地与[静态站点生成器](https://jamstack.org/generators/)结合使用。（Hugo、MkDocs、Gatsby、mdBook、Next、Nuxt 等）。

```yaml
- name: Deploy
  uses: peaceiris/actions-gh-pages@v4
  with:
    github_token: ${{ secrets.GITHUB_TOKEN }}
    publish_dir: ./public
```

