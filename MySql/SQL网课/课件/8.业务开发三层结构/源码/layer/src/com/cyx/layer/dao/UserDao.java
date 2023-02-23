package com.cyx.layer.dao;

import com.cyx.layer.model.User;

/**
 * 数据访问层接口
 */
public interface UserDao {

    int saveUser(String username, String password, String salt);

    User getUserByUsername(String username);
}
