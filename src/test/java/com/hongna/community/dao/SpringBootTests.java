package com.hongna.community.dao;


import com.hongna.community.CommnityApplication;
import com.hongna.community.entity.DiscussPost;
import com.hongna.community.service.DiscussPostService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommnityApplication.class)
public class SpringBootTests {

    @Autowired
    private DiscussPostService discussPostService;

    private  DiscussPost data;

    @BeforeClass
    public static void beforeClass(){
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass");
    }

    @Before
    public void before(){
        System.out.println("before");
         data = new DiscussPost();
        data.setUserId(111);
        data.setTitle("Test title");
        data.setContent("Test content");
        data.setCreateTime(new Date());
    }


    @After
    public void after(){
        System.out.println("after");
    }

    @Test
    public void test1(){
        System.out.println("test1");
    }

    @Test
    public void test2(){
        System.out.println("test2");
    }

    @Test
    public void testFindByID(){
        DiscussPost post = discussPostService.findDiscussPostById(data.getId());
        Assert.assertNotNull(post);
        Assert.assertEquals(data.getTitle(), post.getTitle());

    }

    @Test
    public void testUpdateScore(){
        int i = discussPostService.updateScore(data.getId(), 2000);
        Assert.assertEquals(1, i);
        DiscussPost post = discussPostService.findDiscussPostById(data.getId());
        Assert.assertEquals(2000, post.getScore(),2);
    }
}
