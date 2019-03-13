package com.weqia.wq.component.file;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spinytech.macore.MaApplication;
import com.umeng.analytics.MobclickAgent;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.data.LocalNetPath;
import com.weqia.utils.http.okserver.download.DownloadInfo;
import com.weqia.utils.http.okserver.download.DownloadManager;
import com.weqia.wq.R;
import com.weqia.wq.component.AttachService;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.file.assist.WpsModel;
import com.weqia.wq.component.receiver.AttachMsgReceiver;
import com.weqia.wq.component.utils.AttachUtils;
import com.weqia.wq.component.utils.FileMiniUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.view.title_pop.TitleItem;
import com.weqia.wq.component.view.title_pop.TitlePopup;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.TransData;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.global.FileUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.weqia.wq.component.AttachService.bStop;

public class FileScanActivity extends SharedDetailTitleActivity {
    private TextView tvName;
    private TextView tvSize;
    private ImageView ivIcon;
    private ImageView ivPause;
    private ProgressBar pbProgress;
    private LinearLayout llProgress;
    private Button btnDownload;
    private AttachmentData data;
    private List<AttachmentData> attachmentDataList = new ArrayList<>();
    private boolean bDownloaded = false;
    private AttachmentData localData;
    private FileScanActivity ctx;
    private TitlePopup titlePopup = null;
    private boolean isPause = false;
    boolean canDown = false;
    private static FileScanActivity instance;
    private boolean isModeDown = false;
    private int count = 0;
    private int fileNums = 0;
    List<String> urls = new ArrayList<>();

