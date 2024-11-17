---
create: 2023-07-08
---
## PEB和TEB

> 结构体简介

* PEB：进程环境块（Process Environment Block），存放本进程相关的信息
* TEB：线程环境块（Thread Environment Block），存放本线程相关的信息

#### 1.PEB结构体

```C++
typedef struct _PEB {
  BYTE                          Reserved1[2];		//保留字段
  BYTE                          BeingDebugged;		//是否正在被调试
  BYTE                          Reserved2[1];
  PVOID                         Reserved3[2];
  PPEB_LDR_DATA                 Ldr;				//指向所含dll的信息
  PRTL_USER_PROCESS_PARAMETERS  ProcessParameters;
  PVOID                         Reserved4[3];
  PVOID                         AtlThunkSListPtr;
  PVOID                         Reserved5;
  ULONG                         Reserved6;
  PVOID                         Reserved7;
  ULONG                         Reserved8;
  ULONG                         AtlThunkSListPtr32;
  PVOID                         Reserved9[45];
  BYTE                          Reserved10[96];
  PPS_POST_PROCESS_INIT_ROUTINE PostProcessInitRoutine;
  BYTE                          Reserved11[128];
  PVOID                         Reserved12[1];
  ULONG                         SessionId;
} PEB, *PPEB;
```

dll的信息在`PEB`中的`Ldr`中的`InMemoryOrderModuleList`中的链表结构体中。

#### 2.TEB结构体

```C++
typedef struct _TEB {
  PVOID Reserved1[12];
  PPEB  ProcessEnvironmentBlock;
  PVOID Reserved2[399];
  BYTE  Reserved3[1952];
  PVOID TlsSlots[64];
  BYTE  Reserved4[8];
  PVOID Reserved5[26];
  PVOID ReservedForOle;
  PVOID Reserved6[4];
  PVOID TlsExpansionSlots;
} TEB, *PTEB;
```

#### 3.获取PEB

> TEB结构体存在段寄存器中。

```C++
#include <iostream>
#include <Windows.h>
#include <winternl.h>

int main()
{
	PPEB _peb;	
	_asm {
		mov eax,fs:[0x30]		//在段寄存器的0x30处存放了指向peb的指针
		mov _peb, eax			//将该指针赋值给_peb
	}

	std::cout << "进程正在被调试:"<< (int)_peb->BeingDebugged << std::endl;
	std::cout << "基址：" << _peb->Reserved3[1] << std::endl;
	while (1);
}
```

#### 4.遍历获取dll信息

```C++
#include <iostream>
#include <Windows.h>
#include <winternl.h>

//复制_LDR_DATA_TABLE_ENTRY结构体，创建自己的结构体
typedef struct _LDR_DATA_TABLE_ENTRY_SELF {
    PVOID Reserved1[2];
    LIST_ENTRY InMemoryOrderLinks;
    PVOID Reserved2[2];
    PVOID DllBase;
    PVOID EntryPoint;		//原为保留，实为入口点
    PVOID ImageSize;		//原为保留，实为文件大小
    UNICODE_STRING FullDllName;
    BYTE Reserved4[8];
    PVOID Reserved5[3];
#pragma warning(push)
#pragma warning(disable: 4201) // we'll always use the Microsoft compiler
    union {
        ULONG CheckSum;
        PVOID Reserved6;
    } DUMMYUNIONNAME;
#pragma warning(pop)
    ULONG TimeDateStamp;
} LDR_DATA_TABLE_ENTRY_SELF, * PLDR_DATA_TABLE_ENTRY_SELF;


int main()
{
    PPEB _peb;
    _asm {
        mov eax, fs:[0x30];			//通过TEB地址（段寄存器中）的0x30处，获取PEB地址
        mov _peb, eax
    }
    PLDR_DATA_TABLE_ENTRY_SELF pData;
    int FrontDiff = sizeof(pData->Reserved1);	//计算保留字段1的大小
    PPEB_LDR_DATA Pldr = _peb->Ldr;				
    PLIST_ENTRY FIRST = &(Pldr->InMemoryOrderModuleList);	//获取到LIST_ENTRY的地址
    PLIST_ENTRY CURLST = FIRST->Flink;

    
    do
    {
        pData = (PLDR_DATA_TABLE_ENTRY_SELF)((unsigned)CURLST - FrontDiff);	//获取LDR_DATA_TABLE_ENTRY_SELF地址
        std::wcout 
            <<"ImageSize:" << pData->ImageSize 
            <<"\tdllBase:" << pData->DllBase 
            <<"\tFullPath:"<< pData->FullDllName.Buffer 
            << std::endl;
        CURLST = CURLST->Flink;	//指向下一个
    } while (CURLST!=FIRST);

    while (1);
}


```

