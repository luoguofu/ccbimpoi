package com.weqia.wq.data.newdemand;

import com.weqia.wq.data.BaseData;

public class MemberProjectPower extends BaseData {

    private String userID;
    private String pjId;
    /*超级管理员*/
    private boolean isProjectSuperAdmin = false;
    /*项目管理员*/
    private boolean isProjectManager= false;


    /*文件审阅权限*/
    private boolean isFileQueryPower= false;
    /*文件上传权限*/
    private boolean isFileUploadPower= false;
    /*文件下载权限*/
    private boolean isFileDownLoadPower= false;
    /*文件删除权限*/
    private boolean isFileDeletePower= false;
    /*文件重命名权限*/
    private boolean isFileRenamePower= false;


    /*模型审阅权限*/
    private boolean isModelQueryPower= false;
    /*模型上传权限*/
    private boolean isModelUploadPower= false;
    /*模型下载权限*/
    private boolean isModelDownLoadPower= false;
    /*模型删除权限*/
    private boolean isModelDeletePower= false;
    /*模型重命名权限*/
    private boolean isModelRenamePower= false;

    /*图纸审阅权限*/
    private boolean isDrawingQueryPower= false;
    /*图纸上传权限*/
    private boolean isDrawingUploadPower= false;
    /*图纸下载权限*/
    private boolean isDrawingDownLoadPower= false;
    /*图纸删除权限*/
    private boolean isDrawingDeletePower= false;
    /*图纸重命名权限*/
    private boolean isDrawingRenamePower= false;


