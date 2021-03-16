package com.hongna.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class Comment {
//    评论的id
    private int id;
//    发出评论|回复的 用户id
    private int user_id;
//    判断这个是评论还是回复
    private int entity_type;
//    评论的是哪一个帖子
    private int entity_id;
//    指向的是哪一个回复或评论
    private int target_id;
//    回复或评论显示的内容
    private String content;
//    评论的状态
    private int status;
//    评论创建的时间
    private Date create_time;
}
