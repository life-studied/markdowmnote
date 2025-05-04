---
create: '2025-05-03'
modified: '2025-05-03'
---

# 迭代器与基于range的循环

## 迭代器Iterator

```java
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        // 创建一个集合
        List<String> list = new ArrayList<>();
        list.add("Java");
        list.add("Python");
        list.add("C++");

        // 获取迭代器
        Iterator<String> iterator = list.iterator();
        
        // 使用迭代器遍历集合
        while (iterator.hasNext()) {
            String element = iterator.next();	// update it and return element
            System.out.println(element);

            // 如果需要，可以在这里删除元素
            if ("Python".equals(element)) {
                iterator.remove();
            }
        }
    }
}
```

| 方法名称    | 返回值类型 | 描述                                                         | 使用示例                            |
| ----------- | ---------- | ------------------------------------------------------------ | ----------------------------------- |
| `hasNext()` | `boolean`  | 检查容器中是否还有更多元素。如果有更多元素，返回 `true`；否则返回 `false`。 | `if (iterator.hasNext()) { ... }`   |
| `next()`    | `E`        | 更新迭代器的状态，同时返回容器中的下一个元素。如果容器中没有更多元素，抛出 `NoSuchElementException`。 | `String element = iterator.next();` |
| `remove()`  | `void`     | 从容器中删除最近一次调用 `next()` 返回的元素。如果在调用 `next()` 之前调用 `remove()`，或者在 `next()` 调用之后再次调用 `remove()`，抛出 `IllegalStateException`。 | `iterator.remove();`                |

## 基于range的循环

```java
// 使用增强型 for 循环遍历数组
String[] array = {"Java", "Python", "C++"};
for (String element : array) {
    System.out.println(element);
}
```