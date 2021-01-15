package com.myfactory.health.controller;

import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gakki
 * @version 1.0
 * @description: TODO
 * @date 2021/1/14 17:00
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取登陆用户名
     */
    @GetMapping("/getUsername")
    public Result getUsername(){
        // 获取登陆用户的认证信息,user是
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 登陆用户名
        String username = loginUser.getUsername();
        // 返回给前端
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }

}
