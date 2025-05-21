---
create: '2025-03-28'
modified: '2025-05-20'
---

# libiconv封装

```C++
#include <iconv.h>
#include <string>
#include <stdexcept>
#include <cstdint>
#include <sstream>
#include <iostream>

namespace iconv_space {

	class iconv_exception_base :public std::runtime_error {
	public:
		explicit iconv_exception_base(const char* _message) : std::runtime_error(_message) {}
		explicit iconv_exception_base(const std::string& _message) : std::runtime_error(_message.c_str()) {}
	};

	class iconv_invalid_code : public iconv_exception_base {
	public:
		explicit iconv_invalid_code() : iconv_exception_base("不支持的编码类型或输入，请检查你指定的编码或输入") {}
	};

	class iconv_memory_lack : public iconv_exception_base {
	public:
		explicit iconv_memory_lack() : iconv_exception_base("内存不足") {}
	};

	class iconv_bad_input_str : public iconv_exception_base {
	public:
		explicit iconv_bad_input_str() : iconv_exception_base("输入的字符序列不符合指定的编码规则") {}
	};

	class iconv_unknown_exception : public iconv_exception_base {
	public:
		explicit iconv_unknown_exception() : iconv_exception_base("发生了未知的转换错误") {}
	};

	// C errno => C++ exception
	void from_errno(int err_num)
		throw(iconv_invalid_code, iconv_memory_lack, iconv_bad_input_str, iconv_unknown_exception)
	{
		switch (err_num)
		{
		case E2BIG:		return;
		case EINVAL:	throw iconv_invalid_code{};
		case ENOMEM:	throw iconv_memory_lack{};
		case EILSEQ:	throw iconv_bad_input_str{};
		default:		throw iconv_unknown_exception{};
		}
	}

	// 常用的编码，其余编码自行查询使用Convert
	enum class iconv_encode {
		ASCII,
		GBK, GB2312, GB18030,
		UTF_8,
		UTF_16, UTF_16BE, UTF_16LE,
		UTF_32, UTF_32BE, UTF_32LE
	};

	std::string Convert(std::string_view str_in, const char* from_code, const char* to_code)
		throw(iconv_invalid_code, iconv_memory_lack, iconv_bad_input_str, iconv_unknown_exception) {

		iconv_t conv_device = iconv_open(to_code, from_code);

		const iconv_t err_conv_device = reinterpret_cast<iconv_t>(-1);
		// 打开失败
		if (err_conv_device == conv_device) {
			// 抛出异常
			from_errno(errno);
		}

		// 用于存放所有的输出序列
		std::stringstream ss{};

		// 每次转换若干个字符的输出缓冲区
		constexpr int buffer_size = 20;
		char out_buffer[buffer_size] = {};

		char* in_buffer = const_cast<char*>(str_in.data());
		size_t in_buffer_left = str_in.length();

		// loop 将 str_in 中的字符转换进 out_buffer
		while (in_buffer_left > 0) {
			// 每次刷新输出缓冲区，并转换若干个字符进缓冲区
			char* out_buffer_ptr = out_buffer;
			size_t out_buffer_left = buffer_size;

			auto res = iconv(conv_device, &in_buffer, &in_buffer_left, &out_buffer_ptr, &out_buffer_left);

			constexpr size_t bad_result = static_cast<size_t>(-1);
			if (bad_result == res) {
				from_errno(errno);
			}

			ss.write(out_buffer, buffer_size - out_buffer_left);
		}

		return ss.str();
	}

	std::string Convert(std::string_view str_in, iconv_encode from_code, iconv_encode to_code)
		throw(iconv_invalid_code, iconv_memory_lack, iconv_bad_input_str, iconv_unknown_exception) {
		const char* from_code_str = nullptr;
		switch (from_code)
		{
		case iconv_space::iconv_encode::ASCII:
			from_code_str = "ASCII";
			break;
		case iconv_space::iconv_encode::GBK:
			from_code_str = "GBK";
			break;
		case iconv_space::iconv_encode::GB2312:
			from_code_str = "GB2312";
			break;
		case iconv_space::iconv_encode::GB18030:
			from_code_str = "GB18030";
			break;
		case iconv_space::iconv_encode::UTF_8:
			from_code_str = "UTF-8";
			break;
		case iconv_space::iconv_encode::UTF_16:
			from_code_str = "UTF-16";
			break;
		case iconv_space::iconv_encode::UTF_16BE:
			from_code_str = "UTF-16BE";
			break;
		case iconv_space::iconv_encode::UTF_16LE:
			from_code_str = "UTF-16LE";
			break;
		case iconv_space::iconv_encode::UTF_32:
			from_code_str = "UTF-32";
			break;
		case iconv_space::iconv_encode::UTF_32BE:
			from_code_str = "UTF-32BE";
			break;
		case iconv_space::iconv_encode::UTF_32LE:
			from_code_str = "UTF-32LE";
			break;
		default:
			throw iconv_invalid_code{};
			break;
		}

		const char* to_code_str = nullptr;
		switch (to_code)
		{
		case iconv_space::iconv_encode::ASCII:
			to_code_str = "ASCII";
			break;
		case iconv_space::iconv_encode::GBK:
			to_code_str = "GBK";
			break;
		case iconv_space::iconv_encode::GB2312:
			to_code_str = "GB2312";
			break;
		case iconv_space::iconv_encode::GB18030:
			to_code_str = "GB18030";
			break;
		case iconv_space::iconv_encode::UTF_8:
			to_code_str = "UTF-8";
			break;
		case iconv_space::iconv_encode::UTF_16:
			to_code_str = "UTF-16";
			break;
		case iconv_space::iconv_encode::UTF_16BE:
			to_code_str = "UTF-16BE";
			break;
		case iconv_space::iconv_encode::UTF_16LE:
			to_code_str = "UTF-16LE";
			break;
		case iconv_space::iconv_encode::UTF_32:
			to_code_str = "UTF-32";
			break;
		case iconv_space::iconv_encode::UTF_32BE:
			to_code_str = "UTF-32BE";
			break;
		case iconv_space::iconv_encode::UTF_32LE:
			to_code_str = "UTF-32LE";
			break;
		default:
			throw iconv_invalid_code{};
			break;
		}

		return Convert(str_in, from_code_str, to_code_str);
	}

} // iconv_space

#include <fstream>
#include <string>
int main()
{
	using namespace iconv_space;
	system("chcp 65001>nul");

	std::ifstream ifs("test.txt");
	std::string str;
	std::getline(ifs, str);
	std::cout << str << std::endl;	// 乱码输出

	try
	{
		auto result = Convert(str, iconv_encode::GB2312, iconv_encode::UTF_8);
		std::cout << result << std::endl;	// 正确输出
	}
	catch (std::exception& e)
	{
		std::cout << e.what();
	}

	return 0;
}
```