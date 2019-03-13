package com.weqia.wq.data.global;

import com.weqia.utils.view.jazzyviewpager.JazzyViewPager.TransitionEffect;

public class GlobalConstants {

    public static final int PAGE_CACHE_COUNT = 100;
    public static final String KEY_MC_TYPE = "key_mc_type";
    public static final String KEY_WS_TYPE = "key_ws_type";
    public static final String KEY_MC_READED = "key_mc_readed";
    public static final int REQUESTCODE_GET_DEPAETMENT = 120;
    public static final String KEY_IMAGE_SELECT_SIZE = "select_size";
    public static final String KEY_BASE_INT = "KEY_BASE_INT";
    public static final String KEY_SELECT_TYPE = "select_type";
    public static final String DB_PRE_TALK = "TALK_";
    public static final String KEY_HISTORY = "KEY_HISTORY";
    public static final String DB_PRE_DISCUSS = "DISCUSS_";
    public static final String DB_PRE_TASK = "TASK_";
    public static final String DB_PRE_PROJECT = "PROJECT_";
    public static final String DB_PRE_MODE = "MODE_";
    public static final int REQUESTCODE_GET_PIC = 311;
    public static final int REQUESTCODE_GET_FILE = 313;
    public static final int REQUESTCODE_GET_FILE_URL = 314;
    public static final int REQUESTCODE_READ = 315;
    public static final int REQUESTCODE_ADD_LINK = 316;
    public static final int REQ_GET_RED_PACKET = 317;
    public static final int REQUESTCODE_ADMIN = 106;
    //    public static final int REQUESTCODE_PART_IN = 107;
    public static final long MESSAGE_SHOW_INTERVAL = 5 * 60 * 1000;// 聊天时间显示间隔
    public static final Integer DELAY_TIME_ES = 2000;
    //客户联系人
    public static final String KEY_BASE_BOOLEAN = "KEY_BASE_BOOLEAN";
    public static final int REQUESTCODE_AT_PEOPLE = 3;
    public static String TALK_DB_NAME = "weiqiadb2.db";
    public static final String KEY_BASE_DATA = "KEY_BASE_DATA";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_REGUSER = "regUser";
    public static final int UPLOAD_MAX_FILE_SIZE = 300 * 1024 * 1024;
    public static final String UPLOAD_MAX_FILE_SIZE_STR = "300MB!";

    public static final String KEY_CONTACT_NO_TALK = "contact_no_talk";
    public static final String VIDEO_FORMAT = ".mp4";
    public static final int VIDEO_WIDTH = 320;
    public static final int VIDEO_HEIGHT = 240;

    public static final String KEY_CAN_SELECT = "canSelct";


    public static final String VOICE_FORMAT = "amr";
    public static final String TIME_CHOOSE_LONG = "time_choose_long";

    public static final String SHARE_TITLE = "Hi，给你一条分享！";

    // share
    public static final String WQ_D_URL = "http://ccbim.pinming.cn/";
    public static final float PART_4 = 4.5f;
    public static final String LIST_PICS = "list_pics";
    public static final String CHANGE_HEAD_PICS = "change_head_pics";

    public static final String KEY_ATTACH = "key_attach";

    public static final int G_NOTIFICATION_ID = 99999;

    //搜索字段
    public static final String KEY_SEARCH_TYPE = "search_type";
    public static final String KEY_SEARCH_KEY = "search_key";


    public static final int FACE_NUM_PAGE = 6;// 总共有多少页

    public static int FACE_NUM = 20;// 每页20个表情,还有最后一个删除button

    public static String FORM_DIVIDE = "@@|@@";

    public static TransitionEffect mEffects[] = {TransitionEffect.Standard,
            TransitionEffect.Tablet, TransitionEffect.CubeIn, TransitionEffect.CubeOut,
            TransitionEffect.FlipVertical, TransitionEffect.FlipHorizontal, TransitionEffect.Stack,
            TransitionEffect.ZoomIn, TransitionEffect.ZoomOut, TransitionEffect.RotateUp,
            TransitionEffect.RotateDown, TransitionEffect.Accordion,};// 表情翻页效果

    public static final int PAGE_COUNT = 20;        //为了支持Pad每页加载20条
    /**
     * 推送通知服务名称
     */
    public static final String PUSH_NOTIFICATION_SERVICE_NAME =
            "cn.pinming.bim360.gobal.NotificationService";

