package com.weqia.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoadingInfo;
import com.nostra13.universalimageloader.core.LoadAndDisplayImageTask;
import com.nostra13.universalimageloader.core.ProcessAndDisplayImageTask;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.nostra13.universalimageloader.core.imageaware.ViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SyncLoadInterface;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.nostra13.universalimageloader.utils.UILL;
import com.weqia.BitmapInit;
import com.weqia.data.UtilsConstants;
import com.weqia.utils.L;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;

import java.io.File;

public class BitmapUtil extends ImageLoader {

    private volatile static BitmapUtil instances;

    private DisplayImageOptions commonOptions;
    private DisplayImageOptions localOptions;
    private DisplayImageOptions bigOptions;

    /**
     * Returns singleton class instance
     */
    public static BitmapUtil getInstance() {
        if (instances == null) {
            synchronized (BitmapUtil.class) {
                if (instances == null) {
                    instances = new BitmapUtil();
                }
            }
        }
        return instances;
    }

    protected BitmapUtil() {
    }

    public DisplayImageOptions getCommonOptions() {
        return getCommonOptions(BitmapInit.round, -1, false);
    }

    public DisplayImageOptions getCommonOptions(int degree) {
        return getCommonOptions(true, degree, true);
    }

    public DisplayImageOptions getCommonOptions(boolean round) {
        return getCommonOptions(round, null, true);
    }

    public DisplayImageOptions getCommonOptions(boolean round, Integer degree, boolean force) {
        if (commonOptions == null || force) {
            int resId = -1;
            if (round) {
                resId =
                        MResource.getIdByName(BitmapInit.ctx.getPackageName(), "drawable",
                                "util_img_loading_round");
            } else {
                resId =
                        MResource.getIdByName(BitmapInit.ctx.getPackageName(), "drawable",
                                "util_img_loading");
            }

            BitmapDisplayer displayer = null;
            if (round) {
                if (degree == null || (degree != null && degree == -1)) {
                    degree = 180;
                }
                displayer = new WqBitmapDisplayer(degree);
            } else {
                displayer = new WqBitmapDisplayer();
            }
            DisplayImageOptions tmpDisplayImageOptions = new DisplayImageOptions.Builder() //
                    .showImageOnLoading(resId) // 设置图片在下载期间显示的图片
                    .showImageForEmptyUri(resId)// 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(resId) // 设置图片加载/解码过程中错误时候显示的图片
//                    .delayBeforeLoading(10)
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                    .displayer(displayer).build();// 构建完成
            if (force) {
                return tmpDisplayImageOptions;
            } else {
                commonOptions = tmpDisplayImageOptions;
            }
        }
        return commonOptions;
    }

    public DisplayImageOptions getBigOptions(boolean round, Integer degree, boolean force,
                                             Bitmap bitmap) {
        if (bigOptions == null || force) {
            BitmapDisplayer displayer = null;
            if (round) {
                if (degree == null || (degree != null && degree == -1)) {
                    degree = 180;
                }
                displayer = new WqBitmapDisplayer(degree);
            } else {
                displayer = new WqBitmapDisplayer();
            }
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder() //
                    .delayBeforeLoading(10).cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                    .displayer(displayer);
            if (bitmap != null) {
                Drawable loadDrawable = new BitmapDrawable(BitmapInit.ctx.getResources(), bitmap);
                builder.showImageOnLoading(loadDrawable) // 设置图片在下载期间显示的图片
                        .showImageForEmptyUri(loadDrawable)// 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(loadDrawable); //
            }

            DisplayImageOptions tmpDisplayImageOptions = builder.build();// 构建完成
            if (force) {
                return tmpDisplayImageOptions;
            } else {
                bigOptions = tmpDisplayImageOptions;
            }
        }
        return bigOptions;
    }

    public DisplayImageOptions getLocalOptions() {
        if (localOptions == null) {
            int resId =
                    MResource.getIdByName(BitmapInit.ctx.getPackageName(), "drawable",
                            "util_pic_thumb");

            localOptions = new DisplayImageOptions.Builder() //
                    .showImageOnLoading(resId) // 设置图片在下载期间显示的图片
                    .showImageForEmptyUri(resId)// 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(resId) // 设置图片加载/解码过程中错误时候显示的图片
                    .delayBeforeLoading(10).cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                    .displayer(new WqBitmapDisplayer()).build();// 构建完成
        }


        return localOptions;
    }

    public Bitmap getBitmapFromCache(String memoryCacheKey) {
        Bitmap bitmap = getMemoryCache().get(memoryCacheKey);
        return bitmap;
    }

