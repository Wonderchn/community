package com.hongna.community.service;

import com.hongna.community.entity.User;
import com.hongna.community.util.CommunityConstant;
import com.hongna.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowService implements CommunityConstant {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;
    //关注
    public void follow(int userId,int entityType,int entityId){
        //还得依靠事务解决，因为有多次操作
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String followee = RedisKeyUtil.getFolloweeKey(userId,entityType);
                String follower = RedisKeyUtil.getFollowerKey(entityType,entityId);
                //启用事务
                redisOperations.multi();
                //userId关注entityId
                redisOperations.opsForZSet().add(followee,entityId,System.currentTimeMillis());
                //entityId的粉丝是userId
                redisOperations.opsForZSet().add(follower,userId,System.currentTimeMillis());
                return redisOperations.exec();
            }
        });
    }
    //取消关注
    public void unFollow(int userId,int entityType,int entityId){
        //还得依靠事务解决，因为有多次操作
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String followee = RedisKeyUtil.getFolloweeKey(userId,entityType);
                String follower = RedisKeyUtil.getFollowerKey(entityType,entityId);
                //启用事务
                redisOperations.multi();
                //userId没有关注谁
                redisOperations.opsForZSet().remove(followee,entityId);
                //谁的粉丝没有userId
                redisOperations.opsForZSet().remove(follower,userId);
                return redisOperations.exec();
            }
        });
    }
    //查询关注的实体的数量
    public long findFolloweeCount(int userId,int entityType){
        String followee = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().zCard(followee);
    }
    //查询实体的粉丝数量
    public long findFollowerCount(int entityType,int entityId){
        String follower = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(follower);
    }
    //查询当前用户是否关注该实体
    public boolean hasFollowed(int userId,int entityType,int entityId){
        String followee = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().score(followee,entityId)!=null?true:false;
    }

    //查询某个用户关注的人
    public List<Map<String,Object>> findFollowees(int userId,int offset,int limit){
        String followee = RedisKeyUtil.getFolloweeKey(userId,ENTITY_TYPE_USER);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followee, offset, offset + limit - 1);
        if(targetIds==null){
            return null;
        }
        List<Map<String,Object>> list = new ArrayList<>();
        for(Integer id:targetIds){
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserById(id);
            //用户
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(followee, id);
            //关注时间
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }
        return list;
    }

    //查询某个用户的粉丝
    public List<Map<String,Object>> findFollowers(int userId,int offset,int limit){
        String follower = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER,userId);
        //虽然返回的是set但是是redis内置实现了一个set可以有序排列
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(follower, offset, offset + limit - 1);
        if(targetIds==null){
            return null;
        }
        List<Map<String,Object>> list = new ArrayList<>();
        for(Integer id:targetIds){
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserById(id);
            //用户
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(follower, id);
            //关注时间
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }
        return list;
    }


}
