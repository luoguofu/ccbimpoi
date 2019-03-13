package com.weqia.wq.component.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.RemoteViews;

import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.bitmap.ImageUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.base.NotifyData;
import com.weqia.wq.data.base.RingData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;

import java.util.List;

public class NotificationHelper {
    private static Integer notificationId = GlobalConstants.G_NOTIFICATION_ID;
    private static NotificationManager nm;

    PendingIntentEnum pendingIntentEnum = PendingIntentEnum.MAIN;

    public enum PendingIntentEnum {
        TALK, DISCUSS, MAIN, PUNCH
    }


    public static int getNotifiIcon() {
        return R.drawable.notification_icon;
    }

//    // 单聊,群聊->展示usr icon name content
//    public static void talkNotification(Context context, BaseData data, MsgWarnData warnData) {
//
//        if (data == null) {
//            return;
//        }
//        // 提醒类型 0 - 普通提醒 小红点 1- 相关提醒（负责人,@人员） 2-不标记
//        if (warnData != null && warnData.getWarnType() != MsgWarnData.warnTypeEnum.IMPORTANT.value()) {
//            return;
//        }
//
//        boolean bTalk = true;
//        MsgData msgData = null;
//        DiscussProgress progress = null;
//        String msgLogo = null;
//        String msgTitle = null;
//        String msgContent = null;
//        String business_id = null;
//        Integer unRead = 0;
//        if (data instanceof MsgData) {
//            msgData = (MsgData) data;
//            bTalk = true;
//        } else if (data instanceof DiscussProgress) {
//            progress = (DiscussProgress) data;
//            bTalk = false;
//        }
//        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
//        NotifyData notifyData = null;
//        // String who = "";
//        if (bTalk) {
//            // 聊天跟企业没关了 即便是企业里面有该人 也不能用 对应的需是会员 不然企业同步名字会对应不上
//             SelData contact = ContactUtil.getCMByMid(msgData.getFriend_id(), null, true, true);
////            SelData contact = ContactUtil.getCMByMid(msgData.getFriend_id(), "-2");
//            if (contact != null) {
//                if (StrUtil.notEmptyOrNull(contact.getmLogo())) {
//                    msgLogo = contact.getmLogo();
//                }
//                if (StrUtil.notEmptyOrNull(contact.getmName())) {
//                    msgTitle = contact.getmName();
//                }
//                if (StrUtil.notEmptyOrNull(msgData.getFriend_id())) {
//                    business_id = msgData.getFriend_id();
//                }
//            }
//            if (msgData.getType() == MsgTypeEnum.TEXT.value()) {// 文字
//                if (StrUtil.notEmptyOrNull(msgData.getContent())) {
//                    msgContent = msgData.getContent();
//                }
//            } else if (msgData.getType() == MsgTypeEnum.LINK.value()) {
//                if (StrUtil.notEmptyOrNull(msgData.getContent())) {
//                    try {
//                        LinksData linksData =
//                                JSON.parseObject(msgData.getContent(), LinksData.class);
//                        if (linksData != null) {
//                            msgContent = "[链接]" + linksData.getTitle();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (StrUtil.isEmptyOrNull(msgContent)) {
//                        msgContent = "[链接]";
//                    }
//                }
//            } else if (msgData.getType() == MsgTypeEnum.BUSINESS_CARD.value()) {
//                try {
//                    msgContent = "";
//                    BusinessCardData cardData = JSON.parseObject(msgData.getContent(), BusinessCardData.class);
//                    if (cardData != null && StrUtil.notEmptyOrNull(cardData.getmName())) {
//                        msgContent += cardData.getmName();
//                    }
//                    if (msgData.getWho().intValue() == EnumData.MsgSendPeopleEnum.ME.value()) {
//                        msgContent = "你推荐了" + msgContent;
//                    } else {
//                        msgContent = msgTitle + "向你推荐了" + msgContent;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                msgContent = "[" + MsgTypeEnum.getNameByValue(msgData.getType()) + "]";
//            }
//            unRead = dbUtil.findNoReadByWhereN(MsgData.class, "friend_id='" + business_id + "'");
//        } else {
//            // 会议
//            business_id = progress.getdId();
//            TalkListData talkListData =
//                    dbUtil.findByWhere(TalkListData.class, "business_id='" + business_id + "'");
//            if (talkListData != null) {
//                if (StrUtil.notEmptyOrNull(talkListData.getTitle())) {
//                    msgTitle = talkListData.getTitle();
//                }
//                msgLogo = talkListData.getAvatar();
//                // 进展的发起人
//                SelData contact = ContactUtil.getCMByMid(progress.getMid(), progress.getgCoId());
//                if (contact != null) {
//                    if (StrUtil.notEmptyOrNull(contact.getmName())) {
//                        msgContent = contact.getmName() + ":";
//                    }
//                }
//                if (progress != null && StrUtil.notEmptyOrNull(progress.getContent())) {
//                    msgContent = msgContent + progress.getContent();
//                } else {
//                    msgContent = msgContent + DiscussHandle.getDiscussProgressNotice(progress);
//                }
//                if (progress.getMsgType() != null && progress.getMsgType() == DiscussProgress.DiscussMsgType.BUSINESS_CARD.value()) {
//                    try {
//                        msgContent = "";
//                        BusinessCardData cardData = JSON.parseObject(progress.getUniversal(), BusinessCardData.class);
//                        if (cardData != null && StrUtil.notEmptyOrNull(cardData.getmName())) {
//                            msgContent += cardData.getmName();
//                        }
//
//                        boolean isFriend = false;
//                        if (data != null && StrUtil.notEmptyOrNull(progress.getMid())
//                                && !progress.getMid().equalsIgnoreCase(WeqiaApplication.getInstance().getLoginUser().getMid())) {
//                            isFriend = true;
//                        }
//                        if (!isFriend) {
//                            msgContent = "你推荐了" + msgContent;
//                        } else {
//                            msgContent = contact.getmName() + "向你推荐了" + msgContent;
//                        }
//
//                        if (StrUtil.notEmptyOrNull(msgContent)) {
//                            talkListData.setContent(msgContent);
//                            dbUtil.save(talkListData);
//                        }
////                        progress.setContent();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                unRead =
//                        BaseUtils.msgListUnReadCount(EnumDataTwo.MsgBusinessType.DISCUSS.value(),
//                                business_id);
//            } else {
//                SelData contact = ContactUtil.getCMByMid(progress.getMid(), progress.getgCoId());
//                if (contact != null) {
//                    if (StrUtil.notEmptyOrNull(contact.getmName())) {
//                        msgTitle = contact.getmName() + "发起了会议";
//                    }
//                }
//                if (progress != null && StrUtil.notEmptyOrNull(progress.getContent())) {
//                    msgContent = progress.getContent();
//                }
//            }
//        }
//
//
//        if (StrUtil.notEmptyOrNull(msgContent) && msgContent.startsWith("null")) {
//            msgContent = msgContent.substring(0, 4);
//        }
//
//        if (dbUtil != null) {
//            notifyData = dbUtil.findById(business_id, NotifyData.class);
//            if (notifyData != null) {
//                if (unRead > 1) {
//                    msgContent = "[" + unRead + "条]" + msgContent;
//                }
//                dbUtil.update(notifyData);
//            } else {
//                notifyData = new NotifyData(business_id, msgLogo, msgTitle);
//                dbUtil.save(notifyData);
//            }
//        }
//        // 初始化头像
//        Bitmap bitmap = null;
//        if (bTalk) {
//            bitmap =
//                    WeqiaApplication.getInstance().getBitmapUtil()
//                            .getBitmapFromDiskCache(msgLogo + "&th=4");
//        } else {
//            bitmap = DiscussShowHandle.getIcon(business_id);
//        }
//        if (bitmap == null) {
//            bitmap = drawableToBitmap(context.getResources().getDrawable(getNotifiIcon()));
//        }
//
//        if (bitmap != null) {
//            bitmap = ImageUtil.getRoundedCornerBitmap(bitmap, 16);
//        }
//
//
//        boolean bRing = true;
//        if (warnData != null && warnData.getVoiceType() != null) {
//            if (warnData.getVoiceType().intValue() == EnumData.VoiceTypeEnum.VOICE.value()
//                    .intValue()) {
//                bRing = true;
//            } else if (warnData.getVoiceType().intValue() == EnumData.VoiceTypeEnum.SILENCE.value()
//                    .intValue()) {
//                bRing = false;
//            }
//            String tmp_id;
//            if (bTalk) {
//                tmp_id = GlobalConstants.DB_PRE_TALK;
//            } else {
//                tmp_id = GlobalConstants.DB_PRE_DISCUSS;
//            }
//            if (bTalk) {
//                SilenceData silenceData =
//                        new SilenceData(tmp_id + business_id, bRing ? EnumData.VoiceTypeEnum.VOICE
//                                .value().intValue() : EnumData.VoiceTypeEnum.SILENCE.value()
//                                .intValue());
//                dbUtil.save(silenceData);
//            } else {
//                // boolean bVoice = true;
//                // try {
//                // DiscussData discussData = dbUtil.findById(progress.getdId(), DiscussData.class);
//                // if (discussData != null) {
//                // // disData.getRemind() == 1
//                // // 1提醒，2不提醒 -- 2015年8月24日 根据详情 获取静音icon
//                // if (discussData.getRemind() != null && discussData.getRemind() == 2) {
//                // bVoice = true;
//                // }
//                // }
//                // } catch (Exception e) {
//                // e.printStackTrace();
//                // }
//                SilenceData silenceData =
//                        new SilenceData(tmp_id + business_id, bRing ? EnumData.VoiceTypeEnum.VOICE
//                                .value().intValue() : EnumData.VoiceTypeEnum.SILENCE.value()
//                                .intValue());
//                dbUtil.save(silenceData);
//
//            }
//
//        }
//
//        // 声音类型 1有声音 2无声音 -2015年8月21日 无声的也不要出现在通知栏 但是可以有标数字
//        if (warnData == null || warnData.getVoiceType() == null || warnData.getVoiceType() == 2) {
//            return;
//        }
//
//
//        if (StrUtil.isEmptyOrNull(msgTitle)) {
//            msgTitle = WeqiaApplication.getInstance().getString(R.string.weqia_str) + "提醒";
//        }
//        if (StrUtil.isEmptyOrNull(msgContent) && warnData != null
//                && StrUtil.notEmptyOrNull(warnData.getWarn())) {
//            msgContent = warnData.getWarn();
//        }
//        if (StrUtil.isEmptyOrNull(msgContent)) {
//            msgContent = "您有一条未读消息";
//        }
//
//
//        customerNotify(context, data.getgCoId(), bTalk
//                        ? PendingIntentEnum.TALK
//                        : PendingIntentEnum.DISCUSS, msgTitle, msgContent, business_id, notifyData, bitmap,
//                bRing, notificationId, false);
//    }


