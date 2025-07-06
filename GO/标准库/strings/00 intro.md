---
create: '2025-07-06'
modified: '2025-07-06'
---

# strings包

1. strings包主要包含了对string类型的处理。string类型本身支持各种运算符，例如：+、+=、[]、[:]。
2. 由于string本身是不可变的，因此不存在修改的说法，只存在新建的情况。
3. strings.Reader和strings.Builder结构体提供了从string中读取数据，和写入数据到strings中的封装。
4. 除了Index之外的其他API都不值得记住。