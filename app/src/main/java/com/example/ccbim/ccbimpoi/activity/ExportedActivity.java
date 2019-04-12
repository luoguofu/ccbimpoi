package com.example.ccbim.ccbimpoi.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.example.ccbim.ccbimpoi.util.BaseUtil;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.ccbim.ccbimpoi.MainNewActivity.getPoiExcelDir;
import static com.example.ccbim.ccbimpoi.activity.HomeActivity.getExcelFileIntent;

/**
 * Created by lgf on 2019/4/10.
 */

public class ExportedActivity extends BaseActivity {
    private RecyclerView mCommonRecyclerview;
    private RecyclerView.Adapter<ViewHolder> adapter;
    private List<ProjectCheckData> attachList = new ArrayList<>();
    private Dialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exported_excel);
        setTitle("已导出表单");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initview();
        initData();
    }

    private void initview() {
        mCommonRecyclerview = (RecyclerView) findViewById(R.id.common_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mCommonRecyclerview.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
//        mCommonRecyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = getAdapter();
        mCommonRecyclerview.setAdapter(adapter);
    }

    private void initData() {
/*        List<String> list = getFilesAllName(getPoiExcelDir());
        attachList.clear();
        if (StrUtil.listNotNull(list)) {
            for (String path : list) {
                if (path.endsWith("xls")) {
                    AttachmentData attachmentData = new AttachmentData();
                    attachmentData.setUrl(path);
                    attachmentData.setName(path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")));
                    attachList.add(attachmentData);
                }
            }
        }*/
        attachList.clear();
        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        attachList = dbUtil.findAllByWhereN(ProjectCheckData.class, "isExport = 1", "id");
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private RecyclerView.Adapter<ViewHolder> getAdapter() {
        adapter= new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(ExportedActivity.this).inflate(R.layout.item_common, parent, false);
                ViewHolder holder= new ViewHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                final ProjectCheckData projectCheckData = attachList.get(position);
                holder.excelNameTv.setText(projectCheckData.getExcelFullName());
                holder.excelNameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = getExcelFileIntent(new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls"));
//                        startActivity(intent);
                    }
                });
                holder.excelNameTv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        dialog= DialogUtil.initLongClickDialog(ExportedActivity.this, null, new String[]{"删除", "分享"}, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                switch ((Integer) view.getTag()) {
                                    case 0:
                                        Dialog confirmDialof = DialogUtil.initCommonDialog(ExportedActivity.this, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                switch (i) {
                                                    case -2:
                                                        break;
                                                    case -1:
                                                        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                                                        dbUtil.deleteById(ProjectCheckData.class, projectCheckData.getId());
                                                        BaseUtil.deleteFile(new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls"));
                                                        initData();
                                                        break;

                                                }
                                                dialog.dismiss();
                                            }
                                        }, "确定要删除吗？");
                                        confirmDialof.show();
                                        break;
                                    case 1:
//                                        Intent intent = getExcelFileIntent(new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls"));
//                                        startActivity(intent);
                                        File file = new File(getPoiExcelDir() + File.separator + projectCheckData.getExcelFullName() + ".xls");
                                        getExcelFileIntent(file, ExportedActivity.this);
                                        break;
                                }
                            }
                        });
                        dialog.show();
                        return false;
                    }
                });

            }

            @Override
            public int getItemCount() {
                return StrUtil.listNotNull(attachList) ? attachList.size() : 0;
            }
        };
        return adapter;
    }
    /**
     * RecyclerView的持有者类
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView excelNameTv;


        public ViewHolder(View view) {
            super(view);
            excelNameTv = view.findViewById(R.id.tv_excel_name);

        }
    }
    public static List<String> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }


}
