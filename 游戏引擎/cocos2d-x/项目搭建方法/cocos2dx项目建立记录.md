## cocos2dx项目建立记录

* 1.关于相关的环境配置已经完成，可能会发生部分环境丢失，需要重新配置的情况，详见链接：[(19条消息) cocos2dx学习之路(一)———windows10下cocos2dx环境配置_菩提本无树何处惹尘埃的博客-CSDN博客_cocos电脑配置](https://blog.csdn.net/pengnanzheng/article/details/123137113)

* 2.关于项目搭建：
	* 1.在cocos_workingspace文件夹下先建立一个文件夹，命名为项目名字
	* 2.进入该文件夹，在目录中打开cmd
	* 3.使用命令：cocos new project1 -l cpp -p com.project1.games
	* 注意：命令中的project1改为你的项目名，例如cocos new Hello_World -l cpp -p com.project1.games
	* 4.使用命令：cmake 文件夹名 -G “Visual Studio 16 2019” -AWin32
	* 例如：cmake project1 -G “Visual Studio 16 2019” -AWin32
	* 5.使用vs2019打开项目（注意打开方式）
	* 6.将项目设置为启动项目
	* 7.编译测试是否成功

