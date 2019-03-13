package com.weqia.wq.component.view.picture;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.weqia.utils.view.photo.PhotoView;
import com.weqia.wq.R;

public class PictureScanView extends RelativeLayout {

	private LayoutInflater mInflater;
	private RelativeLayout rlView;
	private PhotoView photoView;

	public PictureScanView(Context context) {
		this(context, null);
	}

	public PictureScanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rlView = (RelativeLayout) mInflater.inflate(R.layout.view_scan_picture, null);
		rlView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(rlView);
		photoView = (PhotoView) findViewById(R.id.photoView);
	}

	public PhotoView getPhotoView() {
		return photoView;
	}

	public void setPhotoView(PhotoView photoView) {
		this.photoView = photoView;
	}

}
