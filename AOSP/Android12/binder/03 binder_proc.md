---
create: '2025-10-17'
modified: '2025-10-17'
---

# binder_proc

binder_proc 就是 Binder 驱动给“每个打开 `/dev/binder*`的进程”建的主控结构体。
它把 `线程、内存、节点、引用、待处理工作、统计、锁` 等所有内核需要维护的“进程级 Binder 状态”打包在一起，是驱动侧 管理 IPC 的进程账本。

```C
/**
 * struct binder_proc - 驱动为每个打开 /dev/binder* 的进程建立的主控制块
 *
 * binder支持多线程调用，因此需要加锁保护
 * （）里注明了保护该成员的锁。
 */
struct binder_proc {
	/* 全局 binder_procs 链表里的链接节点 */
	struct hlist_node proc_node;

	/* 本进程内所有 binder_thread 的红黑树根（inner_lock 保护）*/
	struct rb_root threads;

	/* 本进程所有 Binder 节点（binder_node）按 ptr 排序的红黑树根（inner_lock）*/
	struct rb_root nodes;

	/* 按 desc 号排序的 binder_ref 红黑树根，用于快速 fd → ref 查找（outer_lock）*/
	struct rb_root refs_by_desc;

	/* 按 node 指针排序的 binder_ref 红黑树根，用于快速 node → ref 反向查找（outer_lock）*/
	struct rb_root refs_by_node;

	/* 当前正在等待“进程级工作”的线程链表（inner_lock）*/
	struct list_head waiting_threads;

	/* 进程组 leader 的 PID（初始化后只读）*/
	int pid;

	/* 进程组 leader 的 task_struct（初始化后只读）*/
	struct task_struct *tsk;

	/* 打开 binder 设备时记录的进程凭据（file->f_cred，初始化后只读）*/
	const struct cred *cred;

	/* 延迟工作全局链表 binder_deferred_list 的链接节点（binder_deferred_lock）*/
	struct hlist_node deferred_work_node;

	/* 位图：有哪些延迟工作需要在本进程上执行（binder_deferred_lock）*/
	int deferred_work;

	/* 在唤醒 freeze_wait 队列之前还需完成的未完成事务计数（inner_lock）*/
	int outstanding_txns;

	/* 进程已死亡，仅等所有事务清理后即可释放（inner_lock）*/
	bool is_dead;

	/* 进程被冻结，不再处理任何 binder 事务（inner_lock）*/
	bool is_frozen;

	/*
	 * 冻结后是否收到过同步事务（bit0）
	 * 冻结过程中是否有新的同步事务等待（bit1）（inner_lock）
	 */
	bool sync_recv;

	/* 冻结后是否收到过异步事务（inner_lock）*/
	bool async_recv;

	/* 等待“所有 outstanding_txns 归零”的等待队列（inner_lock）*/
	wait_queue_head_t freeze_wait;

	/* 描述符位图，管理 ref->desc 的分配与回收（outer_lock）*/
	struct dbitmap dmap;

	/* 发给自己但尚未处理的工作项链表（inner_lock）*/
	struct list_head todo;

	/* 本进程的 binder 统计信息（原子变量，无锁）*/
	struct binder_stats stats;

	/* 已递送但尚未处理的死亡通知链表（inner_lock）*/
	struct list_head delivered_death;

	/* 已递送但尚未处理的冻结通知链表（inner_lock）*/
	struct list_head delivered_freeze;

	/* 允许的最大 binder 线程数（inner_lock）*/
	u32 max_threads;

	/*
	 * 已请求但尚未启动的线程数；当前实现只能是 0 或 1（inner_lock）
	 */
	int requested_threads;

	/* 已实际启动的请求线程数（inner_lock）*/
	int requested_threads_started;

	/* 临时引用计数：表示本 proc 结构正被使用（inner_lock）*/
	int tmp_ref;

	/* 默认调度优先级（初始化后不变）*/
	struct binder_priority default_priority;

	/* debugfs 中 /sys/kernel/debug/binder/proc/<pid> 的 dentry */
	struct dentry *debugfs_entry;

	/* 本进程私有的 binder 内存分配器状态 */
	struct binder_alloc alloc;

	/* 所属 binder 上下文（初始化后不变）*/
	struct binder_context *context;

	/* 内层自旋锁；加锁顺序：outer → node → inner */
	spinlock_t inner_lock;

	/* 外层自旋锁；不允许再嵌套 inner 或 node 锁 */
	spinlock_t outer_lock;

	/* binderfs 挂载点里为本进程生成的 /proc/<pid> 日志文件 dentry */
	struct dentry *binderfs_entry;

	/* 是否开启对 oneway 事务的 spam 检测 */
	bool oneway_spam_detection_enabled;
};
```