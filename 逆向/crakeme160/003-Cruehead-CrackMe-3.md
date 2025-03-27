---
create: '2025-03-27'
modified: '2025-03-27'
---

# 003-Cruehead-CrackMe-3

## 注册机

```C++
#include <iostream>
#include <stdint.h>
#include <fstream>
#include <string>

void keygen(char* str) {
	char data[19] = {};
	memcpy(data, str, 18);
	int32_t add_place = 0;

	char ch = 'A';
	// 前14个字符进行xor，计算出校验码
	for (int i = 0; i < 14; ++i, ++ch) {
		str[i] ^= ch;
		add_place += str[i];
		if (str[i] == 0) break;
	}
	add_place ^= 0x12345678;

	// 前14是key，后4是校验
	*(int32_t*)&data[14] = add_place;

	// 写入到注册文件名
	std::ofstream ofs("CRACKME3.KEY", std::ios::binary);
	ofs << data;
}

int main()
{
	std::cout << "please input the words:";
	std::string line;
	std::getline(std::cin, line);

	// 取14个，多退少补
	while (line.size() < 14) {
		line += '!';
	}
	char str[19] = {};
	memcpy(str, line.c_str(), 14);

	// 生成key
	keygen(str);
	return 0;
}
```