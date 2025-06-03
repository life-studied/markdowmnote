---
create: '2025-06-03'
modified: '2025-06-03'
---

# 命名管道io：前端图形界面与后端算法引擎交互框架

```C++
#include "core.h"
#include <sys/types.h>
#include <sys/wait.h>

int main() {
    int rfd[2];
    int wfd[2];
    
    if(-1 == pipe(rfd)) return -1;
    if(-1 == pipe(wfd)) return -1;
    
    pid_t cpid = fork();
    
    if(cpid < 0) {
        return -1;
    }
    else if(0 == cpid) {
        // 子进程
        close(rfd[0]);
        close(wfd[1]);
        
        dup2(rfd[1], 1);	// 标准输入代替读管道
        dup2(wfd[0], 0);	// 标准输出代替写管道
        
        close(rfd[1]);
        close(wfd[0]);
        
        char* argv[] = {"./algorithm", NULL};
        execve(argv[0], argv, NULL);
        exit(-1);
    }
    else {
        // 父进程
        close(rfd[1]);
        close(wfd[0]);
        
        dup2(rfd[0], 0);	// 标准输入代替读管道
        dup2(wfd[1], 1);	// 标准输出代替写管道
        
        close(rfd[0]);
        close(wfd[1]);
    }
    
    printf("./algorithm data/input.dat -g\n");
    fflush(stdout);
    
    char* buf = NULL;
    long len = 0;
    
    if(getline(&buf, &len, stdin) != -1) {
        printf("%d: %s\n", getpid(), buf);
    }
    
    printf("q\n");
    fflush(stdout);
    
    int status = 0;
    wait(&status);
    return 0;
}
```