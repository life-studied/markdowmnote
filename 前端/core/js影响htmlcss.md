# js影响html/css

`getElementById()` 是多个 JavaScript HTML 方法之一。

* `.innerHTML`
* `.属性名`
* `.style.属性名`

## js改变html内容

```js
document.getElementById("demo").innerHTML = "Hello JavaScript";
```

### example

```html
<!DOCTYPE html>
<html>
<body>

<h2>JavaScript 能做什么</h2>

<p id="demo">JavaScript 能够改变 HTML 内容。</p>

<button type="button" onclick='document.getElementById("demo").innerHTML = "Hello JavaScript!"'>点击我！</button>

</body>
</html>
```

## js改变html属性

```js
document.getElementById('myImage').src='/i/eg_bulbon.gif';
```

### example

```html
<!DOCTYPE html>
<html>
<body>

<h2>JavaScript 能做什么？</h2>
<p>JavaScript 能够改变 HTML 属性值。</p>
<p>在本例中，JavaScript 改变了图像的 src 属性值。</p>
<button onclick="document.getElementById('myImage').src='/i/eg_bulbon.gif'">开灯</button>
<img id="myImage" border="0" src="/i/eg_bulboff.gif" style="text-align:center;">
<button onclick="document.getElementById('myImage').src='/i/eg_bulboff.gif'">关灯</button>

</body>
</html>
```

## js改变css

```js
document.getElementById("demo").style.fontSize = "25px";
```

### example

```html
<!DOCTYPE html>
<html>
<body>

<h2>JavaScript 能够做什么</h2>

<p id="demo">JavaScript 能够改变 HTML 元素的样式。</p>

<button type="button" onclick="document.getElementById('demo').style.fontSize='35px'">
点击我！
</button>
    
</body>
</html> 
```

## 参考资料

* [JavaScript 简介](https://www.w3school.com.cn/js/js_intro.asp)