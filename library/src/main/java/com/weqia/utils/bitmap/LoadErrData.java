package com.weqia.utils.bitmap;

import com.weqia.data.UtilData;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;

@Table(name = "load_err_data")
public class LoadErrData extends UtilData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private @Id
    String localPath;
    private long mtime = System.currentTimeMillis();
    
    public LoadErrData() {
    }

    public LoadErrData(String localPath) {
        this.localPath = localPath;
    }
    
    public LoadErrData(String localPath, long time) {
        this.localPath = localPath;
        this.mtime = time;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getMtime() {
        return mtime;
    }

    public void setMtime(long mtime) {
        this.mtime = mtime;
    }
}
