package com.hongna.community.dao;

import com.hongna.community.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MessageMapperTest {
    @Autowired
    private  MessageMapper messageMapper;
    @Test
    public void mapperTest1(){
//        List<Message> messages = messageMapper.selectConversation(111, 0, 10);
//        messages.forEach(message -> System.out.println(message));
        int i = messageMapper.selectConversationCount(112);
        System.out.println(i);

        List<Message>
         messageList= messageMapper.selectLetter("111_115", 0, 5);
        System.out.println(messageList);

        int i1 = messageMapper.selectLetterCount("111_115");
        System.out.println(i1);

        int i2 = messageMapper.selectLetterUnreadCount(111, "111_115");
        System.out.println(i2);
    }
}