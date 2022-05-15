package org.async.codeaudit.common;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.async.codeaudit.entiy.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Component
public class tokenBean {
    @Bean
    public ExpiringMap<String, User> MakeRedis(){
        ExpiringMap<String,User> map = ExpiringMap.builder()
                .maxSize(100)//设计最大链接数
                .expiration(35, TimeUnit.MINUTES)//35分钟后自动删除
                .variableExpiration()
                .expirationPolicy(ExpirationPolicy.ACCESSED)//每次访问元素时,过期时间清零
                .build();
        return map;
    }
}
