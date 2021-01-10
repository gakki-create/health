package com.myfactory.health.controller;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.myfactory.health.entity.Result;
import com.myfactory.health.exception.HealthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Gakki
 * @version 1.0
 * @description: 全局异常处理
 *  ExceptionHandler（spring的异常处理器） 获取的异常 异常的范围从小到大，类似于try catch中的catch
 *  * 与前端约定好的，返回的都是json数据
 * @date 2021/1/7 17:42
 *
 */

@RestControllerAdvice
public class HealExceptionAdvice {

    /**
     *  info:  打印日志，记录流程性的内容
     *  debug: 记录一些重要的数据 id, orderId, userId
     *  error: 记录异常的堆栈信息，代替e.printStackTrace();
     *  工作中不能有System.out.println(), e.printStackTrace();
     */
    private static final Logger log = LoggerFactory.getLogger(HealExceptionAdvice.class);

    /**
    *
    * @param: he
    * @return: com.myfactory.health.entity.Result
    * @author Gakki
    **/
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException he){
        // 将自定义异常信息返回
        return new Result(false, he.getMessage());
    }




/**
    *
    * @param: e
    * @return: com.myfactory.health.entity.Result
    * @author Gakki
    **/

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("发生异常",e);
        return new Result(false, "发生未知错误，操作失败，请联系管理员");
    }




}
