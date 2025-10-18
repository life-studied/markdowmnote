---
create: '2025-10-17'
modified: '2025-10-17'
---

# binder_fops

## binder_fops

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
```

## binder_open

```C
static int binder_open(struct inode *nodp, struct file *filp)
{
	struct binder_proc *proc, *itr;
	struct binder_device *binder_dev;
	struct binderfs_info *info;
	struct dentry *binder_binderfs_dir_entry_proc = NULL;
	bool existing_pid = false;

	binder_debug(BINDER_DEBUG_OPEN_CLOSE, "%s: %d:%d\n", __func__,
		     current->group_leader->pid, current->pid);

	proc = kzalloc(sizeof(*proc), GFP_KERNEL);
	if (proc == NULL)
		return -ENOMEM;

	dbitmap_init(&proc->dmap);
	spin_lock_init(&proc->inner_lock);
	spin_lock_init(&proc->outer_lock);
	get_task_struct(current->group_leader);
	proc->tsk = current->group_leader;
	proc->cred = get_cred(filp->f_cred);
	INIT_LIST_HEAD(&proc->todo);
	init_waitqueue_head(&proc->freeze_wait);
	if (binder_supported_policy(current->policy)) {
		proc->default_priority.sched_policy = current->policy;
		proc->default_priority.prio = current->normal_prio;
	} else {
		proc->default_priority.sched_policy = SCHED_NORMAL;
		proc->default_priority.prio = NICE_TO_PRIO(0);
	}

	/* binderfs stashes devices in i_private */
	if (is_binderfs_device(nodp)) {
		binder_dev = nodp->i_private;
		info = nodp->i_sb->s_fs_info;
		binder_binderfs_dir_entry_proc = info->proc_log_dir;
	} else {
		binder_dev = container_of(filp->private_data,
					  struct binder_device, miscdev);
	}
	refcount_inc(&binder_dev->ref);
	proc->context = &binder_dev->context;
	binder_alloc_init(&proc->alloc);

	binder_stats_created(BINDER_STAT_PROC);
	proc->pid = current->group_leader->pid;
	INIT_LIST_HEAD(&proc->delivered_death);
	INIT_LIST_HEAD(&proc->delivered_freeze);
	INIT_LIST_HEAD(&proc->waiting_threads);
	filp->private_data = proc;

	mutex_lock(&binder_procs_lock);
	hlist_for_each_entry(itr, &binder_procs, proc_node) {
		if (itr->pid == proc->pid) {
			existing_pid = true;
			break;
		}
	}
	hlist_add_head(&proc->proc_node, &binder_procs);
	mutex_unlock(&binder_procs_lock);

	if (binder_debugfs_dir_entry_proc && !existing_pid) {
		char strbuf[11];

		snprintf(strbuf, sizeof(strbuf), "%u", proc->pid);
		/*
		 * proc debug entries are shared between contexts.
		 * Only create for the first PID to avoid debugfs log spamming
		 * The printing code will anyway print all contexts for a given
		 * PID so this is not a problem.
		 */
		proc->debugfs_entry = debugfs_create_file(strbuf, 0444,
			binder_debugfs_dir_entry_proc,
			(void *)(unsigned long)proc->pid,
			&proc_fops);
	}

	if (binder_binderfs_dir_entry_proc && !existing_pid) {
		char strbuf[11];
		struct dentry *binderfs_entry;

		snprintf(strbuf, sizeof(strbuf), "%u", proc->pid);
		/*
		 * Similar to debugfs, the process specific log file is shared
		 * between contexts. Only create for the first PID.
		 * This is ok since same as debugfs, the log file will contain
		 * information on all contexts of a given PID.
		 */
		binderfs_entry = binderfs_create_file(binder_binderfs_dir_entry_proc,
			strbuf, &proc_fops, (void *)(unsigned long)proc->pid);
		if (!IS_ERR(binderfs_entry)) {
			proc->binderfs_entry = binderfs_entry;
		} else {
			int error;

			error = PTR_ERR(binderfs_entry);
			pr_warn("Unable to create file %s in binderfs (error %d)\n",
				strbuf, error);
		}
	}

	return 0;
}
```

## binder_mmap

```C
static int binder_mmap(struct file *filp, struct vm_area_struct *vma)
{
	struct binder_proc *proc = filp->private_data;

	if (proc->tsk != current->group_leader)
		return -EINVAL;

	binder_debug(BINDER_DEBUG_OPEN_CLOSE,
		     "%s: %d %lx-%lx (%ld K) vma %lx pagep %lx\n",
		     __func__, proc->pid, vma->vm_start, vma->vm_end,
		     (vma->vm_end - vma->vm_start) / SZ_1K, vma->vm_flags,
		     (unsigned long)pgprot_val(vma->vm_page_prot));

	if (vma->vm_flags & FORBIDDEN_MMAP_FLAGS) {
		pr_err("%s: %d %lx-%lx %s failed %d\n", __func__,
		       proc->pid, vma->vm_start, vma->vm_end, "bad vm_flags", -EPERM);
		return -EPERM;
	}
	vm_flags_mod(vma, VM_DONTCOPY | VM_MIXEDMAP, VM_MAYWRITE);

	vma->vm_ops = &binder_vm_ops;
	vma->vm_private_data = proc;

	return binder_alloc_mmap_handler(&proc->alloc, vma);
}

