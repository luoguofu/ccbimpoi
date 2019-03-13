package com.weqia.wq.component.view.title_pop;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.weqia.utils.DeviceUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.assist.SharedSearchHolder;

import java.util.ArrayList;

public class TitlePopup extends PopupWindow {
    private Context mContext;
    // 列表弹窗的间隔
    protected final int LIST_PADDING = 12;
    // 实例化一个矩形
    private Rect mRect = new Rect();
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    // 屏幕的宽度和高度
    @SuppressWarnings("unused")
    private int mScreenWidth, mScreenHeight;
    // 判断是否需要添加或更新列表子类项
    private boolean mIsDirty;
    // 位置不在中心
    private int popupGravity = Gravity.NO_GRAVITY;
    // 弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;
    // 定义列表对象
    private ListView mListView;
    // 定义弹窗子类项列表
    private ArrayList<TitleItem> mActionItems = new ArrayList<TitleItem>();

    public TitlePopup(Context context) {
        // 设置布局的参数
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @SuppressWarnings("deprecation")
    public TitlePopup(Context context, int width, int height) {
        super(context);
        this.mContext = context;

        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);

        // 获得屏幕的宽度和高度
        mScreenWidth = DeviceUtil.getDeviceWidth();
        mScreenHeight = DeviceUtil.getDeviceHeight();

        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);


        setBackgroundDrawable(new BitmapDrawable());

        // 设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.view_title_popup, null));

        initUI();
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mListView = (ListView) getContentView().findViewById(R.id.title_list);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                // 点击子类项后，弹窗消失
                dismiss();

                if (mItemOnClickListener != null)
                    mItemOnClickListener.onItemClick(mActionItems.get(index), index);
            }
        });
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view) {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);

        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
                mLocation[1] + view.getHeight());

        // 判断是否需要添加或更新列表子类项
        if (mIsDirty) {
            populateActions();
        }

        // 显示弹窗的位置
        showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth() / 2),
                mRect.bottom);
    }

    /**
     * 设置弹窗列表子项
     */
    private void populateActions() {
        mIsDirty = false;

        // 设置列表的适配器
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                SharedSearchHolder holder = null;
                if (convertView == null) {
                    holder = new SharedSearchHolder();
                    convertView =
                            LayoutInflater.from(mContext).inflate(R.layout.cell_view_popup, null);
                    holder.ivIcon = (CommonImageView) convertView.findViewById(R.id.iv_popup);
                    holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_popup);
                    convertView.setTag(holder);
                } else {
                    holder = (SharedSearchHolder) convertView.getTag();
                }

                TitleItem item = mActionItems.get(position);
                holder.tvTitle.setText(item.mTitle);

                if (item.drawableId != null) {
                    ViewUtils.showView(holder.ivIcon);
                    holder.ivIcon.setImageResource(item.drawableId);
                } else {
                    ViewUtils.hideView(holder.ivIcon);
                }
                if (item.drawableRightId != null) {
                    Drawable drawableRight = null;
                    drawableRight = item.ctx.getResources().getDrawable(item.drawableRightId);
                    drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(),
                            drawableRight.getMinimumHeight());
                    holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            drawableRight, null);
                } else {
                    holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return mActionItems.get(position);
            }

            @Override
            public int getCount() {
                return mActionItems.size();
            }
        });
    }


    /**
     * 添加子类项
     */
    public void addAction(TitleItem action) {
        if (action != null) {
            mActionItems.add(action);
            mIsDirty = true;
        }
    }

    /**
     * 清除子类项
     */
    public void cleanAction() {
        if (!mActionItems.isEmpty()) {
            mActionItems.clear();
            mIsDirty = true;
        }
    }

    /**
     * 根据位置得到子类项
     */
    public TitleItem getAction(int position) {
        if (position < 0 || position > mActionItems.size()) return null;
        return mActionItems.get(position);
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * @author yangyu 功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemOnClickListener {
        public void onItemClick(TitleItem item, int position);
    }
}
