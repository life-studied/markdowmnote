---
create: 2023-07-08
---
## DLLMain

> Windows在加载DLL时，需要一个入口函数来标志。这个入口函数在DLL被调用时，系统自动调用这个函数，作用为：
>
> 1. 接受通告信息：dll由线程或者进程加载、卸载
>
> 2. 初始化dll的状态
>
> 3. 涉及资源的进行正确的资源切换

这个函数在DLL只调用资源时可以是缺省的，系统会自动调用一个没有内容的该函数作为补充。

#### 函数原型

```c++
BOOL APIENTRY DllMain( HANDLE hModule, WORD ul_reason_for_call, LPVOID lpReserved )
```

#### 相关解释

##### 1.声明说明符APIENTRY

其中，`APOENTRY`的定义为`__stdcall`，这表明该函数以标准Pascal的方式被调用（另一种方式是C调用方式，宏定义为`__cdecl`）

> 注：这两种方式的区别在于函数的参数由谁，在何时在栈中删除。另外也涉及到编译时函数名的翻译问题。

```c++
/*
Pascal调用方式：

（1） 参数从左向右依次压入堆栈
（2） 由被调用函数自己来恢复堆栈
（3） 函数名自动加前导下划线，后面紧跟着一个@,其后紧跟着参数的尺寸
*/
//例如：
int __stdcall function(int a,int b);
//被翻译为：_function@8
```

```c++
/*
C调用方式：(隐式使用)

（1） 参数从右向左依次压入堆栈
（2） 由调用者恢复堆栈
（3） 函数名自动加前导下划线
*/
//例如：
int __cdecl function(int a,int b);
//被翻译为：_function
```

##### 2.参数

* **hModule**：是进程中的每个DLL模块被全局唯一的32字节的HINSTANCE句柄标识，用于代表DLL模块在进程虚拟空间中的起始地址。
* **ul_reason_for_call**：指明调用原因。宏定义了四种数字作为调用原因。
	* 首先，明确dll在某个进程调用LoadLibrary时才会被加载到这个进程的地址空间中。其次，如果这个模块没用了，必须使用FreeLibrary来
	* **DLL_PROCESS_ATTACH**：如果该DLL是这个进程没有调用过的，则系统采用这个参数来调用DLLMain。
		* 注意：如果DllMain返回TRUE，则LoadLibrary返回这个模块的句柄。如果DllMain返回FALSE，则系统从进程的地址空间中卸载DLL，并且LoadLibrary返回NULL。并且下次调用依旧采用该参数调用DllMain。
	* **DLL_PROCESS_DETACH**：当DLL被从进程的地址空间解除映射时，系统调用了它的DllMain，传递的ul_reason_for_call值是DLL_PROCESS_DETACH。
	* **DLL_THREAD_ATTACH**：当进程创建一线程时，系统查看当前映射到进程地址空间中的所有DLL文件映像，并用值DLL_THREAD_ATTACH调用DLL的DllMain函数。新创建的线程负责执行这次的DLL的DllMain函数，只有当所有的DLL都处理完这一通知后，系统才允许线程开始执行它的线程函数。
	* **DLL_THREAD_DETACH**：如果线程调用了ExitThread来结束线程（线程函数返回时，系统也会自动调用ExitThread），系统查看当前映射到进程空间中的所有DLL文件映像，并用DLL_THREAD_DETACH来调用DllMain函数，通知所有的DLL去执行线程级的清理工作。
* **lpReserved**：表示一个保留参数，目前已经很少使用。