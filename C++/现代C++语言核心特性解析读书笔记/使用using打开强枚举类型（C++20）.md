## 使用using打开强枚举类型

​	C++20标准扩展了`using`，用于打开强枚举类型的命名空间。注意：在g++和vs中均尚未支持本语法。

```C++
const char* ColorToString(Color c)
{
    switch (c)
    {
        using enum Color;
        case Red: return "Red";
        case Green: return "Green";
        case Blue: return "Blue";
        default: return "none";
    }
}
```

或者是（编译器中依旧会报错）：

```C++
const char* ColorToString(Color c)
{
    switch (c)
    {
        using Color::Red;
        case Red: return "Red";
        case Color::Green: return "Green";
        case Color::Blue: return "Blue";
        default: return "none";
    }
}
```

