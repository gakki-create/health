package com.myfactory.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.dao.CheckGroupServiceDao;
import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.CheckGroup;
import com.myfactory.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    /**
    *
    * @param: checkGroup   checkitemIds
    * @return: void
    * @author Gakki
    **/
    @Autowired
    private CheckGroupServiceDao checkGroupServiceDao;

    @Override
    @Transactional//对数据库有多次操作，要加入事务
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //添加检查组
        checkGroupServiceDao.add(checkGroup);
        //获取检查组的id
        Integer id = checkGroup.getId();
        //遍历检查项id，将检查项的id与检查组id一一对应
        if (checkitemIds!=null) {
            //有勾选
            for (Integer checkitemId : checkitemIds) {
                //往t_CheckGroup_CheckItem添加检查组跟检查项id的一一对应关系
                checkGroupServiceDao.addCheckGroupCheckItem(id,checkitemId);
            }

        }

    }

    /**
    *
    * @param: queryPageBean
    * @return: com.myfactory.health.entity.PageResult
    * @author Gakki
    **/
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //分页步骤，使用分页插件
         /*第二种，Mapper接口方式的调用，推荐这种使用方式。
        PageHelper.startPage(1, 10);
        List<Country> list = countryMapper.selectIf(1);*/
        //1.使用分页插件,确定开始页码以及每页条数
       PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
       //2.判断查询条件是否为空，有查询条件，则模糊查询，无查询条件，则查询所有
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
           //2.1查询条件不为空，要对模糊查询like字段进行字符串拼接，左右拼接两个%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        //3.调用dao方法进行查询,将查询结果封装到page实体类中
        Page<CheckGroup> page=checkGroupServiceDao.findByCondition(queryPageBean.getQueryString());

        //4.将数据总条数及当前页结果返回
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(int checkGroupId) {
        //查询检查组信息用于检查组数据回显
        CheckGroup checkGroup=checkGroupServiceDao.findById(checkGroupId);
        return checkGroup;
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId) {
        //查询检查项信息用于检查项数据回显
        List<Integer> checkItemIdsList= checkGroupServiceDao.findCheckItemIdsByCheckGroupId(checkGroupId);
        return checkItemIdsList;
    }

    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //更新检查组信息,更新t_checkgroup_checkitem这个表中检查组与检查项的关系，最好是采用先删除表关系，再添加的方式

        //1.更新t_checkgroup；2.更新t_checkgroup_checkitem（先删除原有表格关系，再添加新的表格关系）；
        //2.注意：此方法多次操作数据库，要加入事务控制 @Transactional
        //更新检查组信息
        checkGroupServiceDao.update(checkGroup);
        //根据id删除t_checkgroup_checkitem表格关系
        checkGroupServiceDao.deleteCheckGroupCheckItem(checkGroup.getId());
        //建立新关系
        if (checkitemIds!=null) {
            for (Integer checkitemId : checkitemIds) {
                checkGroupServiceDao.addCheckGroupCheckItem(checkGroup.getId(),checkitemId);
            }
        }

    }

    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        //删除项目是应该看是否有中间表关系
        //不能随便删除检查组，判断套餐中有没有被使用检查组t_setmeal_checkgroup
        int count=checkGroupServiceDao.findCountSetmealCheckgroup(id);
        //判断检查组是否被套餐使用，即判断count是否大于0
        if (count>0) {//说明检查组已经被套餐使用了
            throw new HealthException(MessageConstant.CHECKGROUP_IN_USE);
        }
        //程序走到这，说明没被套餐未被使用
        //1.要先删除检查组与检查项的关系，再删除检查组
        checkGroupServiceDao.deleteCheckGroupCheckItem(id);
        //2.删除检查组
        checkGroupServiceDao.delete(id);



    }

    @Override
    public List<CheckGroup> findAll() {
        //调用dao方法查询所有
        return checkGroupServiceDao.findAll();
    }
}
