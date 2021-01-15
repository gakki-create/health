package com.myfactory.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.entity.Result;
import com.myfactory.health.pojo.CheckItem;
import com.myfactory.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* 接收json数据，使用@RestController注解
* */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    /*
    *
    *订阅zookeeper的服务，使用@Reference
    *  */
    @Reference
    private CheckItemService checkItemService;


    @GetMapping("/findAll")
    public Result findAll(){
        //调用服务查询
       List<CheckItem> checkItemList=checkItemService.findAll();
       //封装到result中去,并返回给前端
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){
        checkItemService.add(checkItem);
        //封装到result中去,并返回给前端
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    /*
    * 分页查询：
    * 1.接收前端传来的paginnation参数，
    * 2.调用服务端进行处理
    * 3.返回结果（进行封装）
    * */
    @PostMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用service中的方法，查询当前页的结果集，封装到PageResult
        PageResult<CheckItem> pageResultList=checkItemService.findPage(queryPageBean);
        //封装到result中去,并返回给前端
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResultList);
    }


    @PostMapping("/deleteById")
    public Result deleteById(int id){
        //调用service中的方法，查询当前页的结果集，封装到PageResult
           checkItemService.deleteById(id);
        //封装到result中去,并返回给前端
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }


    @GetMapping("/findById")
    public Result findById(int id){
        //调用service中的方法，查询当前页的结果集，封装到PageResult
        CheckItem checkitem=checkItemService.findById(id);
        //封装到result中去,并返回给前端
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitem);
    }



    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        //调用service中的方法，查询当前页的结果集，封装到PageResult
        checkItemService.update(checkItem);
        //封装到result中去,并返回给前端
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }


}
