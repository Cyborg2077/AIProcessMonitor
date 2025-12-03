package com.kyle.aigf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.kyle.aigf.dao")
@SpringBootApplication
public class AigfApplication {

    public static void main(String[] args) {
        SpringApplication.run(AigfApplication.class, args);
    }
}
