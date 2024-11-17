---
create: 2024-09-04
---
# inline命名空间（版本迭代）

## 1. quick start

只要保持接口一致，就能无缝切换版本。

* 对库开发者：调整inline的位置就能随时更新或回退版本
* 对库使用者：无需修改代码，即可享受到新版本的好处。老版本也可以指定`V1`使用。

```C++
namespace A
{
    namespace V1
    {
        void print()
        {
            // ...
        }
    }
    
    inline namespace V2
    {
        void print()
        {
            // ...
        }
    }
}

int main()
{
    A::print();
    A::V1::print();
}
```



1. 命名空间支持inline修饰，使得其就好像在父空间中一样。其实质是在父空间中加了一行`using namespace`。
2. inline必须加在内部namespace第一次出现的地方，后续再次出现可以不加。