package com.cyx.layer.controller;

import com.cyx.layer.service.UserService;
import com.cyx.layer.service.impl.UserServiceImpl;

/**
 * 控制层
 */
public class UserController {
    /**
     * 控制层调用业务层完成业务处理
     */
    private UserService userService = new UserServiceImpl();

    public String register(String username, String password){
        return userService.register(username, password);
    }

    public String login(String username, String password){
        return userService.login(username, password);
    }
}
