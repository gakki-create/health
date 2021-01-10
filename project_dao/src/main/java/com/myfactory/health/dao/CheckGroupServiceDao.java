package com.myfactory.health.dao;

import com.github.pagehelper.Page;
import com.myfactory.health.pojo.CheckGroup;
import com.myfactory.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupServiceDao {
    //添加检查组信息
    void add(CheckGroup checkGroup);
    //添加检查组与检查项的一一对应关系
    void addCheckGroupCheckItem(@Param("id") Integer id,@Param("checkitemId") Integer checkitemId);

    Page<CheckGroup> findByCondition(String queryString);

    CheckGroup findById(int checkGroupId);

    List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId);

    void update(CheckGroup checkGroup);

    void deleteCheckGroupCheckItem(Integer id);

    int findCountSetmealCheckgroup(int id);

    void delete(int id);
    //查询所有检查组
    List<CheckGroup> findAll();

}
