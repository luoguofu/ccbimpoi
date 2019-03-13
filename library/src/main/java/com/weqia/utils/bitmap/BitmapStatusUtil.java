package com.weqia.utils.bitmap;

import android.support.annotation.NonNull;
import android.view.View;

import com.nostra13.universalimageloader.utils.UILL;
import com.weqia.BitmapInit;
import com.weqia.data.UtilsConstants;
import com.weqia.utils.L;
import com.weqia.utils.RefreshObjEvent;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.utils.datastorage.file.PathUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BitmapStatusUtil {

    private static boolean debug_load = false;
    private static BitmapStatusUtil instance;

    public static BitmapStatusUtil getInstance() {
        if (instance == null) {
            instance = new BitmapStatusUtil();
        }
        return instance;
    }

    // 加载的map
    private Map<String, Integer> loadMap = new LinkedHashMap<>();

    private static Map<String, String> discussLoadMap = null;


    public Map<String, Integer> getLoadMap() {
        return loadMap;
    }

    /**
     * 判断是否是discussUrl
     * @param imgUri
     * @return
     */
    public static boolean isDiscussUrl(String imgUri){
//        L.e("判断imgUri的条件" + imgUri);
        return imgUri.startsWith(UtilsConstants.MUTIL_KEY) && !imgUri.contains("=");
    }

    public void loadErrDo(final String imageUri, final View view) {
        singleLoadComplete(imageUri, false);
        if (BitmapInit.getInstance().getErrList().contains(imageUri)) {
            if (L.D) UILL.e("已经在错误列表了，就不处理了");
            return;
        }
        DbUtil dbUtil = BitmapInit.getInstance().getDbUtil();
        if (dbUtil != null && dbUtil.getDb() != null) {
            if (imageUri.startsWith("data:image")) {
                if (L.D) UILL.e("data:image不加载");
                return;
            }
            if (isDiscussUrl(imageUri)) {
                BitmapUtil.getInstance().clearCache(imageUri);
                dbUtil.updateWhere(DiscussShowData.class, "status='1'", "imgKey='" + imageUri + "'");
                if (L.D) UILL.e("加载dicuss 失败，刷新数据库");
                return;
            } else {
                if (!PathUtil.isPathInDisk(imageUri)) {
                    String tmpSql = "delete from local_net_path where localPath = '" + imageUri + "'";
                    // if (L.D) UILL.e(tmpSql);
                    setLoadCount(imageUri);
                    dbUtil.getDb().execSQL(tmpSql);
                } else {
                    if (L.D) UILL.e("什么情况，反正什么都没写");
                    return;
                }
            }
            if (isDiscussUrl(imageUri)) {
                if (debug_load) L.i("多图头像加载失败的话只加载一次");
                BitmapUtil.getInstance().clearCache(imageUri);
                LoadErrData loadErrData = new LoadErrData(imageUri, TimeUtils.getDayStart());
                BitmapInit.getInstance().getErrList().add(imageUri);
                dbUtil.save(loadErrData, true);
            } else {
                Integer loadCount = getLoadMap().get(imageUri);
                if (loadCount == null || loadCount < 5) {
                    if (L.D) UILL.i("load 几" + loadCount + "次, imageUrl = " + imageUri);
                    // BitmapUtil.getInstance().load(view, imageUri, null);
                } else {
                    BitmapUtil.getInstance().clearCache(imageUri);
                    if (L.D) UILL.i("加载失败次数太多，忽略,加载到不加载的数据库, imageUrl = " + imageUri);
                    LoadErrData loadErrData = new LoadErrData(imageUri, TimeUtils.getDayStart());
                    BitmapInit.getInstance().getErrList().add(imageUri);
                    dbUtil.save(loadErrData, true);
                }
            }
        } else
            UILL.e("错误啦，需要改改啦");
    }

    private void setLoadCount(String imageUri) {
        Integer loadCount = getLoadMap().get(imageUri);
        if (loadCount == null) {
            loadCount = 1;
        }
        loadCount += 1;
        getLoadMap().put(imageUri, loadCount);
    }


    public void loadSuccess(String imageUri) {
        singleLoadComplete(imageUri, true);
        DbUtil dbUtil = BitmapInit.getInstance().getDbUtil();
        if (isDiscussUrl(imageUri)) {
            if (dbUtil != null) {
                DiscussShowData showData = dbUtil.findById(imageUri, DiscussShowData.class);
                if (showData == null) {
                    L.e("没有discussshowdata的数据，错误------" + imageUri);
                } else {
                    if (showData.getStatus() == 0) {
//                        L.i("已经是status == 0了");
                    } else {
                        dbUtil.updateWhere(DiscussShowData.class, "status='0'", "imgKey='" + imageUri + "' and status in (-1,1)");
                    }
                }
            }
        }
        // 加载原图
        String bigKey = "&th=1";
        if (imageUri.endsWith(bigKey)) {
            if (dbUtil != null) {
                removeSmall(imageUri, dbUtil, bigKey, "&th=2");
                removeSmall(imageUri, dbUtil, bigKey, "&th=3");
                removeSmall(imageUri, dbUtil, bigKey, "&th=4");
            }
        }
    }

    private void removeSmall(String imageUri, DbUtil dbUtil, String bigKey, String tmpStr) {
        String th2Str = imageUri.replace(bigKey, tmpStr);
        LoadErrData loadErrData = dbUtil.findById(th2Str, LoadErrData.class);
        if (loadErrData != null) {
            if (L.D) L.e("有加载失败的，刷新小视图, url = " + th2Str);
            dbUtil.deleteById(LoadErrData.class, th2Str);
            clearImage(th2Str);
        } else {
            // if (L.D) L.e("正常，不刷新, url = " + th2Str);
        }
    }

    private void clearImage(String str2Key) {
        if (debug_load) L.i("清空缓存啦-----," + str2Key);
        DbUtil dbUtil = BitmapInit.getInstance().getDbUtil();
        if (dbUtil != null) {
            String tmpSql = "delete from local_net_path where localPath = '" + str2Key + "'";
            if (L.D) UILL.e(tmpSql);
            dbUtil.getDb().execSQL(tmpSql);
        }
        BitmapInit.getInstance().getErrList().remove(str2Key);
        BitmapUtil.getInstance().clearCache(str2Key);
    }

    public static Map<String, String> getDiscussLoadMap() {
        if (discussLoadMap == null) {
            DbUtil dbUtil = BitmapInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                List<DiscussChildLoadData> loadDatas = dbUtil.findAll(DiscussChildLoadData.class);
                if (StrUtil.listNotNull(loadDatas)) {
                    discussLoadMap = new LinkedHashMap<>();
                    for (DiscussChildLoadData tmpData : loadDatas) {
                        discussLoadMap.put(tmpData.getSingleImg(), tmpData.getDiscussKey());
                    }
                }
            }
            if (discussLoadMap != null) {
                L.e("本地已经存在的map" + discussLoadMap.toString());
            }
            if (discussLoadMap == null)
                discussLoadMap = new LinkedHashMap<>();
        }
        return discussLoadMap;
    }

    public static void putDiscussLoad(String imgUrl, String discussKey) {
        if (debug_load) L.i("添加 key == " + imgUrl + ", discussKey == " + discussKey);
//        String encodeImg = discussPre(discussKey) + imgUrl;
        getDiscussLoadMap().put(imgUrl, discussKey);
        DbUtil dbUtil = BitmapInit.getInstance().getDbUtil();
        if (dbUtil != null) {
            DiscussChildLoadData disLoadData = new DiscussChildLoadData(imgUrl, discussKey);
            dbUtil.save(disLoadData, true);
        }
    }

    public static String discussPre(@NonNull String discussKey) {
        return "imim" + discussKey + "=";
    }


    public static void singleLoadComplete(String imgUrl, boolean success) {
        if (!imgUrl.contains(UtilsConstants.MUTIL_KEY) || !imgUrl.contains("=")) {
            return;
        }
        if (!success && BitmapInit.getInstance().getErrList().contains(imgUrl)) {
            if (debug_load) L.e("暂时没有做加载失败的重试处理，有时间再弄----");
        }
        DbUtil dbUtil = BitmapInit.getInstance().getDbUtil();
        if (dbUtil != null) {
            dbUtil.deleteById(DiscussChildLoadData.class, imgUrl);
        }
        if (getDiscussLoadMap().containsKey(imgUrl)) {
            if (debug_load) L.i("加载完map里面的，" + imgUrl);
            String strValue = getDiscussLoadMap().get(imgUrl);
            getDiscussLoadMap().remove(imgUrl);

            if (!getDiscussLoadMap().containsValue(strValue)) {
                if (debug_load) L.e("更新图片--------------------------" + strValue);
                L.e("还有几个在加载 == " + getDiscussLoadMap().size());
                BitmapUtil bitmapUtil = BitmapUtil.getInstance();
                if (bitmapUtil != null)
                    bitmapUtil.clearCache(strValue);
                EventBus.getDefault().post(new RefreshObjEvent(BitmapInit.AVATAR_DOWNLOAD_SUCCESS, null));
                return;
            }
        }
    }
}
