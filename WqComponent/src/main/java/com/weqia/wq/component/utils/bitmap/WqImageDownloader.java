package com.weqia.wq.component.utils.bitmap;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.spinytech.macore.router.MaActionResult;
import com.weqia.BitmapInit;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.bitmap.BitmapStatusUtil;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.utils.bitmap.DiscussShowData;
import com.weqia.utils.bitmap.ImageUtil;
import com.weqia.utils.bitmap.LoadErrData;
import com.weqia.utils.bitmap.MutilImageUtil;
import com.weqia.utils.bitmap.MyBitmapEntity;
import com.weqia.utils.data.LocalNetPath;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class WqImageDownloader extends BaseImageDownloader {
    private WeqiaApplication ctx;
    private boolean debug_mutil = false;

    public WqImageDownloader(WeqiaApplication context) {
        super(context);
        this.ctx = context;
    }

    public WqImageDownloader(WeqiaApplication context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
        this.ctx = context;
    }


    private InputStream Bitmap2IS(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, GlobalConstants.DEFAULT_COMPRESS_QUALITY, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    @Override
    protected InputStream getStreamFromOtherSource(String imageUri, Object extra)
            throws IOException {
        InputStream inputStream = null;
        if (BitmapStatusUtil.isDiscussUrl(imageUri)) {
            inputStream = loadMulti(imageUri);
        } else {
            inputStream = loadSingle(imageUri, extra);
        }
        return inputStream;
    }

    private InputStream loadMulti(String imageUri) {
        if (debug_mutil) L.i("多图头像生成" + imageUri);
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("imageUri", imageUri);
        datamap.put("showType", "1");
        Observable<MaActionResult> obj = RouterUtil.routerActionSync(ctx,"pvdiscuss", "acdiscussshow", datamap);
        MaActionResult result = obj.blockingFirst();
        if (StrUtil.isEmptyOrNull(result.getData())) {
            L.e("这里出现没有showData，是错误的代码----------------------");
            return null;
        }
        DiscussShowData showData = DiscussShowData.fromString(DiscussShowData.class, result.getData());//DiscussShowHandle.getShowData(DiscussShowHandle.cropShowKey(imageUri));
        if (showData == null) {
            L.e("这里出现没有showData，是错误的代码----------------------");
            return null;
        }
        String dId = showData.getdId();
        BitmapUtil bitmapUtil = WeqiaApplication.getInstance().getBitmapUtil();
        if (showData.getStatus() == 1 || showData.getStatus() == 2) {
            if (debug_mutil) L.i("showData状态不为0，清除key待加载 dId = " + dId);
            bitmapUtil.clearCache(showData.getImgKey());
        }
        String iconStr = showData.getIconStrs();
        if (StrUtil.notEmptyOrNull(iconStr) && iconStr.contains(",")) {
            String[] icons = iconStr.split(",");
            List<String> keys = Arrays.asList(icons);
            List<MyBitmapEntity> mEntityList = MutilImageUtil.getBitmapEntitys(keys.size());
            int width = (int) mEntityList.get(0).getWidth();
            Bitmap[] mBitmaps = new Bitmap[mEntityList.size()];
            for (int i = 0; i < mBitmaps.length; i++) {
                String tmpKey = keys.get(i);
                if (StrUtil.isEmptyOrNull(tmpKey) || tmpKey.equalsIgnoreCase("-1") || tmpKey.equalsIgnoreCase("-2")) {
                    mBitmaps[i] =
                            ThumbnailUtils.extractThumbnail(
                                    ImageUtil.getScaleBitmap(ctx.getResources(),
                                            GlobalUtil.getPeopleRes(ctx)), width, width);
                } else {
                    tmpKey = tmpKey.trim();
                    Bitmap tmpBitmap = ctx.getBitmapUtil().getBitmapFromCache(tmpKey, true);
                    if (tmpBitmap == null) {
                        String loadKey = BitmapStatusUtil.getInstance().discussPre(imageUri) + tmpKey;
                        if (BitmapInit.getInstance().getErrList().contains(loadKey)) {
                            if (debug_mutil) L.i("已经加载错误了，不再加载");
                            tmpBitmap =
                                    ThumbnailUtils.extractThumbnail(
                                            ImageUtil.getScaleBitmap(ctx.getResources(),
                                                    GlobalUtil.getPeopleRes(ctx)), width, width);
                        } else {
                            NonViewAware nonViewAware = new NonViewAware(new ImageSize(width, width), ViewScaleType.CROP);
                            nonViewAware.setTag(loadKey);
                            BitmapStatusUtil.putDiscussLoad(loadKey, imageUri);
                            ctx.getBitmapUtil().displayImage(tmpKey, nonViewAware, BitmapUtil.getInstance().getCommonOptions());
                            tmpBitmap =
                                    ThumbnailUtils.extractThumbnail(
                                            ImageUtil.getScaleBitmap(ctx.getResources(),
                                                    GlobalUtil.getPeopleRes(ctx)), width, width);
                        }
                    } else {
                        tmpBitmap = ThumbnailUtils.extractThumbnail(tmpBitmap, width, width);
                    }
                    mBitmaps[i] = tmpBitmap;
                }
                mBitmaps[i] = ImageUtil.getRoundedCornerBitmap(mBitmaps[i], 12);
            }
            Bitmap returnBitmap = ImageUtil.getCombineBitmaps(mEntityList, mBitmaps);
            if (debug_mutil) L.e("生成图片完成" + imageUri);
            return Bitmap2IS(returnBitmap);
        } else {
            try {
                return loadSingle(showData.getIconStrs(), null);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private InputStream loadSingle(String imageUri, Object extra) throws IOException {
        if (imageUri.startsWith("data:image")) {
            if (L.D) L.e("图片是data地址，不加载");
            return null;
        }
        if (BitmapInit.getInstance().getErrList().contains(imageUri)) {
            if (L.D) L.i("忽略内存列表，不在加载" + imageUri);
            return null;
        }
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            LoadErrData loadErrData =
                    dbUtil.findByWhere(LoadErrData.class, "localPath='" + imageUri + "' and mtime="
                            + TimeUtils.getDayStart());
            if (loadErrData != null) {
                BitmapInit.getInstance().getErrList().add(imageUri);
                if (L.D) L.i("图片已经在被忽略列表，不在加载" + imageUri);
                return null;
            }
        }
        String realUrl = null;
        // 获取真实地址
        String netPath = LnUtil.getImageRealPath(imageUri, AttachType.TWO_IMG_PATH.value());
        if (StrUtil.notEmptyOrNull(netPath)) {
            realUrl = netPath;
            // if (L.D) L.e(imageUri + "从本地获取图片地址:" + realUrl);
        } else {
            realUrl = UserService.getBitmapUrl(imageUri);
            if (StrUtil.notEmptyOrNull(realUrl)) {
                if (ctx.getDbUtil() != null) {
                    LocalNetPath tmpPath =
                            new LocalNetPath(realUrl, imageUri, AttachType.TWO_IMG_PATH.value());
                    LnUtil.saveData(tmpPath);
                }
            }
            // if (L.D) L.e("key = " + imageUri + ", real url:" + realUrl);
        }
        if (StrUtil.isEmptyOrNull(realUrl)) {
            // if (L.D) L.e("key = " + imageUri + ", real url is null");
            return null;
        } else {
            return getStreamFromNetwork(realUrl, extra);
        }
    }

    @Override
    protected InputStream getStreamFromContent(String imageUri, Object extra)
            throws FileNotFoundException {
        ContentResolver res = context.getContentResolver();

        Uri uri = Uri.parse(imageUri);
        if (isVideoContentUri(uri)) { // video thumbnail
            Long origId = Long.valueOf(uri.getLastPathSegment());
            Bitmap bitmap =
                    MediaStore.Video.Thumbnails.getThumbnail(res, origId,
                            MediaStore.Images.Thumbnails.MINI_KIND, null);
            if (bitmap != null) {
                return Bitmap2IS(bitmap);
            }
        } else if (isImageUri(uri)) {
            String path = StrUtil.getRealImagePathFromURI(uri);
            int tmpSize = 150;
            // int scaleSize = 400;
            if (StrUtil.notEmptyOrNull(path)) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                // 获取这个图片的宽和高，注意此处的bitmap为null
                BitmapFactory.decodeFile(path, options);
                options.inJustDecodeBounds = false; // 设为 false
                // 计算缩放比
                int h = options.outHeight;
                int w = options.outWidth;

                int beWidth = w / tmpSize;
                int beHeight = h / tmpSize;
                int be = 1;
                if (beWidth < beHeight) {
                    be = beWidth;
                } else {
                    be = beHeight;
                }
                if (be <= 0) {
                    be = 1;
                }
                // L.e(be + "");
                options.inSampleSize = be;
                // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                if (bitmap == null) {
                    return null;
                }
                if (Build.VERSION.SDK_INT < 25) {
                    ExifInfo info = ImageUtil.getPictureDegree(path);
                    if (info.flipHorizontal || info.rotation != 0) {
                        bitmap = ImageUtil.rotaingImageView(bitmap, info);
                    }
                }
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, tmpSize, tmpSize);
                return Bitmap2IS(bitmap);
            }
        } else if (imageUri.startsWith(CONTENT_CONTACTS_URI_PREFIX)) { // contacts photo
            return ContactsContract.Contacts.openContactPhotoInputStream(res, uri);
        }

        return res.openInputStream(uri);
    }

    protected boolean isImageUri(Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("image/");
    }
}
