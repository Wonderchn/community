package com.hongna.community.util;

import java.util.UUID;

public class CommunityUtil {
    //生成随机字符串
    public static String generrateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");

    }
    //MD5加密
    //hello -> abc123def456
    //hello
}
