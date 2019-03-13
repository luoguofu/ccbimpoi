package com.weqia.wq.component.imageselect.file;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.NetworkUtil;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FmActivity extends SharedDetailTitleActivity implements AdapterView.OnItemClickListener {
    public static final String FILE_TYPE_KEY = "FILE_TYPE_KEY";
    public static final int FILE_ALL_TYPE = 2;
    public static final int FILE_ONLY_MODEL = 1;
    public static final int FILE_ONLY_CAD = 3;

    private String root;// 顶层目录
    private TextView tv;// 显示文件的目录
    private File[] files;
    private ListView mListView;
    private FmAdapter mAdapter;
    public static List<String> paths = new ArrayList<>();
    private Integer maxSelect;

    private int fileType;
    private Map<String, Integer> canOpenMap = new HashMap<>(); //可以打开的文件
    private Map<String, Integer> notShowMap = new HashMap<>(); //不可以打开的文件
    private Map<String, Integer> fileCount = new HashMap<>(); //文件数目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fm_main);
        initArgs();
        sharedTitleView.initTopBanner("选择文件", "完成0/" + maxSelect);
        sharedTitleView.getTvRight().setText("上一级");
        sharedTitleView.getTvRight().setVisibility(View.VISIBLE);
        sharedTitleView.getTvRight().setOnClickListener(this);
        tv = (TextView) findViewById(R.id.currPath);
        root = PathUtil.getDefaultdiskpath();
        getPaths().clear();
        fileCount.clear();
        getFiles(root);
    }

    private void initArgs() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            maxSelect = bundle.getInt("maxSelect", 9);
            fileType = bundle.getInt(FILE_TYPE_KEY, FILE_ALL_TYPE);
        } else {
            maxSelect = 9;
            fileType = FILE_ALL_TYPE;
        }
        if (canOpenMap.size() == 0) {
            int resId = 0;
            switch (fileType) {
                case FILE_ONLY_CAD:
                    resId = R.array.cad_format;
                    for (String str : getResources().getStringArray(resId)) {
                        canOpenMap.put(str, 0);
                    }
                    break;
                case FILE_ONLY_MODEL:
                    resId = R.array.can_open_format;
                    for (String str : getResources().getStringArray(resId)) {
                        canOpenMap.put(str, 0);
                    }
                    break;
                default:
                    break;
            }


        }
        if (notShowMap.size() == 0) {
            for (String str : getResources().getStringArray(R.array.not_show_path))
                notShowMap.put(str, 0);
        }
    }

    // 得到所有子文件和目录
    FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                if (!pathname.canRead())
                    return false;
//                if (notShowMap.containsKey(pathname.getAbsolutePath())){
//                    return false;
//                }
                if (pathname.getName().startsWith("."))
                    return false;
                return true;
            } else {
                if (canOpenMap.size() == 0) {
                    return true;
                }
                String name = StrUtil.getFileNameExtensionByPath(pathname.getName());
                if (canOpenMap.containsKey(name)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    };

    public void getFiles(String path) {
        tv.setText(path);
        File f = new File(path);
        // 得到所有子文件和目录
        files = f.listFiles(fileFilter);
        sortFilesByDirectory(files);
        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new FmAdapter(FmActivity.this, files);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }


    // 对文件进行排序
    private void sortFilesByDirectory(File[] files) {
        if (files == null) {
            return;
        }
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                if (f1.isDirectory() && f2.isFile())
                    return -1;
                if (f1.isFile() && f2.isDirectory())
                    return 1;
                // 忽略大小写
                return f1.getName().compareToIgnoreCase(f2.getName());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File f = files[position];
        if (!f.canRead()) {
            Toast.makeText(this, "文件不可读", Toast.LENGTH_SHORT).show();
            return;
        }
        if (f.isFile()) {// 为文件
            if (getPaths().size() >= maxSelect && !getPaths().toString().contains(f.getAbsolutePath())) {
                L.toastShort("最多上传" + maxSelect + "个文件！");
                return;
            }
            if (getPaths().toString().contains(f.getAbsolutePath())) {
                getPaths().remove(f.getAbsolutePath());
            } else {
                getPaths().add(f.getAbsolutePath());
            }
            getAdapter().notifyDataSetChanged();
            sharedTitleView.getButtonStringRight().setText("完成" + getPaths().size() + "/" + maxSelect);
        } else {
            getFiles(f.getAbsolutePath());
        }
    }


    private void uploadConfirm() {
        Dialog mDialog = DialogUtil.initCommonDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -2:
                        break;
                    case -1:
                        if (!NetworkUtil.detect(WeqiaApplication.getInstance())) {
                            L.toastShort(R.string.lose_network_hint);
                            return;
                        }
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        }, "确定要上传选中的" + getPaths().size() + "个文件吗?");
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == sharedTitleView.getButtonLeft()) {
            getPaths().clear();
            finish();
        } else if (v == sharedTitleView.getButtonStringRight()) {
            //TODO  上传文件路径
            uploadConfirm();
        } else if (v == sharedTitleView.getTvRight()) {
            backDo();
        }
    }

    private void backDo() {
        String curPath = tv.getText().toString();
        curPath = curPath.substring(0, curPath.lastIndexOf("/"));
        if (StrUtil.notEmptyOrNull(curPath)) {
            getFiles(curPath);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        backDo();
    }

    public void setAdapter(FmAdapter adapter) {
        mAdapter = adapter;
    }

    public FmAdapter getAdapter() {
        return mAdapter;
    }

    public static List<String> getPaths() {
        return paths;
    }

    public Integer getMaxSelect() {
        return maxSelect;
    }

    public Map<String, Integer> getFileCount() {
        return fileCount;
    }
}
