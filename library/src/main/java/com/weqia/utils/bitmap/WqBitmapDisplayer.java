package com.weqia.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.weqia.BitmapInit;
import com.weqia.utils.L;
import com.weqia.utils.MResource;
import com.weqia.utils.view.TalkMaskImage;

public class WqBitmapDisplayer extends RoundedBitmapDisplayer {

    public WqBitmapDisplayer() {
        super(-1, -1);
    }
    
    public WqBitmapDisplayer(int cornerRadiusPixels) {
        super(cornerRadiusPixels);
    }

    public WqBitmapDisplayer(int cornerRadiusPixels, int marginPixels) {
        super(cornerRadiusPixels, marginPixels);
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom,
            String memCacheKey) {

        if (imageAware.getWrappedView() instanceof TalkMaskImage) {
            TalkMaskImage tkImage = (TalkMaskImage) imageAware.getWrappedView();
            if (tkImage.getTag() != null) {

                if (((String) tkImage.getTag()).equalsIgnoreCase("link")) {
                    tkImage.setImageBitmap(bitmap);
                } else {
                    Point point = BitmapInit.getInstance().getCellPoint().get(memCacheKey);
                    if (point == null) {
                        if (L.D) L.i("本地图片大小不准确" + memCacheKey);
                        point = new Point(imageAware.getWidth(), imageAware.getHeight());
                    }

                    if (((String) tkImage.getTag()).equalsIgnoreCase("send")) {
                        int resId =
                                MResource.getIdByName(BitmapInit.ctx.getPackageName(), "drawable",
                                        "util_send_img_bg");
                        if (resId != 0) {
                            tkImage.setMaskImage(bitmap, resId, point, memCacheKey);
                        } else {
                            tkImage.setImageBitmap(bitmap);
                        }
                    } else if (((String) tkImage.getTag()).equalsIgnoreCase("receive")) {
                        int resId =
                                MResource.getIdByName(BitmapInit.ctx.getPackageName(), "drawable",
                                        "util_receice_img_bg");
                        if (resId != 0) {
                            tkImage.setMaskImage(bitmap, resId, point, memCacheKey);
                        } else {
                            tkImage.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        } else {
            if (cornerRadius == -1 && margin == -1) {
                imageAware.setImageBitmap(bitmap);
            } else {
                imageAware.setImageDrawable(new RoundedDrawable(bitmap, cornerRadius, margin));
            }
//            imageAware.setImageBitmap(bitmap);
        }
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        display(bitmap, imageAware, loadedFrom, null);
    }
}
