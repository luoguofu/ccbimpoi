package com.example.ccbim.ccbimpoi.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.CodeData;
import com.example.ccbim.ccbimpoi.data.ExcelData;
import com.example.ccbim.ccbimpoi.data.NavigHandler;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.example.ccbim.ccbimpoi.util.BaseUtil;
import com.example.ccbim.ccbimpoi.util.SaveToExcelUtil;
import com.example.ccbim.ccbimpoi.widget.HomeListAdapter;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.ZipUtils;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.utils.http.HttpUtil;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.wq.component.AttachService;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.receiver.AttachMsgReceiver;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

import static com.example.ccbim.ccbimpoi.MainNewActivity.getExcelHsfDir;
import static com.example.ccbim.ccbimpoi.MainNewActivity.getPoiExcelDir;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mFrameLayout;
    private TextView mTextBefore;
    private TextView mTextLast;
    private RecyclerView mCommonRecyclerview;
    private HomeListAdapter homeListAdapter;
    private Button mTextWork;
    private Button mTextWorkDone;
    private ImageButton mImgAddForm;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ProjectCheckData> listData = new ArrayList<>();
    private boolean isComplete = false;
    private boolean isRectify = false;              //是否是整改单
    private ArrayList<ProjectCheckData> selectProjectData = new ArrayList<>();
    private Dialog dialog = null;
    private LinearLayout mCompleteLl;
    private TextView mCheckExcelTv, mRectifyExcelTv;
    private int level = 0;
    private int parentId = -1;
    private NavigHandler navHandler;
    private LinearLayout headerView;
    public static int REQUESTCODE_SEARCH = 1001;
    private String companyName;
    private String projectName;
    private String realUrl = "https://ccbimpoi.oss-cn-hangzhou.aliyuncs.com/ccbimpoiExcel.zip";
    private ArrayList<String> excelList = new ArrayList<>();
//    private ArrayList

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        EventBus.getDefault().register(this);
        initView();
        initResourse();
        SharedPreferences sharedPreferences= getSharedPreferences("setting", Context.MODE_PRIVATE);
        companyName = sharedPreferences.getString("companyName", "");
        projectName = sharedPreferences.getString("projectName", "");
