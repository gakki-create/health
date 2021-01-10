package com.myfactory.health.dao;

import com.github.pagehelper.Page;
import com.myfactory.health.pojo.CheckGroup;
import com.myfactory.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealDao {

    //添加套餐
    void add(Setmeal setmeal);

    //添加套餐id与检查组id的关系
    void addsetmealcheckgroupId(@Param("id") Integer id, @Param("checkgroupId") Integer checkgroupId);
    //分页查询
    Page<Setmeal> findPage(String queryString);



    //更新套餐
    void update(Setmeal setmeal);
    //删除检查组与套餐的旧关系
    void deleteSetmealCheckgroup(Integer setmealId);
    //查询套餐信息
    Setmeal findById(int id);
    //查询检查组信息
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    int findByIdOrder(int id);

    void deleteSetmeal(int id);
    //查询套餐信息
    List<Setmeal> findAll();
}
