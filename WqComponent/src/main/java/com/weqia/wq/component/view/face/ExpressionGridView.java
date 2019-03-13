package com.weqia.wq.component.view.face;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.weqia.utils.DeviceUtil;
import com.weqia.utils.view.jazzyviewpager.CirclePageIndicator;
import com.weqia.utils.view.jazzyviewpager.JazzyViewPager;
import com.weqia.wq.R;
import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 表情VIEW
 */
public class ExpressionGridView extends LinearLayout {
    private JazzyViewPager faceViewPager;
    private int currentPage = 0;
    private List<String> mfaceKeys;
    private EditText etInput;
    private Context ctx;
    private CirclePageIndicator indicator;

    public ExpressionGridView(Context ctx) {
        this(ctx, null);
    }

    public ExpressionGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }

    public void initEt(EditText etInput) {
        this.etInput = etInput;
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        LinearLayout layout = new LinearLayout(ctx);
        View view = inflater.inflate(R.layout.cell_face, layout);
        if (view != null) {
            faceViewPager = (JazzyViewPager) view.findViewById(R.id.face_pager);
            indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        }
        this.addView(layout);
        Set<String> keySet = WeqiaApplication.getInstance().getFaceMap()
                .keySet();
        mfaceKeys = new ArrayList<String>();
        mfaceKeys.addAll(keySet);
        initFacePage();
    }

    private void initFacePage() {
        List<View> lv = new ArrayList<View>();
        for (int i = 0; i < GlobalConstants.FACE_NUM_PAGE; ++i)
            lv.add(getGridView(i));
        FacePageAdeapter adapter = new FacePageAdeapter(lv, faceViewPager);
        faceViewPager.setAdapter(adapter);
        faceViewPager.setCurrentItem(currentPage);
        faceViewPager.setTransitionEffect(GlobalConstants.mEffects[0]);
        indicator.setViewPager(faceViewPager);
        adapter.notifyDataSetChanged();
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                currentPage = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    public GridView getGridView(int i) {
        GridView gv = new GridView(ctx);
        gv.setNumColumns(7);
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
        gv.setBackgroundColor(Color.TRANSPARENT);
        gv.setVerticalSpacing(ComponentUtil.px2dip(55));
        gv.setCacheColorHint(Color.TRANSPARENT);
        gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 300));
        gv.setGravity(Gravity.CENTER);
        gv.setAdapter(new FaceAdapter(ctx, i));
        gv.setOnTouchListener(forbidenScroll());
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == GlobalConstants.FACE_NUM) {// 删除键的位置
                    int selection = etInput.getSelectionStart();
                    String text = etInput.getText().toString();
                    if (selection > 0) {
                        String text2 = text.substring(selection - 1);
                        if ("]".equals(text2)) {
                            int start = text.lastIndexOf("[");
                            int end = selection;
                            etInput.getText().delete(start, end);
                            return;
                        }
                        etInput.getText().delete(selection - 1, selection);
                    }
                } else {
                    int count = currentPage * GlobalConstants.FACE_NUM + arg2;
                    // 下面这部分，在EditText中显示表情
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            ctx.getResources(), (Integer) WeqiaApplication
                                    .getInstance().getFaceMap().values()
                                    .toArray()[count]
                    );
                    if (bitmap != null) {
                        int rawHeigh = bitmap.getHeight();
                        int rawWidth = bitmap.getHeight();
                        float density = DeviceUtil.getDeviceDensity();
                        int newHeight = (int) (GlobalConstants.FACE_DEFAULT_FONT_SIZE * density);
                        int newWidth = newHeight;
                        // 计算缩放因子
                        float heightScale = ((float) newHeight) / rawHeigh;
                        float widthScale = ((float) newWidth) / rawWidth;
                        // 新建立矩阵
                        Matrix matrix = new Matrix();
                        matrix.postScale(heightScale, widthScale);
                        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                rawWidth, rawHeigh, matrix, true);
                        ImageSpan imageSpan = new ImageSpan(ctx, newBitmap);
                        String emojiStr = mfaceKeys.get(count);
                        SpannableString spannableString = new SpannableString(
                                emojiStr);
                        spannableString.setSpan(imageSpan,
                                emojiStr.indexOf('['),
                                emojiStr.indexOf(']') + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        int index = etInput.getSelectionStart();// 获取光标所在位置
                        Editable edit = etInput.getEditableText();// 获取EditText的文字
                        if (index < 0 || index >= edit.length()) {
                            edit.append(spannableString);
                        } else {
                            edit.insert(index, spannableString);// 光标所在位置插入文字
                        }
                    } else {
                        String ori = etInput.getText().toString();
                        int index = etInput.getSelectionStart();
                        StringBuilder stringBuilder = new StringBuilder(ori);
                        stringBuilder.insert(index, mfaceKeys.get(count));
                        etInput.setText(stringBuilder.toString());
                        etInput.setSelection(index
                                + mfaceKeys.get(count).length());
                    }

                }

            }
        });
        return gv;
    }

    // 防止乱pageview乱滚动
    private OnTouchListener forbidenScroll() {
        return new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }
        };
    }
}
