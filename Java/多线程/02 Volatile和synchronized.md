---
create: '2025-05-04'
modified: '2025-05-04'
---

# Volatile和synchronized

## 2.1 **`volatile` 关键字**

- **作用**：确保变量的读写操作对所有线程可见，禁止指令重排序。
- **适用场景**：适用于单个变量的读写操作，但不能保证复合操作的原子性。

```java
public class VolatileExample {
    private volatile boolean running = true;

    public void stop() {
        running = false;
    }

    public void run() {
        while (running) {
            System.out.println("Thread is running");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileExample example = new VolatileExample();
        Thread thread = new Thread(example::run);
        thread.start();

        Thread.sleep(1000); // 等待1秒
        example.stop();
    }
}
```

## 2.2 **`synchronized` 关键字**

- **作用**：通过锁机制保证同一时间只有一个线程可以执行同步代码。

- **使用方式**：

    - 同步方法：

        ```java
        public class SynchronizedExample {
            public synchronized void synchronizedMethod() {
                System.out.println("Synchronized method is running");
            }
        }
        ```

    - 同步代码块：

        ```java
        public class SynchronizedExample {
            private final Object lock = new Object();
        
            public void synchronizedBlock() {
                synchronized (lock) {
                    System.out.println("Synchronized block is running");
                }
            }
        }
        ```

- **注意**：

    - `synchronized` 锁的是对象，而不是代码。
    - 同步静态方法时，锁的是类的`Class`对象。