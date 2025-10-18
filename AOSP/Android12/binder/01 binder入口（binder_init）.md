---
create: '2025-10-17'
modified: '2025-10-17'
---

# binder入口（binder_init）

通过上文可知，binder通过`device_initcall`将入口函数`binder_init`地址写入到指定的段中。

在`binder_init`中，binder创建了：

* 调试目录`/sys/kernel/debug/`
  * `binder/proc`目录
  * `state`、`state_hashed`、`stats`、`transactions`、`transactions_hashed`、`transaction_log`、`failed_transaction_log`文件
* 注册静态misc设备节点`/dev/binder`, `/dev/hwbinder`, `/dev/vndbinder`
* `init_binderfs`支持后续的用户程序在**挂载 binderfs 后**，自行动态创建binder节点，无需再修改内核代码重新编译

## 附录：binder_init源码

```C
const struct binder_debugfs_entry binder_debugfs_entries[] = {
	{
		.name = "state",
		.mode = 0444,
		.fops = &state_fops,
		.data = NULL,
	},
	{
		.name = "state_hashed",
		.mode = 0444,
		.fops = &state_hashed_fops,
		.data = NULL,
	},
	{
		.name = "stats",
		.mode = 0444,
		.fops = &stats_fops,
		.data = NULL,
	},
	{
		.name = "transactions",
		.mode = 0444,
		.fops = &transactions_fops,
		.data = NULL,
	},
	{
		.name = "transactions_hashed",
		.mode = 0444,
		.fops = &transactions_hashed_fops,
		.data = NULL,
	},
	{
		.name = "transaction_log",
		.mode = 0444,
		.fops = &transaction_log_fops,
		.data = &binder_transaction_log,
	},
	{
		.name = "failed_transaction_log",
		.mode = 0444,
		.fops = &transaction_log_fops,
		.data = &binder_transaction_log_failed,
	},
	{} /* terminator */
};

// binder_devices_param
char *binder_devices_param = CONFIG_ANDROID_BINDER_DEVICES;
module_param_named(devices, binder_devices_param, charp, 0444);

static int __init binder_init(void)
{
    int ret;
    char *device_name, *device_tmp;
    struct binder_device *device;
    struct hlist_node *tmp;
    char *device_names = NULL;
    
	// 初始化binder内存回收
    ret = binder_alloc_shrinker_init();
    if (ret)
        return ret;

    atomic_set(&binder_transaction_log.cur, ~0U);
    atomic_set(&binder_transaction_log_failed.cur, ~0U);
	
    // 在 /sys/kernel/debug/ 中创建 binder 目录
    binder_debugfs_dir_entry_root = debugfs_create_dir("binder", NULL);
    if (binder_debugfs_dir_entry_root) {
        const struct binder_debugfs_entry *db_entry;
		
        // 在 binder 目录下创建 binder_debugfs_entries 中的7个文件：state、state_hashed...
        binder_for_each_debugfs_entry(db_entry)
            debugfs_create_file(db_entry->name,
                                db_entry->mode,
                                binder_debugfs_dir_entry_root,
                                db_entry->data,
                                db_entry->fops);
		
        // 创建 binder/proc 目录
        binder_debugfs_dir_entry_proc = debugfs_create_dir("proc",
                                                           binder_debugfs_dir_entry_root);
    }

    // 动态创建多个 Binder 设备节点（如 /dev/binder, /dev/hwbinder, /dev/vndbinder 等）
    if (!IS_ENABLED(CONFIG_ANDROID_BINDERFS) &&
        strcmp(binder_devices_param, "") != 0) {

        device_names = kstrdup(binder_devices_param, GFP_KERNEL);
        if (!device_names) {
            ret = -ENOMEM;
            goto err_alloc_device_names_failed;
        }

        device_tmp = device_names;
        // 拆分字符串
        while ((device_name = strsep(&device_tmp, ","))) {
            // 对每个 device 进行初始化
            ret = init_binder_device(device_name);
            if (ret)
                goto err_init_binder_device_failed;
        }
    }
	
    // init_binderfs
    ret = init_binderfs();
    if (ret)
        goto err_init_binder_device_failed;

    return ret;

    // 错误处理
    err_init_binder_device_failed:
    hlist_for_each_entry_safe(device, tmp, &binder_devices, hlist) {
        misc_deregister(&device->miscdev);
        hlist_del(&device->hlist);
        kfree(device);
    }

    kfree(device_names);

    err_alloc_device_names_failed:
    debugfs_remove_recursive(binder_debugfs_dir_entry_root);
    binder_alloc_shrinker_exit();

    return ret;
}
```

## 附录：init_binder_device源码

```C
const struct file_operations binder_fops = {
	.owner = THIS_MODULE,
	.poll = binder_poll,
	.unlocked_ioctl = binder_ioctl,
	.compat_ioctl = compat_ptr_ioctl,
	.mmap = binder_mmap,
	.open = binder_open,
	.flush = binder_flush,
	.release = binder_release,
};

static HLIST_HEAD(binder_devices);

static int __init init_binder_device(const char *name)
{
	int ret;
	struct binder_device *binder_device;

	binder_device = kzalloc(sizeof(*binder_device), GFP_KERNEL);
	if (!binder_device)
		return -ENOMEM;
	
    // binder注册虚拟字符设备所对应的 file_operations
	binder_device->miscdev.fops = &binder_fops;
    // 分配 次设备号
	binder_device->miscdev.minor = MISC_DYNAMIC_MINOR;
	binder_device->miscdev.name = name;

	refcount_set(&binder_device->ref, 1);
	binder_device->context.binder_context_mgr_uid = INVALID_UID;
	binder_device->context.name = name;
	mutex_init(&binder_device->context.context_mgr_node_lock);
	
    // 注册 misc 设备
	ret = misc_register(&binder_device->miscdev);
	if (ret < 0) {
		kfree(binder_device);
		return ret;
	}
	
    // 将 binder 设备加入 binder_devices 链表
	hlist_add_head(&binder_device->hlist, &binder_devices);

	return ret;
}
```

## 附录：init_binderfs源码

```C
static dev_t binderfs_dev;

static struct file_system_type binder_fs_type = {
	.name			= "binder",
	.init_fs_context	= binderfs_init_fs_context,
	.parameters		= binderfs_fs_parameters,
	.kill_sb		= binderfs_kill_super,
	.fs_flags		= FS_USERNS_MOUNT,
};

// 创建
int __init init_binderfs(void)
{
	int ret;
	const char *name;
	size_t len;

	/* Verify that the default binderfs device names are valid. */
	name = binder_devices_param;
	for (len = strcspn(name, ","); len > 0; len = strcspn(name, ",")) {
		if (len > BINDERFS_MAX_NAME)
			return -E2BIG;
		name += len;
		if (*name == ',')
			name++;
	}

	// 向内核申请一个 字符设备号范围
    // binderfs_dev: 保存输出的主设备号
    // "binder" 会出现在 /proc/devices 中，供用户空间识别。
	ret = alloc_chrdev_region(&binderfs_dev, 0, BINDERFS_MAX_MINOR,
				  "binder");
	if (ret)
		return ret;
	
    // 注册文件系统
	ret = register_filesystem(&binder_fs_type);
	if (ret) {
		unregister_chrdev_region(binderfs_dev, BINDERFS_MAX_MINOR);
		return ret;
	}

	return ret;
}
```