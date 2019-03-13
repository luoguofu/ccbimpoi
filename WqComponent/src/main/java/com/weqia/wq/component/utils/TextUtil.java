package com.weqia.wq.component.utils;/**
 * Created by Tx on 2016/10/9.
 */

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * Created by MaLiang on 2016/10/9.
 */
public class TextUtil {
    /**
     *
     * @param activity
     * @param proText  缩放的倍数
     */
    public static void scaleTextSize(Activity activity, float proText) {
        float size;
        Configuration configuration = activity.getResources().getConfiguration();
        size = proText;
        configuration.fontScale = size; //0.85 small size, 1 normal size, 1.15 big etc
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        activity.getBaseContext().getResources().updateConfiguration(configuration, metrics);//更新全局的配置
    }

}
