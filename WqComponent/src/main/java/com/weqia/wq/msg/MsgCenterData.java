package com.weqia.wq.msg;

import com.weqia.utils.StrUtil;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.BaseData;

import java.util.ArrayList;
import java.util.List;

//消息中心
@Table(name = "msg_center")
public class MsgCenterData extends BaseData {

    private static final long serialVersionUID = 1L;

    private
    @Id
    Integer gId;
    private Integer itype; //接口号
    private String pjId;  //项目id
    private String supId;  //特殊字段具体业务具体值（一般代表具体业务所属类型的唯一id,这样可以根据此值，查询出该数据的信息）
    private String supContent;  //右边展示消息
    private String supIcon;   //右边展示图标
    private String mid;
    private String headIcon;
    private String id;
    private String title;
    private String content;  //消息展示内容
    private String files;
    private String gmtCreate;
    private String pics;
    private String attach;
    private Integer sendNo;
    private String cId;//分类id

    private String source; //外部消息来源

    private int business_type;//业务类型


    private String coId;


    public MsgCenterData() {
    }

    public MsgCenterData(int business_type, String supId) {
        this.business_type = business_type;
        this.supId = supId;
        setReaded(1);
    }

    public MsgCenterData(int business_type, String supId, String pjId) {
        this.business_type = business_type;
        this.supId = supId;
        this.pjId = pjId;
        setReaded(1);
    }

    public String getPjId() {
        return pjId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }

    public String getCoId() {
        return coId;
    }

    public void setCoId(String coId) {
        this.coId = coId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public Integer getSendNo() {
        return sendNo;
    }

    public void setSendNo(Integer sendNo) {
        this.sendNo = sendNo;
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

    public String getSupContent() {
        return supContent;
    }

    public void setSupContent(String supContent) {
        this.supContent = supContent;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getSupIcon() {
        return supIcon;
    }

    public void setSupIcon(String supIcon) {
        this.supIcon = supIcon;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFiles() {
        return files;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public void setFiles(String files) {
        if (StrUtil.notEmptyOrNull(files)) {
            List<AttachmentData> temp = BaseData.fromList(AttachmentData.class,
                    files);
            List<AttachmentData> temp1 = new ArrayList<AttachmentData>();
            List<AttachmentData> temp2 = new ArrayList<AttachmentData>();
            for (int i = 0; i < temp.size(); i++) {
                if (GlobalUtil.isShowPic(temp.get(i))) {
                    temp1.add(temp.get(i));
                } else {
                    temp2.add(temp.get(i));
                }
            }
            setPics(temp1.toString());
            setAttach(temp2.toString());
        }
        this.files = files;
    }


    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getgId() {
        return gId;
    }

    public void setgId(Integer gId) {
        this.gId = gId;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public int getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(int business_type) {
        this.business_type = business_type;
    }
}
