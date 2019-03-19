package com.example.ccbim.ccbimpoi.data;

import com.weqia.wq.data.BaseData;

import java.util.ArrayList;

/**
 * Created by lgf on 2019/3/14.
 */

public class CheckDetailData extends BaseData {
    private CellData checkName;       //验收的名称
    private CellData checkStandard;       //验收的标准
    private CellData checkPass;       //验收要求中的合格
    private CellData checkInvolve;       //验收要求中的涉不涉及
    private CellData checkPic;       //附图
    private String remind;          //详情中的备注
    private String samplePic;          //示例图片
    private String problemInfo;      //问题描述
    private String problemDemand;       //整改要求
    private ArrayList<String> picPaths;           //附件
    private String picPathsStr;           //附件
    private int status = 0;              //默认是不显示，1是√符合要求,2是不涉及√ ，3是X  需要整改
    private String checkPath;         //验收路径 如： 结构闭水-排水管洞周围处理

    public CellData getCheckName() {
        return checkName;
    }

    public void setCheckName(CellData checkName) {
        this.checkName = checkName;
    }

    public CellData getCheckStandard() {
        return checkStandard;
    }

    public void setCheckStandard(CellData checkStandard) {
        this.checkStandard = checkStandard;
    }

    public CellData getCheckPass() {
        return checkPass;
    }

    public void setCheckPass(CellData checkPass) {
        this.checkPass = checkPass;
    }

    public CellData getCheckInvolve() {
        return checkInvolve;
    }

    public void setCheckInvolve(CellData checkInvolve) {
        this.checkInvolve = checkInvolve;
    }

    public CellData getCheckPic() {
        return checkPic;
    }

    public void setCheckPic(CellData checkPic) {
        this.checkPic = checkPic;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getSamplePic() {
        return samplePic;
    }

    public void setSamplePic(String samplePic) {
        this.samplePic = samplePic;
    }

    public String getProblemInfo() {
        return problemInfo;
    }

    public void setProblemInfo(String problemInfo) {
        this.problemInfo = problemInfo;
    }

    public String getProblemDemand() {
        return problemDemand;
    }

    public void setProblemDemand(String problemDemand) {
        this.problemDemand = problemDemand;
    }

    public ArrayList<String> getPicPaths() {
        return picPaths;
    }

    public void setPicPaths(ArrayList<String> picPaths) {
        this.picPaths = picPaths;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPicPathsStr() {
        return picPathsStr;
    }

    public void setPicPathsStr(String picPathsStr) {
        this.picPathsStr = picPathsStr;
    }

    public String getCheckPath() {
        return checkPath;
    }

    public void setCheckPath(String checkPath) {
        this.checkPath = checkPath;
    }
}
