package com.weqia.utils.data;

import com.weqia.data.UtilData;

import android.view.View.OnClickListener;

/**
 * 
 * @Description :
 * @author Berwin
 * @version 1.0
 * @created 2013-4-7 下午5:16:31
 * 
 */
public class DialogData extends UtilData {

    /**
     * @Description
     * @author
     * @create at 2013-4-7 下午4:31:59
     * 
     */

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer titleColor;
    private String title;
    private OnClickListener onClickListener;
    private Integer type;

    public DialogData() {}

    public DialogData(Integer id, String title, OnClickListener onClickListener, Integer type) {
        setId(id);
        setTitle(title);
        setOnClickListener(onClickListener);
        setType(type);
    }

    public DialogData(Integer id, String title, Integer titleColor,
            OnClickListener onClickListener, Integer type) {
        setId(id);
        setTitle(title);
        setTitleColor(titleColor);
        setOnClickListener(onClickListener);
        setType(type);
    }


    public Integer getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(Integer titleColor) {
        this.titleColor = titleColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
