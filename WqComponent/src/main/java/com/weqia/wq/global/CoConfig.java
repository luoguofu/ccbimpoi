package com.weqia.wq.global;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.UtilApplication;
import com.weqia.wq.data.global.WeqiaApplication;

public class CoConfig {


    private static String qrPjId;      //二维码扫描的模型所在项目pjId

    //1,不要，0或无 ，要显示
    public static boolean zt;

    public static boolean about_score = zt; // 评分
    public static boolean about_provision = zt; // 条款
    public static boolean setting_commu = zt; // 社区
    public static boolean setting_function = zt; // 功能介绍
    public static boolean setting_share = zt; // wexin

    // public static boolean work_file = true; // 文件
//    public static boolean invite_all = zt;
    public static boolean invite_qq = zt; // qq
    public static boolean invite_wexin = zt; // wexin
    public static boolean login_qq = zt; // qq登录
    public static boolean login_help = zt; // 客服电话
    public static boolean private_server = zt; // 私有云
    public static boolean common_share = zt; // 分享
    public static boolean use_law = zt; // 使用条款

    public static boolean want_guide = true;
    public static boolean want_friend = true;
    public static boolean want_weqiagood = true;
    public static boolean want_invite = true;
    public static boolean want_registr = true;
    public static boolean want_callVocie = true;

    public static boolean is_progress_shikou = false;


    // public static final boolean wq_list_content_default = zt; //消息列表默认提示 公告：老板喊你领红包！等...

    public static void initConfig() {

        String pkName = UtilApplication.ctx.getPackageName();
        if (pkName.equalsIgnoreCase(ModulesConstants.CN_PINMING_ZZ_PACKAGENAME)
                || pkName.equalsIgnoreCase(ModulesConstants.WEQIA_TEST_PACKAGENAME)) {
            CoConfig.zt = false;
        } else {
            CoConfig.zt = true;
        }

        about_score = zt; // 评分
        about_provision = zt; // 条款
        setting_commu = zt; // 社区
        setting_function = zt; // 功能介绍
        setting_share = zt; // wexin

//        invite_all = zt;
        invite_qq = zt; // qq
        invite_wexin = zt; // wexin
        login_qq = zt; // qq登录
        login_help = zt; // 客服电话
        private_server = zt; // 私有云
        common_share = zt; // 分享
        use_law = zt; // 使用条款

        String tmpGuide = WeqiaApplication.ctx.getResources().getString(R.string.co_n_want_guide);
        if (StrUtil.notEmptyOrNull(tmpGuide) && tmpGuide.equalsIgnoreCase("1")) {
            want_guide = false;
        } else {
            want_guide = true;
        }

        String tmpFriend = WeqiaApplication.ctx.getResources().getString(R.string.co_n_want_friend);
        if (StrUtil.notEmptyOrNull(tmpFriend) && tmpFriend.equalsIgnoreCase("1")) {
            want_friend = false;
        } else {
            want_friend = true;
        }

        String tmpWeqiaGood = WeqiaApplication.ctx.getResources().getString(R.string.co_n_want_weqia_good);
        if (StrUtil.notEmptyOrNull(tmpWeqiaGood) && tmpWeqiaGood.equalsIgnoreCase("1")) {
            want_weqiagood = false;
        } else {
            want_weqiagood = true;
        }

        String tmpInvite = WeqiaApplication.ctx.getResources().getString(R.string.co_n_want_invite);
        if (StrUtil.notEmptyOrNull(tmpInvite) && tmpInvite.equalsIgnoreCase("1")) {
            want_invite = false;
        } else {
            want_invite = true;
        }

        String tmpRegister = WeqiaApplication.ctx.getResources().getString(R.string.co_n_want_register);
        if (StrUtil.notEmptyOrNull(tmpRegister) && tmpRegister.equalsIgnoreCase("1")) {
            want_registr = false;
        } else {
            want_registr = true;
        }

        String tmpCallVoice = WeqiaApplication.ctx.getResources().getString(R.string.co_n_want_call_voice);
        /*
         * 1-不要显示语音通验证码，2-要显示语音验证码
         */
        if (StrUtil.notEmptyOrNull(tmpCallVoice) && tmpCallVoice.equalsIgnoreCase("1")) {
            want_callVocie = false;
        } else {
            want_registr = true;
        }
    }

    public static String qrPjId() {
        String tmpPjId = CoConfig.qrPjId;
        return tmpPjId;
    }

    public static void setQrPjId(String pjId) {
        CoConfig.qrPjId = pjId;
    }

    public static String getQrPjId() {
        return qrPjId;
    }
}
