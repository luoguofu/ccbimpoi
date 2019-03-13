package com.weqia.wq.service;

/**
 * Created by 20161005 on 2017/6/15.
 */

public interface SerachInterface {
    /**
     * 状态值
     */
    int value();

    /**
     * 状态的描述
     */
    String description();

    /**
     * key
     */
    String key();

    /**
     * 是否是选择人员
     */
    boolean isSelectMan();


}
