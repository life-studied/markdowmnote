---
create: '2024-12-30'
modified: '2024-12-30'
---

# use strict模式

> 长久以来，JavaScript 不断向前发展且并未带来任何兼容性问题。新的特性被加入，旧的功能也没有改变。
> 这么做有利于兼容旧代码，但缺点是 JavaScript 创造者的任何错误或不完善的决定也将永远被保留在 JavaScript 语言中。

这种情况一直持续到 2009 年 ECMAScript 5 (ES5) 的出现。ES5 规范**增加了新的语言特性并且修改了一些已经存在的特性**。为了保证旧的功能能够使用，大部分的修改是默认不生效的。你需要一个特殊的指令 —— `"use strict"` 来明确地激活这些特性。

## "use strict"

```js
"use strict";

// 代码以现代模式工作
...
```

## 函数范围的"use strict"

`"use strict"` 可以被放在函数体的开头。这样则可以只在该函数中启用严格模式。但通常人们会在整个脚本中启用严格模式。

## 自动启用的"use strict"

现代 JavaScript 支持 “class” 和 “module” —— 高级语言结构，它们会自动启用 `use strict`。因此，如果我们使用它们，则无需添加 `"use strict"` 指令。

## 注意事项

* **确保 “use strict” 出现在脚本的最顶部**：否则严格模式可能无法启用。
* 没有类似于 `"no use strict"` 这样的指令可以使程序返回默认模式：一旦进入了严格模式，就没有回头路了。

## 浏览器控制台启用"use strict"

* 大部分浏览器：

  首先，你可以尝试搭配使用 Shift+Enter 按键去输入多行代码，然后将 `use strict` 放在代码最顶部，就像这样：

  ```javascript
  'use strict'; <Shift+Enter 换行>
  //  ...你的代码
  <按下 Enter 以运行>
  ```

* 旧版本浏览器：

  有一种很丑但可靠的启用 `use strict` 的方法。将你的代码放在这样的包装器中：

  ```javascript
  (function() {
    'use strict';
  
    // ...你的代码...
  })()
  ```