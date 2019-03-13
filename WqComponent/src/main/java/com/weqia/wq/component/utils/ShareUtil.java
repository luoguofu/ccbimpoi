package com.weqia.wq.component.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
//import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.data.ShareTypeEnum;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

public class ShareUtil {
    //
    // private UMShareAPI mShareAPI;
    private static ShareUtil instance;

    /**
     * app调用
     */
    public static void initShare() {
        Resources rs = WeqiaApplication.ctx.getResources();
        PlatformConfig.setWeixin(rs.getString(R.string.weixin_key),
                rs.getString(R.string.weixin_key_secret));
        PlatformConfig.setQQZone(rs.getString(R.string.qq_id), rs.getString(R.string.qq_key));
//        PlatformConfig.setSinaWeibo(rs.getString(R.string.sina_key),
//                rs.getString(R.string.sina_key_secret));
/*        Config.dialogSwitch = false;
        Log.LOG = false;
        Config.IsToastTip = false;
        Config.DEBUG = false;*/
    }

    private ShareUtil(Activity ctx) {
        // mShareAPI = UMShareAPI.get(ctx);
    }

    public static final ShareUtil getInstance(Activity ctx) {
        if (instance == null) {
            instance = new ShareUtil(ctx);
        }
        return instance;
    }


    //     X       1.BIM360同事圈的内容，分享到微信朋友圈，标题自动取；
//            2.BIM360同事圈的内容，如果分享到微信(单聊、群)，标题自动取，内容填写“BIM360”;无图时，取直角的图片；
//            3.同事圈图片、视频、文件超过2个时，并且无内容时，分享到微信、微信朋友圈时，采用URL方式：标题取“Hi，给你一条分享！”。
//          X  4.同事圈图片、视频、文件仅1个时，并且无内容时，分享到微信、微信朋友圈，直接发图、视频、文件。
    private void shareDo(final Activity ctx, final String content, final String title, final String url, final UMImage image, final int type) {
        final SHARE_MEDIA[] displaylist =
                new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QZONE,
//                        SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ,
//                        SHARE_MEDIA.SMS
                };
        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        config.setShareboardBackgroundColor(Color.WHITE);
        config.setCancelButtonVisibility(false);

        new ShareAction(ctx).setDisplayList(displaylist).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                getShareAction(share_media, ctx, content, title, url, image, type).share();
            }
        }).open(config);
    }

    private ShareAction getShareAction(SHARE_MEDIA share_media, Activity ctx, String content, String title, String url, UMImage image, int type) {
        ShareAction shareAction = null;
        if (StrUtil.isEmptyOrNull(title))
            title = GlobalConstants.SHARE_TITLE;

        if (share_media == SHARE_MEDIA.QQ) {
            if (StrUtil.isEmptyOrNull(content)) {
                content = title;
            }
            if (url != null) {
                if (image == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_mode);
                    image = new UMImage(ctx, bitmap);
                }
//                shareAction = new ShareAction(ctx).setPlatform(share_media).withText(title).withTitle(content)
//                        .withTargetUrl(url).withMedia(image);
                shareAction = webShare(ctx, url, image, title, content, share_media);
            } else if (image != null) {
                shareAction = new ShareAction(ctx).setPlatform(share_media).withMedia(image);
            } else {
                if (image == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_mode);
                    image = new UMImage(ctx, bitmap);
                }
                url = GlobalConstants.WQ_D_URL;
//                shareAction = new ShareAction(ctx).setPlatform(share_media).withText(title).withMedia(image).withTitle(content).withTargetUrl(url);
                shareAction = webShare(ctx, url, image, title, content, share_media);
            }
        } else {
            if (type == ShareTypeEnum.WEBO.value()) {
                if (image == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_mode);
                    image = new UMImage(ctx, bitmap);
                } else if (StrUtil.isEmptyOrNull(content)) {
                    title = GlobalConstants.SHARE_TITLE;
                }
//                shareAction = new ShareAction(ctx).setPlatform(share_media).withText(content).withTitle(title)
//                        .withTargetUrl(url).withMedia(image);
                shareAction = webShare(ctx, url, image, title, content, share_media);
            } else if (type == ShareTypeEnum.OTHER.value()) {
                if (url != null) {
                    if (image == null) {
                        Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.bg_image_smallurl);
                        image = new UMImage(ctx, bitmap);
                    }
//                    shareAction = new ShareAction(ctx).setPlatform(share_media).withText(content).withTitle(title)
//                            .withTargetUrl(url).withMedia(image);
                    shareAction = webShare(ctx, url, image, title, content, share_media);
                } else if (image != null) {
                    shareAction = new ShareAction(ctx).setPlatform(share_media).withMedia(image);
                } else {
//                    shareAction = new ShareAction(ctx).setPlatform(share_media).withText(content).withTitle(title);
                    shareAction= webShare(ctx, url, image, title, content, share_media);
                }
            }
        }
        return shareAction;
    }

    public void share(Activity ctx, String content) {
        shareDo(ctx, content, null, null, null, ShareTypeEnum.OTHER.value());
    }

    public void share(Activity ctx, String content, UMImage image) {
        shareDo(ctx, content, null, null, image, ShareTypeEnum.OTHER.value());
    }

    public void share(Activity ctx, String content, String relStr) {
        shareDo(ctx, content, null, relStr, null, ShareTypeEnum.OTHER.value());
    }

    public void share(Activity ctx, String content, UMImage image, String relStr) {
        shareDo(ctx, content, null, relStr, image, ShareTypeEnum.OTHER.value());
    }

    public void share(Activity ctx, String content, String title, String relStr) {
        shareDo(ctx, content, title, relStr, null, ShareTypeEnum.OTHER.value());
    }

    public void share(Activity ctx, String title, String content, UMImage image, String relStr, int type) {
        shareDo(ctx, content, title, relStr, image, type);
    }

    public void share(Activity ctx, String title, String content, UMImage image, String relStr) {
        shareDo(ctx, content, title, relStr, image, ShareTypeEnum.OTHER.value());
    }
    private ShareAction webShare(Activity ctx, String shareUrl, UMImage image, String description, String title, SHARE_MEDIA shareMedia) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        return new ShareAction(ctx).setPlatform(shareMedia).withMedia(web);
    }
}