    public Bitmap getBitmapFromCache(String memoryCacheKey, boolean wantLocal) {
        Bitmap bitmap = getMemoryCache().get(memoryCacheKey);
        if (bitmap == null && wantLocal)
            bitmap = getBitmapFromDiskCache(memoryCacheKey);
        return bitmap;
    }

    public Bitmap getBitmapFromDiskCache(String diskCache) {
        return getBitmapFromDiskCache(diskCache, new ImageSize(72, 72));
    }

    public Bitmap getBitmapFromDiskCache(String diskCache, ImageSize size) {
        Bitmap bitmap = null;
        File file = getDiskCache().get(diskCache);
        if (file != null && file.exists()) {
            bitmap = ImageUtil.getImageThumbnail(file.getPath(), size.getWidth(), size.getHeight());
        }
        return bitmap;
    }

    public void clearCache(String key) {
        if (StrUtil.isEmptyOrNull(key))
            return;
        UILL.e("clear cache key = " + key);
        getMemoryCache().remove(key);
        getDiskCache().remove(key);
    }

    public void clearCache() {
        UILL.e("clear all cache");
        clearMemoryCache();
        clearDiskCache();
    }

    public void load(View imageView, String uri, Integer type) {
        load(imageView, uri, type, null);
    }

    public void loadRound(View imageView, String uri, Integer type) {
        load(imageView, uri, type, true);
    }

    public void load(View imageView, String uri, Integer type, boolean round) {
        load(imageView, uri, type, getCommonOptions(round));
    }

    public void load(View imageView, String uri, Integer type,
                     DisplayImageOptions displayImageOptions) {
        load(imageView, uri, type, displayImageOptions, null);
    }

    public void load(View imageView, String uri, Integer type,
                     DisplayImageOptions displayImageOptions, ImageLoadingListener listener) {
        if (displayImageOptions == null) {
            displayImageOptions = getCommonOptions();
        }

        ImageAware aware = getAware(imageView, uri);
        displayImage(getUrl(uri, type), aware, displayImageOptions, listener);
    }

    public void loadProgress(View imageView, String uri, Integer type,
                             ImageLoadingListener progressListener, ImageLoadingProgressListener progress) {
        if (StrUtil.isEmptyOrNull(uri)) {
            return;
        }

        displayImage(getUrl(uri, type), (ImageView) imageView, getCommonOptions(false),
                progressListener, progress);
    }

    public void loadBigProgress(View imageView, String uri, Integer type, Bitmap bitmap,
                                ImageLoadingListener progressListener, ImageLoadingProgressListener progress) {
        if (StrUtil.isEmptyOrNull(uri)) {
            return;
        }
        displayImage(getUrl(uri, type), (ImageView) imageView,
                getBigOptions(false, null, true, bitmap), progressListener, progress);
    }

    private String getUrl(String uri, Integer type) {
        if (!uri.startsWith(UtilsConstants.MUTIL_KEY)) {
            if (!uri.startsWith("http")) {
                if (type != null) {
                    int index = uri.lastIndexOf("&");
                    if (index == -1) {
                        uri += "&th=" + type;
                    } else {
                        uri = uri.substring(0, index);
                        uri += "&th=" + type;
                    }
                }
            }
        }
        return uri;
    }

    private ImageAware getAware(final View imageView, String uri) {
        ImageAware imageAware;
        if (imageView == null) {
            imageAware = new NonViewAware(uri, configuration.getMaxImageSize(), ViewScaleType.CROP);
        } else if (imageView instanceof ImageView) {
            imageAware = new ImageViewAware((ImageView) imageView);
        } else {
            imageAware = new ViewAware(imageView) {

                @SuppressWarnings("deprecation")
                @Override
                protected void setImageDrawableInto(Drawable drawable, View view) {
                    UILL.e("---");
                    view.setBackgroundDrawable(drawable);
                }

                @SuppressWarnings("deprecation")
                @Override
                protected void setImageBitmapInto(Bitmap bitmap, View view) {
                    // UILL.e("+++");
                    view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    // view.setBa

                }
            };
        }
        return imageAware;
    }

