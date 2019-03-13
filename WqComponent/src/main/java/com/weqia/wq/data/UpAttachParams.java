package com.weqia.wq.data;

import com.weqia.wq.component.utils.request.ServiceParams;

public class UpAttachParams extends ServiceParams {

    private String pjId;


    private String documentType;
    private String classifyId;

    private String fiUrls;

    public UpAttachParams() {
        super();
    }

    public UpAttachParams(Integer type) {
        super(type);
    }

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }


    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }


    public String getFiUrls() {
        return fiUrls;
    }

    public void setFiUrls(String fiUrls) {
        this.fiUrls = fiUrls;
    }
}