#### 5.封装为类

```C++
#include <iostream>
#include <Windows.h>
#include <winternl.h>
#include <vector>

class PEBInfo {
private:
    typedef struct _LDR_DATA_TABLE_ENTRY_SELF {
        LIST_ENTRY InLoadOrderModuleList;
        LIST_ENTRY InMemoryOrderLinks;
        LIST_ENTRY InInitializationOrderModuleList;
        PVOID DllBase;
        PVOID EntryPoint;
        PVOID ImageSize;
        UNICODE_STRING FullDllName;
        BYTE Reserved4[8];
        PVOID Reserved5[3];
#pragma warning(push)
#pragma warning(disable: 4201) // we'll always use the Microsoft compiler
        union {
            ULONG CheckSum;
            PVOID Reserved6;
        } DUMMYUNIONNAME;
#pragma warning(pop)
        ULONG TimeDateStamp;
    } LDR_DATA_TABLE_ENTRY_SELF, * PLDR_DATA_TABLE_ENTRY_SELF;

public:
    struct Info
    {
        PVOID DllBase;
        PVOID EntryPoint;
        PVOID ImageSize;
        UNICODE_STRING FullDllName;
        PLIST_ENTRY PLIST;
        bool isHidden;
        Info() : isHidden(false) {}
    };
private:
    
    PLDR_DATA_TABLE_ENTRY_SELF pData;
    int FrontDiff;
    PPEB_LDR_DATA Pldr;
    PLIST_ENTRY FIRST;
    PLIST_ENTRY CURLST;
    std::vector<Info> nowInfo;
    std::vector<Info> LoadInfo;
    std::vector<Info> IniInfo;
public:
    PEBInfo() : pData(nullptr)
    {
        PPEB _peb;
        _asm {
            mov eax, fs: [0x30] ;
            mov _peb, eax
        }
        
        FrontDiff = sizeof(pData->InLoadOrderModuleList);
        Pldr = _peb->Ldr;
        FIRST = &(Pldr->InMemoryOrderModuleList);
        CURLST = FIRST->Flink;
    }

    std::vector<Info> GetInfo()
    {
        std::vector<Info> Allinfo;
        bool setLoadInfo = false;
        if (LoadInfo.size() == 0)
            setLoadInfo = true;
        do
        {
            Info info;
            pData = (PLDR_DATA_TABLE_ENTRY_SELF)((unsigned)CURLST - FrontDiff);
            if (!pData) throw "pData lost!";
            info.DllBase = pData->DllBase;
            info.FullDllName = pData->FullDllName;
            info.EntryPoint = pData->EntryPoint;
            info.ImageSize = pData->ImageSize;
            info.PLIST = &pData->InMemoryOrderLinks;
            Allinfo.push_back(info);
            if (setLoadInfo)
            {
                info.PLIST = &pData->InLoadOrderModuleList;
                LoadInfo.push_back(info);
                info.PLIST = &pData->InInitializationOrderModuleList;
                IniInfo.push_back(info);
            }
            CURLST = CURLST->Flink;
        } while (CURLST != FIRST);
        if(nowInfo.size() == 0) nowInfo = Allinfo;
        return Allinfo;
    }

    bool HidInfo(LPVOID adrBase)
    {
        for (auto& i : nowInfo)
        {
            if (!i.isHidden && i.DllBase == adrBase)
            {
                
                i.PLIST->Blink->Flink = i.PLIST->Flink;
                i.PLIST->Flink->Blink = i.PLIST->Blink;
                i.isHidden = true;
                break;
            }
        }
        for (auto& i : LoadInfo)
        {
            if (!i.isHidden && i.DllBase == adrBase)
            {

                i.PLIST->Blink->Flink = i.PLIST->Flink;
                i.PLIST->Flink->Blink = i.PLIST->Blink;
                i.isHidden = true;
                break;
            }
        }
        for (auto& i : IniInfo)
        {
            if (!i.isHidden && i.DllBase == adrBase)
            {

                i.PLIST->Blink->Flink = i.PLIST->Flink;
                i.PLIST->Flink->Blink = i.PLIST->Blink;
                i.isHidden = true;
                return true;
            }
        }
        return false;
    }

    void RecoverInfo()
    {
        for (auto& i : nowInfo)
        {
            if (i.isHidden)
            {
                i.PLIST->Blink->Flink = i.PLIST;
                i.PLIST->Flink->Blink = i.PLIST;
                i.isHidden = false;
                break;
            }
        }
        for (auto& i : LoadInfo)
        {
            if (i.isHidden)
            {
                i.PLIST->Blink->Flink = i.PLIST;
                i.PLIST->Flink->Blink = i.PLIST;
                i.isHidden = false;
                break;
            }
        }
        for (auto& i : IniInfo)
        {
            if (i.isHidden)
            {
                i.PLIST->Blink->Flink = i.PLIST;
                i.PLIST->Flink->Blink = i.PLIST;
                i.isHidden = false;
                break;
            }
        }
        return;
    }
};
```

