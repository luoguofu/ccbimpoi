package com.weqia.wq.data;

import com.weqia.wq.data.global.WeqiaApplication;

/**
 * 登录用户
 *
 * @author Dminter
 */
public class LoginUserData extends BaseData {

    /**
     * @Description
     * @author
     * @create at 2013-4-7 上午9:28:47
     */

    private static final long serialVersionUID = 1L;

    private String mid = "";
    private String key = "";
    private String roleId = "1";// 1普通员工 2 管理员 3超级管理员
    private String coId = "";
    private String coName = "";
    private String coLogo = "";
    private String mName = "";
    private String mNo = "";
    private String mLogo = "";
    private String joinStatus = "1";// 1已加入 2 申请中 3未申请 // [状态 1-自动加入 2-待审核 3-同意 4-拒绝 5-取消]
    private String inCoName = "";
    private String inCoId = "";
    private Long loginDate;
    private String mobileId;
    private String pwdStatus;// 是否设置了密码 1-已设置 2-未设置
    private Integer isModifypwd;
    private String currentProjectId;    //当前项目Id

    private Integer teamRoleId;//施工项目人员角色 1项目经理 2班组长 3工人
    private boolean tourist = false;

    public enum teamRoleType {
        MANAGER(1, "项目经理"), LEADER(2, "班组长"), WORKER(3, "工人");
        private String strName;
        private int value;

        private teamRoleType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public static teamRoleType valueOf(int value) {
            for (teamRoleType typeEnum : teamRoleType.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return WORKER;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }

    }


    public String getPwdStatus() {
        return pwdStatus;
    }

    public void setPwdStatus(String pwdStatus) {
        this.pwdStatus = pwdStatus;
    }

    public String getCoId() {
        return coId;
    }

    public void setCoId(String coId) {
        WeqiaApplication.setgMCoId(coId);
        this.coId = coId;
    }


    public LoginUserData() {
    }

    public LoginUserData(String mid) {
        this.mid = mid;
    }

    public String getInCoName() {
        return inCoName;
    }

    public void setInCoName(String inCoName) {
        this.inCoName = inCoName;
    }

    public String getInCoId() {
        return inCoId;
    }

    public void setInCoId(String inCoId) {
        this.inCoId = inCoId;
    }

    public String getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(String joinStatus) {
        this.joinStatus = joinStatus;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNo() {
        return mNo;
    }

    public void setmNo(String mNo) {
        this.mNo = mNo;
    }

    public String getmLogo() {
        return mLogo;
    }

    public void setmLogo(String mLogo) {
        this.mLogo = mLogo;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getCoLogo() {
        return coLogo;
    }

    public void setCoLogo(String coLogo) {
        this.coLogo = coLogo;
    }

    /**
     * 返回 loginDate 的值
     *
     * @return loginDate
     */

    public Long getLoginDate() {
        return loginDate;
    }

    /**
     * 设置 loginDate 的值
     *
     * @param loginDate
     */
    public void setLoginDate(Long loginDate) {
        this.loginDate = loginDate;
    }

    /**
     * 返回 mobileId 的值
     *
     * @return mobileId
     */

    public String getMobileId() {
        return mobileId;
    }

    /**
     * 设置 mobileId 的值
     *
     * @param mobileId
     */
    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }


    public Integer getIsModifypwd() {
        return isModifypwd;
    }

    public void setIsModifypwd(Integer isModifypwd) {
        this.isModifypwd = isModifypwd;
    }


    public Integer getTeamRoleId() {
        return teamRoleId;
    }

    public void setTeamRoleId(Integer teamRoleId) {
        this.teamRoleId = teamRoleId;
    }

    public String getCurrentProjectId() {
        return currentProjectId;
    }

    public void setCurrentProjectId(String currentProjectId) {
        this.currentProjectId = currentProjectId;
    }

    public boolean isTourist() {
        return tourist;
    }

    public void setTourist(boolean tourist) {
        this.tourist = tourist;
    }
}
