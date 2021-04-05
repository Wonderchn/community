package com.hongna.community.dao;

import java.io.IOException;
import java.io.InvalidObjectException;

public class WKTests {
    public static void main(String[] args) throws IOException {
        String cmd ="D:/develop/wkhtmltopdf/bin/wkhtmltoimage --quality 75  https://www.nowcoder.com D:/work/data/wk-image/2.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok");
        }catch (InvalidObjectException e){
            e.printStackTrace();
        }
    }
}