```

## binder_ioctl

```C
static long binder_ioctl(struct file *filp, unsigned int cmd, unsigned long arg)
{
	int ret;
	struct binder_proc *proc = filp->private_data;
	struct binder_thread *thread;
	void __user *ubuf = (void __user *)arg;

	trace_binder_ioctl(cmd, arg);

	ret = wait_event_interruptible(binder_user_error_wait, binder_stop_on_user_error < 2);
	if (ret)
		goto err_unlocked;

	thread = binder_get_thread(proc);
	if (thread == NULL) {
		ret = -ENOMEM;
		goto err;
	}

	switch (cmd) {
	case BINDER_WRITE_READ:
		ret = binder_ioctl_write_read(filp, arg, thread);
		if (ret)
			goto err;
		break;
	case BINDER_SET_MAX_THREADS: {
		u32 max_threads;

		if (copy_from_user(&max_threads, ubuf,
				   sizeof(max_threads))) {
			ret = -EINVAL;
			goto err;
		}
		binder_inner_proc_lock(proc);
		proc->max_threads = max_threads;
		binder_inner_proc_unlock(proc);
		break;
	}
	case BINDER_SET_CONTEXT_MGR_EXT: {
		struct flat_binder_object fbo;

		if (copy_from_user(&fbo, ubuf, sizeof(fbo))) {
			ret = -EINVAL;
			goto err;
		}
		ret = binder_ioctl_set_ctx_mgr(filp, &fbo);
		if (ret)
			goto err;
		break;
	}
	case BINDER_SET_CONTEXT_MGR:
		ret = binder_ioctl_set_ctx_mgr(filp, NULL);
		if (ret)
			goto err;
		break;
	case BINDER_THREAD_EXIT:
		binder_debug(BINDER_DEBUG_THREADS, "%d:%d exit\n",
			     proc->pid, thread->pid);
		binder_thread_release(proc, thread);
		thread = NULL;
		break;
	case BINDER_VERSION: {
		struct binder_version __user *ver = ubuf;

		if (put_user(BINDER_CURRENT_PROTOCOL_VERSION,
			     &ver->protocol_version)) {
			ret = -EINVAL;
			goto err;
		}
		break;
	}
	case BINDER_GET_NODE_INFO_FOR_REF: {
		struct binder_node_info_for_ref info;

		if (copy_from_user(&info, ubuf, sizeof(info))) {
			ret = -EFAULT;
			goto err;
		}

		ret = binder_ioctl_get_node_info_for_ref(proc, &info);
		if (ret < 0)
			goto err;

		if (copy_to_user(ubuf, &info, sizeof(info))) {
			ret = -EFAULT;
			goto err;
		}

		break;
	}
	case BINDER_GET_NODE_DEBUG_INFO: {
		struct binder_node_debug_info info;

		if (copy_from_user(&info, ubuf, sizeof(info))) {
			ret = -EFAULT;
			goto err;
		}

		ret = binder_ioctl_get_node_debug_info(proc, &info);
		if (ret < 0)
			goto err;

		if (copy_to_user(ubuf, &info, sizeof(info))) {
			ret = -EFAULT;
			goto err;
		}
		break;
	}
	case BINDER_FREEZE: {
		struct binder_freeze_info info;
		struct binder_proc **target_procs = NULL, *target_proc;
		int target_procs_count = 0, i = 0;

		ret = 0;

		if (copy_from_user(&info, ubuf, sizeof(info))) {
			ret = -EFAULT;
			goto err;
		}

		mutex_lock(&binder_procs_lock);
		hlist_for_each_entry(target_proc, &binder_procs, proc_node) {
			if (target_proc->pid == info.pid)
				target_procs_count++;
		}

		if (target_procs_count == 0) {
			mutex_unlock(&binder_procs_lock);
			ret = -EINVAL;
			goto err;
		}

		target_procs = kcalloc(target_procs_count,
				       sizeof(struct binder_proc *),
				       GFP_KERNEL);

		if (!target_procs) {
			mutex_unlock(&binder_procs_lock);
			ret = -ENOMEM;
			goto err;
		}

		hlist_for_each_entry(target_proc, &binder_procs, proc_node) {
			if (target_proc->pid != info.pid)
				continue;

			binder_inner_proc_lock(target_proc);
			target_proc->tmp_ref++;
			binder_inner_proc_unlock(target_proc);

			target_procs[i++] = target_proc;
		}
		mutex_unlock(&binder_procs_lock);

		for (i = 0; i < target_procs_count; i++) {
			if (ret >= 0)
				ret = binder_ioctl_freeze(&info,
							  target_procs[i]);

			binder_proc_dec_tmpref(target_procs[i]);
		}

		kfree(target_procs);

		if (ret < 0)
			goto err;
		break;
	}
	case BINDER_GET_FROZEN_INFO: {
		struct binder_frozen_status_info info;

		if (copy_from_user(&info, ubuf, sizeof(info))) {
			ret = -EFAULT;
			goto err;
		}

		ret = binder_ioctl_get_freezer_info(&info);
		if (ret < 0)
			goto err;

		if (copy_to_user(ubuf, &info, sizeof(info))) {
			ret = -EFAULT;
			goto err;
		}
		break;
	}
	case BINDER_ENABLE_ONEWAY_SPAM_DETECTION: {
		uint32_t enable;

		if (copy_from_user(&enable, ubuf, sizeof(enable))) {
			ret = -EFAULT;
			goto err;
		}
		binder_inner_proc_lock(proc);
		proc->oneway_spam_detection_enabled = (bool)enable;
		binder_inner_proc_unlock(proc);
		break;
	}
	case BINDER_GET_EXTENDED_ERROR:
		ret = binder_ioctl_get_extended_error(thread, ubuf);
		if (ret < 0)
			goto err;
		break;
	default:
		ret = -EINVAL;
		goto err;
	}
	ret = 0;
err:
	if (thread)
		thread->looper_need_return = false;
	wait_event_interruptible(binder_user_error_wait, binder_stop_on_user_error < 2);
	if (ret && ret != -EINTR)
		pr_info("%d:%d ioctl %x %lx returned %d\n", proc->pid, current->pid, cmd, arg, ret);
err_unlocked:
	trace_binder_ioctl_done(ret);
	return ret;
}
```

## binder_poll

```C
static __poll_t binder_poll(struct file *filp,
				struct poll_table_struct *wait)
{
	struct binder_proc *proc = filp->private_data;
	struct binder_thread *thread = NULL;
	bool wait_for_proc_work;

