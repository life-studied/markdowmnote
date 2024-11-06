# 设置imgui窗口跟随

```C++
#include <windows.h>

// 获取窗口句柄及位置
HWND hwnd = FindWindow(NULL, "Typora");
RECT rect;
GetWindowRect(hwnd, &rect);
auto rect_to_vec2 = ImVec2((rect.left + rect.right) / 2.0f, (rect.top + rect.bottom) / 2.0f);

// 设置窗口位置
ImGui::SetNextWindowPos(rect_to_vec2, ImGuiCond_Always);

// 创建窗口
ImGui::Begin("Window Title");
// 窗口内容...
ImGui::End();
```

