package com.tanhua.server.serivce;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tanhua.commons.template.SmsTemplate;
import com.tanhua.domain.db.User;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.dubbo.api.UserApi;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务类，封装业务操作，专门给控制器使用
 */
@Service
public class UserService {
    /**
     * 注入dubbo服务接口的代理对象，导入dubbo的包
     */
    @Reference
    private UserApi userApi;
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    // 保存手机验证码到redis中的key的前缀
    private final String SMS_KEY = "sms_key_";

    /**
     * 测试1：根据手机号码查询
     */
    public ResponseEntity<Object> findByMobile(String mobile){
        try {
            // 模拟错误
            String str = null;
            str.length();

            User user = userApi.findByMobile(mobile);
            // 方法返回的对象自动转换为json格式的数据
            return ResponseEntity.ok(user);// 自动把对象转换为json后返回
        } catch (Exception e) {
            e.printStackTrace();
            // 保存错误信息
            Map<String,Object> errorMap = new HashMap<>();
            errorMap.put("errorMessage","根据手机号码查询错误!");
            errorMap.put("errCode",100);
            return ResponseEntity.status(500).body(errorMap);
        }
    }
    /**
     * 测试2：保存手机号码
     */
    public ResponseEntity<Object> saveUser(User user){
        Long userId = userApi.save(user);
        return ResponseEntity.ok(userId);
    }

    /**
     * 接口名称：登录第一步---手机号登录
     * 需求描述：给传入的手机号码发送短信验证码
     */
    public ResponseEntity<Object> login(String phone) {
        //1. 生成短信验证码
        String code = (int)((Math.random() * 9 + 1) * 100000) + "";

        //2. 调用工具类发送短信
        //smsTemplate.sendSms(phone,code);
        code = "123456";

        //3  验证码存储到redis，设置有效时间5分钟
        redisTemplate.opsForValue().set(SMS_KEY+phone,code);
        return ResponseEntity.ok(null);
    }

    /**
     * 接口名称：登录第二步---验证码校验
     * 需求描述：根据用户输入的验证码进行校验
     */
    public ResponseEntity<Object> loginVerification(String phone, String verificationCode) {
        //1. 从redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get(SMS_KEY + phone);

        //2. 验证码校验，校验失败给予错误提示.  校验完成，删除redis中的验证码
        if (redisCode == null || !verificationCode.equals(redisCode)) {
            //2.1 返回错误
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        // 删除验证码
        redisTemplate.delete(SMS_KEY+phone);

        //3. 根据手机号码查询数据库
        User user = userApi.findByMobile(phone);
        Boolean isNew = false;
        //3.1 判断手机号码如果不存在，就自动注册
        if (user == null) {
            user = new User();
            user.setMobile(phone);
            user.setPassword(DigestUtils.md5Hex("123456"));
            // 保存用户
            userApi.save(user);
            // 设置为新用户
            isNew = true;
        }

        //4. 返回
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("token","");
        resultMap.put("isNew",isNew);
        return ResponseEntity.ok(resultMap);
    }
}
