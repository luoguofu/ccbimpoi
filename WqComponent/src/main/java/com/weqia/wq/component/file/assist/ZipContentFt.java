package com.weqia.wq.component.file.assist;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.weqia.component.rcmode.RcBaseListFragment;
import com.weqia.component.rcmode.RcBaseViewHolder;
import com.weqia.component.rcmode.adapter.RcFastAdapter;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.file.ZipFileContentListActivity;
import com.weqia.wq.component.utils.AttachUtils;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.FileMiniUtil;
import com.weqia.wq.component.utils.GlobalUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 20161005 on 2017/6/30.
 */

public class ZipContentFt extends RcBaseListFragment<ZipFileData> {

    private ZipFileContentListActivity ctx;
    private RcFastAdapter<ZipFileData> mAdapter;
    private String zipPath = null;
    private String nodeId;
    private String portInfo;
    private int canAction;
    private Integer nodeType;
    private String pjId;
    /**
     * Zip里面所有的数据集
     */
    private List<ZipFileData> allEnteyLists = new ArrayList<>();
    /**
     * 展示在列表上的数据集
     */
    private List<ZipFileData> showEnteyLists = new ArrayList<>();
    private Dialog zipDlg;
    /**
     * 用于判断是第几层(每进入一个文件夹，就会加一，初始界面为0)
     */
    private int page = 0;

    private SharedTitleActivity.UIHandler handler = new SharedTitleActivity.UIHandler(ctx) {
        @Override
        public void handleMessage(Message msg, SharedTitleActivity viewController) {
            if (msg.what == 0 && msg.obj != null) {
//                L.e(msg.obj.toString());
                String path = msg.obj.toString();
                AttachUtils.openFile(ctx, path, portInfo, ctx.getZipName(), nodeType, canAction, pjId);

//                L.toastShort("解压成功");
                dismissDlg();
            } else if (msg.what == 1) {
                L.e("解压文件失败");
                dismissDlg();
            } else if (msg.what == 2) {
                L.e("关闭对话框");
                dismissDlg();
            }
        }
    };


    private void dismissDlg() {
        if (zipDlg != null)
            handler.sendEmptyMessage(3);
    }

