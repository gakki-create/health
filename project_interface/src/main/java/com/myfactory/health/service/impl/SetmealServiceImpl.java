package com.myfactory.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myfactory.health.dao.SetmealDao;
import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.CheckGroup;
import com.myfactory.health.pojo.Setmeal;
import com.myfactory.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Gakki
 * @version 1.0
 * @description: TODO
 * @date 2021/1/8 17:52
 */

@Service(interfaceClass = SetmealService.class)

public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    //添加套餐信息
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.添加套餐信息
        setmealDao.add(setmeal);
        //2.获取套餐的ID，根据套餐id添加与检查组id的关系（1对多）
        Integer id = setmeal.getId();
        //2.1判断一下checkgroupIds是否为空
        if (checkgroupIds!=null) {
            //2.2遍历数据，添加套餐id与检查组ID的关系
            for (Integer checkgroupId : checkgroupIds) {

                setmealDao.addsetmealcheckgroupId(id,checkgroupId);
            }
        }

        //3.添加事务控制

    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //借助分页插件进行分页查询
        //1.使用分页插件,确定开始页码以及每页条数（当前页数及每页条数）
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //2.判断查询条件是否为空，有查询条件，则模糊查询，无查询条件，则查询所有

        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {

            //2.1查询条件不为空，要对模糊查询like字段进行字符串拼接，左右拼接两个%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");

        }

        //3.调用dao方法进行查询,将查询结果封装到page实体类中
        Page<Setmeal> page=setmealDao.findPage(queryPageBean.getQueryString());

        //4.将数据总条数及当前页结果返回
        return new PageResult(page.getTotal(),page.getResult());

    }


    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.先更新套餐信息
        setmealDao.update(setmeal);
        //2.根据套餐id先删除t_setmeal_checkgroup旧关系
        Integer setmealId = setmeal.getId();

        setmealDao.deleteSetmealCheckgroup(setmealId);

        //3.添加新关系t_setmeal_checkgroup
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {

                setmealDao.addsetmealcheckgroupId(setmealId,checkgroupId);

            }
        }
        //4.添加事务控制
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    @Transactional
    public void delete(int id) throws HealthException{
        //判断套餐是否被订单t_order所使用的
        int count = setmealDao.findByIdOrder(id);
        //如果被使用，则提示用户不能删除
        if (count>0) {
            throw new HealthException("该套餐被订单所使用，不能删除!");
        }
        //如果没被使用，要先删除套餐跟检查项的关系
        setmealDao.deleteSetmealCheckgroup(id);

        setmealDao.deleteSetmeal(id);

        //添加事务控制
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }
}
