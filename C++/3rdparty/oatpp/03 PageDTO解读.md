---
create: 2023-08-26
---
# 03 PageDTO解读

[TOC]

## 解读说明

​	PageDTO是对DTO的拓展，用于生成一页包含多行相同DTO的DTO对象。其中T表示一页中相同的一行DTO。

​	注意，由于PageDTO不会在返回前将内部DTO再进行`Wrapper`序列化，因此在创建的时候每一行DTO都是以`DTO::Wrapper`的类型创建的。并且放入数据也是放入的Wrapper，而非直接DTO。

## 使用方法

​	下面的继承类本质上就是进行了重命名，因此可以直接用`using SamplePageDTO = PageDTO<SampleDTO::Wrapper>;`来代替继承语法。

```C++
#include OATPP_CODEGEN_BEGIN(DTO)

/**
 * 示例传输对象
 */
class SampleDTO : public oatpp::DTO
{
	DTO_INIT(SampleDTO, DTO);
	// 编号
	DTO_FIELD(UInt64, id);
	DTO_FIELD_INFO(id) {
		info->description = ZH_WORDS_GETTER("sample.field.id");
	}
	// 姓名
	DTO_FIELD(String, name);
	DTO_FIELD_INFO(name) {
		info->description = ZH_WORDS_GETTER("sample.field.name");
	}
	// 年龄
	DTO_FIELD(Int32, age);
	DTO_FIELD_INFO(age) {
		info->description = ZH_WORDS_GETTER("sample.field.age");
	}
	// 性别
	DTO_FIELD(String, sex);
	DTO_FIELD_INFO(sex) {
		info->description = ZH_WORDS_GETTER("sample.field.sex");
	}
};

/**
 * 示例分页传输对象
 */
class SamplePageDTO : public PageDTO<SampleDTO::Wrapper> 
{
	DTO_INIT(SamplePageDTO, PageDTO<SampleDTO::Wrapper>);
};

#include OATPP_CODEGEN_END(DTO)
#endif // !_SAMPLE_DTO_
```

## 类名：PageDTO\<T\>

### 字段

- **pageIndex**
  - 类型: UInt64
  - 默认值: 1
  - 必需: 是
  - 描述: 当前页码

- **pageSize**
  - 类型: UInt64
  - 默认值: 5
  - 必需: 是
  - 描述: 每页显示最大数据条数

- **total**
  - 类型: Int64
  - 默认值: 9
  - 必需: 是
  - 描述: 数据的总条数

- **pages**
  - 类型: Int64
  - 默认值: 2
  - 必需: 是
  - 描述: 数据的总页数

- **rows**
  - 类型: `List<T>`
  - 默认值: 空列表
  - 必需: 否
  - 描述: 当前页数据列表

### 构造函数

**PageDTO()**

- 描述：初始化一个空的`PageDTO`对象。
- 参数：无
- 返回值：无

### initAll方法

**void initAll(UInt64 pageIndex, UInt64 pageSize, Int64 total, Int64 pages, List<T> rows)**

- 描述：初始化所有字段的值。
- 参数：
  - `pageIndex` (UInt64): 当前页码。
  - `pageSize` (UInt64): 每页显示最大数据条数。
  - `total` (Int64): 数据的总条数。
  - `pages` (Int64): 数据的总页数。
  - `rows` (List<T>): 当前页数据列表。
- 返回值：无

### calcPages方法

**void calcPages()**

- 描述：根据总条数和每页显示条数计算总页数。
- 参数：无
- 返回值：无

### addData方法

**void addData(T one)**

- 描述：向当前页数据列表中添加一条数据。
- 参数：
  - `one` (T): 要添加的数据。
- 返回值：无

## 文件资料（PageDTO声明）

​	下面资料的主要注意点在于row变量，也就是`List<T>`。

```C++
#pragma once
/*
 Copyright Zero One Star. All rights reserved.

 @Author: awei
 @Date: 2022/10/25 11:20:12

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

	  https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
#ifndef _PAGE_DTO_
#define _PAGE_DTO_
#include "oatpp/core/macro/codegen.hpp"
#include "oatpp/core/Types.hpp"

#include OATPP_CODEGEN_BEGIN(DTO)

/**
 * 分页数据实体
 */
template <class T>
class PageDTO : public oatpp::DTO
{
	// 初始化
	DTO_INIT(PageDTO, DTO);
	/**
	 * 当前页码
	 */
	DTO_FIELD(UInt64, pageIndex) = 1;
	DTO_FIELD_INFO(pageIndex) {
		info->required = true;
#ifndef LINUX
		info->description = u8"当前页码";
#else
		info->description = "page index";
#endif
	}
	/**
	 * 每页显示最大数据条数
	 */
	DTO_FIELD(UInt64, pageSize) = 5;
	DTO_FIELD_INFO(pageSize) {
		info->required = true;
#ifndef LINUX
		info->description = u8"每页数据条数";
#else
		info->description = "page size";
#endif
	}
	/**
	 * 数据的总条数
	 */
	DTO_FIELD(Int64, total) = 9;
	DTO_FIELD_INFO(total) {
		info->required = true;
#ifndef LINUX
		info->description = u8"数据的总条数";
#else
		info->description = "total";
#endif
	}
	/**
	 * 数据的总页数
	 */
	DTO_FIELD(Int64, pages) = 2;
	DTO_FIELD_INFO(pages) {
		info->required = true;
#ifndef LINUX
		info->description = u8"数据的总页数";
#else
		info->description = "pages";
#endif
	}
	/**
	 * 当前页数据列表
	 */
	DTO_FIELD(List<T>, rows) = {};
	DTO_FIELD_INFO(rows) {
#ifndef LINUX
		info->description = u8"当前页数据列表";
#else
		info->description = "page data list";
#endif
	}
public:
	PageDTO() {
		this->total = v_int64(0);
		this->pages = v_int64(0);
	}
	// 初始化所有内容
	void initAll(UInt64 pageIndex, UInt64 pageSize, Int64 total, Int64 pages, List<T> rows) {
		this->pageIndex = pageIndex;
		this->pageSize = pageSize;
		this->total = total;
		this->pages = pages;
		this->rows = rows;
	}
	// 计算总页数
	void calcPages() {
		this->pages = total.getValue(0) / pageSize.getValue(1);
		this->pages = total.getValue(0) % pageSize.getValue(1) == 0 ? this->pages.getValue(0) : this->pages.getValue(0) + 1;
	}
	// 添加一条数据
	void addData(T one) {
		this->rows->push_back(one);
	}
};

#include OATPP_CODEGEN_END(DTO)
#endif // !_PAGE_DTO_

```

