package com.myfactory.health.service;

import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.Order;

import java.util.Map;

public interface OrderMobileService  {

    Order submitOrder(Map<String, String> orderInfo) throws HealthException;

}
