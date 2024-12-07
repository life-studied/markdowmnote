---
create: '2024-12-05'
modified: '2024-12-05'
---

# defer

```C++
#include <iostream>
#include <algorithm>

template <typename F>
struct defer {
	defer(F f) : _f(f) {}
	~defer() { _f(); }
	F _f;
};

template <typename F>
auto wrap(F f) {
	return [f](auto... args) {
		auto pre = []() { std::cout << "start" << std::endl; };
		auto post = []() { std::cout << "end" << std::endl; };

		pre();

		defer defer_post(post);

		return f(std::forward<decltype(args)>(args)...);
		};
}

void test(int i)
{
	std::cout << i << std::endl;
}

int main()
{
	auto newtest = wrap(test);
	newtest(1);
	return 0;
}
```