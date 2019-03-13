package com.weqia.wq.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.Hks;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berwin on 2016/10/19.
 */

public class SelectArrUtil {

    private SelectArrUtil(){

    }

    private static class SelectUtilHolder{
        private static SelectArrUtil instance = new SelectArrUtil();
    }

    public static SelectArrUtil getInstance() {
        return SelectUtilHolder.instance;
    }

    private List<String> mSelectedList;
    private List<String> mSelectedSourceList;

    public static final String getDecodePath(final String path) {
        String realPath = path;
        try {
            realPath = URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            realPath = path;
            e.printStackTrace();
        }
//        L.i("++++++++" + path  + " ++++[[[ decode ]]]++++ " + realPath);
        return realPath;
    }

    /**
     * 获取已选中的图片列表
     *
     * @return
     */
    public List<String> getSelectedImgs() {
        if (mSelectedList == null) {
            String str = WPf.getInstance().get(Hks.select_arr, String.class);
            L.e("存储的已选择图片 == " + str);
            if (StrUtil.notEmptyOrNull(str)) {
                try {
                    mSelectedList = JSONArray.parseArray(str, String.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mSelectedList = null;
                }
            }
            if (mSelectedList == null) {
                mSelectedList = new ArrayList<>();
                WPf.getInstance().remove(Hks.select_arr);
            }
        }
        return mSelectedList;
    }

    public List<String> getSelectedSourceList() {
        if (mSelectedSourceList == null) {
            String str = WPf.getInstance().get(Hks.select_arr_source, String.class);
            if (StrUtil.notEmptyOrNull(str)) {
                try {
                    mSelectedSourceList = JSONArray.parseArray(str, String.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mSelectedSourceList = null;
                }
            }
            if (mSelectedSourceList == null) {
                mSelectedSourceList = new ArrayList<>();
                WPf.getInstance().remove(Hks.select_arr_source);
            }
        }
        return mSelectedSourceList;
    }

    public int getSelImgSize() {
        if (mSelectedList == null)
            return 0;
        return mSelectedList.size();
    }

    public int getSelImgSourceSize() {
        if (mSelectedSourceList == null)
            return 0;
        return mSelectedSourceList.size();
    }

    public String getSelImg(int pos) {
        if (getSelImgSize() <= pos)
            return null;
        return getSelectedImgs().get(pos);
    }

    public boolean isSelImgContain(String path) {
        return getSelectedImgs().contains(path);
    }

    public boolean isSelImgSourceContain(String path) {
        return getSelectedSourceList().contains(path);
    }

    public void resetData(){
        if (mSelectedList != null) {
            mSelectedList.clear();
            clearImage();
        }
        if (mSelectedSourceList != null) {
            clearSourceImage();
        }
    }

    /**
     * 将图片地址添加到已选择列表中
     *
     * @param path
     */
    public boolean addImage(String path, int maxNum) {
        if (getSelectedImgs().contains(path)) {
            return false;
        }
        if (getSelectedImgs().size() == maxNum) {
            L.toastShort("最多只能选择" + maxNum + "张照片");
            return false;
        }
        return addDo(path);
    }

    private boolean addDo(String path) {
        L.e("添加图片地址, " + path);
        getSelectedImgs().add(path);
        WPf.getInstance().put(Hks.select_arr, JSON.toJSON(getSelectedImgs()).toString());
        return true;
    }

    /**
     * 将图片地址添加到已选择列表中
     *
     * @param path
     */
    public boolean addImage(String path) {
        if (getSelectedImgs().contains(path)) {
            return false;
        }
        return addDo(path);
    }

    /**
     * 将图片地址从已选择列表中删除
     *
     * @param path
     */
    public void deleteImage(String path) {
        getSelectedImgs().remove(path);
        WPf.getInstance().put(Hks.select_arr, JSON.toJSON(getSelectedImgs()).toString());
    }

    public void clearImage() {
        getSelectedImgs().clear();
        WPf.getInstance().remove(Hks.select_arr);
    }

    /**
     * 将图片地址添加到已选择列表中
     *
     * @param path
     */
    public boolean addSourceImage(String path) {
        if (getSelectedSourceList().contains(path)) {
            return false;
        }
        if (getSelectedSourceList().size() == GlobalConstants.IMAGE_MAX) {
            return false;
        }
        getSelectedSourceList().add(path);
        WPf.getInstance().put(Hks.select_arr_source, JSON.toJSON(getSelectedSourceList()).toString());
        return true;
    }

    /**
     * 将图片地址从已选择列表中删除
     *
     * @param path
     */
    public void deleteSourceImage(String path) {
        getSelectedSourceList().remove(path);
        WPf.getInstance().put(Hks.select_arr_source, JSON.toJSON(getSelectedSourceList()).toString());
    }

    public void clearSourceImage(){
        getSelectedSourceList().clear();
        WPf.getInstance().remove(Hks.select_arr_source);
    }
}
