package com.weqia.wq.component.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.weqia.component.rcmode.recyclerView.LuRecyclerView;
import com.weqia.utils.ArithUtil;
import com.weqia.utils.DeviceUtil;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.utils.view.CommonImageView;
import com.weqia.utils.view.pullrefresh.PullToRefreshListView;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.utils.locate.LocationActivity;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.component.view.TitleView;
import com.weqia.wq.component.view.picture.PictureGridView;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.EnumData.DataStatusEnum;
import com.weqia.wq.data.MediaData;
import com.weqia.wq.data.MyLocData;
import com.weqia.wq.data.WaitSendData;
import com.weqia.wq.data.WaitUpFileData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalUtil {

    public static String getDbFileName(Context ctx) {
        return "data/data/" + ctx.getPackageName() + "/databases/weiqiadb2.db";
    }

    public static String getDbFile(Context ctx) {
        return "data/data/" + ctx.getPackageName() + "/databases";
    }

    public static String getAllFile(Context ctx) {
        return "data/data/" + ctx.getPackageName() + "/";
    }

    public static int getPeopleRes(Context ctx) {
        return R.drawable.people;
    }

    public static int getIconRes(Context ctx) {
        return R.drawable.icon;
    }

    public static String getPackage(Context ctx) {
        return ctx.getPackageName();
    }

    public static Uri getVideoUriByPath(Activity ctx, String videoPath) {
        Uri mUri = Uri.parse("content://media/external/video/media");
        Uri mImageUri = null;


        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = {videoPath};// 查询条件参数
        @SuppressWarnings("deprecation")
        Cursor cursor =
                ctx.managedQuery(mUri, null, selection, selectionArgs,
                        MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            try {
                int ringtoneID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
            } catch (Exception e) {
            } finally {
                // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
                if (Build.VERSION.SDK_INT < 14) {
                    cursor.close();
                }
            }
        }
        return mImageUri;
    }

    public static String getPathFromUri(Activity ctx, String id) {

        Uri mUri = Uri.parse("content://media/external/images/media");

        String selection = MediaStore.MediaColumns._ID + "=?";
        String[] selectionArgs = {id};// 查询条件参数
        @SuppressWarnings("deprecation")
        Cursor cursor =
                ctx.managedQuery(mUri, null, selection, selectionArgs,
                        MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return "";
        }
        if (cursor.moveToFirst()) {
            try {
                String path = cursor.getString(cursor.getColumnIndex(Images.Media.DATA));
                return path;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
                if (Build.VERSION.SDK_INT < 14) {
                    cursor.close();
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static MediaData getVideoInfoByPath(Activity ctx, String videoPath) {
        Point thumbPoint = getVideoThumbnail(videoPath);

        Uri mUri = Uri.parse("content://media/external/video/media");
        MediaData mediaData = new MediaData(videoPath);

        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = {videoPath};// 查询条件参数
        @SuppressWarnings("deprecation")
        Cursor cursor =
                ctx.managedQuery(mUri, null, selection, selectionArgs,
                        MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                // uri
                int ringtoneID = cursor.getInt(cursor.getColumnIndex(Video.VideoColumns._ID));
                Uri mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
                mediaData.setFileUri(mImageUri);

                String name =
                        cursor.getString(cursor.getColumnIndex(Video.VideoColumns.DISPLAY_NAME));
                mediaData.setName(name);

                long duration = cursor.getLong(cursor.getColumnIndex(Video.VideoColumns.DURATION));
                mediaData.setDuration(duration);

                // size
                String resolution =
                        cursor.getString(cursor.getColumnIndex(Video.VideoColumns.RESOLUTION));
                if (StrUtil.notEmptyOrNull(resolution)) {
                    String[] reStrings = resolution.split("x");
                    if (reStrings != null && reStrings.length == 2) {
                        try {
                            mediaData.setWidthHeight(new Point(Integer.parseInt(reStrings[0]),
                                    Integer.parseInt(reStrings[1])));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        float videoScale = 1.0f;
                        if (thumbPoint != null && thumbPoint.x != 0 && thumbPoint.y != 0) {
                            if ((thumbPoint.x > thumbPoint.y && mediaData.getWidthHeight().x > mediaData
                                    .getWidthHeight().y)
                                    || (thumbPoint.x < thumbPoint.y && mediaData.getWidthHeight().x < mediaData
                                    .getWidthHeight().y)) {
                                videoScale =
                                        ((float) mediaData.getWidthHeight().x)
                                                / ((float) mediaData.getWidthHeight().y);
                                mediaData.setWantRotaion(false);
                            } else {
                                videoScale =
                                        ((float) mediaData.getWidthHeight().y)
                                                / ((float) mediaData.getWidthHeight().x);
                                mediaData.setWantRotaion(true);
                            }
                        } else {
                            if (mediaData.getWidthHeight().x != 0
                                    && mediaData.getWidthHeight().y != 0) {
                                videoScale =
                                        ((float) mediaData.getWidthHeight().x)
                                                / ((float) mediaData.getWidthHeight().y);
                            } else {
                                videoScale = 1.0f;
                            }
                        }
                        mediaData.setVideoScale(videoScale);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
                if (Build.VERSION.SDK_INT < 14) {
                    cursor.close();
                }
            }
            return mediaData;
        } else {
            return mediaData;
        }
    }

    public static boolean isShowPic(AttachmentData temp) {
        return temp.getType() == AttachType.PICTURE.value()
                || temp.getType() == AttachType.VIDEO.value();
    }

    /**
     * 获取
     */
    public static float getVideoScale(Activity ctx, String filePath) {
        if (filePath.startsWith("content")) {
            return 1.0f;
        } else {
            MediaData mediaData = getVideoInfoByPath(ctx, filePath);
            if (mediaData == null) {
                return 1.0f;
            } else {
                return mediaData.getVideoScale();
            }
        }
    }

    public static final Point getVideoThumbnail(String videoPath) {
        // 获取视频的缩略图
        final Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, Thumbnails.MINI_KIND);
        if (bitmap != null) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    try {
                        bitmap.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Point point = new Point(w, h);
            return point;
        } else {
            return null;
        }
    }

    public static void resetSendingStatus() {
        final DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            // 聊天
//            dbUtil.updateWhere(MsgData.class, "send_status = " + MsgSendStatusEnum.ERROR.value(),
//                    "send_status = " + MsgSendStatusEnum.SENDING.value());
//            // 会议
//            dbUtil.updateWhere(DiscussProgress.class,
//                    "sendStatus = " + DataStatusEnum.SEND_ERROR.value(), "sendStatus = "
//                            + DataStatusEnum.SENDIND.value());
//            // 微博
//            dbUtil.updateWhere(WeBoData.class, "sendStatus = " + DataStatusEnum.SEND_ERROR.value(),
//                    "sendStatus = " + DataStatusEnum.SENDIND.value());
//            // 工作圈
//            dbUtil.updateWhere(WcData.class, "sendStatus = " + DataStatusEnum.SEND_ERROR.value(),
//                    "sendStatus = " + DataStatusEnum.SENDIND.value());
//
//            dbUtil.updateWhere(TaskProgress.class,
//                    "sendStatus = " + DataStatusEnum.SEND_ERROR.value(), "sendStatus = "
//                            + DataStatusEnum.SENDIND.value());
//
//            dbUtil.updateWhere(ProjectProgress.class,
//                    "sendStatus = " + DataStatusEnum.SEND_ERROR.value(), "sendStatus = "
//                            + DataStatusEnum.SENDIND.value());
//
//            dbUtil.updateWhere(UpAttachData.class,
//                    "sendStatus = " + DataStatusEnum.SEND_ERROR.value(), "sendStatus = "
//                            + DataStatusEnum.SENDIND.value());

            dbUtil.updateWhere(WaitSendData.class,
                    "sendStatus = " + DataStatusEnum.SEND_ERROR.value(), "sendStatus = "
                            + DataStatusEnum.SENDIND.value());

            List<WaitUpFileData> waitUpFileLists = dbUtil.findAllByWhereNoCo(WaitUpFileData.class, "operateId <> '' and sendStatus in ("
                    + DataStatusEnum.SENDIND.value() + "," + DataStatusEnum.SEND_ERROR.value() + ")");
            if (StrUtil.listNotNull(waitUpFileLists)) {
                for (final WaitUpFileData waitUpData: waitUpFileLists) {
                    final String operateId  = waitUpData.getOperateId();
                    if (StrUtil.notEmptyOrNull(operateId)) {
                        ServiceParams params = new ServiceParams(ComponentReqEnum.UP_LOAD_FILE_SIZE_RELEASE.order());
                        params.put("operateId", operateId);
                        UserService.getDataFromServer(params, new ServiceRequester() {
                            @Override
                            public void onResult(ResultEx resultEx) {
                                dbUtil.updateWhere(WaitUpFileData.class, "operateId = ''", "id=" + waitUpData.getId());
                            }

                            @Override
                            public void onError(Integer errCode) {
                                super.onError(errCode);
                                dbUtil.updateWhere(WaitUpFileData.class, "operateId = ''", "id=" + waitUpData.getId());
                            }
                        });
                    }
                }
            }

            dbUtil.updateWhere(WaitUpFileData.class,
                    "sendStatus = " + DataStatusEnum.SEND_ERROR.value(), "sendStatus = "
                            + DataStatusEnum.SENDIND.value());
        }
    }

//    /**
//     * 判断登录
//     */
//    public static void getLoginInfo(Activity ctx) {
//        LoginUserData loginUser = WeqiaApplication.getInstance().getLoginUser();// 缓存用户信息
//        // 缓存了用户信息
//        if (loginUser != null) {
//            if (StrUtil.listIsNull(WeqiaApplication.getInstance().getCos())) {
//                ContactUtil.getCosFromNt();
//            }
//        } else {
//            // 没缓存用户信息,直接登录
//            if (StrUtil.notEmptyOrNull(WPfCommon.getInstance().get(Hks.user_account, String.class))) {
//                Intent intent = new Intent(ctx, SplashActivity.class);
//                intent.putExtra("toClass", 1); // 1 to LoginActivity 2 to RegLoginActivity
//                ctx.startActivity(intent);
//                ctx.finish();
//            } else {
//                // 不含任何信息,进入注册或登录
//                Intent intent = new Intent(ctx, SplashActivity.class);
//                intent.putExtra("toClass", 2); // 1 to LoginActivity 2 to RegLoginActivity
//                ctx.startActivity(intent);
//                ctx.finish();
//            }
//        }
//    }
//    /**
//     * 发送确认
//     */
//    public static void sendFileConfirm(Activity ctx, DialogInterface.OnClickListener listener) {
//        if (WeqiaApplication.transData != null) {
//            if (WeqiaApplication.transData.isOuter()) {
//                outsideFileDialog(ctx, listener);
//            } else {
//                insideFileDialog(ctx, listener);
//            }
//        }
//    }
//
//    protected static void insideFileDialog(Activity ctx, DialogInterface.OnClickListener listener) {
//        LayoutInflater mInflater = LayoutInflater.from(ctx);
//        BaseData insideData = WeqiaApplication.transData.getInsideData();
//        if (insideData != null && insideData instanceof LinksData) {
//            View linkView = mInflater.inflate(R.layout.view_transpond_link, null);
//            final ImageView ivIcon = (ImageView) linkView.findViewById(R.id.ivIcon);
//            final TextView tvName = (TextView) linkView.findViewById(R.id.tvName);
//            final EditText etExtend = (EditText) linkView.findViewById(R.id.et_link_extend);
//            etExtend.addTextChangedListener(new TextWatcher() {
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if (WeqiaApplication.transData != null) {
//                        WeqiaApplication.transData.setTransExtend(etExtend.getText().toString());
//                    }
//                }
//            });
//            LinksData linksData = (LinksData) insideData;
//            if (StrUtil.notEmptyOrNull(linksData.getTitle())) {
//                tvName.setText(linksData.getTitle());
//            }
//            if (StrUtil.isEmptyOrNull(linksData.getContent())) {
//                if (StrUtil.notEmptyOrNull(linksData.getUrl())) {
//                    tvName.setText(linksData.getUrl());
//                }
//            } else {
//                tvName.setText(linksData.getContent());
//            }
//            if (StrUtil.isEmptyOrNull(linksData.getImage())) {
//                ivIcon.setImageResource(R.drawable.bg_image_bigurl);
//            } else {
//                WeqiaApplication.getInstance().getBitmapUtil()
//                        .load(ivIcon, linksData.getImage(), null); // displayImage(linksData.getImage(),
//
//            }
//            DialogUtil.initOpenFileDialog(ctx, listener, linksData.getTitle(), linkView).show();
//        } else {
//            View view = mInflater.inflate(R.layout.view_transpond, null);
//            final ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
//            final TextView tvName = (TextView) view.findViewById(R.id.tvName);
//
//            BaseData sendToData = WeqiaApplication.transData.getSendToData();
//            if (sendToData != null) {
//                if (sendToData instanceof SelData) {
//                    SelData contact = (SelData) sendToData;
//                    if (StrUtil.notEmptyOrNull(contact.getmName())) {
//                        tvName.setText(contact.getmName());
//                    }
//                    if (StrUtil.notEmptyOrNull(contact.getmLogo())) {
//                        WeqiaApplication
//                                .getInstance()
//                                .getBitmapUtilLoadContact()
//                                .load(ivIcon, contact.getmLogo(),
//                                        ImageThumbTypeEnums.THUMB_VERY_SMALL.value());
//                    } else {
//                        ivIcon.setImageResource(GlobalUtil.getPeopleRes(ctx));
//                    }
//                } else if (sendToData instanceof DiscussData) {
//                    DiscussData discussData = (DiscussData) sendToData;
//                    tvName.setText(DiscussHandle.getDiscussTitle(discussData));
//                    DiscussShowHandle.showIcon(ivIcon, discussData.getdId());
////                    DiscussHandle.showDiscussIcon(ivIcon, discussData);
//                }
//            } else {
//                L.e("内部转发没有目的地");
//            }
//            DialogUtil.initOpenFileDialog(ctx, listener, "确认发送给:", view).show();
//        }
//    }
//
//
//    protected static void outsideFileDialog(Activity ctx, DialogInterface.OnClickListener listener) {
//        String tilte = ctx.getString(R.string.send_to_name);
//        if (WeqiaApplication.transData.getMediaData() != null) {
//
//            String path = WeqiaApplication.transData.getMediaData().getPath();
//            if (path.contains("/external/images/media")) {
//                String realPath = getPathFromUri(ctx, new File(path).getName());
//                if (StrUtil.notEmptyOrNull(realPath)) {
//                    WeqiaApplication.transData.getMediaData().setPath(realPath);
//                }
//            }
//            String name =
//                    ctx.getString(R.string.path_name)
//                            + WeqiaApplication.transData.getMediaData().getNameStr();
//            String size =
//                    ctx.getString(R.string.size_name)
//                            + WeqiaApplication.transData.getMediaData().getSizeStr();
//
//            BaseData sentToData = WeqiaApplication.transData.getSendToData();
//            if (sentToData == null) {
//                tilte += "我的文件";
//            } else if (sentToData instanceof EnterpriseContact) {
//                tilte += ((EnterpriseContact) sentToData).getmName();
//            } else if (sentToData instanceof DiscussData) {
//                tilte += DiscussHandle.getDiscussTitle(((DiscussData) sentToData));
//            }
//
//            if (WeqiaApplication.transData.getContentType() == MsgTypeEnum.IMAGE.value()) {
//                DialogUtil.initOpenFileDialog(
//                        ctx,
//                        listener,
//                        tilte,
//                        new OpenFileView(ctx, name, size, WeqiaApplication.transData.getMediaData()
//                                .getPath())).show();
//            } else {
//                DialogUtil.initOpenFileDialog(ctx, listener, tilte,
//                        new OpenFileView(ctx, name, size, null)).show();
//            }
//        } else {
//            L.e("外部发送没有媒体文件，不能发送");
//        }
//    }
//
//    public static void changeCoDo(CompanyInfoData toCpData) {
//        WeqiaApplication.gWorkerPjId = null;
//        WeqiaApplication.addRf(RefeshKey.MEMBER);
//        WeqiaApplication.addRf(RefeshKey.ENTERPRISE_INFO);
//        if (toCpData == null) {
//            toCpData = new CompanyInfoData();
//            toCpData.setgCoId(null);
//            if (L.D) L.e("没有组织信息，虚构一个");
//        }
//
//        if (WPf.getInstance().getCachedObjs() != null) {
//            WPf.getInstance().getCachedObjs().evictAll();
//        }
//        WPf.setInstance(null);
//
//
//        NotificationHelper.clearNotificationById();
//        WeqiaApplication.setgMCoId(toCpData.getCoId());
//        if (toCpData.getgCoId() != null) {
//            ContactUtil.syncContact(toCpData.getCoId());
//            ContactUtil.getCoDepartment(toCpData.getCoId());
//            PunchUtil.getInstance().getPunchDetail(TimeUtils.getTimesMorning(), true, null, true);
//
//            List<CompanyPlugData> tmpDatas = CoPlugUtil.getCompanyPlugs(toCpData.getCoId());
//            if (tmpDatas == null || tmpDatas.size() == 0) {
//                CoPlugUtil.getCompanyPlugAll(true, true);
//            }
//            XUtil.getCategoryDb();
//            XUtil.getNCategoryDb();
//        }
//        XUtil.initWorkerMember(toCpData.getCoId());
//        XUtil.markCoDownloadContact();
//        // 切换后替换企业信息[全局企业ID,企业名称,企业LOGO,用户角色,审核状态]
//        LoginUserData loginUser = WeqiaApplication.getInstance().getLoginUser();
//        loginUser.setCoId(toCpData.getCoId());
//        loginUser.setCoName(toCpData.getCoName());
//        loginUser.setCoLogo(toCpData.getCoLogo());
//        // 新角色
//        if (StrUtil.notEmptyOrNull(toCpData.getRoleId())) {
//            loginUser.setRoleId(toCpData.getRoleId());
//        } else {
//            loginUser.setRoleId("");
//        }
//        if (StrUtil.notEmptyOrNull(toCpData.getStatus())) {
//            if (toCpData.getStatus().equals("3")) {// 审核通过
//                loginUser.setJoinStatus("1");// 已加入
//            } else if (toCpData.getStatus().equals("2")) {
//                loginUser.setJoinStatus("2");// 申请中
//            }
//            if (!toCpData.getStatus().equals("2")) {
//                GlobalUtil.reporteChangeLog(WeqiaApplication.getgMCoId());
//            }
//        }
//        WeqiaApplication.getInstance().setLoginUser(loginUser);
//
//        if (toCpData.getTeamRoleId() != null) {
//            WeqiaApplication.setTeamRoleId(toCpData.getTeamRoleId());
//        } else {
//            WeqiaApplication.setTeamRoleId(null);
//        }
//
//
//        EventBus.getDefault().post(new RefreshEvent(EnumDataTwo.RefreshEnum.TITLTPOP_RECOVER.getValue()));
//
//    }
//
//    private static void reporteChangeLog(String coId) {
//        ServiceParams serviceParams = new ServiceParams(EnumData.RequestType.REPORTE_LOG.order());
//        serviceParams.put("mCoId", coId);
//        Integer loginWay = WPfCommon.getInstance().get(Hks.login_way, Integer.class, 0);
//        if (loginWay.intValue() == EnumData.LoginWayTypeEnum.QQ.value().intValue()) {
//            serviceParams.put("loginNo",
//                    WPfCommon.getInstance().get(Hks.qq_login_open_id, String.class));
//        } else if (loginWay.intValue() == EnumData.LoginWayTypeEnum.WX.value().intValue()) {
//            serviceParams.put("loginNo",
//                    WPfCommon.getInstance().get(Hks.wx_login_open_id, String.class));
//        } else {
//            serviceParams.put("loginNo", WPfCommon.getInstance()
//                    .get(Hks.user_account, String.class));// .getUserAccount());
//        }
//        UserService.getDataFromServer(serviceParams, new ServiceRequester() {
//            @Override
//            public void onResult(ResultEx resultEx) {
//                if (resultEx.isSuccess()) {
//                }
//            }
//        });
//    }
//
//    /**
//     * 获取已加入的组织列表
//     */
//    public static List<CompanyInfoData> getJoinCoInfos() {
//        List<CompanyInfoData> joinedCompany = new ArrayList<CompanyInfoData>();
//        for (CompanyInfoData coInfo : WeqiaApplication.getInstance().getCos()) {
//            if (coInfo.getStatus().equalsIgnoreCase("1")
//                    || coInfo.getStatus().equalsIgnoreCase("3")) {
//                joinedCompany.add(coInfo);
//            }
//        }
//        return joinedCompany;
//    }

    public static TextView getBanner(SharedTitleActivity ctx, String title) {
        TextView textView =
                (TextView) LayoutInflater.from(ctx).inflate(R.layout.view_banner_text, null);
        textView.setText(title);
        return textView;
    }

    public static int getNotEqualIndex(String moreStr, String lessStr) {
        int notEqual = -1;
        for (int i = 0; i < moreStr.length(); i++) {
            char aS = moreStr.charAt(i);
            if (lessStr.length() <= i) {
                notEqual = i;
                break;
            }
            char bS = lessStr.charAt(i);
            if (aS != bS) {
                notEqual = i;
                break;
            }
        }
        return notEqual;
    }

    /**
     * @param bMute 值为true时为关闭背景音乐。
     */
    public static boolean muteAudioFocus(Context context, boolean bMute) {
        if (context == null) {
            return false;
        }
        boolean bool = false;
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (bMute) {
            int result =
                    am.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        } else {
            int result = am.abandonAudioFocus(null);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        return bool;
    }

    public static String getMac(Context ctx, boolean newVersion) {
        // MD5（MAC+IMEI）生成
        String mac = DeviceUtil.getAdresseMAC(ctx);
        String imei = DeviceUtil.getIMEI();
        String imsi = DeviceUtil.getIMSI();
        if (!newVersion) {
            return StrUtil.getMD5String(mac + imei);
        } else {
            Map<String, String> sMap = new HashMap<>();
            sMap.put("imei", imei);
            sMap.put("wmac", mac);
            sMap.put("sim", imsi);
            return JSONObject.toJSONString(sMap);
        }
    }

    public static String getMac(boolean newVersion) {
        return getMac(null, newVersion);
    }

    /**
     * 计算两点之间距离
     * @return 米
     */
    public static int getDistance(LatLng start, LatLng end) {
        double pi = ArithUtil.div(Math.PI, 180);
        double lat1 = ArithUtil.mul(pi, start.latitude);
        double lat2 = ArithUtil.mul(pi, end.latitude);
        double lon1 = ArithUtil.mul(pi, start.longitude);
        double lon2 = ArithUtil.mul(pi, end.longitude);
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double a1 = ArithUtil.mul(Math.sin(lat1), Math.sin(lat2));
        double a2 = ArithUtil.mul(Math.cos(lat1), Math.cos(lat2));
        double a3 = ArithUtil.mul(a2, Math.cos(lon2 - lon1));
        double a4 = Math.acos(ArithUtil.add(a1, a3));

        double d = ArithUtil.mul(a4, R);
        double length = Math.rint(d * 1000) / 1000;
        return (int) (length * 1000);
    }


    /**
     * 根据视图和文件显示view
     */
    public static void setFileView(String files, PictureGridView pictrueView) {
        // 附件
        if (StrUtil.notEmptyOrNull(files)) {
            List<AttachmentData> attachmentDatas = JSON.parseArray(files, AttachmentData.class);
            setFileView(attachmentDatas, pictrueView);
        }
    }

    public static void setFileView(List<AttachmentData> attachmentDatas, PictureGridView pictrueView) {
        if (attachmentDatas != null) {
            pictrueView.getAddedPaths().clear();
            for (AttachmentData attData : attachmentDatas) {
                if (attData == null) continue;
                if (attData.getType() == AttachType.PICTURE.value()) {
                    pictrueView.getAddedPaths().add(attData.getUrl());
                } else if (attData.getType() == AttachType.FILE.value()) {
                    pictrueView.getAddedPaths().add(
                            AttachType.FILE.value() + GlobalConstants.SPIT_SENDMEDIA
                                    + attData.getName() + GlobalConstants.SPIT_SENDMEDIA
                                    + attData.getUrl());
                } else if (attData.getType() == AttachType.VIDEO.value()) {
                    pictrueView.getAddedPaths().add(
                            AttachType.VIDEO.value() + GlobalConstants.SPIT_SENDMEDIA
                                    + attData.getPlayTime() + GlobalConstants.SPIT_SENDMEDIA
                                    + attData.getUrl() + GlobalConstants.SPIT_SENDMEDIA
                                    + attData.getVideoPrew());
                }
                LnUtil.saveAttachData(attData, null);
            }
            pictrueView.refresh();
        }
    }

    /**
     * 显示地址，包括点击
     */
    public static void showLoc(final SharedTitleActivity ctx, final String title, TextView tvView,
                               final MyLocData myLocData) {
        if (myLocData == null || tvView == null) {
            ViewUtils.hideView(tvView);
            return;
        }
        String addrName = null;
        if (StrUtil.notEmptyOrNull(myLocData.getAddrStr())) {
            addrName = myLocData.getAddrStr();
        }
        if (StrUtil.notEmptyOrNull(myLocData.getAddrName())
                && !"[位置]".equals(myLocData.getAddrName())) {
            addrName = myLocData.getAddrName();
        }
        ViewUtils.showView(tvView);
        ViewUtils.setTextView(tvView, addrName);
        tvView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ctx.startToActivity(LocationActivity.class, title, myLocData);
            }
        });
    }

    /**
     * 显示地址，包括点击
     * @param ctx
     * @param title
     * @param tvView
     * @param addrStr
     * @param adName
     * @param px
     * @param py
     */
    public static void showLoc(final SharedTitleActivity ctx, final String title, TextView tvView,
                               final String addrStr, String adName, final Float px, final Float py) {
        if (StrUtil.isEmptyOrNull(addrStr) || tvView == null) {
            ViewUtils.hideView(tvView);
            return;
        }
        ViewUtils.setTextView(tvView, addrStr);
        ViewUtils.showView(tvView);
        if (StrUtil.isEmptyOrNull(adName)) {
            adName = "[位置]";
        }
        if (px != null && py != null) {
            final String adFname = adName;
            tvView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MyLocData myLocData =
                            new MyLocData((double) px, (double) py, null, null, null, null, null,
                                    null, null, addrStr, null, null, adFname, true);
                    ctx.startToActivity(LocationActivity.class, title, myLocData);
                }
            });
        }
    }

    public static View getEmptyView(Context ctx, boolean canAdd) {
        if (ctx == null) {
            return null;
        }
        LayoutInflater mInflater =
                (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout newEmptyView = null;
        if (canAdd) {
            newEmptyView = (RelativeLayout) mInflater.inflate(R.layout.view_data_can_add, null);
            ViewUtils.setImageRes((CommonImageView) newEmptyView.findViewById(R.id.iv_canadd),
                    R.drawable.data_null_can_add_icon);
        } else {
            newEmptyView = (RelativeLayout) mInflater.inflate(R.layout.view_data_null, null);
            ViewUtils.setImageRes((CommonImageView) newEmptyView.findViewById(R.id.iv_notadd),
                    R.drawable.data_null_icon);
        }
        return newEmptyView;
    }

    /**
     * 加载完成
     * @param ctx
     * @param plListView
     * @param canAdd
     */
    public static void loadComplete(final PullToRefreshListView plListView, final Context ctx,
                                    Boolean canAdd) {
        View empView = null;
        if (canAdd != null) {
            empView = getEmptyView(ctx, canAdd);
        }
        loadComplete(plListView, ctx, empView, null);
    }

    /**
     * 加载完成
     * @param ctx
     * @param canAdd
     */
    public static void loadComplete(final LuRecyclerView recyclerView, final Context ctx, Boolean canAdd) {
        View empView = null;
        if (canAdd != null) {
            empView = getEmptyView(ctx, canAdd);
            recyclerView.setEmptyView(empView);
        }
    }

    /**
     * 加载完成
     * @param ctx
     * @param plListView
     * @param view
     * @param obj        空置
     */
    public static void loadComplete(final PullToRefreshListView plListView, final Context ctx, final View view, String obj) {

        if (plListView == null) {
            return;
        }
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                if (ctx instanceof SharedTitleActivity) {
                    TitleView titleView = ((SharedTitleActivity) ctx).sharedTitleView;
                    if (titleView != null) {
                        ViewUtils.hideView(titleView.getPbTitle());
                    }
                }
                if (view != null) {
                    ListView listView = plListView.getRefreshableView();
                    int adSize = listView.getCount();
                    int heSize = listView.getHeaderViewsCount() + listView.getFooterViewsCount();
                    if (adSize - heSize == 0) {
                        plListView.setEmptyView(view);
                    }
                }
                plListView.onRefreshComplete();
                plListView.onLoadMoreComplete();
                plListView.onLoadTopComplete();
            }
        });
    }

    public static String getStringByInt(Integer intData) {
        return intData == null ? null : intData + "";
    }

