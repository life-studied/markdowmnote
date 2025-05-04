---
create: '2025-05-03'
modified: '2025-05-03'
---

# List

在 Java 中，`List` 是一个接口，它继承自 `Collection` 接口，并定义了一系列用于操作有序集合（也称为序列）的方法。

`List` 接口本身并不提供具体实现，而是由具体的实现类（如 `ArrayList`、`LinkedList` 等）来实现这些方法。

## 接口概览

List提供了一套接口规范，对于所有implement它的class来说，直接用这些接口，而不需要了解它们的实现细节。（当然，得明确各个class的底层到底是什么，方便明确使用场景）

| 方法                                                   | 描述                                                         | 使用案例                                              |
| ------------------------------------------------------ | ------------------------------------------------------------ | ----------------------------------------------------- |
| `boolean add(E e)`                                     | 将指定元素添加到列表末尾                                     | `list.add("Java");`                                   |
| `void add(int index, E element)`                       | 在指定位置插入元素                                           | `list.add(1, "Python");`                              |
| `boolean addAll(Collection<? extends E> c)`            | 将指定集合中的所有元素添加到列表末尾                         | `list.addAll(Arrays.asList("C++", "JavaScript"));`    |
| `boolean addAll(int index, Collection<? extends E> c)` | 从指定位置开始插入指定集合中的所有元素                       | `list.addAll(1, Arrays.asList("C++", "JavaScript"));` |
| `E get(int index)`                                     | 返回指定位置的元素                                           | `String element = list.get(1);`                       |
| `E set(int index, E element)`                          | 替换指定位置的元素                                           | `String oldElement = list.set(1, "Go");`              |
| `E remove(int index)`                                  | 移除指定位置的元素                                           | `String removedElement = list.remove(1);`             |
| `boolean remove(Object o)`                             | 移除首次出现的指定元素                                       | `boolean isRemoved = list.remove("Java");`            |
| `int indexOf(Object o)`                                | 返回首次出现的指定元素的索引，未找到返回 `-1`                | `int index = list.indexOf("Python");`                 |
| `int lastIndexOf(Object o)`                            | 返回最后一次出现的指定元素的索引，未找到返回 `-1`            | `int lastIndex = list.lastIndexOf("Python");`         |
| `List<E> subList(int fromIndex, int toIndex)`          | 返回从 `fromIndex` 到 `toIndex` 的子列表（不包含 `toIndex`） | `List<String> sublist = list.subList(1, 3);`          |
| `boolean contains(Object o)`                           | 判断列表是否包含指定元素                                     | `boolean containsJava = list.contains("Java");`       |
| `int size()`                                           | 返回列表的大小                                               | `int size = list.size();`                             |
| `boolean isEmpty()`                                    | 判断列表是否为空                                             | `boolean isEmpty = list.isEmpty();`                   |
| `void clear()`                                         | 清空列表中的所有元素                                         | `list.clear();`                                       |
| `boolean removeAll(Collection<?> c)`                   | 移除列表中所有与指定集合匹配的元素                           | `list.removeAll(Arrays.asList("Java", "Python"));`    |
| `boolean retainAll(Collection<?> c)`                   | 仅保留列表中与指定集合匹配的元素                             | `list.retainAll(Arrays.asList("Java", "Python"));`    |
| `Object[] toArray()`                                   | 将列表转换为数组                                             | `Object[] array = list.toArray();`                    |
| `<T> T[] toArray(T[] a)`                               | 将列表转换为指定类型的数组                                   | `String[] array = list.toArray(new String[0]);`       |

## 继承自List的class

| 类名         | 特点                                                         | 使用场景                                                     | 示例代码                                  |
| ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ----------------------------------------- |
| `ArrayList`  | 基于**动态数组**实现，支持快速随机访问，但插入和删除效率较低。线程不安全。 | 适用于频繁访问元素，且对插入和删除操作要求不高的场景。       | `List<String> list = new ArrayList<>();`  |
| `LinkedList` | 基于**双向链表**实现，支持高效的插入和删除操作，但随机访问效率较低。线程不安全。 | 适用于频繁插入和删除元素的场景，尤其是需要频繁操作首尾元素的场景。 | `List<String> list = new LinkedList<>();` |
| `Vector`     | 基于**动态数组**实现，线程安全，但性能较低。每次扩容时容量会增加一倍。 | 早期 Java 中的线程安全替代品，但现代开发中较少使用，推荐使用 `Collections.synchronizedList` 包装 `ArrayList`。 | `List<String> list = new Vector<>();`     |
| `Stack`      | 继承自 `Vector`，实现了一个后进先出（LIFO）的栈结构。提供了 `push` 和 `pop` 等栈操作方法。 | 适用于需要实现栈操作的场景，如表达式求值、回溯算法等。       | `Stack<String> stack = new Stack<>();`    |