package com.tanhua.dubbo.test;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserApiTest {

    /**
     * 注意：
     * 1、在dubbo服务工程中，测试dubbo服务类，注入dubbo服务类时候使用@Autowired注解；
     * 2、如果在服务调用者使用dubbo服务，通过@Reference
     * 3、在dubbo服务工程中，如果要通过junit测试，必须要停止dubbo服务。
     */
    @Autowired
    private UserApi userApi;

    @Test
    public void save() {
        User user = new User();
        user.setMobile("18600110022");
        user.setPassword("123456");
        user.setCreated(new Date());
        user.setUpdated(new Date());


        Long userId = userApi.save(user);
        System.out.println("返回的主键值： = " + userId);
    }

    @Test
    public void findByMobile() {
        User user = userApi.findByMobile("18600110011");
        System.out.println("user = " + user);
    }
}
