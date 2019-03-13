package com.weqia.wq.msg;

import com.alibaba.fastjson.annotation.JSONField;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.utils.annotation.sqlite.Transient;
import com.weqia.wq.data.BaseData;

//wq列表
@Table(name = "talk_list")
public class TalkListData extends BaseData {
    /**
     *群聊始终置顶
     */

    private static final long serialVersionUID = 1L;
    private
    @Id
    Integer id;
    private String mid;// 聊天自己ID,会议,某人说某人的ID
    private String title;// 标题
    private String content;// 内容
    private String avatar;// 头像
    private String time;// 显示时间
    private int sort_number = 0;// 排序字段
    private int type;//业务类型 接口号
    private int status = 1;//逻辑删除-1正常2删除

    @JSONField(serialize = false)
    private
    @Transient
    Integer count = 1;

    private int business_type;  //业务类型
    private String business_id;  // 具体业务类别的唯一ID，例如：模型类别这个字段就代表模型的唯一标识nodeId
    private int level = 1;//等级 1级菜单 2级菜单 默认1级菜单


    private String coId;


    /**
     *这个动态属于哪个项目
     */
    private String pjId;
    private String parameter;


    public String getCoId() {
        return coId;
    }

    public void setCoId(String coId) {
        this.coId = coId;
    }


    public TalkListData() {
    }

    public TalkListData(int business_type, String title, String content, String avatar, int type, String mCoId, String time, int sort_number, int tType) {
        super.setgCoId(mCoId);
        this.business_type = business_type;
        this.title = title;
        this.content = content;
        this.avatar = avatar;
        this.time = TimeUtils.getLongTime();
        this.type = type;
        this.time = time;
        this.sort_number = sort_number;
    }

    public TalkListData(String business_id, String title, String content, String avatar, int type, String mCoId, String time, int tType) {
        super.setgCoId(mCoId);
        this.business_id = business_id;
        this.title = title;
        this.content = content;
        this.avatar = avatar;
        this.time = time;
        this.type = type;
    }
    
  
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSort_number() {
        return sort_number;
    }

    public void setSort_number(int sort_number) {
        this.sort_number = sort_number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public int getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(int business_type) {
        this.business_type = business_type;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }

    @Override
    public String getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
