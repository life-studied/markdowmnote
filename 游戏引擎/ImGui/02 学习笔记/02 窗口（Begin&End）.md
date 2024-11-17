---
create: 2023-11-29
---
# 02 窗口（Begin&End）

## Begin

```C++
bool ImGui::Begin(const char* name, bool* p_open, ImGuiWindowFlags flags)
```

参数说明：

* name：窗口名称（中文用`u8`）
* p_open（可选）：写入关闭状态的内存地址
* flags（可选）：窗口样式

## 基本案例

​	`Begin`要和`End`一起使用。

```C++
{
	static bool isOpen = true;
	if (isOpen)
	{
		ImGui::Begin("Test", &isOpen);
		ImGui::End();
	}
}
```

### 窗口样式解读

​	`ImGui`**常用样式**定义在枚举`ImGuiWindowFlags_`中：

```C++
ImGuiWindowFlags_None			= 0,
ImGuiWindowFlags_NoTitleBar		= 1 << 0,	// 禁用标题栏
ImGuiWindowFlags_NoResize		= 1 << 1,	// 禁用右下角用户调整大小
ImGuiWindowFlags_NoMove			= 1 << 2,   // 禁用用户移动窗口
ImGuiWindowFlags_NoCollapse		= 1 << 5,	// 禁用用户折叠窗口
```

​	可以通过**加法**或者**|运算**来进行组合。

### 窗口样式案例

```C++
ImGuiWindowFlags_ wflags{};
auto addflag = [&wflags](ImGuiWindowFlags_ f) { wflags = static_cast<ImGuiWindowFlags_>(wflags | f); };

addflag(ImGuiWindowFlags_NoTitleBar);
addflag(ImGuiWindowFlags_NoResize);
addflag(ImGuiWindowFlags_NoMove);
addflag(ImGuiWindowFlags_NoCollapse);

{
	static bool isOpen = true;
	if (isOpen)
	{
		ImGui::Begin("Test", &isOpen, wflags);
		ImGui::End();
	}
}
```

## 实践思路1（多窗口）

​	创建多窗口，设置一个主窗口作为背景填满整个窗口，其它窗口允许移动。

```C++
ImGuiWindowFlags_ wflags{};
auto addflag = [&wflags](ImGuiWindowFlags_ f) { wflags = static_cast<ImGuiWindowFlags_>(wflags | f); };
addflag(ImGuiWindowFlags_NoDecoration);

{
	static bool isOpen = true;
	if (isOpen)
	{
		ImGui::Begin("Test", &isOpen, wflags);
		ImGui::End();
	}
}
```

## 实践思路2（隐藏背景窗口）

### glfw

​	glfw添加如下设置：

```C++
	glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);  // 设置 背景窗口 透明
```

​	io设置：

```C++
    io.ConfigViewportsNoAutoMerge = true;                       // 防止窗口自动合并上透明窗口
```

### dx

​	修改showwindow第二个参数。

```C++
::ShowWindow(hwnd, SW_HIDE);
```

​	io设置：

```C++
    io.ConfigViewportsNoAutoMerge = true;                       // 防止窗口自动合并上透明窗口
```

### 
