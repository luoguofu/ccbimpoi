package com.weqia.wq.component.view.multiactiontextview;

import android.text.SpannableStringBuilder;

public class InputObject {


    private int startSpan;

    private int endSpan;

    private SpannableStringBuilder stringBuilder;

    private MultiActionTextviewClickListener multiActionTextviewClickListener;

    private Object inputObject;

    private int OperationType;

    public Object getInputObject() {
        return inputObject;
    }

    public void setInputObject(Object inputObject) {
        this.inputObject = inputObject;
    }

    public int getOperationType() {
        return OperationType;
    }

    public void setOperationType(int operationType) {
        OperationType = operationType;
    }

    public int getStartSpan() {
        return startSpan;
    }

    public void setStartSpan(int startSpan) {
        this.startSpan = startSpan;
    }

    public int getEndSpan() {
        return endSpan;
    }

    public void setEndSpan(int endSpan) {
        this.endSpan = endSpan;
    }

    public SpannableStringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(SpannableStringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public MultiActionTextviewClickListener getMultiActionTextviewClickListener() {
        return multiActionTextviewClickListener;
    }

    public void setMultiActionTextviewClickListener(
            MultiActionTextviewClickListener multiActionTextviewClickListener) {
        this.multiActionTextviewClickListener = multiActionTextviewClickListener;
    }

}