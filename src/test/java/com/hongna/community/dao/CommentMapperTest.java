package com.hongna.community.dao;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;


    @Test
    public void test1(){
        int i = commentMapper.selectCountByEntity(1, 228);
        System.out.println(i);
    }
}