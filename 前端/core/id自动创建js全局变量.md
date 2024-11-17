---
create: 2024-11-06
---
# id自动创建js全局变量

在HTML中，如果给一个元素设置了`id`属性，那么这个`id`值会自动成为全局变量名（浏览器设置）。

## html

```html
<button id="myButton">点击我</button>
```

这意味着，如果HTML元素的`id`是`myButton`，那么在JavaScript中，你可以直接使用`myButton`来引用这个元素，而不需要使用`document.getElementById`来获取它。

## js

```js
myButton.addEventListener('click', function() {
  alert('按钮被点击了！');
});
```

