package com.hongna.community.dao;

import com.hongna.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
//    按评论类型来查询帖子
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);

//    根据回复种类和所评论帖子的id 得到回复的总数量
    int selectCountByEntity(int entityType,int entityId);
}
