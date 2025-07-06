---
create: '2025-07-06'
modified: '2025-07-06'
---

# Index函数

`Index`默认是找到子串的位置。

* `IndexByte`：单个字节c的位置
* `IndexAny`：字符集合中，任一字符所在的位置
* `IndexRune`：单个Unicode码点所在位置
* `IndexFunc`：自定义func(rune)所在位置

```go
func Index(s, sep string) int
func IndexByte(s string, c byte) int
func IndexAny(s, chars string) int
func IndexFunc(s string, f func(rune) bool) int
func IndexRune(s string, r rune) int

// 有三个对应的查找最后一次出现的位置
func LastIndex(s, sep string) int
func LastIndexByte(s string, c byte) int
func LastIndexAny(s, chars string) int
func LastIndexFunc(s string, f func(rune) bool) int
```