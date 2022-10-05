## 学python第一天的笔记

#### 1.hello python

> 第一课，安装python，由于很早之前就安装过pycharm，之间跳过，非常的nice

```python
print("hello python")
```

> python的hello world，也可以这么写（在python里单引号和双引号一样能表示字符串）

```python 
print('hello python')
```

#### 2.字符串

> 第二节主要讲的字符串的常用方法

```python
name = "ada lovelace"
print(name.title())		#首字母大写的方法
```

```python
first_name = "ada"
last_name = "lovelace"
full_name = f"{first_name} {last_name}"		#从python3.6开始使用的格式化字符串方法，具体就是将变量放置在括号中，前置f表示格式化字符串
print(f"hello, {full_name.title()}")
```

> 这是一份练习

```python
name1 = "Eric"
print(f"Hello {name1}, would you like to learn some Python today?")
```

> 打印一个名字的大写，小写与首字符大写

```python
name = "jay"
print(name.title())
print(name.lower())
print(name.upper())
```

