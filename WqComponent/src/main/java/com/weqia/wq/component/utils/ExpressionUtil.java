package com.weqia.wq.component.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.weqia.utils.DeviceUtil;
import com.weqia.utils.RegexUtil;
import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.sys.URLSpanUtils;
import com.weqia.wq.data.base.HideUrlData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

/**
 * 表情正则匹配把表情符号转换为表情图片+@XX的正则匹配
 *
 * @author Dminter
 */
public class ExpressionUtil {
    // 表情正则
    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

    public static SpannableString getRealExpression(Context ctx, String message, float fontSize) {
        String hackTxt;
        if (message.startsWith("[") && message.endsWith("]")) {
            hackTxt = message + " ";
        } else {
            hackTxt = message;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);
        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            if (m - k < 8) {
                if (WeqiaApplication.getInstance().getFaceMap().containsKey(str2)) {
                    int face = WeqiaApplication.getInstance().getFaceMap().get(str2);
                    Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), face);
                    if (bitmap != null) {
                        float density = DeviceUtil.getDeviceDensity();
                        int rawHeigh = bitmap.getHeight();
                        int rawWidth = bitmap.getWidth();
                        int newHeight = (int) (fontSize * density);
                        int newWidth = newHeight;
                        // 计算缩放因子
                        float heightScale = ((float) newHeight) / rawHeigh;
                        float widthScale = ((float) newWidth) / rawWidth;
                        // 新建立矩阵
                        Matrix matrix = new Matrix();
                        matrix.postScale(heightScale, widthScale);
                        Bitmap newBitmap =
                                Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeigh, matrix, true);
                        ImageSpan localImageSpan =
                                new ImageSpan(ctx, newBitmap, ImageSpan.ALIGN_BOTTOM);
                        value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return value;
    }


