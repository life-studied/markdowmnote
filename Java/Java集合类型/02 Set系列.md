---
create: '2025-05-03'
modified: '2025-05-03'
---

# Set系列

`Set` 是一个不包含重复元素的集合，没有索引，不能通过索引访问元素。

## 接口

| 方法名称                            | 返回值类型    | 描述                                                         | 使用示例                                            |
| ----------------------------------- | ------------- | ------------------------------------------------------------ | --------------------------------------------------- |
| `add(E e)`                          | `boolean`     | 将指定元素添加到集合中。如果集合中已存在该元素，则不添加并返回 `false`。 | `set.add("Java");`                                  |
| `addAll(Collection<? extends E> c)` | `boolean`     | 将指定集合中的所有元素添加到当前集合中。如果添加了至少一个元素，则返回 `true`。 | `set.addAll(Arrays.asList("Python", "C++"));`       |
| `clear()`                           | `void`        | 清空集合中的所有元素。                                       | `set.clear();`                                      |
| `contains(Object o)`                | `boolean`     | 检查集合中是否包含指定元素。                                 | `set.contains("Java");`                             |
| `containsAll(Collection<?> c)`      | `boolean`     | 检查集合中是否包含指定集合中的所有元素。                     | `set.containsAll(Arrays.asList("Java", "Python"));` |
| `isEmpty()`                         | `boolean`     | 检查集合是否为空。如果集合中没有元素，则返回 `true`。        | `set.isEmpty();`                                    |
| `iterator()`                        | `Iterator<E>` | 返回一个迭代器，用于遍历集合中的元素。                       | `Iterator<String> iterator = set.iterator();`       |
| `remove(Object o)`                  | `boolean`     | 从集合中移除指定元素。如果成功移除，则返回 `true`。          | `set.remove("Java");`                               |
| `removeAll(Collection<?> c)`        | `boolean`     | 从集合中移除指定集合中的所有元素。如果移除了至少一个元素，则返回 `true`。 | `set.removeAll(Arrays.asList("Java", "Python"));`   |
| `retainAll(Collection<?> c)`        | `boolean`     | 仅保留集合中与指定集合匹配的元素。如果集合被修改，则返回 `true`。 | `set.retainAll(Arrays.asList("Java", "Python"));`   |
| `size()`                            | `int`         | 返回集合中的元素数量。                                       | `int size = set.size();`                            |
| `toArray()`                         | `Object[]`    | 将集合中的元素转换为数组。                                   | `Object[] array = set.toArray();`                   |
| `toArray(T[] a)`                    | `<T> T[]`     | 将集合中的元素转换为指定类型的数组。                         | `String[] array = set.toArray(new String[0]);`      |

## 继承自Set的class

| 类名                  | 特点                                                         | 使用场景                                       | 示例代码                                                     |
| --------------------- | ------------------------------------------------------------ | ---------------------------------------------- | ------------------------------------------------------------ |
| `HashSet`             | 基于哈希表实现，元素无序，不允许重复，添加和查找效率高（平均时间复杂度为 O(1)）。 | 适用于需要快速查找且不允许重复元素的场景。     | `Set<String> set = new HashSet<>();`                         |
| `LinkedHashSet`       | 基于哈希表和链表实现，元素按照插入顺序排列，添加和查找效率高（平均时间复杂度为 O(1)）。 | 适用于需要保持元素插入顺序的场景。             | `Set<String> set = new LinkedHashSet<>();`                   |
| `TreeSet`             | 基于红黑树实现，元素自然排序或通过指定的比较器排序，可以进行范围查询。 | 适用于需要对元素进行排序的场景。               | `Set<String> set = new TreeSet<>();`                         |
| `EnumSet`             | 用于存储枚举类型的集合，性能高，内存占用少。                 | 适用于存储枚举类型的场景。                     | `Set<DayOfWeek> set = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);` |
| `CopyOnWriteArraySet` | 线程安全的集合，基于 `CopyOnWriteArrayList` 实现，写操作时会复制整个数组。 | 适用于多线程环境中，读操作远多于写操作的场景。 | `Set<String> set = Collections.newSetFromMap(new ConcurrentHashMap<>());` |