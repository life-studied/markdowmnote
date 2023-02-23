package com.cyx.layer.service;

/**
 * 业务层接口
 */
public interface UserService {

    String register(String username, String password);

    String login(String username, String password);
}
