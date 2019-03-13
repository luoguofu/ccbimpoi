package com.weqia.wq.component.utils.bitmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.weqia.utils.DeviceUtil;
import com.weqia.utils.StrUtil;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.component.view.picture.PictureAdapter;
import com.weqia.wq.component.view.picture.PicturePagerActivity;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

import java.util.ArrayList;
import java.util.List;

public class PictureUtil {

    public static int SINGLE_WIDTH = (int) (84 * DeviceUtil.getDeviceDensity());
    public static int SERACH_SINGLE_WIDTH = (int) (55 * DeviceUtil.getDeviceDensity());
    public static int SHOW_AND_CHANGE_HEAD_PIC = 10109;
    public static String PITURE_TYPE = "picture_type";

    // 查看大图
    public static void viewPicture(Activity ctx, String path, View view) {
        if (StrUtil.isEmptyOrNull(path)) {
            return;
        }
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(path);
        viewPicture(ctx, paths, 0, view);
    }

    public static void showAndChangePicture(Activity ctx, String path, int type, boolean bChange) {
        if (StrUtil.isEmptyOrNull(path)) {
            return;
        }
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(path);
        viewAndChangePicture(ctx, paths, 0, true, null, bChange, type);
    }// 查看头像大图并且修改头像


    // 查看大图
    private static void viewAndChangePicture(Activity ctx, ArrayList<String> paths, int position,
                                             Boolean wantDot, ImageView imageView, boolean bChange, int type) {
        if (!StrUtil.listNotNull(paths)) {
            return;
        }
        Intent intent = new Intent(ctx, PicturePagerActivity.class);
        intent.putExtra("current", position);
        if (wantDot != null) {
            if (paths.size() == 1) {  //BIM3601.0的时候优化，只有一张图片的时候不显示圆点！
                wantDot = false;
            }
            intent.putExtra("bShowDot", wantDot);
        }
        intent.putStringArrayListExtra(GlobalConstants.LIST_PICS, paths);
        intent.putExtra(GlobalConstants.CHANGE_HEAD_PICS, bChange);
        intent.putExtra(PITURE_TYPE, type);
        ctx.startActivityForResult(intent, SHOW_AND_CHANGE_HEAD_PIC);
        ctx.overridePendingTransition(0, 0);
    }

    public static void viewPicture(Activity ctx, ArrayList<String> paths, int position, View view) {
        viewPicture(ctx, paths, position, true, view);
    }

    /**
     * 查看大图
     *
     * @param ctx
     * @param paths
     * @param position
     * @param wantDot
     */
    private static void viewPicture(Activity ctx, ArrayList<String> paths, int position,
                                    Boolean wantDot, View view) {
        if (!StrUtil.listNotNull(paths)) {
            return;
        }
        Intent intent = new Intent(ctx, PicturePagerActivity.class);
        intent.putExtra("current", position);
        if (wantDot != null) {
            if (paths.size() == 1) {  //BIM3601.0的时候优化，只有一张图片的时候不显示圆点！
                wantDot = false;
            }
            intent.putExtra("bShowDot", wantDot);
        }
        intent.putStringArrayListExtra(GlobalConstants.LIST_PICS, paths);
        if (view != null) {
            final Rect startBounds = new Rect();
            view.getGlobalVisibleRect(startBounds);
            intent.putExtra("startBounds", startBounds);
        }
        ctx.startActivity(intent);
        ctx.overridePendingTransition(0, 0);
    }

    private static int gvHeight(int count, float apart) {
        int height = SINGLE_WIDTH;
        // (int) (DeviceUtil.getDeviceWidth() / part);
        switch (count) {
            case 1:
                height =
                        (int) ((GlobalConstants.DEFAULT_SINGLE_PIC_WIDTH * DeviceUtil
                                .getDeviceDensity()));
                break;
            case 2:
            case 3:
                height = height * 1;
                break;
            case 4:
            case 5:
            case 6:
                height = (int) (height * 2 + 3 * DeviceUtil.getDeviceDensity());
                break;
            case 7:
            case 8:
            case 9:
                height = (int) (height * 3 + 6 * DeviceUtil.getDeviceDensity());
                break;

            default:
                height = height * 1;
                break;
        }

        return height;
    }

    // 多张图展示gridview宽度
    private static int gvWidth(int count, float part) {
        int defaultWidth =
                (int) (GlobalConstants.DEFAULT_SINGLE_PIC_WIDTH * DeviceUtil.getDeviceDensity());
        // (int) (DeviceUtil.getDeviceWidth() / part)
        int width = SINGLE_WIDTH;
        switch (count) {
            case 1:
                width = defaultWidth;
                break;
            case 2:
                width = (int) (width * 2 + 3 * DeviceUtil.getDeviceDensity());
                break;
            case 3:
                width = (int) (width * 3 + 6 * DeviceUtil.getDeviceDensity());
                break;
            case 4:
                width = (int) (width * 2 + 3 * DeviceUtil.getDeviceDensity());
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                width = (int) (width * 3 + 6 * DeviceUtil.getDeviceDensity());
                break;

            default:
                width = width * 1;
                break;
        }
        return width;
    }

