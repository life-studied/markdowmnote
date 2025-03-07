---
create: '2025-02-27'
modified: '2025-02-27'
---

# Lru+BufferPool

## Lru

```C++
#pragma once
#include <list>
#include <unordered_set>
#include <mutex>
#include <optional>
#include <unordered_map>
#include <tuple>

template <typename Key>
class LRU
{
private:
	void move_pin_to_cache(const Key& key) {
		pin_table_.erase(key);
		auto it = cache_list_.insert(cache_list_.end(), key);
		quick_find_cache_table_[key] = it;
	}

	void move_cache_to_pin(const Key& key) {
		auto it = quick_find_cache_table_.find(key);
		cache_list_.erase(it->second);
		quick_find_cache_table_.erase(it);

		pin_table_.insert(key);
	}
public:

	LRU(size_t capacity) : capacity_(capacity) {}

	~LRU()
	{
		quick_find_cache_table_.clear();
		cache_list_.clear();
		pin_table_.clear();
	}
	/**
	 * @brief 将指定的Key装载入缓存，并进行固定
	 *
	 * @param key 要插入并固定的Key
	 * @return std::tuple<bool, std::optional<Key>>
	 *         - 第一个元素（bool）：表示操作是否成功。
	 *		   - 第二个元素（bool）：表示是否需要直接load（缓存池有空闲且数据不在其中）
	 *         - 第二个元素（std::optional<Key>）：表示是否需要替换Key，则返回替换的Key；否则为空。
	 */
	std::tuple<bool, bool, std::optional<Key>> PutAndPin(const Key& key) {
		auto success_and_need_replace = [](const Key& key) -> std::tuple<bool, bool, std::optional<Key>> {
			return std::make_tuple(true, false, Key(key));
			};

		auto success_and_load = []() -> std::tuple<bool, bool, std::optional<Key>> {
			return std::make_tuple(true, true, std::nullopt);
			};

		auto success_and_direct_use = []() -> std::tuple<bool, bool, std::optional<Key>> {
			return std::make_tuple(true, false, std::nullopt);
			};

		auto fail = []() -> std::tuple<bool, bool, std::optional<Key>> {
			return std::make_tuple(false, false, std::nullopt);
			};

		std::lock_guard<std::mutex> lock(mtx_);

		// 已经在pin_table里，增加pin数
		if (pin_table_.count(key) > 0) {
			pin_table_.insert(key);
			return success_and_direct_use();
		}
		// 已经在cache_list里，转移到pin_table里
		if (auto it = quick_find_cache_table_.find(key); it != quick_find_cache_table_.end()) {
			move_cache_to_pin(key);
			return success_and_direct_use();
		}
		// 都不在，但有空间进行pin
		if (pin_table_.size() + cache_list_.size() < capacity_) {
			pin_table_.insert(key);
			return success_and_load();
		}
		// 都不在，没有空间进行pin，但有空间可以替换后pin
		if (cache_list_.size() > 0) {
			auto replaced_key = cache_list_.back();
			quick_find_cache_table_.erase(replaced_key);
			cache_list_.pop_back();
			pin_table_.insert(key);
			return success_and_need_replace(replaced_key);
		}
		// 没有任何空间，失败
		return fail();
	}

	void UnPin(const Key& key) {
		std::lock_guard<std::mutex> lock(mtx_);

		auto pin_count = pin_table_.count(key);
		if (pin_count == 1) {
			move_pin_to_cache(key);
		}
		if (pin_count > 1) {
			auto it = pin_table_.find(key);
			pin_table_.erase(it);
		}
		if (pin_count == 0) {
			throw std::exception("double unpin");
		}
	}

	
private:
	// capacity = single_pin_count + cache_count
	size_t capacity_;

	std::list<Key> cache_list_;
	std::unordered_map<Key, typename std::list<Key>::iterator> quick_find_cache_table_;

	// pin 记录
	std::unordered_multiset<Key> pin_table_;
	
	std::mutex mtx_;
};
```

## BufferPool

