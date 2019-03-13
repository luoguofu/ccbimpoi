package com.weqia.wq.component.activity.assist;


public class TalkGridData {

    private int icon;
    private String title;
    private int type;

    public TalkGridData() {}

    public TalkGridData(TalkBanner.AddMediaType mediaType) {
        this.type = mediaType.value();
        this.icon = mediaType.drawId();
        this.title = mediaType.strName();
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TalkGrid [type=" + type + ", icon=" + icon + ", title=" + title + "]";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