//    /**
//     * 带bucket的url地址
//     *
//     * @param bucket
//     * @param url
//     * @return
//     */
//    public static String wrapBucketUrl(String bucket, String url) {
//        if (StrUtil.isEmptyOrNull(bucket))
//            return url;
//        return "2" + GlobalConstants.BUCKET_SPIT + bucket + GlobalConstants.BUCKET_SPIT + url;
//    }

    /**
     * @param accountType
     * @param bucket
     * @param url
     * @return
     */
    public static String wrapBucketUrl(Integer accountType, String bucket, String url) {
        return wrapBucketUrl(null, accountType, bucket, url);
    }

    public static String wrapBucketUrl(String versionId, Integer accountType, String bucket, String url) {
        if (StrUtil.isEmptyOrNull(bucket) || accountType == null) {
            L.e("没有bucketId");
            return url;
        }
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String wrapedUrl;
        if (StrUtil.isEmptyOrNull(versionId)) {
            wrapedUrl = accountType + GlobalConstants.BUCKET_SPIT
                    + bucket + GlobalConstants.BUCKET_SPIT + url;
        } else {
            wrapedUrl = accountType + GlobalConstants.BUCKET_SPIT
                    + bucket + GlobalConstants.BUCKET_SPIT
                    + url + GlobalConstants.BUCKET_SPIT + versionId;
        }

        return wrapedUrl;
    }

    public static String wrapBucketUrlWithVersionId(String versionId) {
        String wrapedUrl = "" + GlobalConstants.BUCKET_SPIT
                + "" + GlobalConstants.BUCKET_SPIT + "" + GlobalConstants.BUCKET_SPIT + versionId;
        return wrapedUrl;
    }
