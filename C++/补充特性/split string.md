# split string

## 1. 无法识别相邻分割字符（传入型）

```C++
static void split(const string& s, vector<string>& tokens, const string& delimiters = " ")
{
	string::size_type lastPos = s.find_first_not_of(delimiters, 0);
	string::size_type pos = s.find_first_of(delimiters, lastPos);
	while (string::npos != pos || string::npos != lastPos) {
		tokens.push_back(s.substr(lastPos, pos - lastPos));
		lastPos = s.find_first_not_of(delimiters, pos);
		pos = s.find_first_of(delimiters, lastPos);
	}
}
```

## 2. 无法识别相邻分割字符（输出型）

```C++
vector<string> split(const string& s, const string& delimiters = " ")
{
    vector<string> tokens{};
	string::size_type lastPos = s.find_first_not_of(delimiters, 0);
	string::size_type pos = s.find_first_of(delimiters, lastPos);
	while (string::npos != pos || string::npos != lastPos) {
		tokens.push_back(s.substr(lastPos, pos - lastPos));
		lastPos = s.find_first_not_of(delimiters, pos);
		pos = s.find_first_of(delimiters, lastPos);
	}
    return tokens;
}
```

## 3. 可以识别相邻分割字符（包括末尾空字符串）

```C++
std::vector<std::string> split(const std::string& s, const std::string& delimiters = " ")
{
	std::vector<std::string> tokens{};
	if (s.size() == 0) return tokens;
	std::string::size_type lastPos = 0;
	std::string::size_type pos = s.find_first_of(delimiters);
	while (std::string::npos != pos || std::string::npos != lastPos) {
		tokens.push_back(s.substr(lastPos, pos - lastPos));
		lastPos = (pos == std::string::npos ? std::string::npos : pos + 1);
		pos = s.find_first_of(delimiters, lastPos);
	}
	return tokens;
}
```

# split from

```C++
std::vector<std::string> split_from(const std::string& str, const std::vector<std::string>& substrs)
{
    std::vector<std::string> results;
    std::vector<std::string> final_results;
    std::function<void(const std::string& str)> split_from_helper = [&](const std::string& str) {
        if (str.empty())
        {
            final_results = results;
            return;
        }
        if (!final_results.empty())
            return;

        for (auto& i : substrs)
        {
			if (i.empty())
				continue;
            if (str.find(i) == 0)
            {
                results.push_back(i);
                split_from_helper(str.substr(i.size()));
                results.pop_back();
            }
        }
        };
	split_from_helper(str);
    return final_results;
}
```

