package com.tanhua.commons.config;

import com.tanhua.commons.properties.SmsProperties;
import com.tanhua.commons.template.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SmsProperties.class
})
public class TanhuaAutoConfiguration {

    /**
     * @Bean
     * 1、修饰方法，自动把方法返回的结果加入容器
     * 2、如果方法有参数，自动去容器找该类型的参数注入到方法中。
     */
    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties){
        return new SmsTemplate(smsProperties);
    }
}