#### 6.使用PEBInfo类

```C++
int main()
{
    PEBInfo info;
    std::vector<PEBInfo::Info> Allinfo;
    try {
        Allinfo = info.GetInfo();
    }
    catch (const char* s)
    {
        std::cout << s << std::endl;
        return -1;
    }

    for (auto const & i : Allinfo)
    {
        
        std::wcout
            << "ImageSize:" << i.ImageSize
            << "\tDllBase:" << i.DllBase
            << "\tEntryPoint" << i.EntryPoint
            << "\tFullPath:" << i.FullDllName.Buffer
            << std::endl;
    }

    std::cout << "Hid-----------------------------------------------" << std::endl;
    info.HidInfo(Allinfo[2].DllBase);
    std::wcout << "HidInfo:" << Allinfo[2].FullDllName.Buffer << std::endl;

    PEBInfo info1;
    std::vector<PEBInfo::Info> Allinfo1;
    try {
        Allinfo1 = info1.GetInfo();
    }
    catch (const char* s)
    {
        std::cout << s << std::endl;
        return -1;
    }
    for (auto const& i : Allinfo1)
    {
        std::wcout
            << "ImageSize:" << i.ImageSize
            << "\tDllBase:" << i.DllBase
            << "\tEntryPoint" << i.EntryPoint
            << "\tFullPath:" << i.FullDllName.Buffer
            << std::endl;
    }

    std::cout << "Recover--------------------------------------------" << std::endl;
    info.RecoverInfo();
    PEBInfo info2;
    std::vector<PEBInfo::Info> Allinfo2;
    try {
        Allinfo2 = info2.GetInfo();
    }
    catch (const char* s)
    {
        std::cout << s << std::endl;
        return -1;
    }
    for (auto const& i : Allinfo2)
    {
        std::wcout
            << "ImageSize:" << i.ImageSize
            << "\tDllBase:" << i.DllBase
            << "\tEntryPoint" << i.EntryPoint
            << "\tFullPath:" << i.FullDllName.Buffer
            << std::endl;
    }
    
}

```

