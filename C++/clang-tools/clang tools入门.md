# clang tools入门

​	Clang工具是为C++开发者所设计的单独的命令行（潜在的图形界面）工具集，这些开发者是已经使用Clang并且喜欢使用Clang作为他们的编译器。这些工具内部提供了面向开发的功能：语法检查，自动格式化，重构等。

## clang-format

​	**Clang-format既是一个库，也是一个单独的工具。**

​	它的目标是根据认证的风格指引去自动格式化C++源码文件。

> 为了达到这个目标，clang-format使用Clang的Lexer去把一个输入文件转换为一个token流，然后改变这些token周围的的所有空格。clang-format的这个目标是既要可以作为一个用户工具（理论上拥有强大的IDE集成），同时又要作为其它重构工具的一部分，比如：在重命名的时候去格式化所有改变的行。

## clang-tidy

​	[clang-tidy](http://clang.llvm.org/extra/clang-tidy/)是一个基于clang的静态代码分析框架，支持C++/C/Objective-C。

​	它是一个功能更强大的lint工具。绝大部分lint工具只能在出现问题的代码地方给出提示，之后需要人为修改，而clang-tidy则能够自动修复功能（当然这个如何修复需要该check作者提供）；并且clang-tidy采用模块化设计，非常容易扩展。如果用户想往clang-tidy添加一个新的检测功能，只需要编写一个clang-tidy check实现，每一个check检测一种问题，例如检测某个违反Code style的模式，检测某些API不正确使用的方法等等。

## clang-check

​	clang-check 是 Clang 编译器提供的一个功能，它在编译过程中对代码进行语法和语义检查，但不会进行像 Clang-Tidy 那样的深层次的代码风格或复杂逻辑的检查。Clang Check 主要用于捕获编译时错误，例如语法错误、类型不匹配等，但它不具备 Clang-Tidy 提供的代码改进建议和风格检查功能。

## 参考资料

* [Introduction · Clang Tools入门教程 (hokein.github.io)](https://hokein.github.io/clang-tools-tutorial/)
* [How To Setup Clang Tooling For LLVM — Clang 19.0.0git documentation](https://clang.llvm.org/docs/HowToSetupToolingForLLVM.html)
* [C/C++ 静态代码检测工具 Clang-tidy 简易教程](https://blog.csdn.net/weiwei9363/article/details/135818540)

