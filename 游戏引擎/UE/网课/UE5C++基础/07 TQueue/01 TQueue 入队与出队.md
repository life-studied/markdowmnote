# TQueue 入队与出队

## Enqueue

```C++
TQueue<int32> q;
q.Enqueue(1);
q.Enqueue(2);
q.Enqueue(3);
q.Enqueue(4);
```

## Dequeue

```C++
TQueue<int32> q;
q.Enqueue(1);

int32 data;
q.Dequque(data);
```

## Pop

```C++
TQueue<int32> q;
q.Enqueue(1);


q.Pop();
```

## Peek

```C++
TQueue<int32> q;
q.Enqueue(1);

auto* pData = q.Peek();
```



