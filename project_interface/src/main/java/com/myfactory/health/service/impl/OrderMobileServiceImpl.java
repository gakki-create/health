package com.myfactory.health.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.dao.MemberDao;
import com.myfactory.health.dao.OrderDao;
import com.myfactory.health.dao.OrderSettingDao;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.Member;
import com.myfactory.health.pojo.Order;
import com.myfactory.health.pojo.OrderSetting;
import com.myfactory.health.service.OrderMobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Gakki
 * @version 1.0
 * @description: 提交体检预约
 * @date 2021/1/12 17:54
 */
@Service(interfaceClass = OrderMobileService.class)
public class OrderMobileServiceImpl implements OrderMobileService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    @Transactional//事务控制
    public Order submitOrder(Map<String, String> orderInfo) throws HealthException {
        //1. 通过日期查询预约设置信息，日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //  预约日期 前端来
        Date orderDate = null;
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约,查询t_ordersetting
        try {
            orderDate = sdf.parse(orderInfo.get("orderDate"));

        } catch (ParseException e) {

            throw new HealthException("日期格式错误，请重新输入");
        }

        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);

        if (orderSetting == null) {
            //说明当前日期不能预约
            throw new HealthException("当前日期不能预约,请重新选择日期");

        }
        //说明当前日期可以预约
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            //说明当前日期预约满了
            throw new HealthException("当前日期预约满了，请选择其他日期预约");
        }

        //说明当前日期可以进行预约，根据手机号码查询用户是否为会员
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = orderInfo.get("telephone");
        Member member = memberDao.findByTelephone(telephone);

        //存在才需要判断是否重复预约
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        if (member != null) {
            //说明会员存在，判断是否已预约
            order.setMemberId(member.getId());
            //判断是否重复预约
            List<Order> orderList = orderDao.findByCondition(order);
            if (!CollectionUtils.isEmpty(orderList)) {
                //说明已经预约过了
                throw new HealthException("已经预约过了，不可重复预约");
            }


        }else {
            //不存在
            member = new Member();
            // name 从前端来
            member.setName(orderInfo.get("name"));
            // sex  从前端来
            member.setSex(orderInfo.get("sex"));
            // idCard 从前端来
            member.setIdCard(orderInfo.get("idCard"));
            // phoneNumber 从前端来
            member.setPhoneNumber(telephone);
            // regTime 系统时间
            member.setRegTime(new Date());
            // password 可以不填，也可生成一个初始密码
            member.setPassword("12345678");
            // remark 自动注册
            member.setRemark("由预约而注册上来的");
            //   添加会员
            memberDao.add(member);
            order.setMemberId(member.getId());
        }

        //程序走到这，说明会员一定已经注册了，而且可以进行预约

        // 预约类型
        order.setOrderType(orderInfo.get("orderType"));
        // 预约状态
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //添加t_order 预约信息
        orderDao.add(order);
        // 更新已预约人数, 更新成功则返回1，数据没有变更则返回0

        int affectedCount = orderSettingDao.editReservationsByOrderDate(orderSetting);

        if(affectedCount == 0){

            throw new HealthException(MessageConstant.ORDER_FULL);

        }
        //5. 返回新添加的订单对象
        return order;
    }
}
