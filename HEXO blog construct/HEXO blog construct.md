## HEXO blog construct

#### 1. 前置工作

##### 1.1 安装git

##### 1.2 安装nodejs

#### 2. 安装HEXO

> 在目标目录下，打开命令行，使用以下命令

```shell
npm install hexo-cli -g
hexo init blog
cd blog
npm install
hexo server		#此时生成了静态网页在端口4000上
```

#### 3. 生成的目录结构

* source目录：存放了md源代码
* theme目录：存放了主题配置
	* 主要是config.yml
* 其它

#### 4. 生成静态网页

> 在对应目录的命令行下使用`hexo g`，创建public目录，里面是md渲染成的静态网页。

#### 5. 选择主题

> 在[Themes | Hexo](https://hexo.io/themes/)中选择对应的主题，进入对应的github仓库，在README.md文档中按要求部署。



