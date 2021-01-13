package com.myfactory.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.myfactory.health.constant.MessageConstant;
import com.myfactory.health.constant.RedisMessageConstant;
import com.myfactory.health.entity.Result;
import com.myfactory.health.utils.SMSUtils;
import com.myfactory.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Gakki
 * @version 1.0
 * @description: 体检预约功能的实现
 * @date 2021/1/12 16:32
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

/**
*
* @param: telephone
* @return: com.myfactory.health.entity.Result
* @author Gakki
 *
 * 验证码的发送功能：使用到的技术：redis缓存技术，SMSutils发送短信技术
 *
**/
    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {

        //发送验证码给前端,前端携带手机号码给后端进行处理

        //1.判断redis中有没有验证码
        Jedis jedis = jedisPool.getResource();

        String key = RedisMessageConstant.SENDTYPE_GETPWD + "_" + telephone;

        //1.1取出redis中的验证码，判断是否存在
        String code = jedis.get(key);

        if (code == null) {
            //1.3说明验证码不存在，要给该手机号码发送验证码
            //2.生成6位数的验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
            //2.1调用SMSUtils发送验证码
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode + "");
                //验证码发送成功后，要存入redis中，保存十分钟

                jedis.setex(key, 10 * 60, validateCode + "");

                //返回成功的结果给前端

                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);

            } catch (ClientException e) {

                e.printStackTrace();

                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }

        }
        //关闭资源
        jedis.close();
        //走到这，说明验证码已经存在了，提示给用户：验证码已存在
        return new Result(false, MessageConstant.SENT_VALIDATECODE);
    }
    @PostMapping("/send4Login")
    public Result send4Login(String telephone) {

        //发送验证码给前端,前端携带手机号码给后端进行处理

        //1.判断redis中有没有验证码
        Jedis jedis = jedisPool.getResource();

        String key = RedisMessageConstant.SENDTYPE_LOGIN+ "_" + telephone;

        //1.1取出redis中的验证码，判断是否存在
        String code = jedis.get(key);

        if (code == null) {
            //1.3说明验证码不存在，要给该手机号码发送验证码
            //2.生成6位数的验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
            //2.1调用SMSUtils发送验证码
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode + "");
                //验证码发送成功后，要存入redis中，保存十分钟

                jedis.setex(key, 10 * 60, validateCode + "");

                //返回成功的结果给前端

                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);

            } catch (ClientException e) {

                e.printStackTrace();

                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);

            }

        }
        //关闭资源
        jedis.close();
        //走到这，说明验证码已经存在了，提示给用户：验证码已存在
        return new Result(false, MessageConstant.SENT_VALIDATECODE);
    }

}
