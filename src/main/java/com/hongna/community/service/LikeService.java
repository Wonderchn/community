package com.hongna.community.service;


import com.hongna.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    public void like(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        //第一次点是点赞，第二次点是取消赞
        //先判断是否点过赞  value是set集合存的是userId
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if(isMember){
            //说明点过赞，这次是取消赞
            redisTemplate.opsForSet().remove(entityLikeKey,userId);
        }else{
            //说明是第一次点赞
            redisTemplate.opsForSet().add(entityLikeKey,userId);
        }
    }

    //查询实体点赞的数量
    public long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体的点赞状态
    //返回int 是为了以后业务扩展 比如点了踩啥的记录状态
    public int findEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId)?1:0;
    }
}
