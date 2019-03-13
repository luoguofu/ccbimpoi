package com.weqia.wq.component.modules.assist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.weqia.utils.CustomLinkMovementMethod;
import com.weqia.utils.DeviceUtil;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.wq.R;
import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.component.utils.ExpressionUtil;

public class ShowInfoFragement extends Fragment {
    private View view;
//    private LayoutInflater mInflater;
    private String info;
    private TextView textView;
    private Activity ctx;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = (Activity) getActivity();
//        mInflater = inflater;
        view = inflater.inflate(R.layout.fragment_showinfo, null);
        textView = (TextView) view.findViewById(R.id.textView);
        info = ctx.getIntent().getExtras().getString("show");
        if (StrUtil.notEmptyOrNull(info)) {
            textView.setText(ExpressionUtil.getExpressionString(ctx, info, 30));
        }
        textView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                return false;
            }
        });
        int navId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int navHeight = getResources().getDimensionPixelSize(navId);
        textView.setMinHeight(DeviceUtil.getDeviceHeight() - navHeight);
        
        float textWidth = ViewUtils.getTextLength(textView.getTextSize(), textView.getText().toString());
        if (textWidth > (DeviceUtil.getDeviceWidth() - ComponentUtil.dip2px(24))) {
            textView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        } else {
            textView.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
        }
        

        textView.setMovementMethod(new CustomLinkMovementMethod());
        ScrollView rlShow = (ScrollView) view.findViewById(R.id.rlShow);
        rlShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.finish();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.finish();
            }
        });
        return view;
    }


}
