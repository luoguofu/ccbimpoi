package com.weqia.utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.weqia.BitmapInit;
import com.weqia.data.UtilsConstants;
import com.weqia.utils.L;

public class TalkMaskImage extends CommonImageView {
    private NinePatch np;

    public TalkMaskImage(Context context) {
        super(context);
    }

    public TalkMaskImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalkMaskImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public Drawable getDrawable() {
        return super.getDrawable();
    }

    public void setMaskImage(Bitmap bitmap, int maskId, Point point, String memCacheKey) {
        String cacheKey =
                UtilsConstants.MASK_KEY + memCacheKey + "_" + maskId + "_" + point.x + "_"
                        + point.y;
        if (BitmapInit.DEBUG_BITMAP) L.e(cacheKey);
        if (bitmap == null || maskId == 0 || point == null || point.x <= 0 || point.y <= 0) {
            return;
        }
        try {
        	setScaleType(getScaleType());
            Bitmap cacheBitmap = BitmapInit.getInstance().getMaskImages().get(cacheKey);
            if (cacheBitmap == null) {
                Bitmap mask = BitmapFactory.decodeResource(getResources(), maskId);
                np = new NinePatch(mask, mask.getNinePatchChunk(), null);
                Bitmap result = Bitmap.createBitmap(point.x, point.y, Config.ARGB_4444);
                // 将遮罩层的图片放到画布中
                Canvas mCanvas = new Canvas(result);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                mCanvas.drawBitmap(bitmap, null, new Rect(0, 0, point.x, point.y), null);
                np.draw(mCanvas, new Rect(0, 0, point.x, point.y), paint);
                paint.setXfermode(null);
                BitmapInit.getInstance().getMaskImages().put(cacheKey, result);
                if (BitmapInit.DEBUG_BITMAP) L.e("没有缓存，直接生成遮罩, 存储到内存");
                // L.w(point.x + "--" + point.y);
                setImageBitmap(result);
            } else {
                if (BitmapInit.DEBUG_BITMAP) L.e("从内存加载遮罩");
                setImageBitmap(cacheBitmap);
            }
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			L.e("Commoncom.weqia.utils.view.CommonImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
		}
        
    }
}
