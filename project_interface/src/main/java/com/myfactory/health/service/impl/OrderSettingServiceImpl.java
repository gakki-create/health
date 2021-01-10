package com.myfactory.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.myfactory.health.dao.OrderSettingDao;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.OrderSetting;
import com.myfactory.health.service.OrderSettingService;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gakki
 * @version 1.0
 * @description: 批量导入预约信息
 * @date 2021/1/9 17:26
 */

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) throws HealthException {
        //1.遍历前端传来的数据集合
        //2.根据时间orderDate来查询，数据库是否有这个日期，如果有，则可以更新数据库t_ordersetting，没有的话，则执行添加操作
        //3.在预约时间已经存在数据库的基础上，要判断前端传来的数据是否小于已预约人数，如果小于，则抛异常，大于则可以更新数据库
        //4.记得添加事务控制
        for (OrderSetting orderSetting : orderSettingList) {
            //判断预约时间在数据库是否存在，可以根据预约时间来查询，先获取日期
            OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());

            if (osInDB != null) {
                //说明数据库已经存在该预约，可以判断前端传来的数据是否大于已有的预约人数来进行更新
                if (orderSetting.getReservations() < osInDB.getReservations()) {
                    throw new HealthException(orderSetting.getOrderDate() + "设置的预约人数小于已有的预约人数，无法更新！");
                }
                //可以进行更新操作
                orderSettingDao.update(orderSetting);
            }
            //不存在预约，执行添加操作
            orderSettingDao.add(orderSetting);
        }
    }


    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        month += "%";
        //查询当月预约的所有集合
        return orderSettingDao.getOrderSettingByMonth(month);

    }

    @Override
    @Transactional
    public void editNumberByDate(OrderSetting orderSetting) throws HealthException {
        //通过日期判断预约设置是否存在？
        OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        //判断要已有预约数是否大于添加的预约
        if (os != null) {
            if (os.getReservations() > orderSetting.getNumber()) {
                throw new HealthException("已有预约数大于要设置的预约数，无法设置！");
            }
            //可以进行更新
            orderSettingDao.update(orderSetting);
        }
        //没有设置预约数，直接添加即可
        orderSettingDao.add(orderSetting);

    }


}
