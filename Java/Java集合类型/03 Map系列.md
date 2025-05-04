---
create: '2025-05-03'
modified: '2025-05-03'
---

# Map 系列

`Map` 是一个接口，用于存储键值对（`key-value`）集合，其中键是唯一的，但值可以重复。`Map` 不允许重复的键，如果尝试将一个键映射到两个不同的值，那么原来的映射将被新的映射覆盖。

## 接口

| 方法名称                                                     | 返回值类型            | 描述                                                         | 使用示例                                                     |
| ------------------------------------------------------------ | --------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `put(K key, V value)`                                        | `V`                   | 将指定的值与此映射中的指定键关联。如果键已存在，则返回被覆盖的值。 | `map.put("key1", "value1");`                                 |
| `get(Object key)`                                            | `V`                   | 返回与指定键关联的值，如果映射不包含该键，则返回 `null`。    | `String value = map.get("key1");`                            |
| `remove(Object key)`                                         | `V`                   | 移除与指定键关联的映射（如果存在）。返回被移除的值，如果键不存在，则返回 `null`。 | `map.remove("key1");`                                        |
| `containsKey(Object key)`                                    | `boolean`             | 如果此映射包含指定键的映射，则返回 `true`。                  | `map.containsKey("key1");`                                   |
| `containsValue(Object value)`                                | `boolean`             | 如果此映射将一个或多个键映射到指定值，则返回 `true`。        | `map.containsValue("value1");`                               |
| `isEmpty()`                                                  | `boolean`             | 如果此映射不包含任何键值对，则返回 `true`。                  | `map.isEmpty();`                                             |
| `size()`                                                     | `int`                 | 返回此映射中的键值对数量。                                   | `int size = map.size();`                                     |
| `clear()`                                                    | `void`                | 从此映射中移除所有映射。                                     | `map.clear();`                                               |
| `keySet()`                                                   | `Set<K>`              | 返回此映射中所有键的集合视图。                               | `Set<String> keys = map.keySet();`                           |
| `values()`                                                   | `Collection<V>`       | 返回此映射中所有值的集合视图。                               | `Collection<String> values = map.values();`                  |
| `entrySet()`                                                 | `Set<Map.Entry<K,V>>` | 返回此映射中所有映射的集合视图。                             | `Set<Map.Entry<String, String>> entries = map.entrySet();`   |
| `forEach(BiConsumer<? super K, ? super V> action)`           | `void`                | 对此映射中的每个键值对执行给定的操作，直到所有元素都被处理或操作本身抛出异常。 | `map.forEach((key, value) -> System.out.println(key + ": " + value));` |
| `computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)` | `V`                   | 如果指定的键在映射中不存在，则使用给定的映射函数计算其值，并将其添加到映射中。 | `map.computeIfAbsent("key2", k -> "default value");`         |

## 继承自 Map 的类

| 类名                | 特点                                                         | 使用场景                                                    | 示例代码                                                     |
| ------------------- | ------------------------------------------------------------ | ----------------------------------------------------------- | ------------------------------------------------------------ |
| `HashMap`           | 基于哈希表实现，键值对无序，允许一个键为 `null` 和多个值为 `null`。添加、删除和查找操作的平均时间复杂度为 O(1)。 | 适用于需要快速查找键值对的场景。                            | `Map<String, String> map = new HashMap<>();`                 |
| `LinkedHashMap`     | 基于哈希表和链表实现，元素按照插入顺序或访问顺序排列。       | 适用于需要保持插入顺序或实现最近最少使用（LRU）缓存的场景。 | `Map<String, String> map = new LinkedHashMap<>();`           |
| `TreeMap`           | 基于红黑树实现，键值对按照键的自然顺序或指定的比较器顺序排列。 | 适用于需要对键值对进行排序的场景。                          | `Map<String, String> map = new TreeMap<>();`                 |
| `EnumMap`           | 用于存储枚举类型的键的映射，性能高，内存占用少。             | 适用于键为枚举类型的场景。                                  | `Map<DayOfWeek, String> map = new EnumMap<>(DayOfWeek.class);` |
| `ConcurrentHashMap` | 线程安全的哈希表实现，支持高并发访问。                       | 适用于多线程环境中需要线程安全的映射。                      | `Map<String, String> map = new ConcurrentHashMap<>();`       |

### 示例代码
以下是一个完整的示例代码，展示如何使用这些实现了 `Map` 接口的类：

```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapExample {
    public static void main(String[] args) {
        // 使用 HashMap
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");
        System.out.println("HashMap: " + hashMap);

        // 使用 LinkedHashMap
        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("key1", "value1");
        linkedHashMap.put("key2", "value2");
        System.out.println("LinkedHashMap: " + linkedHashMap);

        // 使用 TreeMap
        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("key1", "value1");
        treeMap.put("key2", "value2");
        System.out.println("TreeMap: " + treeMap);

        // 使用 EnumMap
        Map<DayOfWeek, String> enumMap = new EnumMap<>(DayOfWeek.class);
        enumMap.put(DayOfWeek.MONDAY, "Start of the week");
        enumMap.put(DayOfWeek.FRIDAY, "End of the week");
        System.out.println("EnumMap: " + enumMap);

        // 使用 ConcurrentHashMap
        Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("key1", "value1");
        concurrentHashMap.put("key2", "value2");
        System.out.println("ConcurrentHashMap: " + concurrentHashMap);
    }
}

enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

### 输出结果
```
HashMap: {key1=value1, key2=value2}
LinkedHashMap: {key1=value1, key2=value2}
TreeMap: {key1=value1, key2=value2}
EnumMap: {MONDAY=Start of the week, FRIDAY=End of the week}
ConcurrentHashMap: {key1=value1, key2=value2}
```