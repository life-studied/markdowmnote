---
create: 2023-11-29
---
# 07 字体（ttf）（ImGuiIO修改）

​	使用ImGuiIO来添加字体。

* 字体文件名
* 字体大小
* nullptr
* 获取全部中文

```C++
ImGuiIO& io = ImGui::GetIO(); (void)io;
io.Fonts->AddFontFromFileTTF("SmileySans-Oblique.ttf", 25, nullptr, io.Fonts->GetGlyphRangesChineseFull());
```

## 字体大小

​	`2560*1440`分辨率下，25大小比较合适。

​	`1920*1440`分辨率下，20大小比较合适。