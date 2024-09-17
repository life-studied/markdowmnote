# memory_order_seq_cst

`std::memory_order_seq_cst`表示最强约束。

所有关于`std::atomic`的使用，如果不带函数。比如`x.store or x.load`，而是`std::atomic<int> a; a = 1`这样，那么就是强一制性的。即在这条语句的时候 **所有这条指令前面的语句不能放到后面，所有这条语句后面的语句不能放到前面来执行**。

