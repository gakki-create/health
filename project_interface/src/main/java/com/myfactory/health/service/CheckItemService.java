package com.myfactory.health.service;

import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    //查询所有检查项
    List<CheckItem> findAll();
    //添加检查项
    void add(CheckItem checkItem);
    //分页查询
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);
    //根据id删除检查项
    void deleteById(int id) throws HealthException;

    CheckItem findById(int id);

    void update(CheckItem checkItem);

}
