# C语言抽象实现接口

​	实现方法很简单，使用函数指针放在接口结构体中。继承它的结构体（实际上是通过组合）调用这个接口的函数指针。

## 抽象接口

```C
typedef void(*printer_print_fn)(void* printer, const char* str);

struct printer_i {
    printer_print_fn print;
}
```

## 继承（组合）接口

### color_printer

```C
struct color_printer {
    const struct printer_i *interface;
    int enable_color;
    const char *color_command;
    char *buf;
}

void color_printer_print(struct color_printer *self, const char* str);

static const struct printer_i printer_interface {
    .print = (printer_print_fn)color_printer_print;
};

struct color_printer *color_printer_new(const char* color_command) {
    struct color_printer *self;
    self = malloc(sizeof(struct color_printer));
    assert(self != NULL);
    
    self->interface = &printer_interface;
    self->color_command = color_command;
    self->enable_color = 1;
    
    self->buf = malloc(100);
    assert(self->buf != NULL);
    
    return self;
}

void color_printer_cleanup(struct color_printer *self) {
    free(self->buf);
    free(self);
}

void color_printer_print(struct color_printer *self, const char* str) {
    if(self->enable_color) {
        printf("%s%s\033[0m", self->color_command, str);
    } else {
        printf("%s", str);
    }
}

void color_printer_disable_color(struct color_printer *self) {
    self->enable_color = 0;
}

void color_printer_enable_color(struct color_printer *self) {
    self->enable_color = 1;
}
```

### plain_printer

```C
struct plain_printer {
    const struct printer_i *interface;
    const char *prefix;
}

void plain_printer_print(struct plain_printer *self, const char* str);

static const struct printer_i printer_interface {
    .print = (printer_print_fn)plain_printer_print;
};

struct plain_printer *plain_printer_new(const char* prefix) {
    struct plain_printer *self;
    self = malloc(sizeof(struct plain_printer));
    assert(self != NULL);
    
    self->interface = &printer_interface;
    self->prefix = prefix;
    
    return self;
}

void plain_printer_cleanup(struct plain_printer *self) {
    free(self);
}

void plain_printer_print(struct plain_printer *self, const char* str) {
    print("%s%s", self->prefix, str);
}
```

## 使用接口的方式

```C
int main()
{
	struct plain_printer *p1;
    struct plain_printer *p2;
    
    struct color_printer *p3;
    struct color_printer *p4;
    
    struct printer_i **p;
    
    printf("\n\n");
    
    p1 = plain_printer_new(">>>");
    p2 = plain_printer_new("~~~");
    p3 = color_printer_new("\033[31;47m");
    p4 = color_printer_new("\033[30;42m");
    
    p = (struct print_i **)p1;
    (*p)->print(p, "Hello from p1");
    
    p = (struct print_i **)p2;
    (*p)->print(p, "Hello from p2");
    
    p = (struct print_i **)p3;
    (*p)->print(p, "Hello from p3");
    
    p = (struct print_i **)p4;
    (*p)->print(p, "Hello from p4");
    
    printf("\n\n");
    
    return 0;
}
```

