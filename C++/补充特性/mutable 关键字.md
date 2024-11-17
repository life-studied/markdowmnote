---
create: 2023-12-01
---
# mutable 关键字

​	**mutalbe**的中文意思是“可变的，易变的”，跟constant（既C++中的const）是反义词。

​	在C++中，**mutable**也是为了突破const的限制而设置的。被**mutable**修饰的变量，将永远处于可变的状态，即使在一个const函数中。

---

​	我们知道，被const关键字修饰的函数的一个重要作用就是为了能够保护类中的成员变量。即：该函数可以使用类中的所有成员变量，但是不能修改他们的值。然而，在某些特殊情况下，我们还是需要在const函数中修改类的某些成员变量，因为要修改的成员变量与类本身并无多少关系，即使修改了也不会对类造成多少影响。当然，你可以说，你可以去掉该函数的const关键字呀！但问题是，我只想修改某个成员变量，其余成员变量仍然希望被const保护。

经典的应用场景比如说：**我要测试一个方法的被调用次数**。

```C++
class Person {
public:
    Person();
    ~Person();

    int getAge() const; /*调用方法*/
    int getCallingTimes() const; /*获取上面的getAge()方法被调用了多少次*/
private:
    int age;
    char *name;
    float score;
    int m_nums;            /*用于统计次数*/
};
```

​	最普遍的作法就是在getAge()的方法体内对m_nums这个变量进行加+1，但是getAge()方法又是const方法，无法修改m_nums这个变量，我又不想去掉const关键字让别人能够修改age等成员变量，这个时候**mutable**关键字就派上用场了：

```C++
#include <iostream>

class Person {
public:
    Person();
    ~Person();

    int getAge() const; /*调用方法*/
    int getCallingTimes() const; /*获取上面的getAge()方法被调用了多少次*/
private:
    int age;
    char *name;
    float score;
    mutable int m_nums;            /*用于统计次数*/
};

Person::Person()
{
    m_nums = 0;
}

Person::~Person(){}

int Person::getAge() const
{
    std::cout << "Calling the method" << std::endl;
    m_nums++;
    // age = 4; 仍然无法修改该成员变量
    return age;
}

int Person::getCallingTimes()const
{
    return m_nums;
}

int main()
{
    Person *person = new Person();
    for (int i = 0; i < 10; i++) {
        person->getAge();
    }
    std::cout << "getAge()方法被调用了" << person->getCallingTimes() << "次" << std::endl;
    delete person;

    getchar();
    return 0;
}
```

