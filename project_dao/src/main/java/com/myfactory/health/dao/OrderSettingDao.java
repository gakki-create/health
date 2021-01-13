package com.myfactory.health.dao;

import com.myfactory.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    //批量导入预约设置信息
    void add(OrderSetting orderSetting);

    //根据日期查询有没有预约
    OrderSetting findByOrderDate(Date orderDate);

    //更新数据库
    void update(OrderSetting orderSetting);

    //查询
    List<Map<String, Integer>>  getOrderSettingByMonth(String month);

    int editReservationsByOrderDate(OrderSetting orderSetting);
}
