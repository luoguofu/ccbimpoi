package com.weqia.wq.component.modules.assist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.utils.ExpressionUtil;
import com.weqia.wq.global.ComponentUtil;

// 员工信息,备注
public class SimpleInfoFragement extends Fragment {
    private View view;
    @SuppressWarnings("unused")
    private LayoutInflater mInflater;
    private String info;
    private String qq;
    private TextView textView;
    private TextView tvQQ;
    private Activity ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = (Activity) getActivity();
        mInflater = inflater;
        view = inflater.inflate(R.layout.fragment_simpeinfo, null);
        textView = (TextView) view.findViewById(R.id.textView);
        tvQQ = (TextView) view.findViewById(R.id.tv_qq);
        info = ctx.getIntent().getExtras().getString("remark");
        qq = ctx.getIntent().getExtras().getString("QQ");
        if (StrUtil.notEmptyOrNull(info)) {
            ComponentUtil.copyTextView(ctx, textView, info);
            SpannableString content = new SpannableString("");
            content = ExpressionUtil.getExpressionString(ctx, info);
            textView.setText(content);
            ComponentUtil.stripUnderlines(textView);
        }
        if (StrUtil.notEmptyOrNull(qq)) {
            ComponentUtil.copyTextView(ctx, tvQQ, qq);
            String content = "ＱＱ：" + "<font color='#5a8b7f'>" + qq + "</font>";
            tvQQ.setText(Html.fromHtml(content));
            ComponentUtil.stripUnderlines(tvQQ);
            tvQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //可以跳转到添加好友，如果qq号是好友了，直接聊天
                        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;//uin是发送过去的qq号码
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        L.toastLong("请检查是否安装QQ");
                    }
                }
            });
        }
        return view;
    }


}
