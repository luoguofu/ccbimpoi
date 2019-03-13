package com.weqia.utils.data;

import com.weqia.data.UtilData;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;

@Table(name="local_net_path")
public class LocalNetPath extends UtilData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private @Id String lnId;
    private String localPath;
    private String netPath;
    private Long cTime;
    private String contentUri;
    private int type;
    private String id;
    private String attData;
    
    public LocalNetPath() {
    }

    public LocalNetPath(String localPath, String netPath, int type) {
        this(localPath, netPath, null, null, type);
    }
    
    public LocalNetPath(String localPath, String netPath, String id, String attData, int type) {
        this.localPath = localPath;
        this.netPath = netPath;
        this.id = id;
        this.attData = attData;
        this.type = type;
        this.lnId = netPath+"|"+type;
        this.cTime = System.currentTimeMillis();
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }

    public Long getcTime() {
        return cTime;
    }

    public void setcTime(Long cTime) {
        this.cTime = cTime;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttData() {
        return attData;
    }

    public void setAttData(String attData) {
        this.attData = attData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLnId() {
        return lnId;
    }

    public void setLnId(String lnId) {
        this.lnId = lnId;
    }
}
