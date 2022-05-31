package com.th.ds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.th.ds.mapper")
public class DataScopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataScopeApplication.class, args);
    }

}