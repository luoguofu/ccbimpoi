package com.weqia.wq.data.base;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;

import java.util.Date;
import java.util.Random;

@Table(name = "notify_data")
public class NotifyData extends BaseData {
    private static final long serialVersionUID = 1L;
    private Integer notify_id;//自动生成
    private
    @Id
    String business_id;//唯一性,同一位联系人等
    private String icon;//缓存icon
    private String title;//缓存title


    public NotifyData() {
    }


    public NotifyData(String business_id, String icon, String title) {
        Random random = new Random(new Date().getTime());
        this.notify_id = random.nextInt(1000000);
        this.business_id = business_id;
        this.icon = icon;
        this.title = title;
    }

    public Integer getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(Integer notify_id) {
        this.notify_id = notify_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
