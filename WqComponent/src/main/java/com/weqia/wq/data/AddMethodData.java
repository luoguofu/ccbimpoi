package com.weqia.wq.data;

public class AddMethodData extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int iconRes;
    private String title;
    private String content;

    public AddMethodData() {}

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
