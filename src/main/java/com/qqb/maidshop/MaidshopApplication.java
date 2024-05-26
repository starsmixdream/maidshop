package com.qqb.maidshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan("com.qqb.maidshop.mapper")
@PropertySource("classpath:application.yml")
public class MaidshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaidshopApplication.class, args);
    }

}
