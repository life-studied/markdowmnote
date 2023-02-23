package com.cyx.layer.service.impl;

import com.cyx.layer.dao.UserDao;
import com.cyx.layer.dao.impl.UserDaoImpl;
import com.cyx.layer.model.User;
import com.cyx.layer.service.UserService;
import com.cyx.layer.util.MD5;

public class UserServiceImpl implements UserService {
    //处理业务需要获取数据，因此需要使用数据访问层
    private UserDao userDao =new UserDaoImpl();

    @Override
    public String register(String username, String password) {
        String salt = MD5.randomStr(30);
        String encrypt = MD5.encrypt(password, salt);
        int affectedRows = userDao.saveUser(username, encrypt, salt);
        return affectedRows == 1 ? "注册成功" : "注册失败";
    }

    @Override
    public String login(String username, String password) {
        User user = userDao.getUserByUsername(username);
        if(user == null) return "账号不存在";
        String salt = user.getSalt();
        String encrypt = MD5.encrypt(password, salt);
        return encrypt.equals(user.getPassword()) ? "登录成功" : "账号或密码错误";
    }
}
