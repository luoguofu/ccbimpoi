package com.weqia.wq.component.view.editlabelview;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by MX on 6/5 0005.
 */
public class EditLabel extends LinearLayout implements View.OnFocusChangeListener {

    EditText input;
    TextView label;
    OnLabelFocusChangeListener focusChangeListener;
    private LinearLayout.LayoutParams inpuTextParams;
    private LinearLayout.LayoutParams labelTextParams;

    public EditLabel(Context context) {
        super(context);
        initLayout(null);
    }

    public EditLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initLayout(attrs);
        }
    }

    void initLayout(AttributeSet attr) {
        Context ctx = getContext();
        input = new EditText(ctx);
        label = new TextView(ctx);
        input.setOnFocusChangeListener(this);
        // Create Default Layout
        inpuTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        labelTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        input.setLayoutParams(inpuTextParams);
        label.setLayoutParams(labelTextParams);
        setOrientation(LinearLayout.VERTICAL);

        input.setGravity(Gravity.LEFT | Gravity.TOP);
        label.setGravity(Gravity.LEFT);
        label.setTextColor(Color.RED);
        input.setTextColor(Color.BLACK);
//        Context context = getContext();
        label.setPadding(5, 2, 5, 2);

        addView(label);
        addView(input);

        label.setVisibility(INVISIBLE);
        setLabelHintText("单位");


        input.setLines(new Random().nextInt(4));
        input.setBackgroundColor(Color.TRANSPARENT);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input.getText().toString().length() > 0
                        && label.getVisibility() == INVISIBLE) {
                    showHint();
                } else if (input.getText().toString().length() == 0
                        && label.getVisibility() == VISIBLE) {
                    hideHint();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    public void setLabelHintText(String string) {
        if (string != null) {
            label.setText(string);
        }
    }

    public void setText(String string) {
        if (string != null) {
            input.setText(string);
        }
    }

    public void setTextHint(String hintText) {
        if (hintText != null) {
            input.setHint(hintText);
        }
    }

    public String getText() {
        return input.getText().toString();
    }

    public EditText getEditText() {
        return input;
    }

    private void showHint() {
        if (label.getVisibility() != View.VISIBLE) {
            label.setVisibility(View.VISIBLE);
        }
    }

    private void hideHint() {
        if (label.getVisibility() != View.INVISIBLE) {
            label.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        label.setSelected(hasFocus);
        if (focusChangeListener != null) {
            focusChangeListener.onFocusChange(this, hasFocus);
        }
    }

}
