package com.myfactory.health.dao;

import com.myfactory.health.pojo.User;

public interface Userdao {
    //根据用户名查询用户
    User findByUsername(String username);
}
