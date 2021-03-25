package com.hongna.community.controller;

import com.hongna.community.entity.Event;
import com.hongna.community.entity.Page;
import com.hongna.community.entity.User;
import com.hongna.community.event.EventProducer;
import com.hongna.community.service.FollowService;
import com.hongna.community.service.UserService;
import com.hongna.community.util.CommunityConstant;
import com.hongna.community.util.CommunityUtil;
import com.hongna.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
public class FollowController implements CommunityConstant{
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;
    @RequestMapping(path="/followees/{userId}",method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId")int userId, Page page, Model model){
        //页面需要用username
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("/followees/"+userId);
        page.setRows((int)followService.findFolloweeCount(userId,CommunityConstant.ENTITY_TYPE_USER));
        List<Map<String, Object>> followees = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if(followees!=null){
            for(Map<String,Object> map:followees){
                //判段当前用户对这个用户的关注状态
                User followeeUser = (User)map.get("user");
                boolean hasFollowed = hasFollowed(followeeUser.getId());
                map.put("hasFollowed",hasFollowed);
            }
        }
        model.addAttribute("users",followees);
        return "/site/followee";
    }

    private boolean hasFollowed(int userId){
        if(hostHolder.getUser()==null){
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), CommunityConstant.ENTITY_TYPE_USER,userId);
    }

    @RequestMapping(path = "/follow",method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType,int entityId){
        User user = hostHolder.getUser();
        if(user==null){
            throw new RuntimeException("用户没有登录");
        }
        followService.follow(user.getId(),entityType,entityId);
        //触发关注事件
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);


        return CommunityUtil.getJsonString(0,"已关注");
    }
    @RequestMapping(path = "/unfollow",method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType,int entityId){
        User user = hostHolder.getUser();
        if(user==null){
            throw new RuntimeException("用户没有登录");
        }
        followService.unFollow(user.getId(),entityType,entityId);
        return CommunityUtil.getJsonString(0,"已取消关注");
    }

    @RequestMapping(path="/followers/{userId}",method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId")int userId, Page page, Model model){
        //页面需要用username
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("/followers/"+userId);
        page.setRows((int)followService.findFollowerCount(CommunityConstant.ENTITY_TYPE_USER,userId));
        List<Map<String, Object>> followers = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if(followers!=null){
            for(Map<String,Object> map:followers){
                //判段当前用户对这个用户的关注状态
                User followeeUser = (User)map.get("user");
                boolean hasFollowed = hasFollowed(followeeUser.getId());
                map.put("hasFollowed",hasFollowed);
            }
        }
        model.addAttribute("users",followers);
        return "/site/follower";
    }

}