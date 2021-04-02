package com.hongna.community.config;

import com.hongna.community.controller.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private Alphainterceptor alphaInterceptor;
    @Resource
    private LoginTicketInterceptor loginTicketInterceptor;
    //    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;
    @Autowired
    private DataInterceptor dataInterceptor;
    @Autowired
    private MessageInterator messageInterator;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)    //不写下几行会拦截一切请求
                .excludePathPatterns("/*/*.css", "/*/*.js", "/*/*.jpg", "/*/*.jpeg")  //这么写会排除拦截一些静态资源
                .addPathPatterns("/register", "/login")      //这么写会增加拦截的路径
        ;
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/*/*.css", "/*/*.js", "/*/*.jpg", "/*/*.jpeg");
//        registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.jpg","/*/*.jpeg");

        registry.addInterceptor(messageInterator)
                .excludePathPatterns("/*/*.css", "/*/*.js", "/*/*.jpg", "/*/*.jpeg", "/*/*.png");
        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/*/*.css", "/*/*.js", "/*/*.jpg", "/*/*.jpeg", "/*/*.png");
    }
}