package com.cyx.jdbc.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionTest {

    public static void main(String[] args) {
        //构建一个学生对象，并为每个字段赋值
        Class<Student> clazz = Student.class;
        try {
            Constructor<? extends Student> c = clazz.getDeclaredConstructor();
            //Student类中的无参构造方法是私有的，因此需要先修改访问权限
            c.setAccessible(true);
            Student s = c.newInstance();
            Field nameField = clazz.getDeclaredField("name");
            nameField.setAccessible(true);
            //给指定对象中的该字段赋值
            nameField.set(s, "李四");

            Field ageField = clazz.getDeclaredField("age");
            ageField.setAccessible(true);
            ageField.set(s, 20);

            // get name => get + N + ame
            String fieldName = nameField.getName();
            String methodName = "get"+ fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            Method m = clazz.getDeclaredMethod(methodName);
            m.setAccessible(true);
            String name = (String) m.invoke(s);
            System.out.println(name);

            methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            m = clazz.getDeclaredMethod(methodName, nameField.getType());
            m.invoke(s, "李刚");
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getMethod(){
        Class<Student> clazz = Student.class;
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method: methods){
            System.out.print(method.getModifiers() + " ");
            System.out.print(method.getName() + " (");
            Class[] types = method.getParameterTypes();
            for(Class c: types){
                System.out.print(c.getName() + ",");
            }
            System.out.println(")");
        }
        System.out.println("===========================");
        try {
            Method method = clazz.getDeclaredMethod("setName", String.class);
            System.out.print(method.getModifiers() + " ");
            System.out.print(method.getName() + " (");
            Class[] types = method.getParameterTypes();
            for(Class c: types){
                System.out.print(c.getName() + ",");
            }
            System.out.println(")");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void getField(){
        Class<Student> clazz = Student.class;
        Field[] fields = clazz.getDeclaredFields();
        for(Field f: fields){
            System.out.print(f.getModifiers() + " ");
            System.out.print(f.getType().getName() + " ");
            System.out.println(f.getName());
        }
        System.out.println("==========================");
        try {
            Field f = clazz.getDeclaredField("name");
            System.out.print(f.getModifiers() + " ");
            System.out.print(f.getType().getName() + " ");
            System.out.println(f.getName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static void getConstructor(){
        Class<Student> clazz = Student.class;
        //获取在类中定义的构造方法
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for(Constructor c: constructors){
            System.out.println(c.getModifiers());
            String name = c.getName(); //构造方法的名字
            Class[] types = c.getParameterTypes();
            System.out.print(name + " ");
            System.out.println(Arrays.toString(types));
        }
        System.out.println("=============================");
        constructors = clazz.getConstructors();
        for(Constructor c: constructors){
            System.out.println(c.getModifiers());
            String name = c.getName(); //构造方法的名字
            Class[] types = c.getParameterTypes();
            System.out.print(name + " ");
            System.out.println(Arrays.toString(types));
        }
        System.out.println("=============================");
        try {
            Constructor c = clazz.getDeclaredConstructor(String.class, int.class);
            System.out.println(c.getModifiers());
            String name = c.getName(); //构造方法的名字
            Class[] types = c.getParameterTypes();
            System.out.print(name + " ");
            System.out.println(Arrays.toString(types));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void getClazz(){
        Class<Student> c1 = Student.class;
        System.out.println(c1.getName());
        Student stu = new Student("张三", 20);
        Class<? extends Student> c2 = stu.getClass();
        //获取父类
        Class<? super Student> c3 = c1.getSuperclass();
        System.out.println(c3.getName());
        try {
            Class c4 = Class.forName("com.cyx.jdbc.reflection.Student");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Class c5 = Integer.TYPE;
        Class c6 = int.class;
        System.out.println(c5.getName());
    }
}