//        downExcelFile(realUrl, getExcelHsfDir() + File.separator + "ccbimpoiExcel.zip");
        downloadFile3("https://ccbimpoi.oss-cn-hangzhou.aliyuncs.com/excelCode.txt");
        ProjectCheckData root = new ProjectCheckData();
        root.setExcelFullName("根目录");
        root.setId(-1);
        getNavHandler().addNavData(root);
    }

    private void initResourse() {
        if (downReceive != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
            filter.setPriority(Integer.MAX_VALUE);
            registerReceiver(downReceive, filter);
        }
    }

    @SuppressLint("NewApi")
    private void initView() {
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        headerView = (LinearLayout) findViewById(R.id.ll_headerView);
        mCompleteLl = (LinearLayout) findViewById(R.id.ll_complete);
        mCheckExcelTv = (TextView) findViewById(R.id.tv_check_excel);
        mRectifyExcelTv = (TextView) findViewById(R.id.tv_rectify_excel);
        mCheckExcelTv.setOnClickListener(this);
        mRectifyExcelTv.setOnClickListener(this);
        mTextBefore = (TextView) findViewById(R.id.text_before);
        mTextBefore.setOnClickListener(this);
        mTextLast = (TextView) findViewById(R.id.text_last);
        mTextLast.setOnClickListener(this);
        mCommonRecyclerview = (RecyclerView) findViewById(R.id.common_recyclerview);
        mTextWork = (Button) findViewById(R.id.text_work);
        mTextWork.setOnClickListener(this);
        mTextWorkDone = (Button) findViewById(R.id.text_work_done);
        mTextWorkDone.setOnClickListener(this);
        mFrameLayout.setOnClickListener(this);
        mCommonRecyclerview.setOnClickListener(this);
        mImgAddForm = (ImageButton) findViewById(R.id.img_add_form);
        mImgAddForm.setOnClickListener(this);
//        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
//        listData = (ArrayList<ProjectCheckData>) dbUtil.findAll(ProjectCheckData.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mCommonRecyclerview.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
//        mCommonRecyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        homeListAdapter = new HomeListAdapter(this, listData);
        mCommonRecyclerview.setAdapter(homeListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 启动刷新的控件
                initData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        btnStatus();
        initData();
    }

    @SuppressLint("NewApi")
    private void btnStatus() {
        if (isComplete) {
            mTextWork.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
            mTextWorkDone.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
            mCompleteLl.setVisibility(View.VISIBLE);
            if (isRectify) {
                mRectifyExcelTv.setTextColor(this.getResources().getColor(R.color.colorblue));
                mCheckExcelTv.setTextColor(this.getResources().getColor(R.color.black_font));
            } else {
                mRectifyExcelTv.setTextColor(this.getResources().getColor(R.color.black_font));
                mCheckExcelTv.setTextColor(this.getResources().getColor(R.color.colorblue));
            }
        } else {
            mTextWorkDone.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
            mTextWork.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
            mCompleteLl.setVisibility(View.GONE);
        }
    }

    public void initData() {
        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        ArrayList<ProjectCheckData> list = new ArrayList<>();
        if (!isComplete) {
            list = (ArrayList<ProjectCheckData>) dbUtil.findAllByWhereN(ProjectCheckData.class, "excelType = 1 and completeStatus = 0 and parentId = " + parentId, "id");
        } else {
            if (isRectify) {
                list = (ArrayList<ProjectCheckData>) dbUtil.findAllByWhereN(ProjectCheckData.class, "excelType = 2" , "id");
            } else {
                list = (ArrayList<ProjectCheckData>) dbUtil.findAllByWhereN(ProjectCheckData.class, "excelType = 1 and (completeStatus = 1 or fileType = 2) and parentId = " + parentId, "id");
//
            }

        }
        if (isRectify) {
            ViewUtils.hideView(headerView);
        } else {
            ViewUtils.showView(headerView);
        }
        listData.clear();
        selectProjectData.clear();
        listData.addAll(list);

        homeListAdapter.update(listData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.action_search:
                startActivityForResult(new Intent(this, SearchExcelActivity.class), REQUESTCODE_SEARCH);
                break;

            case R.id.action_export:
                List<String> pathList = new ArrayList<>();
                pathList.clear();
                for (ProjectCheckData projectCheckData : selectProjectData) {
/*                    if (projectCheckData.getExcelType() == 1) {
                        ArrayList<CellData> tabBody = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabBodyStr(), CellData.class);
                        projectCheckData.setTabBody(tabBody);
                        ArrayList<CellData> tabHead = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabHeadStr(), CellData.class);
                        projectCheckData.setTabHead(tabHead);
                        ArrayList<CellData> tabFoot = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabFootStr(), CellData.class);
                        projectCheckData.setTabFoot(tabFoot);
                        projectCheckData.setExportDate(System.currentTimeMillis());
                        projectCheckData.getTabHead().get(8).setCellName(TimeUtils.getDateYMDFromLong(projectCheckData.getExportDate()));
                        projectCheckData.setIsExport(1);
                        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                        dbUtil.save(projectCheckData, true);
                        SaveToExcelUtil.exportEccel(this, getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls", projectCheckData);
                        pathList.add(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls");
                    } else if (projectCheckData.getExcelType() == 2){
                        ArrayList<CellData> tabHead = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabHeadStr(), CellData.class);
                        projectCheckData.setTabHead(tabHead);
                        ArrayList<CellData> tabFoot = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabFootStr(), CellData.class);
                        projectCheckData.setTabFoot(tabFoot);
                        projectCheckData.setExportDate(System.currentTimeMillis());
                        projectCheckData.getTabHead().get(8).setCellName(TimeUtils.getDateYMDFromLong(projectCheckData.getExportDate()));
                        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                        dbUtil.save(projectCheckData, true);
                        SaveToExcelUtil.exportRectifyEccel(this, getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls", projectCheckData);
                        pathList.add(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls");
                    }*/
                    exportExcel(projectCheckData);
                    pathList.add(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls");
                }
                if (StrUtil.listIsNull(pathList)) {
                    break;
                } else {
                    if (pathList.size() == 1) {
//                        Intent intent = getExcelFileIntent(new File(pathList.get(0)));
//                        startActivity(intent);
                        getExcelFileIntent(new File(pathList.get(0)), this);
                    } else {
                        String zipPath = getPoiExcelDir() + File.separator + "ccbimpoi" + ".zip";
                        File zipFile = new File(zipPath);
                        if (zipFile.exists()) {
                            zipFile.delete();
                        }
                        ZipUtils.zip(pathList, zipPath);
//                        Intent intent = getZipFileIntent(new File(zipPath));
//                        startActivity(intent);
                        getZipFileIntent(new File(zipPath), this);
                    }
                }
//                SaveToExcelUtil util = new SaveToExcelUtil(this, getExcelDir() + File.separator + "demo.xls");
                break;
        }
        return true;
    }

    public void exportExcel(ProjectCheckData projectCheckData) {
        if (projectCheckData.getExcelType() == 1) {
            ArrayList<CellData> tabBody = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabBodyStr(), CellData.class);
            projectCheckData.setTabBody(tabBody);
            ArrayList<CellData> tabHead = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabHeadStr(), CellData.class);
            projectCheckData.setTabHead(tabHead);
            ArrayList<CellData> tabFoot = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabFootStr(), CellData.class);
            projectCheckData.setTabFoot(tabFoot);
            projectCheckData.setExportDate(System.currentTimeMillis());
//        projectCheckData.getTabHead().add(new CellData(TimeUtils.getDateYMDFromLong(Long.parseLong(projectCheckData.getExportDate())), "5", "5", "6", "7"));
            projectCheckData.getTabHead().get(7).setCellName(TimeUtils.getDateYMDFromLong(projectCheckData.getExportDate()));
            projectCheckData.setIsExport(1);
            DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
            dbUtil.save(projectCheckData, true);
            SaveToExcelUtil.exportEccel(this, getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls", projectCheckData);
//            Intent intent = getExcelFileIntent(new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls"));
//            startActivity(intent);
//            File file = new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls");
//            getExcelFileIntent(file, this);
        } else if (projectCheckData.getExcelType() == 2){
            ArrayList<CellData> tabHead = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabHeadStr(), CellData.class);
            projectCheckData.setTabHead(tabHead);
            ArrayList<CellData> tabFoot = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabFootStr(), CellData.class);
            projectCheckData.setTabFoot(tabFoot);
            projectCheckData.setExportDate(System.currentTimeMillis());