    public void displayImage(final String uri, ImageAware imageAware, DisplayImageOptions options,
                             ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {

        if (imageAware instanceof NonViewAware) {
            // UILL.i("同步加载");
        }
        checkConfiguration();
        if (imageAware == null) {
            imageAware = new NonViewAware(uri, configuration.getMaxImageSize(), ViewScaleType.CROP);
            UILL.e("没有imageview");
        }
        if (listener == null) {
            listener = new SimpleImageLoadingListener() {
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    BitmapStatusUtil.getInstance().loadErrDo(imageUri, view);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason, String tag) {
                    BitmapStatusUtil.getInstance().loadErrDo(tag, view);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    BitmapStatusUtil.getInstance().loadSuccess(imageUri);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage, String tag) {
                    super.onLoadingComplete(imageUri, view, loadedImage, tag);
                    BitmapStatusUtil.getInstance().loadSuccess(tag);
                }
            };
        } else {
            if (listener instanceof SyncImageLoadingListener) {
                ((SyncImageLoadingListener) listener).setSyncLoadInterface(new SyncLoadInterface() {

                    @Override
                    public void onLoadError(String imageUri, View view, FailReason failReason) {
                        BitmapStatusUtil.getInstance().loadErrDo(imageUri, view);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        BitmapStatusUtil.getInstance().loadSuccess(imageUri);
                    }
                });
            } else {
                if (L.D) UILL.e("其他的回调，需要处理下");
            }
        }
        if (options == null) {
            options = configuration.defaultDisplayImageOptions;
        }

        if (TextUtils.isEmpty(uri)) {
            engine.cancelDisplayTaskFor(imageAware);
            listener.onLoadingStarted(uri, imageAware.getWrappedView());
            if (options.shouldShowImageForEmptyUri()) {
                imageAware.setImageDrawable(options.getImageForEmptyUri(configuration.resources));
            } else {
                imageAware.setImageDrawable(null);
            }
            if (imageAware instanceof NonViewAware && StrUtil.notEmptyOrNull(((NonViewAware) imageAware).getTag()))
                listener.onLoadingComplete(uri, imageAware.getWrappedView(), null, ((NonViewAware) imageAware).getTag());
            else
                listener.onLoadingComplete(uri, imageAware.getWrappedView(), null);
            return;
        }

        ImageSize targetSize =
                ImageSizeUtils.defineTargetSizeForView(imageAware, configuration.getMaxImageSize());
        String memoryCacheKey = uri;// MemoryCacheUtils.generateKey(uri, targetSize);
        engine.prepareDisplayTaskFor(imageAware, memoryCacheKey);
        listener.onLoadingStarted(uri, imageAware.getWrappedView());

        Bitmap bmp = null;
        if (ImageDownloader.Scheme.ofUri(memoryCacheKey) == ImageDownloader.Scheme.DRAWABLE) {
            bmp = BitmapDecoder.decodeSampledBitmapFromResource(BitmapInit.ctx.getResources(),
                    Integer.parseInt(ImageDownloader.Scheme.DRAWABLE.crop(memoryCacheKey)),
                    targetSize.getWidth(), targetSize.getHeight());
        } else
            bmp = configuration.memoryCache.get(memoryCacheKey);
        if (bmp != null && !bmp.isRecycled()) {
            UILL.d(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, memoryCacheKey);

            if (options.shouldPostProcess()) {
                ImageLoadingInfo imageLoadingInfo =
                        new ImageLoadingInfo(uri, imageAware, targetSize, memoryCacheKey, options,
                                listener, progressListener, engine.getLockForUri(uri));
                ProcessAndDisplayImageTask displayTask =
                        new ProcessAndDisplayImageTask(engine, bmp, imageLoadingInfo,
                                defineHandler(options));
                if (options.isSyncLoading()) {
                    displayTask.run();
                } else {
                    engine.submit(displayTask);
                }
            } else {
                options.getDisplayer().display(bmp, imageAware, LoadedFrom.MEMORY_CACHE,
                        memoryCacheKey);
                if (imageAware instanceof NonViewAware && StrUtil.notEmptyOrNull(((NonViewAware) imageAware).getTag()))
                    listener.onLoadingComplete(uri, imageAware.getWrappedView(), bmp, ((NonViewAware) imageAware).getTag());
                else
                    listener.onLoadingComplete(uri, imageAware.getWrappedView(), bmp);
            }
        } else {
            if (options.shouldShowImageOnLoading()) {
                imageAware.setImageDrawable(options.getImageOnLoading(configuration.resources));
            } else if (options.isResetViewBeforeLoading()) {
                imageAware.setImageDrawable(null);
            }
            ImageLoadingInfo imageLoadingInfo =
                    new ImageLoadingInfo(uri, imageAware, targetSize, memoryCacheKey, options,
                            listener, progressListener, engine.getLockForUri(uri));
            LoadAndDisplayImageTask displayTask =
                    new LoadAndDisplayImageTask(engine, imageLoadingInfo, defineHandler(options));
            if (options.isSyncLoading()) {
                displayTask.run();
            } else {
                engine.submit(displayTask);
            }
        }
    }
}
