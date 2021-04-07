package com.hongna.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan("com.hongna.community.dao")
@EnableScheduling
public class CommnityApplication {
    //解决Netty启动冲突问题
    @PostConstruct
    public void init(){
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }
    public static void main(String[] args) {
        SpringApplication.run( CommnityApplication.class,args);
    }

}
