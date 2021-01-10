package com.myfactory.health.service;

import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.CheckGroup;
import com.myfactory.health.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {
    //添加检查项
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(int checkGroupId);

    List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteById(int id) throws HealthException;

    List<CheckGroup> findAll();

}
