---
create: '2025-12-07'
modified: '2025-12-07'
---

# Windows utf-8设置

```C++
#include <windows.h>

int main() {
    // 设置控制台输入/输出代码页为UTF-8
    SetConsoleOutputCP(CP_UTF8);
    SetConsoleCP(CP_UTF8);
}
```