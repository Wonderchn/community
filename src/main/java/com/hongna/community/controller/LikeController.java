package com.hongna.community.controller;

import com.hongna.community.entity.User;
import com.hongna.community.service.LikeService;
import com.hongna.community.util.CommunityUtil;
import com.hongna.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * likeController 实现了点赞的功能
 */

@Controller
public class LikeController {
    @Autowired
    private LikeService  likeService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId){
        User user = hostHolder.getUser();
        //实现点赞
        likeService.like(user.getId(),entityType,entityId);
        //统计数量
        long likeCount = likeService.findEntityLikeCount(entityType,entityId);
        //状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(),entityType,entityId);
        Map<String,Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);
        return CommunityUtil.getJsonString(0,null,map);
    }
}
