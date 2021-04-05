package com.hongna.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class WkConfig {
    private static final Logger logger = LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.storage}")
    private  String wkImageStorage;

    @PostConstruct
    public void init(){
        //創建圖片目錄
        File file = new File(wkImageStorage);
        if (!file.exists()){
            file.mkdir();
            logger.info("創建wk圖片目錄" + wkImageStorage);
        }
    }
}
