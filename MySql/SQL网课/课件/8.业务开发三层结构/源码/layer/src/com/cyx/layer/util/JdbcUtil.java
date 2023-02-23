package com.cyx.layer.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil {
    private static final String url = "jdbc:mysql://localhost:3306/lesson?serverTimezone=Asia/Shanghai";
    private static final String username = "root";
    private static final String password = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("驱动程序未加载");
        }
    }

    public static int update(String sql, Object...params){
        int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            ps = createPreparedStatement(conn, sql, params);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps, conn);
        }
        return result;
    }

    private static PreparedStatement createPreparedStatement(Connection conn, String sql, Object...params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        if(params != null && params.length > 0){
            for(int i=0; i<params.length; i++){
                ps.setObject(i+1, params[i]);
            }
        }
        return ps;
    }

    /**
     * 关闭连接、执行器、结果集
     * @param closeables
     */
    private static void close(AutoCloseable... closeables){
        if(closeables != null && closeables.length > 0){
            for(AutoCloseable ac: closeables){
                if(ac != null){
                    try {
                        ac.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
    /**
     * 万能查询通过反射实现，必须要保证类中定义字段名与查询结果展示的列名称保持一致
     * @param sql
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public static<T> List<T> query(String sql,Class<T> clazz, Object...params){
        List<T> dataList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            ps = createPreparedStatement(conn, sql, params);
            rs = ps.executeQuery();
            while (rs.next()){
                T t = createInstance(clazz, rs);
                dataList.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, conn);
        }
        return dataList;
    }

    private static<T> T createInstance(Class<T> clazz, ResultSet rs) throws Exception{
        Constructor<T> c = clazz.getConstructor();//获取无参构造
        T t = c.newInstance();//创建对象
        Field[] fields = clazz.getDeclaredFields(); //获取类中定义的字段
        for(Field field: fields){
            String fieldName = field.getName();
            //setId => set id => set + I + d
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method m = clazz.getDeclaredMethod(methodName, field.getType());
            try {
                Object value = rs.getObject(fieldName, field.getType());
                m.invoke(t, value);
            } catch (Exception e){}
        }
        return t;
    }
}
