---
create: '2024-11-24'
modified: '2024-11-24'
---

# tee

用于将输入同时输出到标准输出和文件。相当于一个双通道。

```shell
tee [-a, --append] [FILE]
```

## 1. 查看 | 记录输出

```shell
df -h > a.txt
cat a.txt

# instead
df -h | tee a.txt
```

## 2. 中间日志

```shell
sudo find ~/markdowmnote/ -name "image*" | tee all_image_file.txt | grep "Docker" | tee docker_png.txt
#/home/yunyin/markdowmnote/Docker/assets/image-20230425102844915.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230416193243091.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230416193418059.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230425104402069.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230420122700806.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230425104131859.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230420123250680.png

head -n 5 all_image_file.txt docker_png.txt
#==> all_image_file.txt <==
#/home/yunyin/markdowmnote/Docker/assets/image-20230425102844915.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230416193243091.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230416193418059.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230425104402069.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230420122700806.png
#
#==> docker_png.txt <==
#/home/yunyin/markdowmnote/Docker/assets/image-20230425102844915.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230416193243091.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230416193418059.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230425104402069.png
#/home/yunyin/markdowmnote/Docker/assets/image-20230420122700806.png
```