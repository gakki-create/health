package com.myfactory.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.entity.Result;
import com.myfactory.health.pojo.Setmeal;
import com.myfactory.health.service.SetmealService;
import com.myfactory.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Gakki
 * @version 1.0
 * @description: 套餐列表展示
 * @date 2021/1/10 19:48
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    @GetMapping("/getSetmeal")
    public Result findAll(){
        List<Setmeal> setmealList=setmealService.findAll();
        //循环遍历setmealList，取出Img的值，拼接成图片的全路径,使用七牛的工具类来拼接
        setmealList.forEach(s->{
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
      /*
        for (Setmeal setmeal : setmealList) {
            setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
    }*/
      return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmealList);
    }


}
