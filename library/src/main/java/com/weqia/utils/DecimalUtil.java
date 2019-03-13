package com.weqia.utils;

import java.text.DecimalFormat;
  
public class DecimalUtil {
    public static int upProgress(float progress) {
        return Integer.parseInt(new DecimalFormat("##").format(progress * 100));
    }
}
