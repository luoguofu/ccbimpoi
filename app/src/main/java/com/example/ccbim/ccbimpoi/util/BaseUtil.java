package com.example.ccbim.ccbimpoi.util;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by lgf on 2019/4/4.
 */

public class BaseUtil {
    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

}
