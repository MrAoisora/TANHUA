package com.tanhua.server.controller;

import com.tanhua.domain.db.User;
import com.tanhua.server.serivce.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 测试1：根据手机号码查询
     */
    @GetMapping("/findByMobile")
    public ResponseEntity<Object> findByMobile(String mobile){
        return userService.findByMobile(mobile);
    }
    /**
     * 测试2：保存手机号码
     */
    @PostMapping("saveUser")
    public ResponseEntity<Object> saveUser(@RequestBody User user){
       return userService.saveUser(user);
    }


    /**
     * 接口名称：登录第一步---手机号登录
     * 接口路径：POST/user/login
     * 需求描述：给传入的手机号码发送短信验证码
     */
    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody Map<String,String> paramMap){
        log.info("接口名称：登录第一步---手机号登录");
        String phone = paramMap.get("phone");
        return userService.login(phone);
    }

    /**
     * 接口名称：登录第二步---验证码校验
     * 接口路径：POST/user/loginVerification
     * 需求描述：根据用户输入的验证码进行校验
     */
    @PostMapping("loginVerification")
    public ResponseEntity<Object> loginVerification(@RequestBody Map<String,String> paramMap){
        log.info("接口名称：登录第二步---验证码校验");
        // 获取请求数据
        String phone = paramMap.get("phone");
        String verificationCode = paramMap.get("verificationCode");
        // 调用service校验
        return userService.loginVerification(phone,verificationCode);
    }

}
