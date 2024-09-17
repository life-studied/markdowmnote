# ELF文件头

## readelf -h

可以使用readelf -h查看ELF文件头：

```shell
$ readelf -h Simple.o
ELF Header:
  Magic:   7f 45 4c 46 02 01 01 00 00 00 00 00 00 00 00 00 
  Class:                             ELF64
  Data:                              2's complement, little endian
  Version:                           1 (current)
  OS/ABI:                            UNIX - System V
  ABI Version:                       0
  Type:                              REL (Relocatable file)
  Machine:                           Advanced Micro Devices X86-64
  Version:                           0x1
  Entry point address:               0x0
  Start of program headers:          0 (bytes into file)
  Start of section headers:          1040 (bytes into file)
  Flags:                             0x0
  Size of this header:               64 (bytes)
  Size of program headers:           0 (bytes)
  Number of program headers:         0
  Size of section headers:           64 (bytes)
  Number of section headers:         14
  Section header string table index: 13
```

对应`/usr/include/elf.h`中的结构体：

```C++
#define EI_NIDENT (16)

typedef struct
{
  unsigned char	e_ident[EI_NIDENT];	/* Magic number and other info */
  Elf32_Half	e_type;			/* Object file type */
  Elf32_Half	e_machine;		/* Architecture */
  Elf32_Word	e_version;		/* Object file version */
  Elf32_Addr	e_entry;		/* Entry point virtual address */
  Elf32_Off	e_phoff;		/* Program header table file offset */
  Elf32_Off	e_shoff;		/* Section header table file offset */
  Elf32_Word	e_flags;		/* Processor-specific flags */
  Elf32_Half	e_ehsize;		/* ELF header size in bytes */
  Elf32_Half	e_phentsize;		/* Program header table entry size */
  Elf32_Half	e_phnum;		/* Program header table entry count */
  Elf32_Half	e_shentsize;		/* Section header table entry size */
  Elf32_Half	e_shnum;		/* Section header table entry count */
  Elf32_Half	e_shstrndx;		/* Section header string table index */
} Elf32_Ehdr;

typedef struct
{
  unsigned char	e_ident[EI_NIDENT];	/* Magic number and other info */
  Elf64_Half	e_type;			/* Object file type */
  Elf64_Half	e_machine;		/* Architecture */
  Elf64_Word	e_version;		/* Object file version */
  Elf64_Addr	e_entry;		/* Entry point virtual address */
  Elf64_Off	e_phoff;		/* Program header table file offset */
  Elf64_Off	e_shoff;		/* Section header table file offset */
  Elf64_Word	e_flags;		/* Processor-specific flags */
  Elf64_Half	e_ehsize;		/* ELF header size in bytes */
  Elf64_Half	e_phentsize;		/* Program header table entry size */
  Elf64_Half	e_phnum;		/* Program header table entry count */
  Elf64_Half	e_shentsize;		/* Section header table entry size */
  Elf64_Half	e_shnum;		/* Section header table entry count */
  Elf64_Half	e_shstrndx;		/* Section header string table index */
} Elf64_Ehdr;
```

### 结构体与readelf对照表

| 成员          | readelf 输出结果与含义                                       |
| ------------- | ------------------------------------------------------------ |
| `e_ident`     | Magic: 7f 45 4c 46 01 01 01 00 00 00 00 00 00 00 00 00 <br />Class: ELF32 <br />Data: 2's complement, little endian <br />Version: 1 (current) <br />OS/ABI: UNIX-System V <br />ABI Version: 0 |
| `e_type`      | Type: REL (Relocatable file) ELF文件类型                     |
| `e_machine`   | Machine: Intel 80386 ELF文件的CPU平台属性。相关常量以EM_开头 |
| `e_version`   | Version: 0x1 ELF版本号。一般为常数1                          |
| `e_entry`     | Entry point address: 0x0 入口地址，规定ELF程序的入口虚拟地址，操作系统在加载完该程序后从这个地址开始执行进程的指令。可重定位文件一般没有入口地址，则这个值为0 |
| `e_phoff`     | Start of program headers: 0 (bytes into file) 程序头在文件中的偏移 |
| `e_shoff`     | Start of section headers: 280 (bytes into file) 段表在文件中的偏移 |
| `e_flags`     | Flags: 0x0 ELF标志位，用来标识一些ELF文件平台相关的属性。相关常量的格式一般为EF_machine_flag，machine为平台，flag为标志 |
| `e_ehsize`    | Size of this header: 52 (bytes) ELF文件头本身的大小          |
| `e_phentsize` | Size of program headers: 0 (bytes) 程序头项的大小            |
| `e_phnum`     | Number of program headers: 0 程序头表的个数                  |
| `e_shentsize` | Size of section headers: 40 (bytes) 段表描述符的大小         |
| `e_shnum`     | Number of section headers: 11 段表描述符数量，等于ELF文件中拥有的段的数量 |
| `e_shstrndx`  | Section header string table index: 8 段表字符串表所在的段在段表中的下标 |

### 详细解释

#### ELF魔数`e_ident`

最开始的4个字节是所有ELF文件都必须有的标识码，分别是：

```shell
0x7F	# DEL控制符
0x45 	# E的ascii
0x4c 	# L的ascii
0x46	# F的ascii
```

几乎所有的可执行文件开头都是魔数：

* a.out：`0x01 0x07`
* PE/COFF：`0x4d 0x5a`

---

接下来的字节：

```shell
0x01	# ELF的文件类，01表示32位，02表示64位
0x01 	# 字节序，表示大端还是小端
0x01 	# ELF文件的主版本号，一般是1，因为ELF标准自1.2后就再也没有更新了
```

#### 文件类型`e_type`

这些常量用于在 ELF 文件头的 `e_type` 成员中标识文件的类型。它们帮助操作系统和工具链识别文件应该如何被处理：是作为可执行文件、共享库还是需要进一步链接的对象文件。

| 常量      | 值   | 含义                            |
| --------- | ---- | ------------------------------- |
| `ET_REL`  | 1    | 可重定位文件，一般为 `.o` 文件  |
| `ET_EXEC` | 2    | 可执行文件                      |
| `ET_DYN`  | 3    | 共享目标文件，一般为 `.so` 文件 |

#### 机器类型

机器类型 ELF文件格式被设计成可以在多个平台下使用。

这并**不表示同一个 ELF文件可以在不同的平台下使用**（就像java的字节码文件那样），而是表示**不同平台下的ELF文件都遵循同一套ELF标准**。

| 常量       | 值   | 含义           |
| ---------- | ---- | -------------- |
| `EM_M32`   | 1    | AT&T WE 32100  |
| `EM_SPARC` | 2    | SPARC          |
| `EM_386`   | 3    | Intel x86      |
| `EM_68K`   | 4    | Motorola 68000 |
| `EM_88K`   | 5    | Motorola 88000 |
| `EM_860`   | 6    | Intel 80860    |
