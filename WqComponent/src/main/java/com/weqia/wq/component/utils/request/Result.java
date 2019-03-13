package com.weqia.wq.component.utils.request;

import com.weqia.wq.data.BaseData;

/**
 * 
 * @Description :
 * @author Berwin
 * @version 1.0
 * @created 2013-3-29 上午9:29:04
 * @fileName com.weqia.wq1.data.Result.java
 * 
 */
public class Result extends BaseData {

	/**
	 * @Description
	 * @author
	 * @create at 2013-3-29 上午9:23:46
	 * 
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 返回结果字段
	 */
	private String ret;


	/**
	 * 单个对象字段
	 */
	private String object;

	/**
	 * 数组字段
	 */
	private String list;
	
	private String total;

	private String msg;

	/**
	 * 返回 ret 的值   
	 * @return ret  
	 *
	 */  
	 
	public String getRet() {
		return ret;
	}

	/**  
	 * 设置 ret 的值  
	 * @param ret
	 *
	 */
	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
