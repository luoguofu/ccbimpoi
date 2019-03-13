package com.weqia.wq.component.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.weqia.wq.R;
import com.weqia.wq.component.utils.DialogUtil;

/**
 * Created by lgf on 2018/12/3.
 */

public class FormRowEditView extends LinearLayout {
    private FormRowEditView rootView;
    private TextView mFormNameTv;
    private EditText mFormContentEd;
    private TextView mFromSelectTv;
    private TableRow mFormTable;
    private String mName;
    private String mContent;
    private String mSelectString;
    private Dialog dialog;
    private String fieldId;
    private String fieldType;
    private String fieldDataId;       //编辑是需要添加的id
    private String fieldVal;
    private String tagId;
    private String tagValue = "";
    private String tagValueArr = "";
    public FormRowEditView(Context context) {
        super(context);
    }

    public FormRowEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //加载视图的布局
        rootView = (FormRowEditView) LayoutInflater.from(context).inflate(R.layout.form_row_edittext_layout, this, true);
        mFormTable = rootView.findViewById(R.id.tablerow_form);
        mFormNameTv = rootView.findViewById(R.id.tv_form_name);
        mFormContentEd = rootView.findViewById(R.id.ed_form_content);
        mFromSelectTv = rootView.findViewById(R.id.tv_form_select);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FormRow);

        mName = array.getString(R.styleable.FormRow_rowName);
        mContent = array.getString(R.styleable.FormRow_rowContent);
        if (mName != null) {
            mFormNameTv.setText(mName);
        }
        if (mContent != null) {
            mFormContentEd.setText(mContent);
        }

    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setName(String name) {
        mFormNameTv.setText(name);
    }

    public String getName() {
        return mFormNameTv.getText().toString();
    }

    public void setContent(String content) {
        mFormContentEd.setText(content);
    }

    public String getContent() {
        return mFormContentEd.getText().toString();
    }

    public String getSelectString() {
        return mFromSelectTv.getText().toString();
    }

    public void setSelectString(String mSelectString) {
        mFromSelectTv.setText(mSelectString);
    }

    public TextView getmFormNameTv() {
        return mFormNameTv;
    }

    public void setmFormNameTv(TextView mFormNameTv) {
        this.mFormNameTv = mFormNameTv;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public EditText getmFormContentEd() {
        return mFormContentEd;
    }

    public void setmFormContentEd(EditText mFormContentEd) {
        this.mFormContentEd = mFormContentEd;
    }

    public TextView getmFromSelectTv() {
        return mFromSelectTv;
    }

    public void setmFromSelectTv(TextView mFromSelectTv) {
        this.mFromSelectTv = mFromSelectTv;
    }

    public void setInput(boolean isInput) {
        if (isInput) {
            mFormContentEd.setVisibility(VISIBLE);
            mFromSelectTv.setVisibility(GONE);
            mFormContentEd.setSingleLine(true);
        } else {

        }
    }
    public void setTextarea(boolean isTextarea) {
        if (isTextarea) {
            mFormContentEd.setVisibility(VISIBLE);
            mFromSelectTv.setVisibility(GONE);
            mFormContentEd.setSingleLine(false);
            mFormContentEd.setMaxLines(5);
        } else {

        }
    }

    public void setSelect(boolean isSelect, final Context context, final String[] arrary, final String title) {

        if (isSelect) {
            mFormContentEd.setVisibility(GONE);
            mFromSelectTv.setVisibility(VISIBLE);
            mFormTable.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog = DialogUtil.initLongRadioClickDialog(context, title, arrary, tagValueArr, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView textView =
                                    (TextView) v.findViewById(R.id.tv_dlg_title);
                            mFromSelectTv.setText(textView.getText());
                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                }
            });
        }
    }
    public void setRadio(boolean isRadio, final Context context, final String[] arrary, final String title) {
        if (isRadio) {
            mFormContentEd.setVisibility(GONE);
            mFromSelectTv.setVisibility(VISIBLE);
/*            if (StrUtil.isEmptyOrNull(tagId)) {
                tagId = tagIds[0];
            }*/
            mFormTable.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog = DialogUtil.initLongRadioClickDialog(context, title, arrary, fieldVal, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView textView =
                                    (TextView) v.findViewById(R.id.tv_dlg_title);
                            mFromSelectTv.setText(textView.getText());
//                            tagId = tagIds[(int) textView.getTag()];
//                            setFieldVal(tagIds[(int) textView.getTag()]);
                            tagValue = textView.getText().toString();
                            setFieldVal(textView.getText().toString());
                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                }
            });
        }
    }
    public void setCheck(boolean isCheck, final Context context, final String[] arrary, final String title) {
        if (isCheck) {
//            mFormContentEd.setFocusable(false);
            mFormContentEd.setVisibility(GONE);
            mFromSelectTv.setVisibility(VISIBLE);
            mFormTable.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

/*                    dialog =  DialogUtil.initLongCheckClickDialog(context, title, arrary, tagValueArr, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView textView =
                                    (TextView) v.findViewById(R.id.tv_dlg_title);
                            mFromSelectTv.setText(textView.getText());
*//*                            if (StrUtil.notEmptyOrNull(getFieldVal())) {
                                setFieldVal(getFieldVal() + "," + tagIds[(int) textView.getTag()]);
                            } else {
                                setFieldVal(tagIds[(int) textView.getTag()]);
                            }*//*

                            dialog.dismiss();

                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();*/
                    DialogUtil.showMultiChoiceDialog(context, title, arrary, getFieldVal(), mFromSelectTv, rootView);
                }
            });
        }
    }

    public String getFieldDataId() {
        return fieldDataId;
    }

    public void setFieldDataId(String fieldDataId) {
        this.fieldDataId = fieldDataId;
    }

    public String getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(String fieldVal) {
        this.fieldVal = fieldVal;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getTagValueArr() {
        return tagValueArr;
    }

    public void setTagValueArr(String tagValueArr) {
        this.tagValueArr = tagValueArr;
    }
}