    @Override
    public void initCustomView() {
        super.initCustomView();
        ctx = (ZipFileContentListActivity) getActivity();
        if (getArguments() != null) {
            //  获取到本地的Zip文件路径
            zipPath = getArguments().getString("zipPath");
            nodeId = getArguments().getString("nodeId");
            portInfo = getArguments().getString("portInfo");
            nodeType = getArguments().getInt("nodeType");
            pjId = getArguments().getString("pjId");
            canAction = getArguments().getInt("bCanAction", 1);
        }
        if (StrUtil.notEmptyOrNull(zipPath)) {
            //  根据Zip路径进行解析，获取到Zip文件里的实体，拼接数据类型
            allEnteyLists = ZipHelper.getZipFileEntryList(zipPath);
//            File file = new File(zipPath);
//            if (file.exists()) {
//                int a = zipPath.lastIndexOf(".");
//                String tmpPath = "";
//                if (a == -1) {
//                    tmpPath = zipPath + File.separator;
//                } else {
//                    tmpPath = zipPath.substring(0, a) + File.separator;
//                }
//                try {
//                    ZipHelper.unZip(file, tmpPath + "tmptmptmptmp");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        mAdapter = new RcFastAdapter<ZipFileData>(ctx, R.layout.cell_bim_mode_list_view) {
            @Override
            public void bindingData(RcBaseViewHolder rcBaseViewHolder, final ZipFileData item) {
                CommonImageView ivIcon = rcBaseViewHolder.getView(R.id.ivIcon);
                TextView tvName = rcBaseViewHolder.getView(R.id.tvName);
                TextView tvSize = rcBaseViewHolder.getView(R.id.tvSize);
                TextView tvFrom = rcBaseViewHolder.getView(R.id.tvDate);
                ViewUtils.hideView(tvFrom);
                if (item.isDir()) {
                    String name = item.getFileName().substring(0, item.getFileName().lastIndexOf("/"));
                    if (page > 0) {
                        name = item.getFileName().substring(name.lastIndexOf("/") + 1, name.length());
                    } else {
                    }
                    ivIcon.setImageResource(R.drawable.f_folder);
                    tvName.setText(name);
                    ViewUtils.hideView(tvSize);
                    tvSize.setText("");
                } else {
                    String name = item.getFileName().substring(item.getFileName().lastIndexOf("/") + 1,
                            item.getFileName().length());
                    tvName.setText(name);
                    int res = FileMiniUtil.fileRId(item.getFileName());
                    ivIcon.setImageResource(res);
                    ViewUtils.showView(tvSize);
                    tvSize.setText(StrUtil.formatFileSize(String.valueOf(item.getFileSize() / 1024 + "")));
                }
                rcBaseViewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isDir()) {
                            //  进入文件夹，预览文件夹里的文件
                            page++;
                            getShowFileList(page);
                        } else {
                            //  是文件，直接解压到指定目录
                            unModeZip(item, true);
                        }
                    }
                });
            }
        };
        setAdapter(mAdapter);
        //  进去列表进行初次解析
        if (StrUtil.listNotNull(allEnteyLists)) {
            for (ZipFileData entry : allEnteyLists) {
                if (entry.getDirFile() == null || entry.getDirFile().length == 1) {
                    showEnteyLists.add(entry);
                }
            }
            if (StrUtil.listIsNull(showEnteyLists)) {
                int index = allEnteyLists.get(0).getEntryName().split("/").length;
                for (ZipFileData entry : allEnteyLists) {
                    if (entry.getDirFile() != null && entry.getDirFile().length == index) {
                        showEnteyLists.add(entry);
                    }
                }
            }
        }
        setAll(showEnteyLists);
        rcListView.setLoadMoreEnabled(false);
        rcListView.setNoMore(false);
        // 将数据适配到列表
        if (StrUtil.listNotNull(showEnteyLists) && showEnteyLists.size() == 1) {
            unModeZip(showEnteyLists.get(0), false);
            ctx.finish();
        }
    }

    private void unModeZip(ZipFileData item, boolean showDlg) {
        int a = zipPath.lastIndexOf(".");
        String tmpPath = "";
        if (a == -1) {
            tmpPath = zipPath + File.separator + item.getZipEntry().getName();
        } else {
            tmpPath = zipPath.substring(0, a) + File.separator + item.getZipEntry().getName();
        }
        String wrapedPath = GlobalUtil.wrapNodePath(nodeId, tmpPath);
        if (new File(tmpPath).exists()) {
            AttachUtils.openFile(ctx, wrapedPath, portInfo, ctx.getZipName(), nodeType, canAction, pjId);
            return;
        }
        if (showDlg) {
            dismissDlg();
            zipDlg = DialogUtil.commonLoadingDialog(ctx, "正在解压，请稍后...");
            zipDlg.setCancelable(false);
            zipDlg.show();
        }
        ZipTask task = new ZipTask();
        task.execute(item);
    }

    // 创建异步解析类
    class ZipTask extends AsyncTask<ZipFileData, Void, Void> {

        @Override
        protected Void doInBackground(final ZipFileData... params) {
            // 获取数据实体对象，进行解析
            ZipFileData item = params[0];
            try {
                int a = zipPath.lastIndexOf(".");
                String tmpPath = "";
                if (a == -1) {
                    tmpPath = zipPath + File.separator;
                } else {
                    tmpPath = zipPath.substring(0, a) + File.separator;
                }

                ZipHelper.resloveZipEntry(
                        item.getZipEntry(), zipPath, tmpPath,
                        new ZipHelper.ZipInterface() {
                            @Override
                            public void unzipSuccess(String path) {
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = GlobalUtil.wrapNodePath(nodeId, path);
                                handler.sendMessage(msg);
                            }
                        });
//                                ctx.setResult(Activity.RESULT_OK);
            } catch (IOException e) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 后退操作
     */
    public void goBack() {
        if (page == 0) {
            ctx.finish();
        } else {
            // TODO: 2017/7/24 获取上一页文件夹数据 
            page--;
            getShowFileList(page);
        }
    }

    /**
     * 对指定文件夹进行文件解析并展示
     * @param page 第一层文件夹
     */
    public void getShowFileList(int page) {
        showEnteyLists.clear();
        if (page == 0) {
            for (ZipFileData entry : allEnteyLists) {
                if (entry.getDirFile() == null || entry.getDirFile().length == page + 1) {
                    showEnteyLists.add(entry);
                }
            }
        } else {
            for (ZipFileData entry : allEnteyLists) {
                if (entry.getDirFile() != null && entry.getDirFile().length == page + 1) {
                    showEnteyLists.add(entry);
                }
            }
        }
        setAll(showEnteyLists);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }

    @Override
    public void loadMore() {
    }

    @Override
    protected String getFiterText(ZipFileData entry) {
        return null;
    }
}
