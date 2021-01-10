package com.myfactory.health.exception;

/**
 * @author Gakki
 * @version 1.0
 * @description: 自定义异常：
 * 1.区分系统异常与自定义异常的区别
 * 2.终止已经不符合业务逻辑的代码
 * @date 2021/1/7 17:36
 */

public class HealthException extends RuntimeException{

    //继承RuntimeException
    public HealthException(String message){
        super(message);
    }

}
