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
vector<string> split(const string& s, const string& delimiters = " ")
{
	vector<string> tokens{};
	if (s.size() == 0) return tokens;
	string::size_type lastPos = 0;
	string::size_type pos = s.find_first_of(delimiters);
	while (string::npos != pos || string::npos != lastPos) {
		tokens.push_back(s.substr(lastPos, pos - lastPos));
		lastPos = (pos == string::npos ? string::npos : pos + 1);
		pos = s.find_first_of(delimiters, lastPos);
	}
	return tokens;
}
```

