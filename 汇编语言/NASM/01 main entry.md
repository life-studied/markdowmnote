---
create: '2025-09-24'
modified: '2025-09-24'
---

# main entry

```assembly
global _start

section .text
_start:
	mov rax, 60
	mov rdi, 0
	syscall
```

* global：定义global变量（可被外部看到）
* section：声明所在段
* _start：默认入口名，同时也是标签
* syscall：系统调用
  * rax：系统调用号，60是exit
  * rdi：函数第一个参数