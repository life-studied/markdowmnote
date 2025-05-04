---
create: '2025-05-03'
modified: '2025-05-03'
---

# Queue 系列

`Queue` 是一个接口，用于表示一个先进先出（FIFO）的集合。它提供了插入、删除和检查元素的方法，主要用于处理任务队列或消息队列等场景。

## 接口

| 方法名称     | 返回值类型    | 描述                                                         | 使用示例                                        |
| ------------ | ------------- | ------------------------------------------------------------ | ----------------------------------------------- |
| `add(E e)`   | `boolean`     | 将指定元素插入队列，如果队列已满，则抛出 `IllegalStateException`。 | `queue.add("element1");`                        |
| `offer(E e)` | `boolean`     | 将指定元素插入队列，如果队列已满，则返回 `false`。           | `boolean added = queue.offer("element1");`      |
| `remove()`   | `E`           | 检索并移除队列的头元素，如果队列为空，则抛出 `NoSuchElementException`。 | `String element = queue.remove();`              |
| `poll()`     | `E`           | 检索并移除队列的头元素，如果队列为空，则返回 `null`。        | `String element = queue.poll();`                |
| `element()`  | `E`           | 检索队列的头元素，但不移除它，如果队列为空，则抛出 `NoSuchElementException`。 | `String head = queue.element();`                |
| `peek()`     | `E`           | 检索队列的头元素，但不移除它，如果队列为空，则返回 `null`。  | `String head = queue.peek();`                   |
| `isEmpty()`  | `boolean`     | 检查队列是否为空。                                           | `boolean isEmpty = queue.isEmpty();`            |
| `size()`     | `int`         | 返回队列中的元素数量。                                       | `int size = queue.size();`                      |
| `clear()`    | `void`        | 清空队列中的所有元素。                                       | `queue.clear();`                                |
| `iterator()` | `Iterator<E>` | 返回一个迭代器，用于遍历队列中的元素。                       | `Iterator<String> iterator = queue.iterator();` |

## 继承自 Queue 的类

| 类名            | 特点                                                         | 使用场景                                                     | 示例代码                                                     |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `LinkedList`    | 基于双向链表实现，支持在队列头部和尾部进行高效的插入和删除操作。 | 适用于需要频繁插入和删除元素的场景，尤其是需要实现双向队列的场景。 | `Queue<String> queue = new LinkedList<>();`                  |
| `ArrayDeque`    | 基于数组实现的双端队列，支持在队列头部和尾部进行高效的插入和删除操作。 | 适用于需要高性能的队列操作，尤其是需要实现双向队列的场景。   | `Queue<String> queue = new ArrayDeque<>();`                  |
| `PriorityQueue` | 基于优先级堆实现，元素按照自然顺序或指定的比较器顺序排列。每次取出优先级最高的元素。 | 适用于需要根据优先级处理元素的场景，例如任务调度。           | `Queue<String> queue = new PriorityQueue<>();`               |
| `BlockingQueue` | 一个线程安全的队列接口，支持在队列为空或满时阻塞操作。       | 适用于多线程环境中，需要在队列为空或满时进行阻塞的场景。     | `BlockingQueue<String> queue = new LinkedBlockingQueue<>();` |

### 示例代码
以下是一个完整的示例代码，展示如何使用这些实现了 `Queue` 接口的类：

```java
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueExample {
    public static void main(String[] args) {
        // 使用 LinkedList
        Queue<String> linkedListQueue = new LinkedList<>();
        linkedListQueue.add("element1");
        linkedListQueue.add("element2");
        System.out.println("LinkedList Queue: " + linkedListQueue);

        // 使用 ArrayDeque
        Queue<String> arrayDequeQueue = new ArrayDeque<>();
        arrayDequeQueue.add("element1");
        arrayDequeQueue.add("element2");
        System.out.println("ArrayDeque Queue: " + arrayDequeQueue);

        // 使用 PriorityQueue
        Queue<String> priorityQueue = new PriorityQueue<>();
        priorityQueue.add("element1");
        priorityQueue.add("element2");
        System.out.println("PriorityQueue: " + priorityQueue);

        // 使用 BlockingQueue
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        blockingQueue.add("element1");
        blockingQueue.add("element2");
        System.out.println("BlockingQueue: " + blockingQueue);
    }
}
```

### 输出结果
```
LinkedList Queue: [element1, element2]
ArrayDeque Queue: [element1, element2]
PriorityQueue: [element1, element2]
BlockingQueue: [element1, element2]
```