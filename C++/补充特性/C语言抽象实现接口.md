---
create: 2024-08-08
---
# C语言抽象实现接口

​	实现方法很简单，使用函数指针放在接口结构体中。继承它的结构体（实际上是通过组合）调用这个接口的函数指针。

## 抽象接口

```C
#pragma once
typedef void(*printer_print_fn)(void* printer, const char* str);

struct printer_i {
	printer_print_fn print;
};
```

## 继承（组合）接口

### color_printer

#### 头文件

```C
#pragma once
#include "printer_i.h"
struct color_printer {
	const struct printer_i* interface;
	int enable_color;
	const char* color_command;
	char* buf;
};

void color_printer_print(struct color_printer* self, const char* str);

struct color_printer* color_printer_new(const char* color_command);

void color_printer_cleanup(struct color_printer* self);

void color_printer_print(struct color_printer* self, const char* str);

void color_printer_disable_color(struct color_printer* self);

void color_printer_enable_color(struct color_printer* self);
```

#### cpp文件

```C
#include "color_printer.h"

#include <stdlib.h>
#include <assert.h>
#include <stdio.h>

static const struct printer_i printer_interface {
	(printer_print_fn)color_printer_print
};

struct color_printer* color_printer_new(const char* color_command)
{
	struct color_printer* self;
	self = (color_printer*)malloc(sizeof(struct color_printer));
	assert(self != NULL);

	self->interface = &printer_interface;
	self->color_command = color_command;
	self->enable_color = 1;

	self->buf = (char*)malloc(100);
	assert(self->buf != NULL);

	return self;
}

void color_printer_cleanup(struct color_printer* self)
{
	free(self->buf);
	free(self);
}

void color_printer_print(struct color_printer* self, const char* str)
{
	if (self->enable_color) {
		printf("%s%s\033[0m\n", self->color_command, str);
	}
	else {
		printf("%s\n", str);
	}
}

void color_printer_disable_color(struct color_printer* self)
{
	self->enable_color = 0;
}

void color_printer_enable_color(struct color_printer* self)
{
	self->enable_color = 1;
}


```

### plain_printer

#### 头文件

```C
#pragma once
#include "printer_i.h"
struct plain_printer {
	const struct printer_i* interface;
	const char* prefix;
};

void plain_printer_print(struct plain_printer* self, const char* str);

struct plain_printer* plain_printer_new(const char* prefix);

void plain_printer_cleanup(struct plain_printer* self);

void plain_printer_print(struct plain_printer* self, const char* str);
```

#### cpp文件

```C
#include "plain_printer.h"

#include <stdlib.h>
#include <assert.h>
#include <stdio.h>

static const struct printer_i printer_interface {
	(printer_print_fn)plain_printer_print
};

struct plain_printer* plain_printer_new(const char* prefix)
{
	struct plain_printer* self;
	self = (plain_printer*)malloc(sizeof(struct plain_printer));
	assert(self != NULL);

	self->interface = &printer_interface;
	self->prefix = prefix;

	return self;
}

void plain_printer_cleanup(struct plain_printer* self)
{
	free(self);
}

void plain_printer_print(struct plain_printer* self, const char* str)
{
	printf("%s%s\n", self->prefix, str);
}

```

## 使用接口的方式

```C
#include "color_printer.h"
#include "plain_printer.h"

int main()
{
	struct plain_printer* p1;
	struct plain_printer* p2;

	struct color_printer* p3;
	struct color_printer* p4;

	struct printer_i** p;

	printf("\n\n");

	p1 = plain_printer_new(">>>");
	p2 = plain_printer_new("~~~");
	p3 = color_printer_new("\033[31;47m");
	p4 = color_printer_new("\033[30;42m");

	p = (struct printer_i**)p1;
	(*p)->print(p, "Hello from p1");

	p = (struct printer_i**)p2;
	(*p)->print(p, "Hello from p2");

	p = (struct printer_i**)p3;
	(*p)->print(p, "Hello from p3");

	p = (struct printer_i**)p4;
	(*p)->print(p, "Hello from p4");

	printf("\n\n");

	return 0;
}
```

