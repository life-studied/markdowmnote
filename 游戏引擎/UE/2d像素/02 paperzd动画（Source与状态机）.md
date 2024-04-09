# paperzd动画（Source与状态机）.md

[TOC]

## 1. Animation Source

![image-20240323193755072](./assets/image-20240323193755072.png)

### 添加动画序列

* 添加新的动画序列
* 修改名字
* 选择对应的纸片序列

![image-20240323193905179](./assets/image-20240323193905179.png)

## 2. 动画状态机（PaperZD AnimBP）

​	需要选择从Source创建对应的AnimBP。

![image-20240323194043326](./assets/image-20240323194043326.png)

### 创建状态机

<img src="./assets/image-20240325090621818.png" alt="image-20240325090621818" style="zoom:33%;" />

### 创建state

<img src="./assets/image-20240325090808158.png" alt="image-20240325090808158" style="zoom:33%;" />

### 设置state的动画

<img src="./assets/image-20240325090838590.png" alt="image-20240325090838590" style="zoom:33%;" />

### 创建新的state并设置状态转移条件

<img src="./assets/image-20240325091057354.png" alt="image-20240325091057354" style="zoom:33%;" />

​	在init时获取角色蓝图，方便在后续动画时进行判断：

![image-20240325091337379](./assets/image-20240325091337379.png)

​	获取速度，作为状态变化的条件：

![image-20240325093220791](./assets/image-20240325093220791.png)

### 将animation配置到character上

![image-20240325093335332](./assets/image-20240325093335332.png)

### 设置sprite旋转方向

​	如果需要根据玩家操作，使得角色动画转向不同的方向。可以通过在输入input中判断输入的值，获取角色的sprite对象，设置其z轴的相对旋转方向。

![image-20240325115858459](./assets/image-20240325115858459.png)