    public static final String DOWNLOAD_COUNT_SERVICE_NAME = "com.weqia.wq.DownloadCountService";
    public static final String UPLOAD_COUNT_SERVICE_NAME = "com.weqia.wq.UploadCountService";

    public static final String PUSH_CONTENT_KEY = "pushdata";

    // title key
    public static final String KEY_TOP_BANNER_TITLE = "title";
    public static final String KEY_TOP_BANNER_INT = "key_top_banner_int";
    public static final String KEY_PARAM_DATA = "basedata";
    public static final String KEY_BUNDLE_DATA = "base_bundle_data";

    public static final String KEY_LOC_DATA = "key_loc_data";
    public static final String KEY_ATTACH_OP = "key_attach_op";
    public static final String KEY_UPLOAD_FILE = "uploadFile";

    //modify
    public static final String KEY_MODIFY_NAME = "name";
    public static final String KEY_MODIFY_LENGTH = "length";
    public static final String KEY_MODIFY_TYPE = "inputType";

    // download
    public static final String KEY_DOWN_PERCENT = "download_percent";
    public static final String KEY_DOWN_COMPLETE = "download_complete";
    public static final String KEY_DOWN_ID = "download_id";
    public static final String KEY_DOWN_ERR_MSG = "download_err_msg";

    //是否需要打开模型（模型下载不需要打开模型，默认为true）
    public static final String KEY_NO_OPEN_MODE = "KEY_NO_OPEN_MODE";
    public static final String KEY_DOWN_NODEID = "download_nodeid";
    public static final String KEY_DOWN_FILE = "download_file";
    // upload progress
    public static final String KEY_UPLOAD_DATA = "upload_data";
    // video
    public static final String KEY_VIDEO_PATH = "video_path";
    public static final String KEY_VIDEO_TIME = "video_time";
    public static final String KEY_VIDEO_URI = "video_uri";

    public static final String KEY_COID = "param_coid";

    public static final int REQUESTCODE_GET_LOC = 119;
    public static final Integer DELAY_TIME_SECOND = 1000;

    public static final Integer IMAGE_MAX = 9;

    public static final int LARGE_PAGE_SIZE = 1000;


    public static boolean debug_upfile = false;

    public static final Integer DEFAULT_SINGLE_PIC_WIDTH = 150;

    public static final int DEFAULT_COMPRESS_QUALITY = 30;

    public static final Integer MAX_FILE_SIZE = 300;

    public static final String SPIT_SENDMEDIA = "=";

    public static final int FACE_DEFAULT_FONT_SIZE = 20;

    public static final String BUCKET_SPIT = "#__#";
    //模型文件下载---用于拼接文件名称和version
    public static final String FILE_BACK_SPIT = "+__+";
    public static final String NODE_SPIT = "#*_#";
    public static final String SELECT_POS_PREW = "#-0*7&_#";
    public static final String SELECT_TYPE = "SELECT_TYPE";
    public static final String SELECT_TASK = "SELECT_TASK";
    public static final String SELECT_DISCUSS = "SELECT_DISCUSS";
    public static final String KEY_PARAM_ENTITY = "param_discuss";
    public static final int ID_EDIT = 1;
    public static final int ID_COMPLETE = 2;
    public static final int ID_OVER = 8;
    public static final int ID_DELETE = 3;
    public static final int ID_DELETE_PART_IN = 25;


    //搜索字段
    public static final String KEY_SEARCH_MID = "search_mid";
    public static final String KEY_SEL_PROGRESS = "sel_progress";

    public static final Integer ICON_VIDEO_WIDTH = 115;

    public static final String SHARE_URL_TAG = "/share/shareInfo.htm?id=";
    public static String LAW_URL = "http://zhuang.pinming.cn/u.htm";

    //权限申请Code
    //读取手机状态
    public static final int READ_PHONE_STATE_CODE = 1111;
    //写入外部存储卡
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 1112;
    //获取定位
    public static final int ACCESS_COARSE_LOCATION_CODE = 1113;
    //录音权限
    public static final int RECORD_AUDIO_CODE = 1114;

    public static final String REFRESHCURRENTPROJEC = "REFRESHCURRENTPROJEC";

    public static final int CLOUD_LINE_RADIUS = 10;      //云线半圆的半径
    public static final int CLOUD_LINE_DIA = 20;           //云线半圆的直径

}
