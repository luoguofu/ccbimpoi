package com.weqia.wq.component.utils.autoupdate;


import com.weqia.wq.data.BaseData;

/**
 * <br/>Author:hihiwjc
 * <br/>Email:hihiwjc@live.com
 * <br/>Date:2016/8/2 0002
 * <br/>Func:
 */
public class VersionData extends BaseData {
    private String versionName;//最新版本名称
    private int versionCode;//最新版本号
    private String versionContent;//更新日志
    private String downloadUrl;//apk下载地址
    private double size;//文件大小,单位为MB
    private int coercivenessUpgrade;//是否强制升级
    private int coercivenessCode;//在此版本下的版本必须强制升级
    private int detectionUpgrade;//检测升级
    private int automateUpgrade;//自动升级

    public int getAutomateUpgrade() {
        return automateUpgrade;
    }

    public void setAutomateUpgrade(int automateUpgrade) {
        this.automateUpgrade = automateUpgrade;
    }

    public boolean getCoercivenessUpgrade(int curVerCode) {
        return coercivenessUpgrade == 1&&curVerCode<coercivenessCode;
    }

    public int getCoercivenessCode() {
        return coercivenessCode;
    }

    public void setCoercivenessCode(int coercivenessCode) {
        this.coercivenessCode = coercivenessCode;
    }

    public int getDetectionUpgrade() {
        return detectionUpgrade;
    }

    public void setDetectionUpgrade(int detectionUpgrade) {
        this.detectionUpgrade = detectionUpgrade;
    }

    public int getConstraintUpdate() {
        return coercivenessUpgrade;
    }

    public void setCoercivenessUpgrade(int coercivenessUpgrade) {
        this.coercivenessUpgrade = coercivenessUpgrade;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionContent() {
        return versionContent;
    }

    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
    }
}
