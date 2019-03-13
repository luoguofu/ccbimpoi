package com.weqia.wq.component.view.picture;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.imageselect.SelectMediaUtils;
import com.weqia.wq.component.utils.AttachUtils;
import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.component.utils.FileMiniUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.video.VideoPlayActivity;
import com.weqia.wq.component.view.picture.PictureAddAdapter.PictureViewHolder;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.EnumData.ImageThumbTypeEnums;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

import java.util.ArrayList;

// 多图组件,选取展示
public class PictureGridView extends LinearLayout {

    private GridView gvPic;
    private PictureAddAdapter adapter;
    private ArrayList<String> addedpaths;
    private SharedTitleActivity ctx;

    private int maxSize;
    private boolean blankShow;
    private BitmapUtil bitmapUtil;

    // 只有一个添加入口，则需要弹出选择文件
    private boolean singleAdd = false;
    // 是否可以编辑
    private boolean addEnabled = true;

    public PictureGridView(SharedTitleActivity context) {
        this(context, null, true, GlobalConstants.IMAGE_MAX, true);
    }

    public PictureGridView(SharedTitleActivity context, boolean addEnabled) {
        this(context, null, true, GlobalConstants.IMAGE_MAX, addEnabled);
    }

    public PictureGridView(SharedTitleActivity ctx, int maxSize) {
        this(ctx, null, true, maxSize, true);
    }

    public PictureGridView(SharedTitleActivity ctx, boolean blankShow, boolean addEnabled) {
        this(ctx, null, blankShow, GlobalConstants.IMAGE_MAX, addEnabled);
    }

    public PictureGridView(SharedTitleActivity ctx, AttributeSet attrs, boolean blankShow,
                           int size, boolean addEnabled) {
        super(ctx, attrs);
        setOrientation(LinearLayout.VERTICAL);
        this.blankShow = blankShow;
        this.addEnabled = addEnabled; // 是否能添加
        this.maxSize = size; // 最大的图片数量
        this.ctx = ctx;
        initViews();
    }

