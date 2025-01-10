---
create: 2024-04-27
modified: '2024-11-17'
---

# Optional：可能是None的类型

​	由于某个参数可能是None的情况非常常见，因此提供了Optional的类型标注：

```python
from typing import Optional

def f(x: Optional[int]) -> int:
    if x is None:
        return 0
    return x

f(None)
f(0)
```