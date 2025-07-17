---
create: '2025-07-17'
modified: '2025-07-17'
---

# vscode+clangd+MSVC

## 问题1：MSVC不支持compiler_command.json

由于clangd需要compiler_command.json来辅助寻找头文件路径（如果没有这个文件，clangd会根据.vscode/settings.json中的配置，需要手动给每个引用的头文件路径告诉clangd，很麻烦）。

但是MSVC对compiler_command.json的支持不好，一般不会生成它。

## 解决方案

利用MSVC内置工具链的Ninja来调用cl.exe，以生成compiler_command.json，注意，要在“Developer PowerShell For VS2022”的环境下才能找到MSVC下的Ninja。

```shell
cmake -S . -B build -G "Ninja" -DCMAKE_EXPORT_COMPILE_COMMANDS=ON
```

## 问题2：clangd找不到MSVC内置的win sdk头文件路径

问题：在MSVC下使用vscode的clangd作为解析引擎时，即使让MSVC调用Ninja生成compiler_command.json，clangd可能无法正确解析它。即使正确解析了它，也可能出现找不到MSVC内置的头文件路径的情况（win sdk 和 MSVC 的 include 目录）。

例如，在使用sdl2的时候，clangd正确找到了sdl的include目录，但是找不到sdl引用了win sdk的头文件在哪里（对`#include "SDL.h"`报错）：

```
In included file: 'sal.h' file not foundclang(pp_file_not_found)
SDL_stdinc.h(357, 10): Error occurred here
```

这个sal.h就是win sdk中的一个头文件。

即使编译能成功，clangd依然不能正常找到它。

## 解决方案

经过多方查找，确实没有合适的解决方法去让clangd能找到这个头文件。

因此，在Windows下，如果使用MSVC做cmake的开发，还是老老实实用微软提供的c/cpp扩展去做吧。