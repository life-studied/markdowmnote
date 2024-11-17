---
create: 2023-11-27
---
# std::span

​	C++20引入了`std::span`作为一种语法糖，用于表示连续内存范围。它提供了一种轻量级的、非拥有式的、零开销的方式来引用数组或其他连续内存块。`std::span`可以用于传递数组片段给函数，或者在函数内部对连续内存进行操作，而无需进行内存拷贝。

```C++
#include <iostream>
#include <span>

void printNumbers(std::span<int> numbers) 
{
    for (const int &num : numbers) 
    {
        std::cout << num << " ";
    }
    std::cout << std::endl;
}

int main() 
{
    int arr[] = { 1, 2, 3, 4, 5 };
    printNumbers({arr,3});

    return 0;
}
```

