package com.weqia.wq.data;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;

@Table(name="replay_height_data")
public class ReplyHeightData extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private @Id String rId;
    private int height;
    private int type;
    private int count;
    
    public ReplyHeightData() {
    }
    
    public ReplyHeightData(String rId, int height, int type, int count) {
        super();
        this.rId = rId;
        this.height = height;
        this.type = type;
        this.setCount(count);
    }
    
    
    public String getrId() {
        return rId + "|" + type;
    }
    public void setrId(String rId) {
        this.rId = rId;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
