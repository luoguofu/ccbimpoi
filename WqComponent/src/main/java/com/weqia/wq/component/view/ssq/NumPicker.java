package com.weqia.wq.component.view.ssq;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.weqia.wq.R;

public class NumPicker extends LinearLayout {

    private WheelView mProvincePicker;
//    public Integer mProvinceIndex;
    private String[] nums;
    private String selectText;
    private int current = 0;

    public NumPicker(Context context, int currentValue) {
        super(context, null);
        this.current = currentValue /5 - 1;
        if (this.current < 0 ) {
            this.current = 0;
        }
        this.setOrientation(VERTICAL);
        nums = getResources().getStringArray(R.array.punch_notifi_time);
        mProvincePicker = (WheelView) LayoutInflater.from(context).inflate(R.layout.pop_num, null);
        if (nums != null) {
            ArrayList<String> items = new ArrayList<>();
            for (String string : nums) {
                items.add(string);
            }
            mProvincePicker.setData(items);
            mProvincePicker.setDefault(current);
            selectText = nums[current];
        }

        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (nums != null) {
                    selectText = nums[id];
                }
            }

            @Override
            public void selecting(int id, String text) {}
        });
        addView(mProvincePicker, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
    }
    
    public String getSelectText() {
        return selectText;
    }
}
