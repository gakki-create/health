package com.myfactory.health.dao;

import com.github.pagehelper.Page;
import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    //查询所有检查项
    List<CheckItem> findAll();

    //添加检查项
    void add(CheckItem checkItem);

    //分页查询
    Page<CheckItem> findByCondition(String queryString);
    //查询该检查项是否被使用
    int findCountByCheckItemId(int id);

    void deleteById(int id);

    CheckItem findById(int id);

    void update(CheckItem checkItem);

}
