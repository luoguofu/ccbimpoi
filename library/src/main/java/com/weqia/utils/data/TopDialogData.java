package com.weqia.utils.data;

import com.weqia.data.UtilData;

import android.view.View.OnClickListener;

public class TopDialogData extends UtilData {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer icon;
	private Integer titleColor;
	private String title;
	private OnClickListener onClickListener;

	public TopDialogData() {
	}

	public TopDialogData(Integer id, String title, Integer icon,
			OnClickListener onClickListener) {
		setId(id);
		setTitle(title);
		setOnClickListener(onClickListener);
		setIcon(icon);
	}

	public TopDialogData(Integer id, String title, Integer titleColor,
			OnClickListener onClickListener, Integer icon) {
		setId(id);
		setTitle(title);
		setTitleColor(titleColor);
		setOnClickListener(onClickListener);
		setIcon(icon);
	}

	public Integer getIcon() {
		return icon;
	}

	public void setIcon(Integer icon) {
		this.icon = icon;
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

}
