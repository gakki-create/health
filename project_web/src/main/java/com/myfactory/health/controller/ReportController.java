package com.myfactory.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.entity.Result;
import com.myfactory.health.pojo.Setmeal;
import com.myfactory.health.service.ReportService;
import com.myfactory.health.service.SetmealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myfactory.health.constant.MessageConstant.GET_BUSINESS_REPORT_SUCCESS;

/**
 * @author Gakki
 * @version 1.0
 * @description: TODO
 * @date 2021/1/15 20:04
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;


    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        //调用服务查询
        //套餐数量
        List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();
        //套餐名称的集合
        List<String> setmealNames = new ArrayList<>();
        //从setmealCount集合中取出套餐名称
        if (setmealCount != null) {
            for (Map<String, Object> stringObjectMap : setmealCount) {
                //循环遍历套餐数量集合，取出套餐名称
                setmealNames.add((String) stringObjectMap.get("name"));
            }
        }
        //封装返回的结果

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",setmealCount);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res){
        //获取模板的路径template/report_template.xlsx
        String template = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
    }

    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() throws Exception {

        Map<String,Object> businessReport = reportService.getBusinessReport();


      return new Result(true,GET_BUSINESS_REPORT_SUCCESS,businessReport);
    }



}
