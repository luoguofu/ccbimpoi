package com.weqia.wq.data;

import com.weqia.wq.component.utils.request.ResultEx;

/**
 * 上传进度通知内容
 *
 * @author Berwin
 */
public class PercentData extends BaseData {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private Integer itype;
    private Boolean success;
    private ResultEx resultEx;
    private Integer errorCode;
    private Integer percent;

    public PercentData(String id, Integer itype, Boolean success, ResultEx resultEx,
                       Integer errorCode, Integer percent) {
        super();
        this.id = id;
        this.itype = itype;
        this.success = success;
        this.resultEx = resultEx;
        this.errorCode = errorCode;
        this.percent = percent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getItype() {
        return itype;
    }

    public void setItype(Integer itype) {
        this.itype = itype;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean isSuccess) {
        this.success = isSuccess;
    }

    public ResultEx getResultEx() {
        return resultEx;
    }

    public void setResultEx(ResultEx resultEx) {
        this.resultEx = resultEx;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
