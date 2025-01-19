---
create: '2025-01-18'
modified: '2025-01-18'
---

# std::latch与std::barrier

## std::latch

`std::latch`可以视作一个门闩，只有当所有人都到位（也就是计数归零）时，才会释放（wait结束阻塞）。

它是一个一次性的计数对象，计数器为0后就不能重复使用。

* `latch(ptrdiff_t initCount);`构造函数
* `count_down(ptrdiff_t n = 1);`计数减少
* `wait();`阻塞等待计数归零
* `bool try_wait();`检查计数器是否为0（true为0）
* `arrive_and_wait();`计数减少，然后等待

---

下面演示了一个运动员赛跑的使用案例，使用一个std::latch作为发令枪，另一个std::latch作为终点人数计数器：

```c++
#include <iostream>
#include <vector>
#include <latch>
#include <thread>
#include <chrono>
#include <random>

class Runner
{
public:
    Runner(const std::string &name) : name(name) {}

    void run(std::latch &start, std::latch &finish)
    {
        std::mt19937 eng(std::random_device{}());
        std::uniform_int_distribution<unsigned int> dist(0, 1000);
        start.wait();
        auto start_time = std::chrono::system_clock::now();
        std::this_thread::sleep_for(std::chrono::milliseconds(9600 + dist(eng)));
        auto end_time = std::chrono::system_clock::now();
        time = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time);
        finish.count_down();
    }

    bool operator<(const Runner &rhs) const
    {
        return time < rhs.time;
    }
public:
    std::string name;
    std::chrono::milliseconds time{};
};


int main(int argc, char const *argv[])
{
    system("chcp 65001");
    std::vector<Runner> runners = {Runner{"张三"}, Runner{"李四"}, Runner{"王五"}, Runner{"赵六"}, Runner{"孙七"}};
    const int runnerCount = runners.size();
    std::latch start(1);
    std::latch finish(runnerCount);
    std::vector<std::thread> threads;
    for(int i = 0; i < runnerCount; i++)
    {
        threads.emplace_back(&Runner::run, &runners[i], std::ref(start), std::ref(finish));
    }

    std::cout << "比赛开始" << std::endl;
    start.count_down();

    finish.wait();
    std::cout << "比赛结束" << std::endl;
    std::cout << "比赛结果:" << std::endl;
    std::sort(runners.begin(), runners.end());
    for(auto &t : runners)
    {
        std::cout << t.name << ":" << t.time << std::endl;
    }
    for(auto &t : threads)
    {
        t.join();
    }
    return 0;
}
```

## std::barrier

`std::barrier`在每次计数器归零时重置，开始新一轮的计数。可以看作是一个不断翻转的沙漏。

在归零时会调用开始时设置的回调函数。

```C++
template <typename CompletionFunction>
class barrier;
```

* `barrier(ptrdiff_t initCount, func on_complete = {});`构造函数
* `arrive(ptrdiff_t n = 1);`计数减少
* `wait();`阻塞等待计数归零
* `arrive_and_wait(ptrdiff_t n = 1);`计数减少并等待
* `arrive_and_drop(ptrdiff_t n = 1);`计数减少，下一次永久计数也减少