    /*土建管理员*/
    private boolean isCadTjslManager= false;
    /*安装管理员*/
    private boolean isCadAzslManager= false;
    /*5D施工 管理员*/
    private boolean isBim5DSgManager= false;
    /*5D咨询 管理员*/
    private boolean isBim5DZxManager= false;
    /*脚手架管理员*/
    private boolean isBimJsjManager= false;
    /*场布管理员*/
    private boolean isBimSgChManager= false;
    /*Revit*/
    private boolean isRevitAzslManager= false;
    /*模板软件*/
    private boolean isBimMbManager= false;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }


    /*项目看板权限*/
   /* public boolean IsProjectBoardManager;*/

    public boolean isProjectSuperAdmin() {
        return isProjectSuperAdmin;
    }

    public void setProjectSuperAdmin(boolean projectSuperAdmin) {
        isProjectSuperAdmin = projectSuperAdmin;
    }

    public boolean isProjectManager() {
        return isProjectManager;
    }

    public void setProjectManager(boolean projectManager) {
        isProjectManager = projectManager;
    }

    public boolean isFileQueryPower() {
        return isFileQueryPower;
    }

    public void setFileQueryPower(boolean fileQueryPower) {
        isFileQueryPower = fileQueryPower;
    }

    public boolean isFileUploadPower() {
        return isFileUploadPower;
    }

    public void setFileUploadPower(boolean fileUploadPower) {
        isFileUploadPower = fileUploadPower;
    }

    public boolean isFileDownLoadPower() {
        return isFileDownLoadPower;
    }

    public void setFileDownLoadPower(boolean fileDownLoadPower) {
        isFileDownLoadPower = fileDownLoadPower;
    }

    public boolean isFileDeletePower() {
        return isFileDeletePower;
    }

    public void setFileDeletePower(boolean fileDeletePower) {
        isFileDeletePower = fileDeletePower;
    }

    public boolean isFileRenamePower() {
        return isFileRenamePower;
    }

    public void setFileRenamePower(boolean fileRenamePower) {
        isFileRenamePower = fileRenamePower;
    }

    public boolean isModelQueryPower() {
        return isModelQueryPower;
    }

    public void setModelQueryPower(boolean modelQueryPower) {
        isModelQueryPower = modelQueryPower;
    }

    public boolean isModelUploadPower() {
        return isModelUploadPower;
    }

    public void setModelUploadPower(boolean modelUploadPower) {
        isModelUploadPower = modelUploadPower;
    }

    public boolean isModelDownLoadPower() {
        return isModelDownLoadPower;
    }

    public void setModelDownLoadPower(boolean modelDownLoadPower) {
        isModelDownLoadPower = modelDownLoadPower;
    }

    public boolean isModelDeletePower() {
        return isModelDeletePower;
    }

    public void setModelDeletePower(boolean modelDeletePower) {
        isModelDeletePower = modelDeletePower;
    }

    public boolean isModelRenamePower() {
        return isModelRenamePower;
    }

    public void setModelRenamePower(boolean modelRenamePower) {
        isModelRenamePower = modelRenamePower;
    }

    public boolean isDrawingQueryPower() {
        return isDrawingQueryPower;
    }

    public void setDrawingQueryPower(boolean drawingQueryPower) {
        isDrawingQueryPower = drawingQueryPower;
    }

    public boolean isDrawingUploadPower() {
        return isDrawingUploadPower;
    }

    public void setDrawingUploadPower(boolean drawingUploadPower) {
        isDrawingUploadPower = drawingUploadPower;
    }

    public boolean isDrawingDownLoadPower() {
        return isDrawingDownLoadPower;
    }

    public void setDrawingDownLoadPower(boolean drawingDownLoadPower) {
        isDrawingDownLoadPower = drawingDownLoadPower;
    }

    public boolean isDrawingDeletePower() {
        return isDrawingDeletePower;
    }

    public void setDrawingDeletePower(boolean drawingDeletePower) {
        isDrawingDeletePower = drawingDeletePower;
    }

    public boolean isDrawingRenamePower() {
        return isDrawingRenamePower;
    }

    public void setDrawingRenamePower(boolean drawingRenamePower) {
        isDrawingRenamePower = drawingRenamePower;
    }

    public boolean isCadTjslManager() {
        return isCadTjslManager;
    }

    public void setCadTjslManager(boolean cadTjslManager) {
        isCadTjslManager = cadTjslManager;
    }

    public boolean isCadAzslManager() {
        return isCadAzslManager;
    }

    public void setCadAzslManager(boolean cadAzslManager) {
        isCadAzslManager = cadAzslManager;
    }

    public boolean isBim5DSgManager() {
        return isBim5DSgManager;
    }

    public void setBim5DSgManager(boolean bim5DSgManager) {
        isBim5DSgManager = bim5DSgManager;
    }

    public boolean isBim5DZxManager() {
        return isBim5DZxManager;
    }

    public void setBim5DZxManager(boolean bim5DZxManager) {
        isBim5DZxManager = bim5DZxManager;
    }

    public boolean isBimJsjManager() {
        return isBimJsjManager;
    }

    public void setBimJsjManager(boolean bimJsjManager) {
        isBimJsjManager = bimJsjManager;
    }

    public boolean isBimSgChManager() {
        return isBimSgChManager;
    }

    public void setBimSgChManager(boolean bimSgChManager) {
        isBimSgChManager = bimSgChManager;
    }

    public boolean isRevitAzslManager() {
        return isRevitAzslManager;
    }

    public void setRevitAzslManager(boolean revitAzslManager) {
        isRevitAzslManager = revitAzslManager;
    }

    public boolean isBimMbManager() {
        return isBimMbManager;
    }

    public void setBimMbManager(boolean bimMbManager) {
        isBimMbManager = bimMbManager;
    }

    /**
     * 判断项目成员是否有权限，负责人和管理员权限全开，其他成员看具体权限
     */
    public boolean isPower(boolean isPower) {
        if (isProjectSuperAdmin() || isProjectManager()) {
            return true;
        } else {
            return isPower;
        }
    }
    /**
     * 判断是否是管理员
     */
    public boolean isManagerPower() {
        if (isProjectSuperAdmin() || isProjectManager()) {
            return true;
        } else {
            return false;
        }
    }
}
