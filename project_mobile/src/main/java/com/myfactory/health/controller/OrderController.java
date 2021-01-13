package com.myfactory.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.entity.Result;
import com.myfactory.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Gakki
 * @version 1.0
 * @description: TODO
 * @date 2021/1/12 21:28
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @GetMapping("/findById")
    public Result findById(int id){

        Map<String,Object> orderInfo=orderService.findById(id);

        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);

    }
}
