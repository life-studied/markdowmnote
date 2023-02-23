package com.cyx.layer.starter;

import com.cyx.layer.controller.UserController;
import com.cyx.layer.model.User;

import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {
        UserController controller = new UserController();
        Scanner sc = new Scanner(System.in);
//        System.out.println("请输入注册账号：");
//        String username = sc.next();
//        System.out.println("请输入注册密码：");
//        String password =  sc.next();
//        String result = controller.register(username, password);
//        System.out.println(result);

        System.out.println("请输入登录账号：");
        String username1 = sc.next();
        System.out.println("请输入登录密码：");
        String password1 =  sc.next();
        String loginResult = controller.login(username1, password1);
        System.out.println(loginResult);
    }
}