    public static FileScanActivity getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filescan);
        instance = this;
        ctx = this;
        initView();
        initData();
    }

    private void initView() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvSize = (TextView) findViewById(R.id.tvSize);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        llProgress = (LinearLayout) findViewById(R.id.llProgress);
        ivPause = (ImageView) findViewById(R.id.ivPause);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            data = (AttachmentData) extras.getSerializable("attachmentData");
            if (getIntent().hasExtra("attachmentDataList")) {
                attachmentDataList = (List<AttachmentData>) extras.getSerializable("attachmentDataList");
                if (StrUtil.listNotNull(attachmentDataList)) {
                    isModeDown = true;
                }
            }
            if (getIntent().hasExtra("fileNums")) {
                fileNums = extras.getInt("fileNums");
            }
            canDown = extras.getBoolean("canDown");
        }

        L.e("--------candown == " + canDown);
        if (!canDown && data != null && StrUtil.notEmptyOrNull(data.getUrl()) && !GlobalUtil.isCombination(data.getUrl())) {
            sharedTitleView.initTopBanner("文件预览", R.drawable.selector_btn_details);
        } else {
            sharedTitleView.initTopBanner("文件预览");
        }

        if (data != null) {
            String name = data.getName();
            tvName.setText(name);
            String fileSize = data.getFileSize();
            if (StrUtil.isEmptyOrNull(fileSize)) {
                tvSize.setText(FileUtil.formetFileSize(Double.parseDouble("0") * 1024D));
            } else {
                tvSize.setText(FileUtil.formetFileSize(Double.parseDouble(fileSize) * 1024D));
            }
            ivIcon.setImageResource(FileMiniUtil.fileBigRId(name));
        }
        localData = getDbUtil().findById(data.getUrl(), AttachmentData.class);
        if (localData != null) {
            localData.setType(data.getType());
        }
        ViewUtils.hideViews(FileScanActivity.this, R.id.tvInfo);
    }

    private void initData() {
        DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(data.getUrl());
        if (downloadInfo != null) {
            if (StrUtil.isEmptyOrNull(downloadInfo.getTargetPath()) || !new File(downloadInfo.getTargetPath()).exists()) {
                DownloadManager.getInstance().removeTask(data.getUrl());
                downloadInfo = null;
                L.e("这个文件已经被删除，删除下载的信息");
            }
        }
        if (downloadInfo == null) {
            if (!canDown) {
                ViewUtils.showViews(FileScanActivity.this, R.id.tvInfo, R.id.tvInfo1);
                ViewUtils.hideViews(FileScanActivity.this, R.id.tvInfo2, R.id.btnDownload);
                return;
            }
            if (!isModeDown) {
                Intent intent = new Intent(this, AttachService.class);
                intent.putExtra(GlobalConstants.KEY_ATTACH_OP, data);
                startService(intent);
            } else {
                Intent intent = new Intent(this, AttachService.class);
                intent.putExtra(GlobalConstants.KEY_ATTACH_OP, (Serializable) attachmentDataList);
                startService(intent);
            }
            llProgress.setVisibility(View.VISIBLE);
        } else {
            if (!canDown) {
                ViewUtils.showViews(FileScanActivity.this, R.id.tvInfo, R.id.tvInfo1);
                ViewUtils.hideViews(FileScanActivity.this, R.id.tvInfo2, R.id.btnDownload);
                return;
            }
            if (downloadInfo.getState() == DownloadManager.FINISH) {
                String tmpPath = downloadInfo.getTargetPath();
                data = localData;
                bDownloaded = true;
                llProgress.setVisibility(View.GONE);
                if (FileMiniUtil.supportPreView(data.getName()) || FileMiniUtil.canOpenFile(data.getName())) {
                    ViewUtils.hideViews(FileScanActivity.this, R.id.tvInfo, R.id.tvInfo2);
                } else {
                    ViewUtils.showViews(FileScanActivity.this, R.id.tvInfo, R.id.tvInfo2);
                }

                if (FileMiniUtil.canOpenFile(tmpPath) || FileMiniUtil.isZip(tmpPath)) {
                    //data.getVideoPrew()为模型的视口数据
                    AttachUtils.openFile(ctx, GlobalUtil.wrapNodePath(data.getNodeId(), data.getLoaclUrl()),
                            data.getVideoPrew(), data.getName(), data.getsType(),data.getPjId());
                    return;
                }

                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("用其他应用打开");

            } else if (downloadInfo.getState() == DownloadManager.PAUSE) {
                isPause = true;
                llProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("继续下载");
                //} else if (!canDown && preClassName.equals("FileActivity")) {
            } else {
                if (!isModeDown) {
                    Intent intent = new Intent(this, AttachService.class);
                    intent.putExtra(GlobalConstants.KEY_ATTACH_OP, data);
                    startService(intent);
                } else {
                    Intent intent = new Intent(this, AttachService.class);
                    intent.putExtra(GlobalConstants.KEY_ATTACH_OP, (Serializable) attachmentDataList);
                    startService(intent);
                }
                llProgress.setVisibility(View.VISIBLE);
            }
        }
//        if (downloadInfo == null || downloadInfo.getState() == DownloadManager.FINISH || downloadInfo.getState() == DownloadManager.PAUSE) {
//             else
//        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bDownloaded) {
                    if (data != null && StrUtil.notEmptyOrNull(data.getLoaclUrl()) && FileMiniUtil.supportPreView(data.getLoaclUrl()) && isAvilible(ctx, "cn.wps.moffice_eng")) {
                        openFile(data.getLoaclUrl());
                    } else {
                        //data.getVideoPrew()为模型的视口数据
//                        AttachUtils.openFile(ctx,
//                                GlobalUtil.wrapNodePath(data.getNodeId(), data.getLoaclUrl()),
//                                data.getVideoPrew(),
//                                data.getName(),
//                                data.getsType());
                        Intent openFileIntent = FileMiniUtil.openFile(data.getLoaclUrl());
                        try {
                            ctx.startActivity(openFileIntent);
                        } catch (Exception e) {// 无对应打开方式,列出所有打开方式
                            Intent openUnKnowFileIntent = FileMiniUtil.getUnKnowIntent(data.getLoaclUrl());
                            ctx.startActivity(openUnKnowFileIntent);
                            e.printStackTrace();
                        }
                    }
                } else {
                    isPause = false;
                    btnDownload.setVisibility(View.GONE);
                    llProgress.setVisibility(View.VISIBLE);
                    if (!isModeDown) {
                        Intent intent = new Intent(ctx, AttachService.class);
                        intent.putExtra(GlobalConstants.KEY_ATTACH_OP, data);
                        startService(intent);
                    } else {
                        Intent intent = new Intent(ctx, AttachService.class);
                        intent.putExtra(GlobalConstants.KEY_ATTACH_OP, (Serializable) attachmentDataList);
                        startService(intent);
                    }
                    llProgress.setVisibility(View.VISIBLE);

                }
            }
        });
        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.getInstance().pauseTask(data.getUrl());
                isPause = true;
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("继续下载");
                llProgress.setVisibility(View.GONE);
            }
        });
    }

    private boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager(); //获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名//从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    public boolean openFile(String path) {
        String packageName = ctx.getPackageName();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.READ_ONLY); // 打开模式
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, packageName); // 第三方应用的包名，用于对改应用合法性的验证
        //        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录
        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);
        File file = new File(path);
        if (file == null || !file.exists()) {
            System.out.println("文件为空或者不存在");
            AttachUtils.openFile(ctx, GlobalUtil.wrapNodePath(data.getNodeId(), data.getLoaclUrl()));
            return false;
        }
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            L.e("打开wps异常" + e.toString());
            e.printStackTrace();
            AttachUtils.openFile(ctx, GlobalUtil.wrapNodePath(data.getNodeId(), data.getLoaclUrl()));
            return false;
        }
        return true;
    }

    //    private boolean isPreScan(String className) {
    //        return className.equals("FileActivity");
    //    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == sharedTitleView.getButtonLeft()) {
            backDo();
        } else if (v == sharedTitleView.getButtonRight()) {
            titleOp(v);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backDo();
    }

    private void backDo() {
        this.finish();
        if (data != null && StrUtil.notEmptyOrNull(data.getUrl())) {
            L.e("暂停" + data.getUrl());
            DownloadManager.getInstance().pauseTask(data.getUrl());
        }
        if (count < attachmentDataList.size()) {
            bStop = true;
            Iterator<AttachmentData> iterator = attachmentDataList.iterator();
            while(iterator.hasNext()){
                AttachmentData data = iterator.next();
                for (String url : urls) {
                    if (url.equals(data.getUrl())) {
                        iterator.remove();
                    }
                }
            }
            for (AttachmentData data : attachmentDataList) {
                DownloadManager.getInstance().removeTask(data.getUrl());
            }
            bStop = false;
        }
    }

    private void titleOp(View view) {
        titlePopup =
                new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.addAction(new TitleItem(this, "转发", null));
        titlePopup.addAction(new TitleItem(this, "发送", null));
        titlePopup.addAction(new TitleItem(this, "重新下载", null));
        titlePopup.addAction(new TitleItem(this, "查看全部文件", null));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(TitleItem item, int position) {
                switch (position) {
                    case 0:
                        //转发
                        TransData transData = new TransData();
                        transData.setOuter(false);
                        transData.setInsideData(data);
                        transData.setContentType(EnumData.MsgTypeEnum.FILE.value());
//                        WeqiaApplication.transData = transData;
//                        Intent intent = new Intent(ctx, OpenFileActivity.class);
//                        ctx.startActivity(intent);
                        break;
                    case 1:
                        if (bDownloaded && data != null && StrUtil.notEmptyOrNull(data.getLoaclUrl())) {
                            File locFile = new File(data.getLoaclUrl());
                            if (locFile != null && locFile.exists()) {
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(locFile));
                                shareIntent.setType("*/*");
                                startActivity(Intent.createChooser(shareIntent, "发送"));
                            } else {
                                L.toastShort("文件未下载~");
                            }
                        } else {
                            L.toastShort("文件未下载~");
                        }


                        break;
                    case 2:
                        //删除本地文件
//                        if (localData != null && StrUtil.notEmptyOrNull(localData.getLoaclUrl())) {
//                            DownloadManager.getInstance().removeTask(data.getUrl());
//                            isPause = false;
//                            getDbUtil().deleteById(AttachmentData.class, localData);
//                            intent = new Intent(ctx, AttachService.class);
//                            intent.putExtra(GlobalConstants.KEY_ATTACH_OP, data);
//                            startService(intent);
//                            llProgress.setVisibility(View.VISIBLE);
//                            btnDownload.setVisibility(View.GONE);
//
//                            File file = new File(localData.getLoaclUrl());
//                            if (file != null && file.exists()) {
//                                file.delete();
//                            }
//                        }
                        break;
                    case 3:
//                        intent = new Intent(ctx, FileAllActivity.class);
//                        startActivity(intent);
                        break;
                }
            }
        });
        titlePopup.show(view);

    }

    private AttachMsgReceiver downReceive = new AttachMsgReceiver() {

        @Override
        public void downloadCountReceived(Intent intent) {
            if (intent != null) {
                if (!isModeDown) {
                    String urlStr = intent.getStringExtra(GlobalConstants.KEY_DOWN_ID);
                    if (!urlStr.equalsIgnoreCase(data.getUrl())) {
                        return;
                    }
                    String downPercent = intent.getStringExtra(GlobalConstants.KEY_DOWN_PERCENT);
                    Boolean bComplete =
                            intent.getBooleanExtra(GlobalConstants.KEY_DOWN_COMPLETE, false);
                    if (StrUtil.isEmptyOrNull(urlStr) || StrUtil.isEmptyOrNull(downPercent)
                            || bComplete == null) {
                        return;
                    }
                    if (bComplete == false && downPercent.equalsIgnoreCase("100%")) {
                        String errMsg = intent.getStringExtra(GlobalConstants.KEY_DOWN_ERR_MSG);
                        if (StrUtil.isEmptyOrNull(errMsg))
                            errMsg = "当前网络不稳定，请重试!";
                        ViewUtils.hideView(pbProgress);
                        ViewUtils.showView(tvSize);
                        tvSize.setText(errMsg);
                        MobclickAgent.reportError(FileScanActivity.this, errMsg);
                        return;
                    }
                    downPercent = downPercent.replace("%", "");
                    Double progress = Double.parseDouble(downPercent);
                    int showPb = Integer.parseInt(new DecimalFormat("##").format(progress));
                    pbProgress.setProgress(showPb);
                    Double curSize = 0d;
                    boolean sizeNotShow = false;
                    if (data != null && StrUtil.notEmptyOrNull(data.getFileSize())) {
                        curSize = Double.parseDouble(data.getFileSize()) * showPb / 100;
                    } else {
                        sizeNotShow = true;
                    }
                    String fileSize = "";
                    if (StrUtil.notEmptyOrNull(data.getFileSize()))
                        fileSize = FileUtil.formetFileSize(Double.parseDouble(data.getFileSize()) * 1024D);
                    else
                        L.e("-------------------------文件大小为空---------------");
                    String currentSize = FileUtil.formetFileSize(curSize * 1024D);
                    ViewUtils.showViews(pbProgress, tvSize);
                    tvSize.setText("正在下载....(" + currentSize + "/" + fileSize + ")");
                    if (sizeNotShow) {
                        tvSize.setVisibility(View.GONE);
                    } else {
                        tvSize.setVisibility(View.VISIBLE);
                    }
                    if (bComplete) {
                        bDownloaded = true;
                        File file = (File) intent.getSerializableExtra(GlobalConstants.KEY_DOWN_FILE);
                        String fileName = file.getName();
                        String end = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
                        String nodeId = intent.getStringExtra(GlobalConstants.KEY_DOWN_NODEID);
                        //模型下载不需要打开模型预览
                        boolean noOpenMode = intent.getBooleanExtra(GlobalConstants.KEY_NO_OPEN_MODE, false);
                        String tmpPath = file.getPath();
                        if (FileMiniUtil.canOpenFile(tmpPath) || FileMiniUtil.isZip(tmpPath)) {
                            //EnumDataTwo.RefreshEnum.NETFILE_DOWN_STATUS_REFRESH.getValue()
                            if (!noOpenMode) {
                                EventBus.getDefault().post(new RefreshEvent(51));
                                ////data.getVideoPrew()为模型的视口数据
                                AttachUtils.openFile(ctx,
                                        GlobalUtil.wrapNodePath(nodeId, tmpPath),
                                        data.getVideoPrew(),
                                        data.getName(),
                                        data.getsType(),
                                        data.isbCanAction(), data.getPjId());
                            } else {
                                L.toastShort("下载已完成！");
                                ctx.finish();
                            }
                            return;
                        } else if (FileMiniUtil.isImage(end) || FileMiniUtil.isVedio(end)) {
                            //图片和视屏下载直接返回
                            L.toastShort("下载已完成！");
                            ctx.finish();
                            return;
                        }
                        data = getDbUtil().findById(data.getUrl(), AttachmentData.class);
                        llProgress.setVisibility(View.GONE);
                        btnDownload.setVisibility(View.VISIBLE);
                        btnDownload.setText("用其他应用打开");
//                    if (FileMiniUtil.supportPreView(data.getName())) {
//                        ViewUtils.hideViews(FileScanActivity.this, R.id.tvInfo);
//                    } else {
//                        ViewUtils.showViews(FileScanActivity.this, R.id.tvInfo);
//                    }
                    }
                } else {

                    Boolean bComplete = intent.getBooleanExtra(GlobalConstants.KEY_DOWN_COMPLETE, false);
                    String errMsg = intent.getStringExtra(GlobalConstants.KEY_DOWN_ERR_MSG);
                    MobclickAgent.reportError(FileScanActivity.this, errMsg);
                    if (bComplete) {
                        File file = (File) intent.getSerializableExtra(GlobalConstants.KEY_DOWN_FILE);
                        String path = intent.getStringExtra(GlobalConstants.KEY_DOWN_ID);
                        for (int i = 0; i < urls.size(); i++) {
                            if (path.equals(urls.get(i))) {
                                return;
                            }
                        }
                        urls.add(path);
                        count++;
                        Log.e("tag", "count: " + count + "" + "fileNum:" + attachmentDataList.size());
                        pbProgress.setVisibility(View.VISIBLE);
                        tvSize.setVisibility(View.VISIBLE);
                        int progress = (fileNums - attachmentDataList.size() + count) * 100 / fileNums;
                        if (progress >= 100) {
                            pbProgress.setProgress(100);
                            tvSize.setText(100 + "%");
                        } else {
                            pbProgress.setProgress(progress);
                            tvSize.setText(progress + "%");
                        }
                        if (count == attachmentDataList.size()) {
                            Log.e("tag", "count == fileNum");
                            pbProgress.setProgress(100);
                            tvSize.setText("100%");
                            pbProgress.setVisibility(View.GONE);
                            tvSize.setVisibility(View.GONE);

                            data.setLoaclUrl(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf("/")));
                            MaApplication.getMaApplication().onDownloadFileOp(data.toString(), 1);
                            L.toastShort("下载已完成！");
                            ctx.finish();
                        }
                    }

                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (downReceive != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
            filter.setPriority(Integer.MAX_VALUE);
            registerReceiver(downReceive, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (downReceive != null) {
            unregisterReceiver(downReceive);
        }
    }

}