//        projectCheckData.getTabHead().add(new CellData(TimeUtils.getDateYMDFromLong(Long.parseLong(projectCheckData.getExportDate())), "5", "5", "6", "7"));
            projectCheckData.getTabHead().get(8).setCellName(TimeUtils.getDateYMDFromLong(projectCheckData.getExportDate()));

            DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
            dbUtil.save(projectCheckData, true);
            SaveToExcelUtil.exportRectifyEccel(this, getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls", projectCheckData);
//            Intent intent = getExcelFileIntent(new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls"));
//            startActivity(intent);
//            File file = new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls");
//            getExcelFileIntent(file, this);
        }

//        File file = new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls");
//        if (file.exists()) {
//            UMImage image = new UMImage(this, file);
//            ShareUtil.getInstance(this).share(this, "通过滴水不漏给你分享一个文件", projectCheckData.getExcelFullName(), image, "");
//        }
    }

    //android获取一个用于打开Excel文件的intent
    public static void getExcelFileIntent(File file, Context context) {
/*        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory("android.intent.category.DEFAULT");
        Uri pdfUri;
        pdfUri = Uri.fromFile(file);
        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        intent.setType("application/vnd.ms-excel");

        try {
            context.startActivity(Intent.createChooser(intent, file.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //android获取一个用于打开zip文件的intent
    public static void getZipFileIntent(File file, Context context) {
/*        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/x-zip-compressed");
        return intent;*/

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory("android.intent.category.DEFAULT");
        Uri pdfUri;
        pdfUri = Uri.fromFile(file);
        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        intent.setType("application/x-zip-compressed");

        try {
            context.startActivity(Intent.createChooser(intent, file.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 添加成功 刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final RefreshEvent event) {
        String key = event.key;
        if ("projectCheckDataRefresh".equals(key)) {
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.text_before:
                break;
            case R.id.text_last:
                break;
            case R.id.text_work:
                isComplete = false;
                isRectify = false;
                btnStatus();
                initData();
                break;
            case R.id.text_work_done:
                isComplete = true;
                btnStatus();
                initData();
                break;
            case R.id.frame_layout:
                break;
            case R.id.common_recyclerview:
                break;
            case R.id.img_add_form:
//                startActivity(new Intent(this, FormListActivity.class));
                showAddFormDialog();
                break;
            case R.id.tv_check_excel:
                isRectify = false;
                btnStatus();
                initData();
                break;
            case R.id.tv_rectify_excel:
                isRectify = true;
                btnStatus();
                initData();
                break;
        }
    }

    private void showAddFormDialog() {
//        final String[] items = {"防水检查1", "防水检查2", "防水检查3", "防水检查4"};
//        ArrayList<String> excelList = new ArrayList<>();
        excelList.clear();
        excelList.add("新建文件夹");
//        for (ExcelEnum excelEnum : ExcelEnum.values()) {
//            excelList.add(excelEnum.getStrName());
//        }
        final String[] items = excelList.toArray(new String[excelList.size()]);
/*        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setTitle("");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAddAddressDialog(items[which]);
            }
        });
*//*        listDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAddAddressDialog(items[which]);
                dialog.dismiss();
            }
        });*//*
        listDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        listDialog.show();*/
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        List<ExcelData> list = dbUtil.findAll(ExcelData.class);
        if (StrUtil.listNotNull(list)) {
            for (ExcelData excelData : list) {
                excelList.add(excelData.getExcelName());
            }
        }

        dialog=DialogUtil.initListDialog(this, "表单选择", excelList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String name = (String) v.getTag();
                if ("新建文件夹".endsWith(name)) {
                    showAddDirDialog();
                } else {
                    showAddAddressDialog(name);
                }

            }
        });
        dialog.show();
    }

    private void showAddDirDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_creat_dir, null);
        final EditText dirEt = view.findViewById(R.id.et_dir_name);
        Dialog dialog = DialogUtil.initCommonDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (StrUtil.notEmptyOrNull(dirEt.getText().toString())) {
                    WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                    List<ProjectCheckData> rootList = dbUtil.findAllByWhere(ProjectCheckData.class, "excelFullName = '" + dirEt.getText().toString() + "' and parentId = -1 and fileType = 2");

                    if (StrUtil.listNotNull(rootList) && parentId == -1) {

                    } else {
                        ProjectCheckData data = new ProjectCheckData();
                        data.setExcelFullName(dirEt.getText().toString());
                        data.setLevel(level + 1);
                        data.setParentId(parentId);
                        data.setFileType(2);
                        dbUtil.save(data);
                    }
                    initData();
                } else {
                    L.toastShort("文件夹名称不能为空");
                }
                dialogInterface.dismiss();
            }
        }, view, "");
        dialog.show();
    }

    private void showAddAddressDialog(final String name) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_form_dialog_layout, null);
        customizeDialog.setTitle("");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edit_text =
                                (EditText) dialogView.findViewById(R.id.edit_text);
                        if (StrUtil.notEmptyOrNull(edit_text.getText().toString())) {
                            WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                            List<ExcelData> list = dbUtil.findAllByWhere(ExcelData.class, "excelName = '" + name + "'");
                            ExcelData excelData = null;
                            if (StrUtil.listNotNull(list)) {
                                excelData = list.get(0);
                            }
                            if (excelData == null) {
                                L.toastShort("创建失败");
                                return;
                            }
                            ProjectCheckData data = JSON.parseObject(excelData.getExcelJson(), ProjectCheckData.class);
                            data.setCheckPartName(edit_text.getText().toString());
                            data.setExcelFullName(data.getCheckPartName() + data.getExcelName());
                            if (StrUtil.notEmptyOrNull(getSharedPreferences("setting", Context.MODE_PRIVATE).getString("companyName", ""))) {
                                data.getTabHead().get(3).setCellName(getSharedPreferences("setting", Context.MODE_PRIVATE).getString("companyName", ""));
                            }
                            if (StrUtil.notEmptyOrNull(getSharedPreferences("setting", Context.MODE_PRIVATE).getString("projectName", ""))) {
                                data.getTabHead().get(9).setCellName(getSharedPreferences("setting", Context.MODE_PRIVATE).getString("projectName", ""));
                            }
                            data.getTabHead().add(new CellData(edit_text.getText().toString(), "3", "3", "6", "7"));
                            data.setTabHeadStr(data.getTabHead().toString());
                            data.setTabBodyStr(data.getTabBody().toString());
                            data.setTabFootStr(data.getTabFoot().toString());
                            data.setLevel(level + 1);
                            data.setParentId(parentId);
//                            DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                            dbUtil.save(data);
                            initData();
/*                            ArrayList<ProjectCheckData> list = (ArrayList<ProjectCheckData>) dbUtil.findAll(ProjectCheckData.class);
                            ProjectCheckData projectCheckData = list.get(0);
                            ArrayList<CellData> bodyList = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabBodyStr(), CellData.class);
                            ArrayList<CellData> headList = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabHeadStr(), CellData.class);
                            ArrayList<CellData> footList = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabFootStr(), CellData.class);
                            projectCheckData.setTabBody(bodyList);
                            projectCheckData.setTabHead(headList);
                            projectCheckData.setTabFoot(footList);*/
                        } else {
                            L.toastShort("部位不能为空");
                        }

                    }
                });
        customizeDialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        customizeDialog.show();
    }


    public NavigHandler getNavHandler() {
        if (navHandler == null) {
            navHandler = new NavigHandler(this, headerView) {
                @Override
                public void loadData(int currentId) {
                    parentId = currentId;
                    initData();
                }
            };
        }
        return navHandler;
    }

    public ArrayList<ProjectCheckData> getListData() {
        return listData;
    }

    public void setListData(ArrayList<ProjectCheckData> listData) {
        this.listData = listData;
    }

    public ArrayList<ProjectCheckData> getSelectProjectData() {
        return selectProjectData;
    }

    public void setSelectProjectData(ArrayList<ProjectCheckData> selectProjectData) {
        this.selectProjectData = selectProjectData;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (downReceive != null) {
            unregisterReceiver(downReceive);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_SEARCH) {
                String selectExcelName = data.getStringExtra("selectExcelName");
                String beginTime = data.getStringExtra("beginTime");
                String endTime = data.getStringExtra("endTime");
                initSearchData(selectExcelName, beginTime, endTime);

            }
        }
    }

    public void initSearchData(String selectExcelName, String beginTime, String endTime) {
        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        ArrayList<ProjectCheckData> list = new ArrayList<>();
        if (StrUtil.isEmptyOrNull(selectExcelName) && StrUtil.isEmptyOrNull(beginTime) && StrUtil.isEmptyOrNull(endTime)) {
            return;
        }
        if (!isComplete) {
            list = (ArrayList<ProjectCheckData>) dbUtil.findAllByWhereN(ProjectCheckData.class, "excelType = 1 and completeStatus = 0 and fileType = 1 " +
                    (StrUtil.notEmptyOrNull(selectExcelName) ? (" and excelName = '" + selectExcelName) + "'" : "")
                    + (StrUtil.notEmptyOrNull(beginTime) ? (" and exportDate >= " + beginTime) : "") +
                    (StrUtil.notEmptyOrNull(endTime) ? (" and exportDate < " + endTime) : ""), "id");

        } else {
            if (isRectify) {
                list = new ArrayList<>();
            } else {
                list = (ArrayList<ProjectCheckData>) dbUtil.findAllByWhereN(ProjectCheckData.class, "excelType = 1 and completeStatus = 1 and fileType = 1 " +
                        (StrUtil.notEmptyOrNull(selectExcelName) ? (" and excelName = '" + selectExcelName) + "'" : "")
                        + (StrUtil.notEmptyOrNull(beginTime) ? (" and exportDate >= " + beginTime) : "") +
                        (StrUtil.notEmptyOrNull(endTime) ? (" and exportDate < " + endTime) : ""), "id");

            }

        }

        listData.clear();
        listData.addAll(list);
        homeListAdapter.notifyDataSetChanged();
    }

    private void downExcelFile(final String realUrl, final String filePath) {
/*        new Thread(new Runnable() {
            @Override
            public void run() {
                download(realUrl, filePath);
            }
        }).start();*/
        AttachmentData data = new AttachmentData();
        data.setUrl(realUrl);
        data.setRealUrl(realUrl);
        data.setName(filePath.substring(filePath.lastIndexOf("/") + 1));
        Intent intent = new Intent(this, AttachService.class);
        intent.putExtra(GlobalConstants.KEY_ATTACH_OP, data);
        startService(intent);
    }

    private AttachMsgReceiver downReceive = new AttachMsgReceiver() {
        @Override
        public void downloadCountReceived(Intent intent) {
            if (intent != null) {
                String urlStr = intent.getStringExtra(GlobalConstants.KEY_DOWN_ID);
                String downPercent = intent.getStringExtra(GlobalConstants.KEY_DOWN_PERCENT);
                Boolean bComplete =
                        intent.getBooleanExtra(GlobalConstants.KEY_DOWN_COMPLETE, false);
                if (StrUtil.isEmptyOrNull(urlStr) || StrUtil.isEmptyOrNull(downPercent)
                        || bComplete == null) {
                    return;
                }

                if (bComplete) {
                    File file = (File) intent.getSerializableExtra(GlobalConstants.KEY_DOWN_FILE);
                    String path = intent.getStringExtra(GlobalConstants.KEY_DOWN_ID);


                    File file1 = new File(getExcelHsfDir() + "/hsf");
                    if (file1.exists()) {
                        BaseUtil.deleteFile(file1);
                    }
                    unZipSources(file);
                    saveExcel();


                }
            }
        }
    };

    private void download(String realUrl,String filePath) {
        HttpUtil.getInstance().download(realUrl, filePath, "", new FileCallback() {

            @Override
            public void onSuccess(final File file) {
                // 刷新下载的数据
                File file1 = new File(getExcelHsfDir() + "/hsf");
                if (file1.exists()) {
                    BaseUtil.deleteFile(file1);
                }
                unZipSources(file);
                saveExcel();
                L.d("下载完成，返回True，让程序继续往下走");
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                L.e("下载错误，这次不再下载");
            }
        });
    }
    private void unZipSources(File file) {
        try {
            ZipUtils.unZip(file, getExcelHsfDir() + "/hsf/", "utf-8");
        } catch (IOException e) {
            L.e("素材压缩包解压失败");
            e.printStackTrace();
        }

    }

    private void saveExcel() {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        dbUtil.clear(ExcelData.class);
        File excelFile = new File(getExcelHsfDir() + "/hsf/ccbimpoiExcel");
        if (excelFile.exists()) {
            File[] files = excelFile.listFiles();
            for (File subFile : files) {
                File jsonFile = new File(subFile.getAbsolutePath() + File.separator + "excelJson.txt");
                if (jsonFile.exists()) {
                    String excelJson = getFileContent(jsonFile);
                    if (StrUtil.notEmptyOrNull(excelJson)) {
                        ProjectCheckData projectCheckData=JSON.parseObject(excelJson, ProjectCheckData.class);
                        ExcelData excelData = new ExcelData();
                        excelData.setExcelJson(excelJson);
                        excelData.setPath(subFile.getAbsolutePath());
                        excelData.setExcelName(projectCheckData.getExcelName());
                        dbUtil.save(excelData);
                        L.e(excelData.getExcelName());
                    }
                }
            }
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    public static String getFileContent(File file) {
        String content = "";
        if (!file.isDirectory()) { //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader
                                = new InputStreamReader(instream, "gb2312");
                        BufferedReader buffreader = new BufferedReader(inputreader);
//                        BufferedReader buffreader = new BufferedReader(new FileReader(file));
                        String line = "";
                        //分行读取
                        while ((line = buffreader.readLine()) != null) {
                            content += line + "\n";
                        }
                        instream.close();//关闭输入流
                    }
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d("TestFile", e.getMessage());
                }
            }
        }
