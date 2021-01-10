package com.myfactory.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.entity.Result;
import com.myfactory.health.pojo.OrderSetting;
import com.myfactory.health.service.OrderSettingService;
import com.myfactory.health.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Gakki
 * @version 1.0
 * @description: 文件下载功能的实现
 * @date 2021/1/9 16:58
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){

        try {
            //1.使用POI工具类读取文件内容
            List<String[]> excel = POIUtils.readExcel(excelFile);
            //2.把 List<String[]> 转换成List<OrderSetting>
            List<OrderSetting> orderSettingList = new ArrayList<>();
            //3.日期格式解析
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            Date orderDate = null;
            OrderSetting os = null;
            for (String[] strings : excel) {
                orderDate =sdf.parse(strings[0]);
                int number =Integer.valueOf(strings[1]);
                os = new OrderSetting(orderDate,number);
                orderSettingList.add(os);
            }
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }


    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        //返回一个map集合给前端data模型
        List<Map<String, Integer>> data=orderSettingService.getOrderSettingByMonth(month);

        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,data);

    }

    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){

        orderSettingService.editNumberByDate(orderSetting);

        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);

    }




}
