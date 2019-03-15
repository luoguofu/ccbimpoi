package com.example.ccbim.ccbimpoi.data;

/**
 * Created by lgf on 2019/3/14.
 */

public class ChecDetailData {
    private CellData checkName;       //验收的名称
    private CellData checkStandard;       //验收的标准
    private CellData checkPass;       //验收要求中的合格
    private CellData checkInvolve;       //验收要求中的涉不涉及
    private CellData checkPic;       //附图

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
}