    private void initViews() {
        bitmapUtil = WeqiaApplication.getInstance().getBitmapUtil(); // 得到图片工具类的实例
        final View view = LayoutInflater.from(ctx).inflate(R.layout.view_reused_picture, null); // 就是一个GridView的布局！！
        gvPic = (GridView) view.findViewById(R.id.gv_reused_picture); // 找到实例
        setPicLayoutParam(1); // 设定布局的高
        addView(view);
        addedpaths = new ArrayList<String>(); // 图片的路径
        adapter = new PictureAddAdapter(ctx, this.blankShow, this.maxSize, this.addEnabled) {

            @Override
            public void setData(final int position, PictureViewHolder holder) {// 选择图片之后执行的操作
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivIcon.setTag(position);
                if (isShowDelete()) {
                    ViewUtils.showView(holder.ivDelete);
                } else {
                    ViewUtils.hideView(holder.ivDelete); // 这里选择的是隐藏
                }
                if (position >= getPaths().size()) {
                    ViewUtils.hideView(holder.tvName);
                    ViewUtils.hideView(holder.ivDelete);
                    ViewUtils.hideView(holder.ivVideoPlay);
                    if (!isAddEnabled()) {
                        return;
                    }
                    if (position == getPaths().size()) { // 这一步在第三次的时候没有直接跳到
                        if (getPaths().size() == getMaxPicture()) {
                            ctx.getBitmapUtil().load(holder.ivIcon,
                                    Scheme.DRAWABLE.wrap(R.drawable.image_sub_button_normal + ""),
                                    null);
                        } else {
                            ctx.getBitmapUtil().load(holder.ivIcon,
                                    Scheme.DRAWABLE.wrap(R.drawable.image_add_button_normal + ""),
                                    null);
                        }
                    } else {
                        ctx.getBitmapUtil()
                                .load(holder.ivIcon,
                                        Scheme.DRAWABLE.wrap(R.drawable.image_sub_button_normal
                                                + ""), null);
                    }
                } else { // 这里执行：
                    final String path = getPaths().get(position); // addedpaths.get(position);
                    if (StrUtil.notEmptyOrNull(path)) {
                        String[] mutilPaths = path.split(GlobalConstants.SPIT_SENDMEDIA);
                        // 普通文件
                        if (mutilPaths.length == 1) { // 调试的时候我只选择了一张图片，这里是等于1；
                            loadImage(holder, path);
                            ViewUtils.hideView(holder.tvName);
                            ViewUtils.hideView(holder.ivVideoPlay);
                        } else if (mutilPaths.length >= SelectMediaUtils.VideoPathCount) {
                            String type = mutilPaths[0];
                            String info = mutilPaths[1];
                            String des = mutilPaths[2];
                            if (type.equalsIgnoreCase(AttachType.PICTURE.value() + "")) {
                                loadImage(holder, des);
                                ViewUtils.hideView(holder.tvName);
                                ViewUtils.hideView(holder.ivVideoPlay);
                            } else if (type.equalsIgnoreCase(AttachType.VIDEO.value() + "")) {
                                ViewUtils.showView(holder.ivVideoPlay);
                                loadVideo(holder, info);
                                if (mutilPaths.length == SelectMediaUtils.VideoPathCount + 1) {
                                    String tmpStr = mutilPaths[3];
                                    if (StrUtil.notEmptyOrNull(tmpStr)) {
                                        if (PathUtil.isPathInDisk(tmpStr)) {
                                            bitmapUtil.displayImage(tmpStr, holder.ivIcon,
                                                    bitmapUtil.getLocalOptions());
                                        } else {
                                            bitmapUtil.load(holder.ivIcon, tmpStr,
                                                    ImageThumbTypeEnums.THUMB_VERY_SMALL.value());
                                        }

                                    } else {
                                        ctx.getBitmapUtil().load(holder.ivIcon,
                                                Scheme.DRAWABLE.wrap(R.drawable.video_icon + ""),
                                                null);
                                    }
                                } else {
                                    ctx.getBitmapUtil().load(holder.ivIcon,
                                            Scheme.DRAWABLE.wrap(R.drawable.video_icon + ""), null);
                                }
                            } else if (type.equalsIgnoreCase(AttachType.FILE.value() + "")) {
                                loadFile(holder, info);
                                ViewUtils.hideView(holder.ivVideoPlay);
                            }
                        } else {
                            ViewUtils.hideView(holder.ivVideoPlay);
                            ctx.getBitmapUtil().load(holder.ivIcon,
                                    Scheme.DRAWABLE.wrap(R.drawable.people + ""), null);
                            ViewUtils.hideView(holder.tvName);
                        }
                    } else {
                        ctx.getBitmapUtil().load(holder.ivIcon,
                                Scheme.DRAWABLE.wrap(R.drawable.people + ""), null);
                    }
                }
            }
        };
        gvPic.setAdapter(adapter);
        gvPic.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!addEnabled) {
                    if (position >= addedpaths.size()) {
                        return;
                    }
                    String path = addedpaths.get(position);
                    if (StrUtil.isEmptyOrNull(path)) {
                        return;
                    }

                    int type = -1;
                    if (SelectMediaUtils.isImagePath(path)) {
                        type = AttachType.PICTURE.value();
                    } else if (SelectMediaUtils.isVideoPath(path)) {
                        type = AttachType.VIDEO.value();
                    } else if (SelectMediaUtils.isFilePath(path)) {
                        type = AttachType.FILE.value();
                    }

                    if (path.contains(GlobalConstants.SPIT_SENDMEDIA)) {
                        path =
                                path.substring(
                                        path.lastIndexOf(GlobalConstants.SPIT_SENDMEDIA) + 1,
                                        path.length());
                    }
                    AttachmentData data = LnUtil.getAttachment(path, type);
                    if (data != null) {
                        View iv = view.findViewById(R.id.iv_icon);
                        AttachUtils.attachClick(ctx, data, iv);
                    }
                } else {
                    if (position >= adapter.getPaths().size()) {
                        if (position == adapter.getPaths().size()) {
                            if (adapter.getPaths().size() == adapter.getMaxPicture()) {
                                adapter.setShowDelete(!adapter.isShowDelete());
                            } else {
                                if (isSingleAdd()) {
                                    SelectMediaUtils.showAttachDialog(ctx, PictureGridView.this);
                                } else {
                                    addPicture();
                                }
                            }
                        } else {
                            adapter.setShowDelete(!adapter.isShowDelete());
                        }
                    } else {
                        if (StrUtil.listNotNull(addedpaths)&&position<addedpaths.size()){
                            final String path = addedpaths.get(position);
                            if (StrUtil.notEmptyOrNull(path)) {
                                if (adapter.isShowDelete()) {
                                    deletePic(path);
                                } else {
                                    iconClick(path);
                                }
                            }
                        }
                    }
                }
            }
        });
        adapter.setItems(SelectArrUtil.getInstance().getSelectedImgs());
    }

    /**
     * 加载图片
     *
     * @param holder
     * @param path
     */
    private void loadImage(PictureViewHolder holder, final String path) {
        if (PathUtil.isPathInDisk(path)) {
            bitmapUtil.load(holder.ivIcon, FileUtil.getFormatFilePath(path), null);
        } else {
            // 网络图片
            bitmapUtil.load(holder.ivIcon, path, ImageThumbTypeEnums.THUMB_VERY_SMALL.value());
        }
    }

    /**
     * 加载视频
     *
     * @param holder
     * @param info
     */
    private void loadVideo(PictureViewHolder holder, String info) {
        if (StrUtil.notEmptyOrNull(info)) {
            String name = "";
            try {
                name = Long.parseLong(info) / 1000 + "秒";
            } catch (NumberFormatException e) {
            }
            holder.tvName.setText(name);
            ViewUtils.hideView(holder.tvName);
        } else {
            ViewUtils.hideView(holder.tvName);
        }
    }

    /**
     * 加载文件
     *
     * @param holder
     * @param info
     */
    private void loadFile(PictureViewHolder holder, String info) {
        if (StrUtil.notEmptyOrNull(info)) {
            ViewUtils.showView(holder.tvName);
            if (info.length() > 8) {
                holder.tvName.setText("     " + info);
            } else {
                holder.tvName.setText(info);
            }
            Drawable drawable = ctx.getResources().getDrawable(FileMiniUtil.sendFileRId(info));
            if (drawable != null) {
                holder.ivIcon.setImageDrawable(drawable);
            } else {
                holder.ivIcon.setImageResource(R.drawable.bg_file);
            }
        } else {
            ViewUtils.hideView(holder.tvName);
            holder.ivIcon.setImageResource(R.drawable.bg_file);
        }
    }


    /**
     * 显示大图
     *
     * @param path
     */
    private void iconClick(String path) {
        if (SelectMediaUtils.isImagePath(path)) {
            // int nImgNum = getImageSelectInfo();
            int index = 0;
            for (int i = 0; i < SelectArrUtil.getInstance().getSelImgSize(); i++) {
                String tmpPath = SelectArrUtil.getInstance().getSelImg(i);
                bitmapUtil.getMemoryCache().remove(tmpPath);
                bitmapUtil.getMemoryCache().remove(Scheme.FILE.wrap(tmpPath));
                if (tmpPath.equalsIgnoreCase(path)) {
                    index = i;
                    break;
                }
            }

            SelectMediaUtils.addPicByDetail(ctx, this, (ArrayList<String>) SelectArrUtil.getInstance().getSelectedImgs(), index);
            // int canAddSize = adapter.getMaxPicture() - nImgNum;
            // Intent i = new Intent(ctx, ImageBrowseActivity.class);
            // i.putExtra(GlobalConstants.EXTRA_IMAGES, selectStrs);
            // i.putExtra(GlobalConstants.EXTRA_INDEX, index);
            // i.putExtra(GlobalConstants.KEY_IMAGE_SELECT_SIZE, canAddSize);
            // ctx.startActivityForResult(i, SelectMediaUtils.REQ_GET_PIC);
        } else {
            if (path.startsWith(AttachType.VIDEO.value() + GlobalConstants.SPIT_SENDMEDIA)) {
                String[] mutilPaths = path.split(GlobalConstants.SPIT_SENDMEDIA);
                if (mutilPaths.length >= 3) {
                    String info = mutilPaths[1];
                    String des = mutilPaths[2];

                    String filePath = des;
                    Integer playTime = (int) (Long.parseLong(info) / 1000);
                    AttachmentData fileData = new AttachmentData(filePath, playTime);
                    if (fileData != null) {
                        ctx.startToActivity(VideoPlayActivity.class, fileData);
                    }
                }
            }
        }
    }

    public int getImageSelectInfo() {
        SelectArrUtil.getInstance().clearImage();
        SelectArrUtil.getInstance().clearSourceImage();
        int noImgNum = 0;
        for (String tmpPath : getAddedPaths()) {
            if (tmpPath.contains(GlobalConstants.SPIT_SENDMEDIA)) {
                if (tmpPath.startsWith(AttachType.PICTURE.value() + GlobalConstants.SPIT_SENDMEDIA)) {
                    SelectArrUtil.getInstance().addImage(tmpPath);
                } else {
                    noImgNum++;
                }
            } else {
                SelectArrUtil.getInstance().addImage(tmpPath);
            }
        }
        return noImgNum;
    }

    public void addPicture() {
        SelectMediaUtils.addPic(ctx, this);
        // if (getAdapter().canAdd()) {
        // int nImgNum = getImageSelectInfo();
        // int canAddSize = getAdapter().getMaxPicture() - nImgNum;
        // L.e("bitmap add num :" + canAddSize);
        // Map<String, String> paramMap = new HashMap<String, String>();
        // paramMap.put(GlobalConstants.KEY_IMAGE_SELECT_SIZE, canAddSize + "");
        // paramMap.put(GlobalConstants.KEY_SELECT_TYPE, AttachType.PICTURE.value() + "");
        // ctx.startToActivityForResult(ImageListActivity.class, paramMap,
        // SelectMediaUtils.REQ_GET_PIC);
        // }
    }

    public void deletePic(String path) {
        getAddedPaths().remove(path);
        refresh();
    }

    public PictureAddAdapter getAdapter() {
        return adapter;
    }

    public ArrayList<String> getAddedPaths() {
        return addedpaths;
    }

    public void refresh() {
        switch (getAddedPaths().size()) {
            case 0:
            case 1:
            case 2:
            case 3:
                setPicLayoutParam(1);
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                setPicLayoutParam(2);
                break;
            default:
                break;
        }
        adapter.setItems(getAddedPaths());
    }

    private void setPicLayoutParam(int row) {
        int height = 0;
        switch (row) {
            case 0:
                height = 0;
                break;
            case 1:
                height = ComponentUtil.dip2px(64);
                break;
            case 2:
                height = ComponentUtil.dip2px(64 * 2 + 6);
                break;

            default:
                break;
        }
        LinearLayout.LayoutParams param =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        param.setMargins(ComponentUtil.dip2px(6), ComponentUtil.dip2px(5),
                ComponentUtil.dip2px(6), ComponentUtil.dip2px(5));
        gvPic.setLayoutParams(param);
    }

    public GridView getGvPic() {
        return gvPic;
    }

    public boolean isSingleAdd() {
        return singleAdd;
    }

    public void setSingleAdd(boolean singleAdd) {
        this.singleAdd = singleAdd;
    }

    public boolean isAddEnabled() {
        return addEnabled;
    }

    public void setAddEnabled(boolean addEnabled) {
        this.addEnabled = addEnabled;
    }
}
