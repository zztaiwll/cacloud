package com.cacloud.gateway;

import com.cacloud.redis.common.config.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacloudGatewayApplicationTests {
    @Autowired
    private RedisService redisService;
    @Test
    public void contextLoads() {
    }
    @Test
    public void redisTest(){
        redisService.setCacheObject("123","234");
        System.out.println(redisService.getCacheObject("123").toString());
    }

}
