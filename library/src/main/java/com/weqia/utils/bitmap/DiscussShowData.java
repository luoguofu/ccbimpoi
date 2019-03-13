package com.weqia.utils.bitmap;

import com.weqia.data.UtilData;
import com.weqia.data.UtilsConstants;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;

/**
 * Created by berwin on 15/8/17.
 */
@Table(name = "discuss_show_data")
public class DiscussShowData extends UtilData {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dId;
    private String manIds;
    private String gCoId;
    private String prinId;
    private String title;
    private int status = -1; // -1默认状态，0，加载成功，1，加载失败，2，修改待加载
    private @Id
    String imgKey;
    private String iconStrs;

    public DiscussShowData() {}

    public DiscussShowData(String dId, String manIds, String prinId, String gCoId) {
        this.dId = dId;
        this.manIds = manIds;
        this.gCoId = gCoId;
        this.prinId = prinId;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getManIds() {
        return manIds;
    }

    public void setManIds(String manIds) {
        this.manIds = manIds;
    }

    public String getgCoId() {
        return gCoId;
    }

    public void setgCoId(String gCoId) {
        this.gCoId = gCoId;
    }

    public String getPrinId() {
        return prinId;
    }

    public void setPrinId(String prinId) {
        this.prinId = prinId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgKey() {
        return UtilsConstants.MUTIL_KEY + this.getdId();
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIconStrs() {
        return iconStrs;
    }

    public void setIconStrs(String iconStrs) {
        this.iconStrs = iconStrs;
    }
}