    // 同事圈搜索图片gv高度保持不变
    private static int gvSerachHeight(int count, float apart) {
        int height = SERACH_SINGLE_WIDTH;
        switch (count) {
            case 1:
            case 2:
            case 3:
            case 4:
                height = height * 1;
                break;
            default:
                height = height * 1;
                break;
        }
        return height;
    }

    // 同事圈搜索图片gv宽度
    private static int gvSerachWidth(int count, float part) {
        int width = SERACH_SINGLE_WIDTH;
        switch (count) {
            case 1:
                width = SERACH_SINGLE_WIDTH * 2;
                break;
            case 2:
                width = (int) (width * 2 + 4 * DeviceUtil.getDeviceDensity()); //两张图片就有一个间距所以就是4（代表横间距的宽度）
                break;
            case 3:
                width = (int) (width * 3 + 8 * DeviceUtil.getDeviceDensity());
                break;
            case 4:
                width = (int) (width * 4 + 12 * DeviceUtil.getDeviceDensity());
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                width = (int) (width * 4 + 12 * DeviceUtil.getDeviceDensity());
                break;

            default:
                width = width * 1;
                break;
        }
        return width;
    }

    // 搜索多张图展示gridview列数
    private static int gvSerachNumColumns(int count) {
        int numColumns = 1;
        switch (count) {
            case 1:
                numColumns = 1;
                break;
            case 2:
                numColumns = 2;
                break;
            case 3:
                numColumns = 3;
                break;
            case 4:
                numColumns = 4;
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                numColumns = 4;
                break;
            default:
                numColumns = 1;
                break;
        }
        return numColumns;
    }


    // public static int gvOneWidth() {
    // int width =
    // (int) (GlobalConstants.DEFAULT_SINGLE_PIC_WIDTH * DeviceUtil.getDeviceDensity());
    // return width;
    // }

    // 多张图展示gridview列数
    private static int gvNumColumns(int count) {

        int numColumns = 1;
        switch (count) {
            case 1:
                numColumns = 1;
                break;
            case 2:
                numColumns = 2;
                break;
            case 3:
                numColumns = 3;
                break;
            case 4:
                numColumns = 2;
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                numColumns = 3;
                break;
            default:
                numColumns = 1;
                break;
        }
        return numColumns;
    }

    /**
     * 设置宫格的图片view
     *
     * @param ctx
     * @param gvView
     * @param gFiles
     */
    public static void setPicView(SharedTitleActivity ctx, GridView gvView,
                                  final List<AttachmentData> gFiles) {
        int size = gFiles.size();
        List<AttachmentData> allList = new ArrayList<AttachmentData>();
        List<AttachmentData> listPic = new ArrayList<AttachmentData>();
        int picPos = -1;
        for (int i = 0; i < gFiles.size(); i++) {
            AttachmentData data = gFiles.get(i);
            if (data.getType() == AttachType.PICTURE.value()
                    || data.getType() == AttachType.VIDEO.value()) {
                listPic.add(data);
                picPos = i;
            }
            allList.add(data);
        }
        Point tmppoint = null;
        if (listPic.size() == 1) {
            final AttachmentData data = gFiles.get(picPos);
            float picScale = data.getPicScale();
            tmppoint = ComponentUtil.getImagePoint(picScale);
        }
        PictureAdapter adapter = new PictureAdapter(ctx, tmppoint);
//        if (ctx instanceof WeboSerachAcitivity) {
//            gvView.setNumColumns(gvSerachNumColumns(size));
//        } else {
            gvView.setNumColumns(gvNumColumns(size));
//        }
        LinearLayout.LayoutParams params = null;
//        if (ctx instanceof WeboSerachAcitivity) {
//            tmppoint = null;
//        }
        if (tmppoint != null) {
            params = new LayoutParams(tmppoint.x, tmppoint.y);
        } else {
//            if (ctx instanceof WeboSerachAcitivity) {
//                params =
//                        new LayoutParams(PictureUtil.gvSerachWidth(size, GlobalConstants.PART_4), PictureUtil.gvSerachHeight(size, GlobalConstants.PART_4));  //动态配置权重
//                gvView.setHorizontalSpacing(4);
//                gvView.setVerticalSpacing(0);
//            } else {
                params =
                        new LayoutParams(ComponentUtil.gvWidth(size, GlobalConstants.PART_4),
                                ComponentUtil.gvHeight(size, GlobalConstants.PART_4));
//            }
        }

        params.topMargin = ComponentUtil.dip2px(4);
        gvView.setLayoutParams(params);
        gvView.setGravity(Gravity.LEFT);
        gvView.setAdapter(adapter);
        adapter.setItems(allList);
    }






    protected static void deleteKey(String defaultKey) {
        if (StrUtil.isEmptyOrNull(defaultKey)) {
            return;
        }
        WeqiaApplication.getInstance().getBitmapUtil().getDiskCache().remove(defaultKey);
        WeqiaApplication.getInstance().getBitmapUtil().getMemoryCache().remove(defaultKey);
        WeqiaApplication.getInstance().getBitmapUtil().getDiskCache()
                .remove(Scheme.FILE.wrap(defaultKey));
        WeqiaApplication.getInstance().getBitmapUtil().getMemoryCache()
                .remove(Scheme.FILE.wrap(defaultKey));
    }
}
