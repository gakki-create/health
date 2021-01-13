package com.myfactory.health.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.RedisMessageConstant;
import com.myfactory.health.entity.Result;
import com.myfactory.health.pojo.Order;
import com.myfactory.health.service.OrderMobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author Gakki
 * @version 1.0
 * @description: 体检预约实现
 * @date 2021/1/12 17:21
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {
    //需要用到的东西先注入到spring
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderMobileService orderMobileService;

    /**
    *
    * @param: paramMap
    * @return: com.myfactory.health.entity.Result
    * @author Gakki
    **/
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> paramMap){
        //1.先在controller层检验验证码是否正确
        Jedis jedis = jedisPool.getResource();

        String key =  RedisMessageConstant.SENDTYPE_GETPWD + "_" + paramMap.get("telephone");

        //取出redis缓存中code
        String code = jedis.get(key);

        if (StringUtils.isEmpty(code)) {
            //说明验证码不存在，提醒用户发送验证码
            return new Result(false,"请发送验证码");
        }

        //说明验证码已经存在
        //判断用户输入的验证码是否正确
        if (!code.equals(paramMap.get("validateCode"))) {
            //用户名输入验证码错误，返回给前端结果
            return new Result(false,"验证码输入错误，请重新输入验证码");
        }
        //说明验证码输入正确，可以调用service的方法进行预约

        //在这之前，要先删除redis里面缓存的验证码
        jedis.del(key);

        //还要添加预约类型；
        paramMap.put("orderType", Order.ORDERTYPE_WEIXIN);

        //返回给前端一个order供后续使用
        Order order=orderMobileService.submitOrder(paramMap);

        // 预约成功页面展示时需要用到id
        return   new Result(true,"预约成功！",order);
    }

}
