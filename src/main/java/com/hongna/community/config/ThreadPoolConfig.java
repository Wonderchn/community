package com.hongna.community.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskScheduler getScheduler(){
        return new ThreadPoolTaskScheduler();
    }
}