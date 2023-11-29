# 06 控件：颜色编辑器（Color Edit）

​	使用`ImVec4`来存储颜色编辑器的结果。

```C++
ImVec4 color{};

// In while loop
ImGui::ColorEdit4("color edit", reinterpret_cast<float*>(&color), ImGuiColorEditFlags_::ImGuiColorEditFlags_AlphaBar);	//绘制颜色编辑器，（可选）附加透明度选项
```

