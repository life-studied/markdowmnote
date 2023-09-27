# 05 FString路径操作

## 5.1 添加路径

```C++
FString path = FPaths::ProjectDir();		//get relative path for project
FString fullPath  = FPaths::ConvertRelativePathToFull();	//to full path

FString exePath = "Co/hello.exe";
FString finalPath = fullPath + TEXT("/") + exePath;		//hard

FString finalPath = fullPath / exePath;	//easy
//or
fullPath.PathAppend(*exePath, exePath.Len());
```

## 5.2 单位化路径

```C++
FString path = TEXT("F:\\PJ\\hh\\Saved/d/c.exe");		//not stardard
FPaths::NormalizeFilename(path);
```