//    public static String upWrapBucketVersionId(String url) {
//        if (isCombination(url)) {
//            return url.substring(0, url.indexOf(GlobalConstants.BUCKET_SPIT));
//        }
//        return null;
//    }

    public static boolean isCombination(String url) {
        return url.contains(GlobalConstants.BUCKET_SPIT);
    }

    /**
     * 带noteId的本地地址
     * @param noteId
     * @param path
     * @return
     */
    public static String wrapNodePath(String noteId, String path) {
        if (StrUtil.isEmptyOrNull(noteId))
            return path;
        return noteId + GlobalConstants.NODE_SPIT + path;
    }

    public static String unWrapedNodeId(String notedPath) {
        if (notedPath.contains(GlobalConstants.NODE_SPIT)) {
            return notedPath.substring(0, notedPath.indexOf(GlobalConstants.NODE_SPIT));
        }
        return null;
    }

    public static String unWrapedPath(String notedPath) {
        if (notedPath.contains(GlobalConstants.NODE_SPIT)) {
            return notedPath.substring(notedPath.indexOf(GlobalConstants.NODE_SPIT) + GlobalConstants.NODE_SPIT.length());
        }
        return notedPath;
    }

    public static String unWrapBucketUrl(String wrapBucketUrl) {
        if (wrapBucketUrl.contains(GlobalConstants.BUCKET_SPIT)) {
            return wrapBucketUrl.substring(wrapBucketUrl.lastIndexOf(GlobalConstants.BUCKET_SPIT) + GlobalConstants.BUCKET_SPIT.length());
        }
        return wrapBucketUrl;
    }
}
