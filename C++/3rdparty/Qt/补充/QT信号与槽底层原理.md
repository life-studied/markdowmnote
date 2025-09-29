---
create: '2025-09-24'
modified: '2025-09-24'
---

# 信号与槽底层原理

核心就是一个全局的`vector<list<Connection>>`。其中list里存储了槽函数（一个信号可以对应多个槽函数），而vector的下标则代表对应的信号。

```C++
// class Connection {
// void* signal_obj_addr;
// void* slot_obj_addr;
// SignalObj::signal_type* signal;
// SlotObj::slot_type* slot;
// ...
// };

vector<list<Connection>> connections;
```

## connect

每当connect时，都会这样做：

```C++
auto signal_idx = get_signal_idx_by_(xxx_signal);
connections[signal_idx].push_back(Connection{xxx_signal, xxx_slot, 
                                             ... /*其它元信息*/});
```

## emit signal

而emit会被MOC转换成一个函数调用：`this->active(signal_idx);`

然后通过核心的`vector<list<Connection>>`，调用到对应的信号下的槽函数list。

```C++
void active(int signal_idx) {
    for(auto& slot: connections[signal_idx]) {
        slot();
    }
}
```