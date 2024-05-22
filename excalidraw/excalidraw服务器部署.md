# excalidraw服务器部署

## 1. 环境安装

* docker
* docker-compose

## 2. 仓库clone

[alswl/excalidraw-collaboration: excalidraw with collaboration feature, self-hosting, and only one-click deploy (github.com)](https://github.com/alswl/excalidraw-collaboration)

```shell
git clone git@github.com:alswl/excalidraw-collaboration.git
```

## 3. docker-compose

```shell
cd excalidraw-collaboration
docker-compose up -d
```

## 4. 其它

* 可以修改docker-compose.yaml文件中的端口，来设置自己服务器开放的端口。
* 协作不能使用，需要配置nginx和https的ssl证书

## 5. 中文手写体插件

* [在 Microsoft Edge 中添加、关闭或删除扩展 - Microsoft 支持](https://support.microsoft.com/zh-cn/microsoft-edge/在-microsoft-edge-中添加-关闭或删除扩展-9c0ec68c-2fbc-2f2c-9ff0-bdc76f46b026#:~:text=将 Chrome Web Store 中的扩展添加到 Microsoft Edge 1,Chrome ”。 4 当系统显示扩展所需权限提示时，仔细查看权限，然后选择“ 添加扩展 ”（如果要继续）。 5 你将看到一条确认扩展已添加的最终提示。)
* [Excalidraw custom font (google.com)](https://chromewebstore.google.com/detail/excalidraw-custom-font/afbeaojffbjckicjpkecknoocdpmgoah)

## 参考资料

* [docker-compose自部署：excalidraw在线手绘白板画图工具_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Dh4y1s7rB/?spm_id_from=333.337.search-card.all.click&vd_source=7ea28e304f19f399517ee153057d1f10)