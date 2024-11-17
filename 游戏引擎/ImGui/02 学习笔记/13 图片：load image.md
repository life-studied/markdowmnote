---
create: 2023-12-03
---
# 图片：load image

​	基于OpenGL3。基于`stb_image.h`头文件。

​	原文`Github`链接：[图像加载和显示示例 ·ocornut/imgui 维基 (github.com)](https://github.com/ocornut/imgui/wiki/Image-Loading-and-Displaying-Examples)

## 1. 思路说明

​	在ImGui中，导入图片的做法是先使用图形API，从硬盘中将图片导入到纹理（`Texture`），再把Texture导入到Image中。

## 2. 导入图片到纹理（封装函数）

```C++
#pragma once
#define STB_IMAGE_IMPLEMENTATION
#include "GL/glew.h"
#include "GLFW/glfw3.h"
#include "stb_image.h"

// Simple helper function to load an image into a OpenGL texture with common settings
bool LoadTextureFromFile(const char* filename, GLuint* out_texture, int* out_width, int* out_height)
{
	// Load from file
	int image_width = 0;
	int image_height = 0;
	unsigned char* image_data = stbi_load(filename, &image_width, &image_height, NULL, 4);
	if (image_data == NULL)
		return false;

	// Create a OpenGL texture identifier
	GLuint image_texture;
	glGenTextures(1, &image_texture);
	glBindTexture(GL_TEXTURE_2D, image_texture);

	// Setup filtering parameters for display
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE); // This is required on WebGL for non power-of-two textures
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE); // Same

	// Upload pixels into texture
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image_width, image_height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image_data);
	stbi_image_free(image_data);

	*out_texture = image_texture;
	*out_width = image_width;
	*out_height = image_height;

	return true;
}
```

## 3. 使用函数

```C++
#include "imgui.h"
#include "TextureLoading.hpp"

int test()
{
    // Load Image
	static int my_image_width = 0;
	static int my_image_height = 0;
	static GLuint my_image_texture = 0;
	static bool ret = LoadTextureFromFile("./circle-IBase.png", &my_image_texture, &my_image_width, &my_image_height);
	if (ret) 
	{ 
		SetCursorPosX(width / 2);
		SetCursorPosY(length / 10);
		Image((void*)(intptr_t)my_image_texture, ImVec2(200, 100));
	}
}
```

