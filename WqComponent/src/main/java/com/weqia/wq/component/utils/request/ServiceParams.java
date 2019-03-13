package com.weqia.wq.component.utils.request;

import com.alibaba.fastjson.JSON;
import com.weqia.utils.StrUtil;
import com.weqia.utils.http.okgo.model.RequestParams;
import com.weqia.utils.reflect.RefUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.data.LoginUserData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

/**
 * 请求参数
 *
 * @author Berwin
 * @version 1.0
 * @Description :
 * @created 2013-3-31 上午12:15:22
 * @fileName com.weqia.wq1.utils.request.ServiceParams.java
 */
public class ServiceParams extends RequestParams {

    public static final String APP_KEY = "402880835ea8701c015eb6c8f2d30009";

    private String mid;// 用户ID
    private Integer itype;// 接口类型
    private String prevId;// 上翻
    private String nextId;// 下翻
    private Integer size;// 分页大小
    private String sign;// 签名
    private String mCoId;// 企业ID
    private String s;// 来自Android,来自苹果,来着网站...
    private Integer fileIType;
    private boolean hasMore = true; //列表是否有更多
    private boolean hasCoId = false;
    private int isEs;
    private float version = 5.6f;
    private String appKey = APP_KEY;
    private String fiIds;
    private String urls;

    private String pjId;
    private String operateId;

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public ServiceParams() {
    }

    public ServiceParams(Integer iType) {
        this(iType, null, null, null, null, WeqiaApplication
                .getgMCoId());
    }

    public ServiceParams(Integer iType, String mid) {
        this(iType, mid, null, null, null, WeqiaApplication.getgMCoId());
    }

    public ServiceParams(Integer iType, Integer prevId, Integer nextId) {
        this(iType, GlobalUtil.getStringByInt(prevId), GlobalUtil.getStringByInt(nextId));
    }

    public ServiceParams(Integer iType, String prevId, String nextId) {
        this(iType, WeqiaApplication.getInstance().getLoginUser().getMid(), prevId, nextId, GlobalConstants.PAGE_COUNT,
                WeqiaApplication.getgMCoId());
    }

    public ServiceParams(Integer iType, String mid, String prevId, String nextId) {
        this(iType, mid, prevId, nextId, GlobalConstants.PAGE_COUNT, WeqiaApplication.getgMCoId());
    }

    public ServiceParams(Integer iType, Integer count) {
        this(iType, null, null, null, count, WeqiaApplication
                .getgMCoId());
    }

    public ServiceParams(Integer iType, String mid, String prevId, String nextId, Integer size) {
        this(iType, mid, prevId, nextId, size, WeqiaApplication.getgMCoId());
    }

    public ServiceParams(Integer iType, String mid, String prevId, String nextId, Integer size, String coId) {
        setItype(iType);
        if (StrUtil.isEmptyOrNull(mid)) {
            LoginUserData loginUserData = WeqiaApplication.getInstance().getLoginUser();
            if (loginUserData != null) {
                setMid(loginUserData.getMid());  //设置当前用户
            }
        } else {
            setMid(mid);
        }
        setPrevId(prevId); //翻页设置 
        setNextId(nextId);
        setSize(size);  //设置一页的显示多少条
        if (!hasCoId) {
            setmCoId(null);
        } else {
            if (iType > 300 && StrUtil.notEmptyOrNull(coId)) {  //接口值大于三百且企业ID不为空
                setmCoId(coId);  //设置企业ID
            } else {
                setmCoId(null);
            }
        }
        // 请求加入企业ID
        // Android1,苹果2,网站3
        setS("1");
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Integer getItype() {
        return itype;
    }

    public void setItype(Integer itype) {
        this.itype = itype;
    }

    public String getPrevId() {
        return prevId;
    }

    public void setPrevId(String prevId) {
        this.prevId = prevId;
    }

    public void setPrevId(Integer prevId) {
        this.prevId = GlobalUtil.getStringByInt(prevId);
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public void setNextId(Integer nextId) {
        this.nextId = GlobalUtil.getStringByInt(nextId);
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size == null) {
            size = GlobalConstants.PAGE_COUNT;
        }
        this.size = size;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getmCoId() {
        return mCoId;
    }

    public void setmCoId(String mCoId) {
        this.mCoId = mCoId;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public void setIsEs(int isEs) {
        this.isEs = isEs;
    }

    public int getIsEs() {
        return isEs;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ServiceParams> T fromString(Class<? extends ServiceParams> cls, String jsonString) {
        return (T) RefUtil.setFieldValue(cls, jsonString);
    }

    public Integer getFileIType() {
        return fileIType;
    }

    public void setFileIType(Integer fileIType) {
        this.fileIType = fileIType;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = false;
    }

    public boolean isHasCoId() {
        return false;
    }

    public void setHasCoId(boolean hasCoId) {
        this.hasCoId = hasCoId;
        if (!this.hasCoId) {
            this.setmCoId(null);
        } else {
            this.setmCoId(WeqiaApplication.getgMCoId());
        }
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getFiIds() {
        return fiIds;
    }

    public void setFiIds(String fiIds) {
        this.fiIds = fiIds;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
