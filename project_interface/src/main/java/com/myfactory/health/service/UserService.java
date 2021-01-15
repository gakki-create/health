package com.myfactory.health.service;

import com.myfactory.health.pojo.User;

public interface UserService {
    User findByUsername(String username);

}
