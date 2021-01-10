package com.myfactory.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.dao.CheckItemDao;
import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.CheckItem;
import com.myfactory.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

//使用alibaba的包,发布服务，interfaceClass指定服务的接口类

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    //实现CheckItemService，并重写findAll方法

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        //查询所有检查项
        return checkItemDao.findAll();
    }

    @Override
    public void add(CheckItem checkItem) {
        //添加检查项
        checkItemDao.add(checkItem);
    }

    /*
    * 分页查询：使用分页插件PageHelper进行分页
    * 1.QueryPageBean传过来的参数有CurrentPage，PageSize以及查询条件，查询条件为用户填写，
    *所以要判断用户是否填写查询条件
    * */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
         /*第二种，Mapper接口方式的调用，推荐这种使用方式。
         PageHelper.startPage(1, 10);
         List<Country> list = countryMapper.selectIf(1);*/
         //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //如果查询条件非空，开始分页查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            // 有查询条件，要拼接%，like后要接“%”
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //无查询条件直接查询即可
        // 紧接着的查询语句会被分页
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        //封装结果到PageResult中去
        PageResult<CheckItem> pageResult = new PageResult<>(page.getTotal(), page.getResult());
        //返回封装后的结果集
        return pageResult ;
    }


    @Override
    public void deleteById(int id) throws HealthException {
        //判断这个检查项是否被使用
        int count = checkItemDao.findCountByCheckItemId(id);
        if (count>0) {
            //表示检查项被使用
            throw new HealthException(MessageConstant.CHECKITEM_IN_USE);
        }
        //没使用过就可以调用checkItemDao进行删除
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
