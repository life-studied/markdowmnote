## C/C++在不同平台下的宏定义

> 如何检测不同平台并保证代码的健壮性

#### 1.Windows平台

```C++
#ifdef _WIN32
//define something for Windows (32-bit and 64-bit, this part is common)
#ifdef _WIN64
   //define something for Windows (64-bit only)
#else
   //define something for Windows (32-bit only)
#endif
#endif
```

#### 2.Linux平台

```C++
#ifdef __linux__
//define something for linux
#endif
```

#### 3.其它平台

```C++
#ifdef __APPLE__
#ifdef TARGET_IPHONE_SIMULATOR
// iOS Simulator
#endif
#elif TARGET_OS_IPHONE
// iOS device
#elif TARGET_OS_MAC
// Other kinds of Mac OS
#elif __ANDROID__
// android
#elif __unix__ // all unices not caught above
// Unix
#endif
```

