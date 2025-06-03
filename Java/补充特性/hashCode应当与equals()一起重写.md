---
create: '2025-06-04'
modified: '2025-06-04'
---

# hashCode应当与equals()一起重写

在Java中，基于哈希的集合（如`HashSet`和`HashMap`）：

* 集合会先调用该对象的`hashCode()`方法，根据哈希码定位对象在哈希表中的位置
* 通过`equals()`方法来判断是否是相同对象

---

如果只重写了`equals()`方法，而没有重写`hashCode()`方法：即使两个对象通过`equals()`方法比较是相等的，但它们的哈希码不同（因为没有重写`hashCode()`），那么它们会被存储在哈希表的不同位置：

- **无法正确存储**：集合可能会认为两个相等的对象是不同的对象，从而将它们都存储下来。
- **无法正确查找**：当尝试查找一个对象时，集合会根据其哈希码定位到错误的位置，即使对象存在，也可能找不到。

```Java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Person)) return false;
        Person other = (Person) obj;
        return Objects.equals(name, other.name) && age == other.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```