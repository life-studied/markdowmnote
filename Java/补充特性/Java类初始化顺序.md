---
create: '2025-06-04'
modified: '2025-06-04'
---

# Java类初始化顺序

1. **父类的构造方法**：首先调用父类的构造方法。
2. **子类的实例初始化块**：按照它们在代码中出现的顺序依次执行。
3. **子类的构造方法体**：最后执行子类的构造方法体中的代码。

```Java
class Parent {
    Parent() {
        System.out.println("Parent Constructor");
    }
}

class Child extends Parent {
    {
        System.out.println("Child Instance Block 1");
    }

    {
        System.out.println("Child Instance Block 2");
    }

    Child() {
        System.out.println("Child Constructor");
    }
}

public class Main {
    public static void main(String[] args) {
        new Child(); // 创建Child对象
    }
}

/* output:
Child Instance Block 1
Child Instance Block 2
Child Constructor
*/
```