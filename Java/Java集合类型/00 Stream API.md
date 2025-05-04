---
create: '2025-05-03'
modified: '2025-05-03'
---

# Stream API

几乎所有 Java 标准库中的容器类都提供了 `stream()` 方法，用于将容器转换为一个 `Stream` 对象。

使用Sream API可以有更清晰的语义，同时更不易出错。当然在一定程度下可能会失去灵活性和高性能，需要权衡取舍。

| 方法名称                                                     | 返回值类型      | 描述                                                   | 使用示例                                                     |
| ------------------------------------------------------------ | --------------- | ------------------------------------------------------ | ------------------------------------------------------------ |
| `filter(Predicate<? super T> predicate)`                     | `Stream<T>`     | 过滤流中的元素，返回满足条件的元素组成的流             | `list.stream().filter(x -> x > 5).collect(Collectors.toList());` |
| `map(Function<? super T, ? extends R> mapper)`               | `<R> Stream<R>` | 将流中的每个元素映射为另一个值，返回新的流             | `list.stream().map(x -> x * 2).collect(Collectors.toList());` |
| `flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)` | `<R> Stream<R>` | 将流中的每个元素映射为一个流，然后将这些流合并为一个流 | `list.stream().flatMap(x -> Arrays.stream(x.split(" "))).collect(Collectors.toList());` |
| `distinct()`                                                 | `Stream<T>`     | 去除流中的重复元素                                     | `list.stream().distinct().collect(Collectors.toList());`     |
| `limit(long maxSize)`                                        | `Stream<T>`     | 返回流中的前 `maxSize` 个元素                          | `list.stream().limit(3).collect(Collectors.toList());`       |
| `skip(long n)`                                               | `Stream<T>`     | 跳过流中的前 `n` 个元素                                | `list.stream().skip(2).collect(Collectors.toList());`        |
| `sorted()`                                                   | `Stream<T>`     | 对流中的元素进行自然排序                               | `list.stream().sorted().collect(Collectors.toList());`       |
| `sorted(Comparator<? super T> comparator)`                   | `Stream<T>`     | 对流中的元素进行自定义排序                             | `list.stream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());` |
| `forEach(Consumer<? super T> action)`                        | `void`          | 遍历流中的每个元素                                     | `list.stream().forEach(System.out::println);`                |
| `forEachOrdered(Consumer<? super T> action)`                 | `void`          | 按照流的顺序遍历每个元素                               | `list.stream().forEachOrdered(System.out::println);`         |
| `toArray()`                                                  | `Object[]`      | 将流中的元素转换为数组                                 | `Object[] array = list.stream().toArray();`                  |
| `toArray(IntFunction<A[]> generator)`                        | `<A> A[]`       | 将流中的元素转换为指定类型的数组                       | `String[] array = list.stream().toArray(String[]::new);`     |
| `reduce(BinaryOperator<T> accumulator)`                      | `Optional<T>`   | 对流中的元素进行归并操作                               | `Optional<Integer> sum = list.stream().reduce((a, b) -> a + b);` |
| `reduce(T identity, BinaryOperator<T> accumulator)`          | `T`             | 对流中的元素进行归并操作，提供初始值                   | `int sum = list.stream().reduce(0, (a, b) -> a + b);`        |
| `collect(Collector<? super T, A, R> collector)`              | `<R> R`         | 将流中的元素收集到指定的容器中                         | `List<Integer> result = list.stream().collect(Collectors.toList());` |
| `min(Comparator<? super T> comparator)`                      | `Optional<T>`   | 返回流中的最小值                                       | `Optional<Integer> min = list.stream().min(Integer::compare);` |
| `max(Comparator<? super T> comparator)`                      | `Optional<T>`   | 返回流中的最大值                                       | `Optional<Integer> max = list.stream().max(Integer::compare);` |
| `anyMatch(Predicate<? super T> predicate)`                   | `boolean`       | 检查流中是否有满足条件的元素                           | `boolean anyMatch = list.stream().anyMatch(x -> x > 5);`     |
| `allMatch(Predicate<? super T> predicate)`                   | `boolean`       | 检查流中所有元素是否满足条件                           | `boolean allMatch = list.stream().allMatch(x -> x > 5);`     |
| `noneMatch(Predicate<? super T> predicate)`                  | `boolean`       | 检查流中是否有不满足条件的元素                         | `boolean noneMatch = list.stream().noneMatch(x -> x > 5);`   |
| `findFirst()`                                                | `Optional<T>`   | 返回流中的第一个元素                                   | `Optional<Integer> first = list.stream().findFirst();`       |
| `findAny()`                                                  | `Optional<T>`   | 返回流中的任意一个元素                                 | `Optional<Integer> any = list.stream().findAny();`           |
| `count()`                                                    | `long`          | 返回流中的元素数量                                     | `long count = list.stream().count();`                        |