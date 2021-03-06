package com.myfactory.health.service;

import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.CheckGroup;
import com.myfactory.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    //添加套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);
    //分页查询
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);
    //更新套餐
    void update(Setmeal setmeal, Integer[] checkgroupIds);
    //查询套餐信息
    Setmeal findById(int id);
    //查询检查组信息
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    void delete(int id) throws HealthException;
    //查询所有套餐信息
    List<Setmeal> findAll();
    //查询单个套餐下的信息
    Setmeal findDetailById(int id);
    //查询套餐名称集合
    List<Map<String, Object>> findSetmealCount();

}
