---
create: '2025-10-17'
modified: '2025-10-17'
---

# binder入口（device_initcall）

在`common/drivers/android/binder.c`中，可以看到这样一行代码：

```C
device_initcall(binder_init);
```

在`Linux`内核中，驱动程序通常是用`xxx_initcall(fn)`启动的，这实际上是一个宏定义，被定义在平台对应的`init.h`文件中：

```C++
#define pure_initcall(fn)		__define_initcall(fn, 0)

#define core_initcall(fn)		__define_initcall(fn, 1)
#define core_initcall_sync(fn)		__define_initcall(fn, 1s)
#define postcore_initcall(fn)		__define_initcall(fn, 2)
#define postcore_initcall_sync(fn)	__define_initcall(fn, 2s)
#define arch_initcall(fn)		__define_initcall(fn, 3)
#define arch_initcall_sync(fn)		__define_initcall(fn, 3s)
#define subsys_initcall(fn)		__define_initcall(fn, 4)
#define subsys_initcall_sync(fn)	__define_initcall(fn, 4s)
#define fs_initcall(fn)			__define_initcall(fn, 5)
#define fs_initcall_sync(fn)		__define_initcall(fn, 5s)
#define rootfs_initcall(fn)		__define_initcall(fn, rootfs)
#define device_initcall(fn)		__define_initcall(fn, 6)
#define device_initcall_sync(fn)	__define_initcall(fn, 6s)
#define late_initcall(fn)		__define_initcall(fn, 7)
#define late_initcall_sync(fn)		__define_initcall(fn, 7s)
```

实际上调用的是`__define_initcall()`宏，这个宏的第二个参数表示优先级，数字越小，优先级越高，带s的优先级低于不带s的优先级。

`__define_initcall()`宏的核心是创建了一个函数指针变量指向`fn`函数，该变量被存储到对应的段中`.initcall##id.init`：（同时防止了变量和死代码优化等额外操作）

```C++
// sec的名字
#define __initcall_section(__sec, __iid)			\
	#__sec ".init"

// 函数指针变量
#define ____define_initcall(fn, __unused, __name, __sec)	\
	static initcall_t __name __used 			\
		__attribute__((__section__(__sec))) = fn;

#define __unique_initcall(fn, id, __sec, __iid)			\
	____define_initcall(fn,					\
		__initcall_stub(fn, __iid, id),			\
		__initcall_name(initcall, __iid, id),		\
		__initcall_section(__sec, __iid))

#define ___define_initcall(fn, id, __sec)			\
	__unique_initcall(fn, id, __sec, __initcall_id(fn))

#define __define_initcall(fn, id) ___define_initcall(fn, id, .initcall##id)
```

在Linux内核启动过程中，只需要在上面的段地址处取出函数指针，一个个执行即可。