package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;

public class RoundedCornerImageView extends com.weqia.utils.view.CommonImageView {

//	private Paint mMaskPaint;
//	private Path mMaskPath;

	public RoundedCornerImageView(Context context) {
		this(context, null);
	}

	public RoundedCornerImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		init();
	}

//	@TargetApi(11)
//	private void init() {
//		this.mMaskPaint = new Paint();
//		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//		this.mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//	}
//
//	private void generateMaskPath(int width, int height) {
//		this.mMaskPath = new Path();
//		this.mMaskPath.addRoundRect(new RectF(0.0F, 0.0F, width, height), 38.0f, 38.0f, Path.Direction.CW);
//		this.mMaskPath.setFillType(Path.FillType.INVERSE_WINDING);
//	}
//
//	@Override
//	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//		super.onSizeChanged(w, h, oldw, oldh);
//		if ((w != oldw) || (h != oldh))
//			generateMaskPath(w, h);
//
//	}
//
//	protected void onDraw(Canvas canvas) {
//		// 保存当前layer的透明橡树到离屏缓冲区。并新创建一个透明度爲255的新layer
//		int saveCount = canvas.saveLayerAlpha(0.0F, 0.0F, canvas.getWidth(), canvas.getHeight(), 255,
//				Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
//		super.onDraw(canvas);
//		if (this.mMaskPath != null) {
//			canvas.drawPath(this.mMaskPath, this.mMaskPaint);
//		}
//		canvas.restoreToCount(saveCount);
//	}
}