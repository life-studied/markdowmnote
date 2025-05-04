---
create: '2025-05-04'
modified: '2025-05-04'
---

# Java泛型

## quickview

Java泛型是一种强大的类型安全机制，允许在类、接口和方法中使用类型参数，从而提高代码的复用性和安全性。以下是泛型的快速使用效果展示：

### 泛型类

```java
public class Box<T> {
    private T content;

    public void setContent(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }
}

public class Main {
    public static void main(String[] args) {
        Box<String> stringBox = new Box<>();
        stringBox.setContent("Hello");
        System.out.println(stringBox.getContent()); // 输出：Hello

        Box<Integer> intBox = new Box<>();
        intBox.setContent(123);
        System.out.println(intBox.getContent()); // 输出：123
    }
}
```

### 泛型接口

```java
public interface DataHolder<T> {
    T getData();
    void setData(T data);
}

public class DataHolderImpl<T> implements DataHolder<T> {
    private T data;

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }
}

public class Main {
    public static void main(String[] args) {
        DataHolder<String> stringHolder = new DataHolderImpl<>();
        stringHolder.setData("Java");
        System.out.println(stringHolder.getData()); // 输出：Java
    }
}
```

### 泛型方法

```java
public class GenericMethods {
    public static <T> void print(T data) {
        System.out.println(data);
    }
}

public class Main {
    public static void main(String[] args) {
        GenericMethods.print("Hello"); // 输出：Hello
        GenericMethods.print(123); // 输出：123
        GenericMethods.print(3.14); // 输出：3.14
    }
}
```

---

## 详细知识点

### 1\. 泛型类

**定义泛型类**

```java
public class GenericClass<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
```

- `T` 是类型参数，可以在类的属性和方法中使用。
- 类的使用者可以在实例化时指定具体的类型，例如 `GenericClass<String>` 或 `GenericClass<Integer>`。

**使用泛型类**

```java
public class Main {
    public static void main(String[] args) {
        GenericClass<String> stringClass = new GenericClass<>();
        stringClass.setData("Hello");
        System.out.println(stringClass.getData()); // 输出：Hello

        GenericClass<Integer> integerClass = new GenericClass<>();
        integerClass.setData(123);
        System.out.println(integerClass.getData()); // 输出：123
    }
}
```

### 2\. 泛型接口

**定义泛型接口**

```java
public interface GenericInterface<T> {
    T getData();
    void setData(T data);
}
```

- 泛型接口可以定义类型参数，这些参数可以在接口的方法中使用。

**实现泛型接口**

```java
public class ConcreteClass implements GenericInterface<String> {
    private String data;

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }
}
```

- 实现类可以指定具体的类型参数，例如 `GenericInterface<String>`。
- 也可以保留泛型，例如 `GenericClass<T> implements GenericInterface<T>`。

### 3\. 泛型方法

**定义泛型方法**

```java
public class GenericMethods {
    public static <T> T getData(T data) {
        return data;
    }
}
```

- 泛型方法的类型参数在方法声明中定义，例如 `<T>`。
- 泛型方法可以独立于类的泛型参数使用。

**使用泛型方法**

```java
public class Main {
    public static void main(String[] args) {
        String result1 = GenericMethods.getData("Hello");
        System.out.println(result1); // 输出：Hello

        Integer result2 = GenericMethods.getData(123);
        System.out.println(result2); // 输出：123
    }
}
```

## 其它

#### 泛型的类型擦除

- Java泛型在编译时会被擦除，运行时无法获取具体的泛型类型。
- 例如，`List<String>` 和 `List<Integer>` 在运行时都被视为 `List`。

#### 泛型方法的静态调用

- 静态方法也可以是泛型方法，但泛型参数不能从类中继承，需要在方法调用时指定。
  ```java
  public static <T> void printArray(T[] array) {
      for (T element : array) {
          System.out.println(element);
      }
  }
  ```