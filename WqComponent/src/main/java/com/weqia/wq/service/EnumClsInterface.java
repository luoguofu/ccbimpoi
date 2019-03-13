package com.weqia.wq.service;

import com.weqia.wq.data.BaseData;

public interface EnumClsInterface {
	/**
     * 获取对应的类型
     * @Description
     * @return  
     *
     */
    public Class<? extends BaseData> cls();

    /**
     * 获取枚举的ID
     * 
     * @Title: order
     * @return int
     * @throws
     */
    public int order();
    
    /**
     * 修改内型
     * @Description
     * @return  
     *
     */
    public int type();
    
}