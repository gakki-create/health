package com.myfactory.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.entity.PageResult;
import com.myfactory.health.entity.QueryPageBean;
import com.myfactory.health.entity.Result;
import com.myfactory.health.pojo.CheckGroup;
import com.myfactory.health.pojo.Setmeal;
import com.myfactory.health.service.SetmealService;
import com.myfactory.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Gakki
 * @version 1.0
 * @description: 检查套餐
 * @date 2021/1/8 17:26
 * @RestController = @Controller + @ResponseBody(表示要注入到spring容器且方法必须有返回值（Result）)
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        //获取图片的名称originalFilename，再截取图片后缀名extension
        String originalFilename = imgFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        //- 生成唯一文件名，拼接后缀名
        String filename = UUID.randomUUID() + extension;
        //- 调用七牛上传文件
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), filename);
            //- 返回数据给页面
            //{
            //    flag:
            //    message:
            //    data:{
            //        imgName: 图片名,
            //        domain: QiNiuUtils.DOMAIN
            //    }
            //}
            Map<String, String> map = new HashMap<String, String>();
            map.put("imgName", filename);
            map.put("domain", QiNiuUtils.DOMAIN);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    @PostMapping("/add")
    public Result upload(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        //调用SetmealService方法添加即可

        setmealService.add(setmeal, checkgroupIds);

        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }


    @GetMapping("/findById")
    public Result findById(int id) {
        //解决编辑时套餐信息回显问题
        //调用SetmealService方法查询套餐信息
        Setmeal setmeal = setmealService.findById(id);
        //new一个map用于封装结果集

        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap.put("setmeal", setmeal);
        resultMap.put("domain", QiNiuUtils.DOMAIN);

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, resultMap);
    }

    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id) {
        //解决编辑时检查组信息回显问题
        //根据套餐id调用SetmealService方法查询
        List<Integer> checkGroupIds = setmealService.findCheckgroupIdsBySetmealId(id);

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, checkGroupIds);
    }


    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

        //调用SetmealService方法添加即可
        //分页查询结果的封装：service中的Page（分页插件返回的结果）--》service中的Pageresult--》controller中的Result
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pageResult);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        //调用SetmealService方法更新即可
        setmealService.update(setmeal, checkgroupIds);

        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }


    @GetMapping("/delete")
    public Result delete(int id) {

        //调用SetmealService方法删除套餐信息
       setmealService.delete(id);

        return new Result(true, MessageConstant.DELEDE_SETMEAL_SUCCESS);
    }


}