    public static void tipNotify(Context context, String msgTitle, String msgContent,
                                 boolean bRing, int nId, boolean showAlways, PendingIntentEnum pendingIntentEnum) {
        // NotificationHelper.showMessageNotification(context, bRing, nm, tipStr
        // + ":(您有1条新消息)", warnData.getWarn());
        Bitmap bitmap = ImageUtil.readBitMap(context, getNotifiIcon());
        bitmap = ImageUtil.getRoundedCornerBitmap(bitmap, 16);
        customerNotify(context, null, pendingIntentEnum, msgTitle, msgContent, null, null, bitmap,
                bRing, nId, showAlways);
    }

    public static void punchNotify(Context context, String msgTitle, String msgContent,
                                   boolean bRing, int nId, boolean showAlways, PendingIntentEnum pendingIntentEnum) {
        Bitmap bitmap = ImageUtil.readBitMap(context, getNotifiIcon());
        bitmap = ImageUtil.getRoundedCornerBitmap(bitmap, 16);
        customerNotify(context, null, pendingIntentEnum, msgTitle, msgContent, null, null, bitmap,
                bRing, nId, showAlways, "android.resource://" + context.getPackageName() + "/"
                        + R.raw.punch_notice);
    }

    private static void customerNotify(Context context, String msgCoId,
                                       PendingIntentEnum pendingIntentEnum, String msgTitle, String msgContent,
                                       String business_id, NotifyData notifyData, Bitmap bitmap, boolean bRing, int nId,
                                       boolean showAlways) {
        customerNotify(context, msgCoId, pendingIntentEnum, msgTitle, msgContent, business_id,
                notifyData, bitmap, bRing, nId, showAlways, null);
    }

