package com.myfactory.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.myfactory.health.dao.MemberDao;
import com.myfactory.health.dao.OrderDao;
import com.myfactory.health.service.ReportService;
import com.myfactory.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gakki
 * @version 1.0
 * @description: TODO
 * @date 2021/1/16 17:03
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    /**
     * 获得运营统计数据
     * Map数据格式：
     * reportDate（当前时间）--String
     * todayNewMember（今日新增会员数） -> number
     * totalMember（总会员数） -> number
     * thisWeekNewMember（本周新增会员数） -> number
     * thisMonthNewMember（本月新增会员数） -> number
     * todayOrderNumber（今日预约数） -> number
     * todayVisitsNumber（今日到诊数） -> number
     * thisWeekOrderNumber（本周预约数） -> number
     * thisWeekVisitsNumber（本周到诊数） -> number
     * thisMonthOrderNumber（本月预约数） -> number
     * thisMonthVisitsNumber（本月到诊数） -> number
     * hotSetmeal（热门套餐（取前4）） -> List<Map<String,Object>>
     */

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReport() throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        //reportDate
        Date today=new Date();
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        // 星期一
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(today));
        // 星期天
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
        // 1号
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDay4ThisMonth());
        // 本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        String reportDate = sdf.format(today);

        //=======================会员数量===========================
        //todayNewMember 今日新增会员
        int todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //totalMember
        int totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember 本周新增会员数
        int thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember 本月新增会员数
        int thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);

        //========================订单统计============================
        //todayOrderNumber 今日预约数
        int todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //todayVisitsNumber 今日到诊数
        int todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        //thisWeekOrderNumber 本周预约数
        int thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday, sunday);
        //thisWeekVisitsNumber 本周到诊数
        int thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber 本月预约数
        int thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber 本月到诊数
        int thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        //========================== 热门套餐========================
        //hotSetmeal
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        resultMap.put("reportDate",reportDate);
        resultMap.put("todayNewMember",todayNewMember);
        resultMap.put("totalMember",totalMember);
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);

        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        resultMap.put("hotSetmeal",hotSetmeal);

        return resultMap;
    }

}
