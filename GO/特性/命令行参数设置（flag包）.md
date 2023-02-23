## 命令行参数设置（flag包）

#### 案例

```go
package main

import (
	"flag"
	"fmt"
)

var infile *string = flag.String("i", "infile", "File contains values for sorting")
var outfile *string = flag.String("o", "outfile", "File to receive sorted values")
var algorithm *string = flag.String("a", "qsort", "Sort algorithms")

func main() {
	flag.Parse()	//解析命令行参数并给变量赋值

	if infile != nil {
		fmt.Println("infile = ", *infile, "outfile = ", *outfile, "algorithm = ", *algorithm)
	}
}
```

#### 使用

```shell
go build sorter.go	#编译为二进制文件
.\sorter.exe -i unsorted.dat -o sorted.dat -a buttlesort	#使用
```

