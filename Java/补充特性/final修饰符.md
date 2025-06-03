---
create: '2025-06-04'
modified: '2025-06-04'
---

# final修饰符

在 Java 中，`final` 关键字可以用来修饰类、方法和变量。下面是 `final` 修饰方法和变量的具体含义：

1. **final 修饰变量**：

    - 常量：当一个变量被声明为 `final` 时，它的值就不能被改变。这样的变量通常被称为常量。
        - 基本类型：对于基本数据类型（如 `int`, `double` 等），`final` 变量的值一旦被赋值后就不能再改变。
        - 引用类型：对于引用类型（如对象或数组），`final` 变量不能改变其引用的地址，但可以通过该引用去修改对象的内容。
        
        ```java
        final int a = 5; // 基本数据类型的 final 变量
        final int[] arr = {1, 2, 3}; // 引用类型的 final 变量
        
        a = 10; // 编译错误：不能重新赋值 final 变量 a
        arr[0] = 10; // 允许，可以修改数组内容
        arr = new int[]{4, 5, 6}; // 编译错误：不能重新赋值 final 变量 arr
        ```

2. **final 修饰类成员**：

    * 空白final：

        * 常量

        * 在使用前初始化这个空白 final字段。

            ```java
            class Poppet {
                private int i;
                Poppet(int ii) { i = ii; }
            }
            public class BlankFinal {
                private final int i = 0; // 初始化了的final
                private final int j; // 空白final
                private final Poppet p; // 空白final 引用
                // 空白final字段必须在构造器里初始化
                public BlankFinal() {
                    j = 1; // 初始化空白 final
                    p = new Poppet(1); // 初始化空白 final 引用
                }
                public BlankFinal(int x) {
                    j = x; // 初始化空白 final
                    p = new Poppet(x); // 初始化空白 final 引用
                }
                public static void main(String[] args) {
                    new BlankFinal();
                    new BlankFinal(47);
                }
            }
            ```

3. **final 修饰方法**：

    - 禁止重写：当一个方法被声明为 `final` 时，这个方法就不能在其子类中被重写（覆盖）。

        ```java
        class Parent {
            final void show() {
                System.out.println("Parent's method");
            }
        }
        
        class Child extends Parent {
            // 编译错误：不能重写 final 方法
            // void show() {
            //     System.out.println("Child's method");
            // }
        }
        ```