    private static void customerNotify(Context context, String msgCoId,
                                       PendingIntentEnum pendingIntentEnum, String msgTitle, String msgContent,
                                       String business_id, NotifyData notifyData, Bitmap bitmap, boolean bRing, int nId,
                                       boolean showAlways, String ringPath) {
        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (!showAlways) {
//            int msgCount[] = XUtil.bottomCount();
//            int count = msgCount[0] + 1;
//            BadgeUtil.setBadgeCount(context, count);
        }
        if (nId != 0) {
            notificationId = nId;
        }
        Notification notification = new Notification();
        notification.icon = getNotifiIcon();
        notification = notifyInit(bRing, notification, ringPath);
        RemoteViews view = null;
//        if (pendingIntentEnum == PendingIntentEnum.DISCUSS) {
//            view = new RemoteViews(context.getPackageName(), R.layout.custom_notification_discuss);
//        } else {
            view = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
//        }
        view.setImageViewBitmap(R.id.notification_icon, bitmap);
        view.setTextViewText(R.id.notification_text, msgTitle);
        view.setTextViewText(R.id.notification_subtext, msgContent);
        view.setTextViewText(R.id.notification_time,
                TimeUtils.getDateHM(System.currentTimeMillis()));
        notification.contentView = view;
        Intent intent = null;
//        if (pendingIntentEnum == PendingIntentEnum.TALK) {
//            intent = new Intent(context, TalkActivity.class);
//            intent.putExtra("friend_id", business_id);// 对方ID
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        } else if (pendingIntentEnum == PendingIntentEnum.DISCUSS) {
//            intent = new Intent(context, DiscussProgressActivity.class);
//            DiscussData discussData = new DiscussData();
//            discussData.setdId(business_id);
//            discussData.setgCoId(msgCoId);
//            intent.putExtra(GlobalConstants.KEY_PARAM_DATA, discussData);
//        } else if (pendingIntentEnum == PendingIntentEnum.MAIN) {
//            intent = new Intent(context, BottomMenuActivity.class);
//            // intent.putExtra("btype", 11111);
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        } else if (pendingIntentEnum == PendingIntentEnum.PUNCH) {
//            intent = new Intent(context, PunchActivity.class);
//            // if (nId != PunchNotifiUtil.NOTIFI_AUTO) {
//            intent.putExtra("from", "notice");
//            intent.putExtra(GlobalConstants.KEY_COID, WeqiaApplication.getgMCoId());
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            // }
//        }
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = contentIntent;
        // 提醒规则 消息提醒
        // "客户端接收连续多条消息，只提醒3次，后面的消息只改变数字，不做声音提醒和振动提醒，通知栏提醒可以的话只改变数字；[1min]"
        Long now_time = TimeUtils.getTimeLong();
        Long last_notification_time =
                WPf.getInstance().get(Hks.last_notification_time, Long.class, 0L);
        Integer notifi_counts = WPf.getInstance().get(Hks.notification_counts, Integer.class, 0);
        WPf.getInstance().put(Hks.last_notification_time, now_time);
        if ((now_time - last_notification_time) <= 60 * 1000) {
            notifi_counts += 1;
            WPf.getInstance().put(Hks.notification_counts, notifi_counts);
            if (notifyData != null) {
                if (notifi_counts <= 3) {
                    nm.notify(notifyData.getNotify_id(), notification);
                } else {
                    notification.defaults = Notification.DEFAULT_LIGHTS;
                    nm.notify(notifyData.getNotify_id(), notification);
                }
            } else {
                if (notifi_counts <= 3) {
                    nm.notify(notificationId, notification);
                } else {
                    notification.defaults = Notification.DEFAULT_LIGHTS;
                    nm.notify(notificationId, notification);
                }
            }
        } else {
            WPf.getInstance().put(Hks.notification_counts, 0);
            if (notifyData != null) {
                nm.notify(notifyData.getNotify_id(), notification);
            } else {
                nm.notify(notificationId, notification);
            }
        }
    }

