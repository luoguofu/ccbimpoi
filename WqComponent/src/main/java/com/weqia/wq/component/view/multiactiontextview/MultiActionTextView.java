package com.weqia.wq.component.view.multiactiontextview;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.weqia.utils.CustomLinkMovementMethod;
import com.weqia.wq.R;

public class MultiActionTextView {

    @SuppressWarnings("deprecation")
    public static void setSpannableText(TextView textView,
                                        SpannableStringBuilder stringBuilder, int highLightTextColor,Context ctx) {
        textView.setMovementMethod(CustomLinkMovementMethod.getInstance());
        textView.setText(stringBuilder, TextView.BufferType.SPANNABLE);
        textView.setLinkTextColor(highLightTextColor);
        textView.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.text_click));
    }


    public static void addActionOnTextViewWithLink(final InputObject inputObject) {
        inputObject.getStringBuilder().setSpan(
                new MultiActionTextViewClickableSpan(true) {
                    @Override
                    public void onClick(View widget) {
                        inputObject.getMultiActionTextviewClickListener()
                                .onTextClick(inputObject);
                    }
                }, inputObject.getStartSpan(), inputObject.getEndSpan(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public static void addActionOnTextViewWithoutLink(
            final InputObject inputObject) {
        inputObject.getStringBuilder().setSpan(
                new MultiActionTextViewClickableSpan(false) {
                    @Override
                    public void onClick(View widget) {
                        inputObject.getMultiActionTextviewClickListener()
                                .onTextClick(inputObject);
                    }
                }, inputObject.getStartSpan(), inputObject.getEndSpan(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}