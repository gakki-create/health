package com.myfactory.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.myfactory.health.dao.OrderDao;
import com.myfactory.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author Gakki
 * @version 1.0
 * @description: TODO
 * @date 2021/1/12 23:25
 */
@Service(interfaceClass =OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    /**
    *
    * @param: id
    * @return: java.util.Map<java.lang.String,java.lang.Object>
    * @author Gakki
    **/
    @Override
    public Map<String, Object> findById(int id) {
        return orderDao.findById4Detail(id);
    }
}
