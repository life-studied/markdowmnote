---
create: '2025-05-04'
modified: '2025-05-04'
---

# Callable和Future

## 3.1 **`Callable` 与 `Future`**

- **`Callable`**：类似于`Runnable`，但可以返回值。
- **`Future`**：用于获取`Callable`任务的返回值。

```java
import java.util.concurrent.*;

public class CallableExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = () -> "Hello from Callable";
        Future<String> future = executor.submit(task);

        System.out.println("Future result: " + future.get()); // 输出：Hello from Callable
        executor.shutdown();
    }
}
```