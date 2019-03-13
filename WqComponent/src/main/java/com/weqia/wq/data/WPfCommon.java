package com.weqia.wq.data;

import com.weqia.data.StatedPerference;

/**
 * 不用登陆信息的文件存储，存储在一个固定文件里面
 * 
 * @author Berwin
 * 
 */
public class WPfCommon extends StatedPerference {

    private static WPfCommon instance;

    public static WPfCommon getInstance() {
        if (instance == null) {
            instance = new WPfCommon();
        }
        return instance;
    }

//    public String getPerName() {
//        return "wpf_common";
//    }
}
