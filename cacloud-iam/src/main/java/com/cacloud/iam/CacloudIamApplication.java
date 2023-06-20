package com.cacloud.iam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 1、想要远程调用别的服务
 * 1）、引入open-feign
 * 2）、编写一个接口，告诉SpringCloud这个接口需要调用远程服务
 * 1、声明接口的每一个方法都是调用哪个远程服务的那个请求
 * 3）、开启远程调用功能
 */
@MapperScan("com.cacloud.iam.dao")
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("com.cacloud.iam.feign")
@ComponentScan("com.cacloud")
public class CacloudIamApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacloudIamApplication.class, args);
    }

}