    // private static Notification notifyInit(boolean bRing, Notification notification) {
    // return notifyInit(bRing, notification, null);
    // }

    private static Notification notifyInit(boolean bRing, Notification notification, String ringPath) {
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 300;
        notification.ledOffMS = 1000;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        boolean needSound = WPf.getInstance().get(Hks.msg_content_show, Boolean.class, true);// StatedPerference.getMsgSoundSet();
        boolean needVibrate = WPf.getInstance().get(Hks.msg_vibrate, Boolean.class, true); // StatedPerference.getMsgVibrateSet();
        needSound = needSound && bRing;
        needVibrate = needVibrate && bRing;

        RingData ringData = null;
        String ringInfo = WPf.getInstance().get(Hks.ring_info, String.class);
        if (StrUtil.notEmptyOrNull(ringInfo)) {
            ringData = RingData.fromString(RingData.class, ringInfo);
        }
        if (StrUtil.notEmptyOrNull(ringPath)) {
            if (ringData == null) {
                ringData = new RingData();
            }
            ringData.setUri(ringPath);
        }
        if (ringData != null && StrUtil.notEmptyOrNull(ringData.getUri())) {
            if (needSound && needVibrate) {
                notification.sound = Uri.parse(ringData.getUri());
                notification.defaults = Notification.DEFAULT_VIBRATE;
            } else if (needSound) {
                notification.sound = Uri.parse(ringData.getUri());
            } else if (needVibrate) {
                notification.defaults = Notification.DEFAULT_VIBRATE;
            }
        } else {
            if (needSound && needVibrate) {
                notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            } else if (needSound) {
                notification.defaults = Notification.DEFAULT_SOUND;
            } else if (needVibrate) {
                notification.defaults = Notification.DEFAULT_VIBRATE;
            }
        }


        return notification;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap =
                Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE
                                ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    @SuppressLint("NewApi")
    public static void showMessageNotification(Context context, boolean bRing,
                                               NotificationManager nm, String title, String msgContent) {
        Intent select = new Intent();
//        select.setClass(context, BottomMenuActivity.class);
        select.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationId = GlobalConstants.G_NOTIFICATION_ID;
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, select, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context) //
                .setContentTitle(msgContent) //
                .setSmallIcon(getNotifiIcon()) //
                .setContentIntent(contentIntent) //
                .setWhen(System.currentTimeMillis()) //
                .build();


        if (WPf.getInstance().get(Hks.msg_set, Boolean.class, true)) {
            showMessageNotificationLocal(context, bRing, nm, notification, notificationId);
        }
    }

