package com.myfactory.health.dao;



import com.myfactory.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    public void add(Order order);
    public List<Order> findByCondition(Order order);
    public Map findById4Detail(Integer id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map<String,Object>> findHotSetmeal();

    int findOrderCountBetweenDate(@Param("startDate")String startDate,@Param("endDate") String endDate);
}
