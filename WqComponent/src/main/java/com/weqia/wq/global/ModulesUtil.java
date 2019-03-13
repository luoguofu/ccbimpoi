package com.weqia.wq.global;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.weqia.BitmapInit;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;
import com.weqia.utils.bitmap.WqBitmapDisplayer;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.EnumData;

/**
 * Created by berwin on 2017/8/16.
 */

public class ModulesUtil {

    public static DisplayImageOptions getMyOptions(int roundPx) {
        int resId1;
        if (roundPx != 0) {
            resId1 = MResource.getIdByName(BitmapInit.ctx.getPackageName(), "drawable", "util_img_loading_round");
        } else {
            resId1 = MResource.getIdByName(BitmapInit.ctx.getPackageName(), "drawable", "util_img_loading");
        }

        WqBitmapDisplayer displayer = null;
        displayer = new WqBitmapDisplayer(roundPx);
        DisplayImageOptions tmpDisplayImageOptions = (new DisplayImageOptions.Builder()).showImageOnLoading(resId1).showImageForEmptyUri(resId1).showImageOnFail(resId1).delayBeforeLoading(10).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(false).displayer(displayer).build();
        return tmpDisplayImageOptions;

    }

    public static boolean judgeWeqiaIp() {
        boolean flag = false;
        String servIp = UserService.SERV_IP;
        if (StrUtil.notEmptyOrNull(servIp)) {
            if (servIp.equals(EnumData.HttpServer.SERV_FORMAL.strName())
                    || servIp.equals(EnumData.HttpServer.SERV_PREPARE.strName())) {
                flag = true;
            }
        }
        return flag;
    }

    public static boolean judgeZtIp() {
        boolean flag = false;
        String servIp = UserService.SERV_IP;
        if (StrUtil.notEmptyOrNull(servIp)) {
            if (servIp.equals(EnumData.HttpServer.ZT_SERV.strName())) {
                flag = true;
            }
        }
        return flag;
    }


}