    private static void showMessageNotificationLocal(Context context, boolean bRing,
                                                     NotificationManager nm, Notification notification, int notificationId) {
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 300;
        notification.ledOffMS = 1000;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        boolean needSound = WPf.getInstance().get(Hks.msg_content_show, Boolean.class, true);// StatedPerference.getMsgSoundSet();
        boolean needVibrate = WPf.getInstance().get(Hks.msg_vibrate, Boolean.class, true);// StatedPerference.getMsgVibrateSet();
        needSound = needSound && bRing;
        needVibrate = needVibrate && bRing;


        RingData ringData = null;
        String ringInfo = WPf.getInstance().get(Hks.ring_info, String.class);
        if (StrUtil.notEmptyOrNull(ringInfo)) {
            ringData = RingData.fromString(RingData.class, ringInfo);
        }
        if (ringData != null && StrUtil.notEmptyOrNull(ringData.getUri())) {
            if (needSound && needVibrate) {
                notification.sound = Uri.parse(ringData.getUri());
                notification.defaults = Notification.DEFAULT_VIBRATE;
            } else if (needSound) {
                notification.sound = Uri.parse(ringData.getUri());
            } else if (needVibrate) {
                notification.defaults = Notification.DEFAULT_VIBRATE;
            }
        } else {
            if (needSound && needVibrate) {
                notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            } else if (needSound) {
                notification.defaults = Notification.DEFAULT_SOUND;
            } else if (needVibrate) {
                notification.defaults = Notification.DEFAULT_VIBRATE;
            }
        }

        // 提醒规则 消息提醒
        // "客户端接收连续多条消息，只提醒3次，后面的消息只改变数字，不做声音提醒和振动提醒，通知栏提醒可以的话只改变数字；[1min]"
        Long now_time = TimeUtils.getTimeLong();
        Long last_notification_time = WPf.getInstance().get(Hks.last_notification_time, Long.class);
        ;
        Integer notifiCounts = WPf.getInstance().get(Hks.notification_counts, Integer.class);
        ;
        // StatedPerference.setLastNotificationTime(now_time);
        WPf.getInstance().put(Hks.last_notification_time, now_time);
        if ((now_time - last_notification_time) <= 60 * 1000) {
            notifiCounts += 1;
            // StatedPerference.setLastNotificationTimes(notifiCounts);
            WPf.getInstance().put(Hks.notification_counts, notifiCounts);
            if (notifiCounts <= 3) {
                nm.notify(notificationId, notification);
            } else {
                notification.defaults = Notification.DEFAULT_LIGHTS;
                nm.notify(notificationId, notification);
            }
        } else {
            // StatedPerference.setLastNotificationTimes(0);
            WPf.getInstance().put(Hks.notification_counts, 0);
            nm.notify(notificationId, notification);
        }
    }

