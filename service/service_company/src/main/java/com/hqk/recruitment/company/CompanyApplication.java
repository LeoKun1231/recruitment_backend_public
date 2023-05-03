package com.hqk.recruitment.company;


import com.hqk.recruitment.filter.MyFeignClientInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.hqk.recruitment.company.mapper")
@ComponentScan(value = "com.hqk")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hqk.recruitment")
public class CompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }

    /**
     * 以免远程调用报401
     * @return
     */
    @Bean
    public MyFeignClientInterceptor myFeignClientInterceptor(){
        return new MyFeignClientInterceptor();
    }
}
