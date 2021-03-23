package com.hongna.community.controller;

import com.hongna.community.annotation.LoginRequired;
import com.hongna.community.entity.User;
import com.hongna.community.service.FollowService;
import com.hongna.community.service.LikeService;
import com.hongna.community.service.UserService;
import com.hongna.community.util.CommunityConstant;
import com.hongna.community.util.CommunityUtil;
import com.hongna.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;
    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    /**
     * 传入图片
     * @param headerImage SpringMvc特有对象 可以控制前台传入对象
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/upload" , method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage == null){
            model.addAttribute("error","你没有上传任何图片");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
//
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath + "/" + filename);
        try{
            //存储文件
            headerImage.transferTo(dest);
        }catch (IOException e){
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("服务器异常");
        }
        //更新当前用户的头像的路径
        //http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    /**
     * 读取照片
     * @param fileName
     */
    @RequestMapping(path = "header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response){
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("ima ge/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }
    @LoginRequired
    @RequestMapping(path="password",method = RequestMethod.POST)
    public String changePassword(Model model,
                                 String oldPassword,
                                 String newPassword,
                                 String confirmPassword){
        if(StringUtils.isBlank(oldPassword)){
            model.addAttribute("olderror","请输入原密码");
            return "/site/setting";
        }
        if(StringUtils.isBlank(newPassword)){
            model.addAttribute("newerror","请输入新密码");
            return  "/site/setting";
        }
        if(StringUtils.isBlank(confirmPassword)){
            model.addAttribute("confirmerror","请输入确认密码");
            return "/site/setting";
        }
        if(!newPassword.equals(confirmPassword)){
            model.addAttribute("confirmerror","两次密码不一致请重新输入");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        String password = user.getPassword();
        if(!CommunityUtil.md5(oldPassword+user.getSalt()).equals(password)){
            model.addAttribute("olderror","原密码输入错误，请重新输入");
            return "/site/setting";
        }
        newPassword = CommunityUtil.md5(newPassword+user.getSalt());
        userService.updatePassword(user.getId(),newPassword);
        return "redirect:/logout";
    }

    /**
     * 个人详情页
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(path="/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }

        //用户
        model.addAttribute("user", user);
        //点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);


        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);

//粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);

//是否关注
        boolean hasFollowed = false;
        if(hostHolder.getUser() != null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);
        return "/site/profile";
    }
}
