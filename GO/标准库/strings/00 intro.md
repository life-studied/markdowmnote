---
create: '2025-07-06'
modified: '2025-07-08'
---

# strings包

1. strings包主要包含了对string类型的处理。string类型本身支持各种运算符，例如：+、+=、[]、[:]，因此不需要记忆太多方法。
2. 切片操作不会新建string，而是指向同一个底层数组（因为string不可变的特性）
3. 由于string本身是不可变的，因此不存在修改的说法。只要修改、增加、删除，本质上都是新建了string。
4. strings.Reader结构体提供了从string中读取数据的封装，实现了io.Reader等接口
5. strings.Builder结构体提供了写入数据到strings中的封装，实现了io.Writer等接口。
6. 除了Index之外的其他API都不值得记住。