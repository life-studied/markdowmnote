package com.cyx.jdbc;

import java.sql.*;
import java.util.Scanner;

public class PreparedStatementTest {

    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        System.out.println("请输入商品名称：");
        String goodsName = sc.nextLine();
        String url = "jdbc:mysql://localhost:3306/lesson?serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "root";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT id,name,number,price,agent_id FROM goods WHERE name= ? LIMIT 0, 20";
            //创建预处理执行器
            PreparedStatement ps = conn.prepareStatement(sql);
            //设置占位符替换的值
            ps.setString(1, goodsName);
            // SELECT id,name,number,price,agent_id FROM goods WHERE name='小米10' or 1='1'
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int number = rs.getInt("number");
                double price = rs.getDouble("price");
                long agentId = rs.getLong("agent_id");
                System.out.println(id + "," + name + "," +number + "," +price + "," +agentId);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void sqlInjection(){
        Scanner sc =new Scanner(System.in);
        System.out.println("请输入商品名称：");
        String goodsName = sc.nextLine();
        goodsName = "'" + goodsName +"'";

        String url = "jdbc:mysql://localhost:3306/lesson?serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "root";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement s = conn.createStatement();
            // SELECT id,name,number,price,agent_id FROM goods WHERE name='小米10' or 1='1'
            String sql = "SELECT id,name,number,price,agent_id FROM goods WHERE name= " + goodsName + "LIMIT 0, 20";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int number = rs.getInt("number");
                double price = rs.getDouble("price");
                long agentId = rs.getLong("agent_id");
                System.out.println(id + "," + name + "," +number + "," +price + "," +agentId);
            }
            rs.close();
            s.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
