package com.myfactory.health.service;

import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    //批量导入预约设置信息
    void add(List<OrderSetting> orderSettingList) throws HealthException;
    //查询当月预约信息
    List<Map<String, Integer>> getOrderSettingByMonth(String month);
    //编辑最大预约数
    void editNumberByDate(OrderSetting orderSetting) throws HealthException;
}
