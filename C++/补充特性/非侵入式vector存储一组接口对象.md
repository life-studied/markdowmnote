---
create: '2025-10-01'
modified: '2025-10-01'
---

# 无继承下vector存储一组接口对象

非侵入式地完成对相同接口对象的封装，从而能通过统一的循环调用。（依然会依赖动态多态，因此不能减少相关方面的开销）

```C++
#include <memory>
#include <iostream>
#include <vector>

struct unit
{
    template <typename T>
    unit(T&& obj) : unit_(std::make_unique<unit_model<T>>(std::forward<T>(obj))) {}

    // common interface
    void attack() { unit_->attack(); }

private:
    struct unit_concept { virtual void attack() = 0; /*...*/ };

    template <typename T>
    struct unit_model : public unit_concept {
        unit_model(T&& unit) : t(unit) {}
        // common interface
        void attack() override { t.attack(); }
    private:
        T t;	// copy
    };

private:
    std::unique_ptr<unit_concept> unit_;
};

class A {
public:
    void attack() { std::cout << "A attack\n";}
};

class B {
public:
    void attack() { std::cout << "B attack\n";}
};

int main() {
    std::vector<unit> v;
    v.emplace_back(A{});
    v.emplace_back(B{});

    for(auto& i : v) {
        i.attack();
    }
}

```