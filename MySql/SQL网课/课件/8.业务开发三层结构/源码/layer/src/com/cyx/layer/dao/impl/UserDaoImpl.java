package com.cyx.layer.dao.impl;

import com.cyx.layer.dao.UserDao;
import com.cyx.layer.model.User;
import com.cyx.layer.util.JdbcUtil;

import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public int saveUser(String username, String password, String salt) {
        String sql = "INSERT INTO `user` (`username`, `password`, `salt`) VALUES (?, ?, ?)";
        Object[] params = { username, password, salt };
        return JdbcUtil.update(sql, params);
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT username,password,salt FROM user WHERE username=?";
        List<User> users = JdbcUtil.query(sql, User.class, username);
        return users.size() == 0 ? null : users.get(0);
    }
}
