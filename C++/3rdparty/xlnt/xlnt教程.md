---
create: 2023-07-22
---
## xlnt教程

[TOC]

​	关键字：xlnt;workbook;worksheet;cell;

### 一. 读取并修改已有的Excel文件

#### 1. 导入必要的头文件

在你的C++程序中，你需要包含`xlnt`库的头文件。你可以使用以下代码导入必要的头文件：

```cpp
#include <xlnt/xlnt.hpp>
using namespace xlnt;
```

#### 2. 打开和读取Excel文件

在使用`xlnt`库之前，你需要打开一个Excel文件。你可以使用以下代码来打开一个**已存在的Excel文件**：

```cpp
workbook wb;
wb.load("path/to/file.xlsx");
```

#### 3. 读取单元格数据

一旦打开了Excel文件，你就可以读取单元格的值。以下是一个示例代码，演示如何读取工作表中特定单元格的值：

```cpp
worksheet ws = wb.sheet_by_index(0); // 获取第一个工作表		sheet_by_id/sheet_by_title
cell c = ws.cell("A1"); // 获取A1单元格
std::string value = c.to_string(); // 以字符串形式获取单元格的值
```

#### 4. 写入单元格数据

你可以使用`xlnt`库将数据写入Excel文件的单元格中。以下是一个示例代码，演示如何将值写入工作表中的特定单元格：

```cpp
worksheet ws = wb.sheet_by_index(0); // 获取第一个工作表
cell c = ws.cell("A1"); // 获取A1单元格
c.value(42); // 将整数值写入单元格

// 或者，你可以直接使用以下代码来写入单元格：
ws.cell("A1").value(42);
```

#### 5. 保存和关闭Excel文件

完成对Excel文件的读取和写入操作后，记得保存并关闭文件。你可以使用以下代码来保存和关闭文件：

```cpp
wb.save("path/to/save/file.xlsx");
wb.close();
```

#### 6.综合使用

```C++
#include <xlnt/xlnt.hpp>
using namespace xlnt;

int main()
{
    workbook wb;
	wb.load("path/to/file.xlsx");
    
    worksheet ws = wb.sheet_by_index(0); // 获取第一个工作表		sheet_by_id/sheet_by_title
	cell c = ws.cell("A1"); // 获取A1单元格
	std::string value = c.to_string(); // 以字符串形式获取单元格的值
    
    worksheet ws = wb.sheet_by_index(0); // 获取第一个工作表
    cell c = ws.cell("A1"); // 获取A1单元格
    c.value(42); // 将整数值写入单元格

    // 或者，你可以直接使用以下代码来写入单元格：
    ws.cell("A1").value(42);
    
    wb.save("path/to/save/file.xlsx");
	wb.close();
}
```

### 二. 创建一个Excel文件

```C++
#include <xlnt/xlnt.hpp>
using namespace xlnt;

int main()
{
    workbook wb; // 创建Workbook对象
    worksheet ws = wb.active_sheet(); // 获取默认的工作表

    // 设置单元格的值
    ws.cell("A1").value("Hello");
    ws.cell("B1").value(42);
    ws.cell("C1").value(3.14);

    // 保存Excel文件
    wb.save("./test.xlsx");

    return 0;
}

```

