package com.myfactory.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.constant.RedisMessageConstant;
import com.myfactory.health.entity.Result;
import com.myfactory.health.exception.HealthException;
import com.myfactory.health.pojo.Member;
import com.myfactory.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author Gakki
 * @version 1.0
 * @description: 手机号码登录
 * @date 2021/1/13 16:49
 */

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;


    /**
     * @param: loginInfo 、res
     * @return: com.myfactory.health.entity.Result
     * @author Gakki
     * 根据电话号码来登录
     **/
    @PostMapping("/check")
    public Result checkMember(@RequestBody Map<String,String> loginInfo, HttpServletResponse res){
        //接收从前端传来的电话号码以及验证码
        String telephone = loginInfo.get("telephone");
        String validateCode = loginInfo.get("validateCode");
        //验证码的验证
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        //先判断验证码是否正确
        Jedis jedis = jedisPool.getResource();
        //从redis取出c验证码
        String code = jedis.get(key);
        if (code == null) {
            //提示用户再次发送验证码
            return new Result(false,"验证码发送失败，请重新发送");
        }
        if (!code.equals(validateCode)) {
            //提示用户验证码输入错误
            return new Result(false,"验证码输入错误，请重新输入");
        }
        //验证码使用之后要清除
        jedis.del(key);

        //再判断电话号码是否是已注册会员会员，如果不是，自动注册为会员

        Member member = memberService.findByTelephone(telephone);
        if (member==null) {
            //说明会员不存在，要进行注册

            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            member.setRemark("手机快速注册");
            memberService.add(member);

        }
        //说明会员存在，可以进行登录，并且要记录登录状态
        // 跟踪记录的手机号码，代表着会员
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setMaxAge(30*24*60*60); // 存1个月
        cookie.setPath("/"); // 访问的路径 根路径下时 网站的所有路径 都会发送这个cookie
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

}
