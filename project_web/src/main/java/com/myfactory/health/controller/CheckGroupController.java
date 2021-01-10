package com.myfactory.health.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.entity.Result;
import com.myfactory.health.pojo.CheckGroup;
import com.myfactory.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*使用@RestController表示要有返回值*/
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    /**
     *订阅服务
     */
    @Reference
    private CheckGroupService checkGroupService;


    /**
    *添加检查项
    */
    @RequestMapping("/add")
    /*@RequestBody表示从请求体获取参数*/
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){

        //调用服务
        checkGroupService.add(checkGroup,checkitemIds);

        //响应结果
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);

    }

    @RequestMapping("/findPage")
    //分页查询检查组checkgroup
    /*@RequestBody表示从请求体获取参数*/
    public Result findPage(@RequestBody QueryPageBean queryPageBean){

        //调用服务
        PageResult pageResult=checkGroupService.findPage(queryPageBean);

        //响应结果
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);

    }
    /**
    *
    * @param: null
    * @return:
    * @author Gakki
     * 检查组数据编辑：包括：检查组信息查询（信息回显），更新检查项的信息，更新检查组信息
    **/
    @RequestMapping("/findById")
    //查询检查组checkgroup的信息
    /*@RequestBody表示从请求体获取参数*/

    public Result findById(int checkGroupId){
        //1.编辑检查组时检查组的数据回显
        //调用服务
        CheckGroup checkGroup=checkGroupService.findById(checkGroupId);

        //响应结果
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    //更新检查项checkitem的信息（在t_CheckItemIdsByCheckGroupId中查询）
    public Result findCheckItemIdsByCheckGroupId(int checkGroupId){
        //1.编辑检查组时检查项的数据回显（已勾选的）,返回一个前端需要的checkitemid集合
        //调用服务
        List<Integer> checkItemIds=checkGroupService.findCheckItemIdsByCheckGroupId(checkGroupId);
        //响应结果
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
    }

    @RequestMapping("/update")
    //修改检查组checkitem的信息（在t_CheckItemIdsByCheckGroupId中查询）
    public Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        //1.修改检查组信息
        //调用服务
        checkGroupService.update(checkGroup,checkitemIds);
        //响应结果
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @PostMapping("/deleteById")
    //修改检查组checkitem的信息（在t_CheckItemIdsByCheckGroupId中查询）
    //id通过携带参数传到后端
    public Result deleteById(int id){
        //1.修改检查组信息
        //调用服务
        checkGroupService.deleteById(id);
        //响应结果
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }


    @GetMapping("/findAll")
    //接收get请求
    public Result findAll(){
        //调用服务，直接查询所有即可
        List<CheckGroup> checkGroupList=checkGroupService.findAll();
        //响应结果
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS,checkGroupList);
    }


}