```C++
#pragma once
#include <utility>
#include <mutex>
#include <shared_mutex>
#include <condition_variable>
constexpr int disk_size = 20;
constexpr int buffer_size = disk_size / 5;

class BufferPool {

	using block_type = char[20];
public:
	BufferPool() {
		for (auto i = 0; i < buffer_size; ++i)
			unused_buffer_pool.push_back(i);
	}

	void Write(int disk_idx, char data) {
		int buffer_idx;
		{
			std::shared_lock<std::shared_mutex> lock(disk_map_mutex);
			buffer_idx = disk_map[disk_idx];
		}
		
		auto idx = disk_block_write_idx[disk_idx].fetch_add(1);
		pool[buffer_idx][idx] = data;
		//std::printf("write success:\tthread:%d,\tdisk:%d, idx:%d, data:%c \n", std::this_thread::get_id(), disk_idx, idx, data);
	}

	std::mutex& GetLock(int i) {
		return mtxs[i];
	}

	int GetBufferIdx(int disk_idx) {
		std::shared_lock<std::shared_mutex> lock(disk_map_mutex);
		return disk_map[disk_idx];
	}

	void Replace(int replaced_disk_idx, int disk_idx) {
		//std::printf("buffer replace:\tthread:%d,\t%d replace %d, size:%d\n", std::this_thread::get_id(), disk_idx, replaced_disk_idx, disk_map.size());

		int replaced_buffer_idx;
		{
			std::shared_lock<std::shared_mutex> lock(disk_map_mutex);
			replaced_buffer_idx = disk_map[replaced_disk_idx];
		}
		// 刷脏 换页
		copy_from_buffer_to_disk(replaced_buffer_idx, replaced_disk_idx);
		copy_from_disk_to_buffer(disk_idx, replaced_buffer_idx);

		// 更新 disk_map
		{
			std::unique_lock<std::shared_mutex> lock(disk_map_mutex);
			remove_from_disk_map(replaced_disk_idx);
			save_into_disk_map(disk_idx, replaced_buffer_idx);
		}
	}

	void Load(int disk_idx) {
		auto free_buffer = get_buffer_free();
		load_disk_to_free_buffer(disk_idx, free_buffer.value());
	}

	void CheckLoad(int disk_idx) {
		while (true) {
			// 检查disk是否装载成功
			{
				std::shared_lock<std::shared_mutex> lock(disk_map_mutex);
				auto it = disk_map.find(disk_idx);
				if (it != disk_map.end()) {
					return;
				}
			}

			// 没有找到buffer，说明正在等待其它线程进行replace
			//std::printf("wait replace:\tthread:%d,\tdisk_id:%d\n", std::this_thread::get_id(), disk_idx);
			std::this_thread::yield();
			continue;
			
		}
	}

	void FlushAll() {
		for (auto& i : disk_map) {
			auto buffer_idx = i.second;
			auto disk_idx = i.first;
			copy_from_buffer_to_disk(buffer_idx, disk_idx);
		}
	}

	void Print() {
		for (auto& i : disk) {
			for (auto& j : i) {
				std::cout << j;
			}
			std::cout << std::endl;
		}
	}
private:
	std::optional<int> get_buffer_free() {
		std::optional<int> ret;
		std::lock_guard<std::mutex> lock(unused_buffer_pool_mutex);
		if (unused_buffer_pool.empty()) {
			return std::nullopt;
		}

		ret = unused_buffer_pool.front();
		unused_buffer_pool.pop_front();

		return ret;
	}

	void load_disk_to_free_buffer(int disk_idx, int buffer_idx) {
		//std::printf("buffer load:\tthread:%d,\t%d load into %d,\tsize:%d\n", std::this_thread::get_id(), disk_idx, buffer_idx, disk_map.size());

		std::unique_lock<std::shared_mutex> lock(disk_map_mutex);
		save_into_disk_map(disk_idx, buffer_idx);
	}

	void copy_from_disk_to_buffer(int disk_idx, int buffer_idx) {
		memcpy(pool[buffer_idx], disk[disk_idx], sizeof(block_type));
	}

	void copy_from_buffer_to_disk(int buffer_idx, int disk_idx) {
		memcpy(disk[disk_idx], pool[buffer_idx], sizeof(block_type));
	}

	void save_into_disk_map(int disk_idx, int buffer_idx) {
		disk_map[disk_idx] = buffer_idx;
	}

	void remove_from_disk_map(int old_disk_idx) {
		disk_map.erase(old_disk_idx);
	}
private:
	// 缓存池
	std::array<block_type, buffer_size> pool = {};
	// 每个disk块目前写到哪里了
	std::array<std::atomic<int>, disk_size> disk_block_write_idx = {};
	// 每个缓存块的锁
	std::array<std::mutex, buffer_size> mtxs;
	// 模拟磁盘
	std::array<block_type, disk_size> disk = {};
	// 快速查找：disk idx 被缓存在的 buffer idx
	std::unordered_map<int, int> disk_map;
	// disk_map 保护
	std::shared_mutex disk_map_mutex;
	// disk_map 避免高度竞争，使用cv
	std::condition_variable_any disk_map_cv;
	// 未被使用的buffer
	std::list<int> unused_buffer_pool;
	// unused_buffer_pool 保护
	std::mutex unused_buffer_pool_mutex;

};
```

## main

编写测试：在20个disk里，根据每个disk的编号写入数据。每个线程从任意disk下标开始，循环遍历每个disk并写入一个对应的数据，最终形成20个disk的磁盘数据映像。

```C++
#include <iostream>
#include <array>
#include <random>
#include <cassert>

#include "LRUCache.h"
#include "BufferPool.h"


int main()
{
	BufferPool pool;

	LRU<int> l(buffer_size);

	std::array<std::thread, disk_size> thread_pool;

	
	for (int i = 0; i < disk_size; ++i) {
		thread_pool[i] = std::thread([&, i = i]() {
			std::random_device rd;
			std::mt19937 eng(rd());
			std::uniform_int_distribution<int> gen(0,19);
			int k = gen(eng);
			int j = 0; 
			for (; j < 20; ) {
				auto [success, direct_load, replaced_disk_idx] = l.PutAndPin(k);

				// 没有能用的buffer
				if (!success) {
					std::this_thread::yield();
					continue;
				}
				
				if (direct_load) {
					// 不在pool里，但有空闲buffer，直接加载
					pool.Load(k);
				}
				else if (replaced_disk_idx) {
					// 需要替换buffer的方式，加载
					pool.Replace(replaced_disk_idx.value(), k);
				}
				else {
					// 策略已经放入buffer pool里，但不一定立即生效
					pool.CheckLoad(k);
				}

				int buffer_idx = pool.GetBufferIdx(k);

				// 写入数据
				char data = k % 4 + '1';
				pool.Write(k, data);
				l.UnPin(k);
				++k;
				++j;
				k %= 20;
			}
			});
	}

	for (int i = 0; i < disk_size; ++i) {
		if(thread_pool[i].joinable())
			thread_pool[i].join();
	}

	pool.FlushAll();
	pool.Print();

}
```