	thread = binder_get_thread(proc);
	if (!thread)
		return EPOLLERR;

	binder_inner_proc_lock(thread->proc);
	thread->looper |= BINDER_LOOPER_STATE_POLL;
	wait_for_proc_work = binder_available_for_proc_work_ilocked(thread);

	binder_inner_proc_unlock(thread->proc);

	poll_wait(filp, &thread->wait, wait);

	if (binder_has_work(thread, wait_for_proc_work))
		return EPOLLIN;

	return 0;
}
```

## binder_flush

```C
static int binder_flush(struct file *filp, fl_owner_t id)
{
	struct binder_proc *proc = filp->private_data;

	binder_defer_work(proc, BINDER_DEFERRED_FLUSH);

	return 0;
}
```

## binder_release

```C
static int binder_release(struct inode *nodp, struct file *filp)
{
	struct binder_proc *proc = filp->private_data;

	debugfs_remove(proc->debugfs_entry);

	if (proc->binderfs_entry) {
		simple_recursive_removal(proc->binderfs_entry, NULL);
		proc->binderfs_entry = NULL;
	}

	binder_defer_work(proc, BINDER_DEFERRED_RELEASE);

	return 0;
}
```

## compat_ptr_ioctl

```C
long compat_ptr_ioctl(struct file *file, unsigned int cmd, unsigned long arg)
{
	if (!file->f_op->unlocked_ioctl)
		return -ENOIOCTLCMD;

	return file->f_op->unlocked_ioctl(file, cmd, (unsigned long)compat_ptr(arg));
}
```