    /**
     * @XXX着色
     */
    // public static void dealPeople(Context context,
    // SpannableString spannableString, Pattern patten, int init)
    // throws Exception {
    // Matcher matcher = patten.matcher(spannableString);
    // while (matcher.find()) {
    // String key = matcher.group();
    // if (matcher.init() < init) {
    // continue;
    // }
    // int end = matcher.init() + key.length(); // 计算@XXX的长度，也就是要替换的字符串的长度
    // CharSequence tempName = spannableString.subSequence(
    // matcher.init() + 1, end - 1);
    // // 判断@XXX xxx内容的合法性,即不是本联系人内的不着色
    // if (tempName != null && ContactUtil.hasContactByName(tempName)) {
    // spannableString.setSpan(new ForegroundColorSpan(context
    // .getResources().getColor(R.color.blue)), matcher
    // .init(), end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    // }
    // if (end < spannableString.length()) { // 如果整个字符串还未验证完，则继续。。
    // dealPeople(context, spannableString, patten, end);
    // }
    // }
    // }
    public static void dealPeople(Context context, SpannableString spannableString, Pattern patten,
                                  int start) throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            int end = matcher.start() + key.length();
            spannableString.setSpan(
                    new ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
                    matcher.start(), end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (end < spannableString.length()) {
                dealPeople(context, spannableString, patten, end);
            }
        }
    }

    /**
     * 话题涂色
     */
    public static void dealTopic(Context context, SpannableString spannableString, Pattern patten,
                                 int start) throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            int end = matcher.start() + key.length();
            spannableString.setSpan(
                    new ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
                    matcher.start(), end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (end < spannableString.length()) { // 如果整个字符串还未验证完，则继续。。
                dealTopic(context, spannableString, patten, end);
            }
        }
    }

    // 获取文本里的链接
    public static HideUrlData getUrls(String str) {
        ArrayList<String> urls = new ArrayList<String>();
        Pattern pattern = Pattern.compile(RegexUtil.REGEX_URL, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        boolean isFind = matcher.find();
        while (isFind) {
            urls.add(matcher.group());
            str = str.replace(matcher.group(), "");
            isFind = matcher.find(matcher.end() - 1);
        }
        HideUrlData hideUrlData = new HideUrlData();
        hideUrlData.setRet(str);
        hideUrlData.setUrls(urls);
        return hideUrlData;
    }


    // 获取表情文本
    public static SpannableString getExpressionString(Context context, String str) {
        return getExpressionString(context, str, true, true, GlobalConstants.FACE_DEFAULT_FONT_SIZE, 0);
    }

    public static SpannableString getExpressionString(Context context, String str, float fontSize) {
        return getExpressionString(context, str, true, true, fontSize, 0);
    }

    public static SpannableString getExpressionString(Context context, String str, float fontSize, int color) {
        return getExpressionString(context, str, true, true, fontSize, color);
    }

    public static SpannableString getExpressionString(Context context, String str, boolean wantArt,
                                                      boolean wantTopic) {
        return getExpressionString(context, str, wantArt, wantTopic, GlobalConstants.FACE_DEFAULT_FONT_SIZE, 0);
    }
    public static SpannableString getExpressionString(Context context, String str, boolean wantArt,
                                                      boolean wantTopic,int color) {
        return getExpressionString(context, str, wantArt, wantTopic, GlobalConstants.FACE_DEFAULT_FONT_SIZE, color);
    }

    public static SpannableString getExpressionString(Context context, String str, boolean wantArt,
                                                      boolean wantTopic, float fontSize, int color) {

        if (StrUtil.isEmptyOrNull(str)) {
            return new SpannableString("");
        }
        SpannableString spannableString = new SpannableString(str);
        try {
            if (str.contains("[") && str.contains("]")) {
                spannableString = getRealExpression(context, str, fontSize);
            }
            // 通过传入的正则表达式来生成一个pattern @xxx
            if (wantArt) {
                if (str.contains("@")) {
                    Pattern peoplePattern =
                            Pattern.compile(RegexUtil.REGEX_PEOPLE, Pattern.CASE_INSENSITIVE); // 通过传入的正则表达式来生成一个pattern
                    decorateTrendInSpannableString(context, spannableString, peoplePattern, color);
                }

            }
            if (wantTopic) {
                if (str.contains("#")) {
                    // 话题着色
                    Pattern topicPattern =
                            Pattern.compile(RegexUtil.REGEX_TOPIC, Pattern.CASE_INSENSITIVE);
                    decorateTrendInSpannableString(context, spannableString, topicPattern, color);
                }
            }
            decorateTrendInSpannableString(context, spannableString, MyPatterns.PHONE, color);
            decorateTrendInSpannableString(context, spannableString, MyPatterns.EMAIL_ADDRESS, color);
            decorateTrendInSpannableString(context, spannableString, MyPatterns.WEB_URL, color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spannableString;
    }

    public static SpannableString getExpressionAndSerachString(Context context, String str, boolean wantArt,
                                                               boolean wantTopic, float fontSize, boolean bSearch, String searchWord) {
        return getExpressionAndSerachString(context, str, wantArt,
                wantTopic, fontSize, bSearch, searchWord, 0);
    }

    // 获取表情文本并且给搜索关键字标蓝
    public static SpannableString getExpressionAndSerachString(Context context, String str, boolean wantArt,
                                                               boolean wantTopic, float fontSize, boolean bSearch, String searchWord, int color) {

        if (StrUtil.isEmptyOrNull(str)) {
            return new SpannableString("");
        }
        SpannableString spannableString = new SpannableString(str);
        try {
            if (str.contains("[") && str.contains("]")) {
                spannableString = getRealExpression(context, str, fontSize);
            }
            // 通过传入的正则表达式来生成一个pattern @xxx
            if (wantArt) {
                if (str.contains("@")) {
                    Pattern peoplePattern =
                            Pattern.compile(RegexUtil.REGEX_PEOPLE, Pattern.CASE_INSENSITIVE); // 通过传入的正则表达式来生成一个pattern
                    decorateTrendInSpannableString(context, spannableString, peoplePattern, color);
                }

            }
            if (wantTopic) {
                if (str.contains("#")) {
                    // 话题着色
                    Pattern topicPattern =
                            Pattern.compile(RegexUtil.REGEX_TOPIC, Pattern.CASE_INSENSITIVE);
                    decorateTrendInSpannableString(context, spannableString, topicPattern, color);
                }
            }
            decorateTrendInSpannableString(context, spannableString, MyPatterns.PHONE, color);
            decorateTrendInSpannableString(context, spannableString, MyPatterns.EMAIL_ADDRESS, color);
            decorateTrendInSpannableString(context, spannableString, MyPatterns.WEB_URL, color);
            //是否是搜索关键字标蓝
            if (bSearch) {
                if (StrUtil.isEmptyOrNull(searchWord)) {
                    return new SpannableString("");
                }
                ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(R.color.kq_blue));
                int bstart = str.indexOf(searchWord);  //返回指定字符串在目标字符串出现的一个次的索引！
                int eindex = bstart + searchWord.length();
                spannableString.setSpan(span, bstart, eindex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spannableString;
    }


    // 把需要着色的下标找色
    public static Spannable decorateTrendInSpannableString(Context ctx, Spannable spannableString,
                                                           Pattern pattern, int color) {
        List<Map<String, Object>> list = getStartAndEndIndex(spannableString.toString(), pattern);
        int size = list.size();
        if (list != null && size > 0) {
            for (int i = 0; i < size; i++) {
                Map<String, Object> map = list.get(i);
                int start = (Integer) map.get("startIndex");
                int end = (Integer) map.get("endIndex");
                String spanStr = spannableString.subSequence(start, end).toString();
                if (pattern == MyPatterns.PHONE) {
                    spannableString.setSpan(new URLSpanUtils("tel:" + spanStr,color), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (pattern == MyPatterns.EMAIL_ADDRESS) {
                    spannableString.setSpan(new URLSpanUtils("mailto:" + spanStr,color), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (pattern == MyPatterns.WEB_URL) {
                    spannableString.setSpan(new URLSpanUtils(spanStr,color), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    if (color == 0) {
                        color = ctx.getResources().getColor(
                                R.color.underline_color);
                    }
                    CharacterStyle characterStyle =
                            new ForegroundColorSpan(color);
                    spannableString.setSpan(characterStyle, start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spannableString;
    }

    // 找需要着色的下标
    public static List<Map<String, Object>> getStartAndEndIndex(String sourceStr, Pattern pattern) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Matcher matcher = pattern.matcher(sourceStr);
        boolean isFind = matcher.find();
        while (isFind) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("startIndex", matcher.start());
            map.put("endIndex", matcher.end());
            list.add(map);
            isFind = matcher.find((Integer) map.get("endIndex") - 1);
        }
        return list;
    }

}
