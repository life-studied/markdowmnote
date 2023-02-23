package com.cyx.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTest {

    public static void main(String[] args) {
        //jdbc:使用jdbc连接技术
        //mysql://localhost:3306 使用的是MySQL数据库协议，访问的是本地计算机3306端口
        String url = "jdbc:mysql://localhost:3306/lesson?serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "root";
        List<Account> accounts = new ArrayList<>();
        //MySQL8.0
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //获取连接
            Connection conn = DriverManager.getConnection(url, username, password);
            //在连接上创建SQL语句执行器
            Statement s = conn.createStatement();
//            String sql = "SELECT account,balance,state FROM account";
//            //使用执行器执行查询，得到一个结果集
//            ResultSet rs = s.executeQuery(sql);
//            while (rs.next()){//光标移动
//                //通过列名称获取列的值
//                String account = rs.getString("account");
//                double balance = rs.getDouble(2);
//                int state = rs.getInt("state");
//                Account a = new Account(account, balance, state);
//                accounts.add(a);
//            }
//            rs.close();
            String updateSql = "UPDATE account SET balance = balance + 1000 WHERE account=123457";
            //执行更新时，返回的都是受影响的行数
            int affectedRows = s.executeUpdate(updateSql);
            System.out.println(affectedRows);
            s.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        accounts.forEach(System.out::println);
    }
}
