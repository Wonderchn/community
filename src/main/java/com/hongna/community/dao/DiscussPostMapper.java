package com.hongna.community.dao;

import com.hongna.community.entity.Comment;
import com.hongna.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);



    /**
     * 插入新的帖子功能
     * @param discussPost
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 查询帖子内容
     * @param id 帖子对应的id
     * @return
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新帖子回复内容
     * @param id
     * @param commentCount
     * @return
     */
    int updateCommentCount(int id, int commentCount);

    /**
     * 更新帖子(置顶，加精)
     * @param id
     * @param type
     * @return
     */
    int updateType(int id, int type);

    /**
     * 更改帖子状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(int id, int status);

    /**
     * 更改帖子的分数 (拿来排行)
     * @param id
     * @param score
     * @return
     */
    int updateScore(int id, double score);
}