//        EncodingUtils.(strLine.getBytes(),"utf-8");
        return content;
    }

    private void downloadFile3(final String url) {
        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("资源下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                String mSDCardPath = getExcelHsfDir();
                File dest = new File(mSDCardPath, url.substring(url.lastIndexOf("/") + 1));
                if (dest.exists()) {
                    dest.delete();
                }
                sink = Okio.sink(dest);
                bufferedSink = Okio.buffer(sink);
                bufferedSink.writeAll(response.body().source());
                bufferedSink.close();
                L.e("资源文件地址：" + dest.getAbsolutePath());
                String currentCode = getFileContent(dest);
                SharedPreferences sharedPreferences= getSharedPreferences("excelCode", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String lastCode = sharedPreferences.getString("resourceCode", "");
                File hsfFile = new File(getExcelHsfDir() + "/hsf");
                if (StrUtil.notEmptyOrNull(currentCode)) {
                    CodeData codeData = JSON.parseObject(currentCode, CodeData.class);
                    if (!lastCode.equals(codeData.getExcelCode()) || !hsfFile.exists()) {
                        downExcelFile(realUrl, getExcelHsfDir() + File.separator + "ccbimpoiExcel.zip");
                        editor.putString("resourceCode", codeData.getExcelCode());
                        editor.commit();
                    }
                }
            }

        });
    }
}