    /**
     * 通过id来清除通知 chenjh
     */
    public static void clearNotificationById() {
        if (nm == null) {
            nm =
                    (NotificationManager) WeqiaApplication.ctx
                            .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            List<NotifyData> notifyDatas = dbUtil.findAll(NotifyData.class);
            if (StrUtil.listNotNull(notifyDatas)) {
                for (NotifyData data : notifyDatas) {
                    nm.cancel(data.getNotify_id());
                    dbUtil.deleteByWhere(NotifyData.class, "notify_id=" + data.getNotify_id());
                }
            }
        }
        nm.cancel(GlobalConstants.G_NOTIFICATION_ID);
    }

    public static void clearNotificationById(String business_id) {
        if (nm == null) {
            nm =
                    (NotificationManager) WeqiaApplication.ctx
                            .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            NotifyData notifyData =
                    dbUtil.findByWhere(NotifyData.class, "business_id='" + business_id + "'");
            if (notifyData != null) {
                nm.cancel(notifyData.getNotify_id());
                dbUtil.deleteByWhere(NotifyData.class, "business_id='" + business_id + "'");
            }
        }
    }

    /**
     * 通过id来清除通知 chenjh
     */
    public static void clearNotificationById(int id) {
        if (nm == null) {
            nm =
                    (NotificationManager) WeqiaApplication.ctx
                            .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        nm.cancel(id);
    }
}
