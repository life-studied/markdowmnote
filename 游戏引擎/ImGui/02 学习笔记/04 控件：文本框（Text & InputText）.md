# 04 控件：文本框（Text & InputText）

## Text

​	直接使用`ImGui::Text`创建即可

```C++
ImGui::Text(u8"文本");
```

### 修改Text

```C++
std::string text("hello");
// 修改
ImGui::Text(text.c_str());
```



## InputText

​	使用缓冲区接收输入，设置缓存区大小，可以用IM_ARRAYSIZE自动设置。

```C++
char textBox[50] = "Text Box";
InputText("Hello", textBox, IM_ARRAYSIZE(textBox));
```

