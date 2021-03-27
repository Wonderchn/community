package com.hongna.community.util;

import com.hongna.community.CommnityApplication;
import com.hongna.community.dao.LoginTicketMapper;
import com.hongna.community.entity.LoginTicket;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;


@SpringBootTest
class MailClientTest {
    @Resource
    LoginTicketMapper loginTicketMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void testTextMail(){
        mailClient.sendMail("2568084855@qq.com",  "TEST", "WELCOME");
    }
    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "sunday");
        String content = templateEngine.process("mail/demo", context);
        System.out.println(context);
        mailClient.sendMail("a1073506500@qq.com", "HTML", content);


    }
    @Test
    public void testInserLoginTicket()
    {
//        LoginTicket loginTicket = new LoginTicket();
//        loginTicket.setUserId(101);
//        loginTicket.setTicket("abc");
//        loginTicket.setStatus(0);
//        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000*60*10));
//        loginTicketMapper.insertLoginTicket(loginTicket);
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        loginTicketMapper.updateStatus("abc",1);
        System.out.println(loginTicket);
    }}